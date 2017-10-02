package com.example.mingwei_countbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is an activity to create a new counter.
 * User need to enter name of the counter, initial value and comment (optional).
 * Non-numeric or negative initial value will catch an exception and show a Toast message.
 *
 * This class load the ArrayList of Counter object from counterList.sav and add new Counter object
 * to the end.
 */

public class CreateActivity extends AppCompatActivity {

    private static final String FILENAME = "counterList.sav";

    private ArrayList<Counter> counterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    // add Counter object to ArrayList and then call CounterListActivity by click Create button.
    public void createCounter(View view) {
        try {
            loadFromFile();

            EditText createName = (EditText) findViewById(R.id.createName);
            String name = createName.getText().toString();

            EditText createComment = (EditText) findViewById(R.id.createComment);
            String Comment = createComment.getText().toString();

            EditText createInitialValue = (EditText) findViewById(R.id.createInitialValue);
            int initialValue = Integer.parseInt(createInitialValue.getText().toString());

            Counter newCounter = new Counter(name, initialValue);
            counterList.add(newCounter);

            saveInFile();

            Intent intent = new Intent(this, CounterListActivity.class);
            startActivity(intent);

        } catch (NumberFormatException exc) {
            Toast.makeText(getApplicationContext(), "Initial value should be numeric",
                    Toast.LENGTH_SHORT).show();
        } catch (NegativeValueException exc) {
            Toast.makeText(getApplicationContext(), "Initial value should be non-negative",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Taken from f17TueLab3
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-09-19
            Type listType = new TypeToken<ArrayList<Counter>>(){}.getType();
            counterList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            counterList = new ArrayList<Counter>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }

    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(counterList, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
