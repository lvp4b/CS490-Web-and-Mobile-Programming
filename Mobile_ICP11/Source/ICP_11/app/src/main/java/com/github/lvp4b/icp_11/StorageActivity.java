package com.github.lvp4b.icp_11;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class StorageActivity extends AppCompatActivity {
    EditText txt_content;
    EditText contenttoDisplay;
    String FILENAME = "MyAppStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        txt_content = (EditText) findViewById(R.id.id_txt_mycontent);
        contenttoDisplay = (EditText) findViewById(R.id.id_txt_display);
    }

    public void saveTofile(View v) throws IOException {
        // ICP Task4: Write the code to save the text
        File file = new File(getApplicationContext().getFilesDir(), "storage.txt");
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(txt_content.getText());
        }
    }

    public void retrieveFromFile(View v) throws IOException {
        // ICP Task4: Write the code to display the above saved text
        File file = new File(getApplicationContext().getFilesDir(), "storage.txt");
        try (Scanner in = new Scanner(file)){
            Toast.makeText(this, in.next(), Toast.LENGTH_SHORT).show();
        }
    }
}
