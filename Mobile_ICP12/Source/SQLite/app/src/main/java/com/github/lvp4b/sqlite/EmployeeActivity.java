package com.github.lvp4b.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.lvp4b.sqlite.databinding.ActivityEmployeeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployeeActivity extends AppCompatActivity {

    private ActivityEmployeeBinding binding;
    private static final String TAG = "EmployeeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        String[] queryCols = new String[]{"_id", SampleDBContract.Employer.COLUMN_NAME};
        String[] adapterCols = new String[]{SampleDBContract.Employer.COLUMN_NAME};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                queryCols,                                // The columns to return
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.employerSpinner.setAdapter(cursorAdapter);

        binding.saveButton.setOnClickListener(view -> saveToDB());

        binding.searchButton.setOnClickListener(view -> readFromDB());

    }

    public void delete(View view) {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        if (binding.deleteButton.getTag() == null) {
            Toast.makeText(this, "Tap on the row to delete", Toast.LENGTH_LONG).show();
        } else {
            database.delete(SampleDBContract.Employee.TABLE_NAME, "_id = ?", new String[]{binding.deleteButton.getTag().toString()});
            Toast.makeText(this, "Deleted row " + binding.deleteButton.getTag(), Toast.LENGTH_LONG).show();
            binding.deleteButton.setTag(null);
            readFromDB();
        }
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding.firstnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding.lastnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding.jobDescEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0));

        Log.d("getINT", ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0) + "");
        Log.d("getColumnName", ((Cursor) binding.employerSpinner.getSelectedItem()).getColumnName(0));

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.dobEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date);

            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.employedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }

        if (binding.deleteButton.getTag() == null) {
            long newRowId = database.insert(SampleDBContract.Employee.TABLE_NAME, null, values);

            Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
        } else {
            database.update(SampleDBContract.Employee.TABLE_NAME, values, "_id=?",
                    new String[]{binding.deleteButton.getTag().toString()});
            Toast.makeText(this, "Updated row", Toast.LENGTH_LONG).show();
        }
        readFromDB();
    }

    private void readFromDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] selectionArgs = {"%", "%"};

        Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_EMPLOYER, selectionArgs);
        binding.recycleView.setAdapter(new SampleJoinRecyclerViewCursorAdapter(this, cursor, binding));
    }
}