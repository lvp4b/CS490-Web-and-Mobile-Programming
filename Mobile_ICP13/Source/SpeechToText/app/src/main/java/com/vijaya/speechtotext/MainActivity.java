package com.vijaya.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(v -> startVoiceInput());

        tts = new TextToSpeech(this, status -> {
            tts.setLanguage(Locale.US);
            if (status != TextToSpeech.SUCCESS) {
                mVoiceInputTv.setText(Html.escapeHtml("Failed to initialize text to speech"));
            }
        });

        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mVoiceInputTv.addTextChangedListener(new TextWatcher() {
            String name = "user";
            boolean reentrant = false;

            private void say(String s) {
                reentrant = true;
                mVoiceInputTv.setText(Html.escapeHtml(s));
                tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                reentrant = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (reentrant) {
                    return;
                }

                String text = s.toString().toLowerCase();
                if (text.contains("hello")){
                    say("What is your name?");
                } else if (text.contains("name")) {
                    String lastWord = text.substring(text.lastIndexOf(' ') + 1);
                    name = lastWord;
                    say("Hello " + name);
                } else if (text.contains("not feeling well")) {
                    say("I can understand. Please tell your symptoms in short.");
                } else if (text.contains("thank")) {
                    say("Thank you too " + name + " take care");
                } else if (text.contains("medicine")) {
                    say("I think you have a fever. Please take this medicine.");
                } else {
                    say("I'm sorry, I didn't understand that.");
                }
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null && !result.isEmpty()) {
                        mVoiceInputTv.setText(result.get(0));
                    }
                }
                break;
            }

        }
    }
}