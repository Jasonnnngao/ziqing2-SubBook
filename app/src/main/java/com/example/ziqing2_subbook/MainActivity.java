package com.example.ziqing2_subbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ADD = 1;
    final private int REQUEST_CODE_EDIT = 2;
    private ArrayList<Subscription> subscriptions;
    private Gson gson;
    private ListView mListView;
    private ArrayAdapter<Subscription> mArrayAdapter;
    private final String FILE_NAME = "sub_book.dat";
    private int indexEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        loadFromFile();
        mListView = findViewById(R.id.list_view);
        mArrayAdapter = new ArrayAdapter<Subscription>(this,R.layout.list_item,subscriptions);

        final Activity that = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(that,SubscriptionActivity.class);
                intent.putExtra("purpose","edit");
                indexEdited = i;
                intent.putExtra("content",gson.toJson(subscriptions.get(i)));
                startActivityForResult(intent,REQUEST_CODE_EDIT);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mListView.setAdapter(mArrayAdapter);
    }

    public void onAddBtnClicked(View view){
        Intent intent = new Intent(this,SubscriptionActivity.class);
        intent.putExtra("purpose","add");
        startActivityForResult(intent,REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_ADD){
                Subscription subscription = gson.fromJson(data.getStringExtra("result"),Subscription.class);
                subscriptions.add(subscription);
            }

            else if(requestCode == REQUEST_CODE_EDIT){
                if (data.getBooleanExtra("is_deleted",false)){
                    subscriptions.remove(indexEdited);
                } else {
                    subscriptions.set(indexEdited, gson.fromJson(data.getStringExtra("result"),Subscription.class));
                }
            }


            saveInFile();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME,
                    0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(subscriptions, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Following line based on https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html retrieved 2015-09-21
            Type listType = new TypeToken<ArrayList<Subscription>>() {
            }.getType();
            subscriptions = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            subscriptions = new ArrayList<>();
        }
    }

}
