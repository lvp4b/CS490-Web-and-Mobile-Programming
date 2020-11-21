package com.github.lvp4b.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lvp4b.sqlite.databinding.ActivityEmployeeBinding;
import com.github.lvp4b.sqlite.databinding.EmployeeListItemBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SampleJoinRecyclerViewCursorAdapter extends RecyclerView.Adapter<SampleJoinRecyclerViewCursorAdapter.ViewHolder> {

    Context mContext;
    Cursor mCursor;
    private final com.github.lvp4b.sqlite.databinding.ActivityEmployeeBinding binding;

    public SampleJoinRecyclerViewCursorAdapter(Context context, Cursor cursor, ActivityEmployeeBinding binding) {
        mContext = context;
        mCursor = cursor;
        this.binding = binding;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EmployeeListItemBinding itemBinding;
        private final com.github.lvp4b.sqlite.databinding.ActivityEmployeeBinding binding;

        public ViewHolder(View itemView, ActivityEmployeeBinding binding) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
            this.binding = binding;
        }

        public void bindCursor(Cursor cursor) {
            itemBinding.firstnameLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_FIRSTNAME)
            ));
            itemBinding.lastnameLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_LASTNAME)
            ));
            itemBinding.jobDescLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION)
            ));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH)));
            itemBinding.dobLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            itemBinding.nameLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_NAME)
            ));
            itemBinding.descLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_DESCRIPTION)
            ));

            calendar.setTimeInMillis(cursor.getLong(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_FOUNDED_DATE)));
            itemBinding.foundedLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            itemBinding.foundedLabel.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(SampleDBContract.Employee._ID)));
            calendar.setTimeInMillis(cursor.getLong(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE)));
            String employed = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
            itemBinding.cardView.setOnClickListener(view -> {
                binding.deleteButton.setTag(itemBinding.foundedLabel.getTag());
                binding.firstnameEditText.setText(itemBinding.firstnameLabel.getText());
                binding.lastnameEditText.setText(itemBinding.lastnameLabel.getText());
                binding.jobDescEditText.setText(itemBinding.jobDescLabel.getText());
                binding.dobEditText.setText(itemBinding.dobLabel.getText());
                binding.jobDescEditText.setText(itemBinding.jobDescLabel.getText());
                binding.employedEditText.setText(employed);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.employee_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, binding);
        return viewHolder;
    }
}