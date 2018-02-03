package com.example.ziqing2_subbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SubscriptionActivity extends AppCompatActivity {
    private String purpose;

    private EditText name;
    private EditText charge;
    private EditText date;
    private EditText comment;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Intent intent = getIntent();
        purpose = intent.getStringExtra("purpose");

        gson = new Gson();
        Button delete = findViewById(R.id.btn_delete);
        name = findViewById(R.id.et_name);
        charge = findViewById(R.id.et_charge);
        date = findViewById(R.id.et_date);
        comment = findViewById(R.id.ed_comment);

        final Calendar calendar = Calendar.getInstance();

        if (purpose.equals("edit")){
            Subscription subscription = gson.fromJson(intent.getStringExtra("content"),Subscription.class);
            name.setText(subscription.getName());
            charge.setText(subscription.getMonthly_charge().toString());
            date.setText(subscription.getDateString());
            comment.setText(subscription.getComment());
            calendar.setTime(subscription.getDate());
        }

        final Activity that = this;

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePickerDialog =  new DatePickerDialog(
                        that,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                                calendar.set(Calendar.YEAR,year);
                                calendar.set(Calendar.MONTH,month);
                                calendar.set(Calendar.DAY_OF_MONTH,day_of_month);
                                date.setText(format.format(calendar.getTime()));
                            }
                        }
                        , calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)
                );
                mDatePickerDialog.show();
            }
        });


        if (purpose.equals("add")){
            delete.setVisibility(View.GONE);
        }


    }

    private boolean checkFields(){
        if (name.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (charge.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Monthly charge cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public void onConfirmClicked(View view){
        if (checkFields()) {
            Date datetime = new Date();
            try {
                datetime = format.parse(date.getText().toString());
            } catch (ParseException e){
                e.printStackTrace();
            }
            Subscription subscription = new Subscription(
                    name.getText().toString(),
                    datetime,
                    Double.parseDouble(charge.getText().toString()),
                    comment.getText().toString()
            );
            String subscription_str = gson.toJson(subscription);
            Intent intent = new Intent();
            intent.putExtra("result",subscription_str);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void onDeleteClicked(View view){
        Intent intent = new Intent();
        intent.putExtra("is_deleted",true);
        setResult(RESULT_OK,intent);
        finish();
    }
}
