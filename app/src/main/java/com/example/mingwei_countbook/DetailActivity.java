package com.example.mingwei_countbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
 * This class shows the detail information of selected Counter object.
 * Every attribute of the object can be changed, except date. After click Save button, changes will
 * be saved and return to CounterListActivity.
 * User can increase or decrease current value of the counter at there. Every increment or decrement
 * will be saved to file immediately.
 * User can also reset current value to initial value (this will be saved immediately) or delete
 * this counter.
 *
 * This class access the counterList.sav file and read specific position of selected Counter object,
 * then operate and save the changes.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String FILENAME = "counterList.sav";

    private ArrayList<Counter> counterList;

    private int position;

    private EditText etName;
    private EditText etInitialValue;
    private EditText etCurrentValue;
    private EditText etComment;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        position = getIntent().getIntExtra("ArrayList position", 0);

        etName = (EditText) findViewById(R.id.editName);
        etInitialValue = (EditText) findViewById(R.id.editInitialValue);
        etCurrentValue = (EditText) findViewById(R.id.editCurrentValue);
        etComment = (EditText) findViewById(R.id.editComment);
    }

    // load file and display attributes
    protected void onStart() {
        super.onStart();

        loadFromFile();

        etName.setText(String.valueOf(counterList.get(position).getName()));
        etInitialValue.setText(String.valueOf(counterList.get(position).getInitialValue()));
        etCurrentValue.setText(String.valueOf(counterList.get(position).getCurrentValue()));
        etComment.setText(String.valueOf(counterList.get(position).getComment()));
        tvDate.setText(String.valueOf(counterList.get(position).getDate()));
    }

    // increase current value and save, then display new value
    public void increment(View view) {
        counterList.get(position).increment();
        counterList.get(position).setDate();
        saveInFile();

        etCurrentValue.setText(String.valueOf(counterList.get(position).getCurrentValue()));
        tvDate.setText(String.valueOf(counterList.get(position).getDate()));
    }

    // decrease current value and save, then display new value
    public void decrement(View view) {
        try{
            counterList.get(position).decrement();
            counterList.get(position).setDate();
            saveInFile();

            etCurrentValue.setText(String.valueOf(counterList.get(position).getCurrentValue()));
            tvDate.setText(String.valueOf(counterList.get(position).getDate()));
        } catch (NegativeValueException exc) {
            Toast.makeText(getApplicationContext(), "Current value should be non-negative",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // save all changes in the EditText field, and return to CounterListActivity
    public void save(View view) {
        try {
            String name = etName.getText().toString();
            String comment = etComment.getText().toString();
            int initialValue = Integer.parseInt(etInitialValue.getText().toString());
            int currentValue = Integer.parseInt(etCurrentValue.getText().toString());

            counterList.get(position).setName(name);
            counterList.get(position).setComment(comment);
            counterList.get(position).setInitialValue(initialValue);
            counterList.get(position).setCurrentValue(currentValue);
            counterList.get(position).setDate();

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

    // make current value equal to initial value then save to file
    public void reset(View view) {
        counterList.get(position).reset();

        saveInFile();

        etCurrentValue.setText(String.valueOf(counterList.get(position).getCurrentValue()));
        tvDate.setText(String.valueOf(counterList.get(position).getDate()));
    }

    // remove this Counter from ArrayList then save
    public void delete(View view) {
        counterList.remove(position);

        saveInFile();

        Intent intent = new Intent(this, CounterListActivity.class);
        startActivity(intent);
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
