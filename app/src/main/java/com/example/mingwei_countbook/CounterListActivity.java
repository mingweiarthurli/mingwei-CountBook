package com.example.mingwei_countbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class is used to create a counter list activity to display the counter list.
 * This class uses an custom adaptor to adapt counterList, which is an ArrayList of Counter object,
 * to a ListView.
 * There is a new counter button to call another activity (CreateActivity) to show some blank for
 * initialize attribute of a new Counter object.
 * Each row of the ListView can be clicked to call another activity (DetailActivity) to let user
 * edit attributes of existing Counter object or increase or decrease the number.
 * When this activity is called, it will read counter list from counterList.sav. When this activity calls
 * another activity, counter list will be stored in counterList.sav.
 */

public class CounterListActivity extends AppCompatActivity {

    private static final String FILENAME = "counterList.sav";

    private ArrayList<Counter> counterList;

    private ListView lvCounter;
    private CounterListAdapter adapter;
    private TextView summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvCounter = (ListView) findViewById(R.id.counterListView);
        counterList = new ArrayList<Counter>();

        summary = (TextView) findViewById(R.id.summary);

        /* used for test, not a part of final code
        try {
            counterList.add(new Counter("Name", 1));
        } catch (NegativeValueException e) {

        }
        */

        // Init adapter
        adapter = new CounterListAdapter(getApplicationContext(), counterList);
        lvCounter.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadFromFile();

        // refresh ListView
        adapter = new CounterListAdapter(getApplicationContext(), counterList);
        lvCounter.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        summary.setText("Total counter: " + String.valueOf(counterList.size()));

        // call DetailActivity by click rows of ListView
        lvCounter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CounterListActivity.this, DetailActivity.class);
                /* this won't work:
                 * Intent intent = new Intent(this, DetailActivity.class);
                 */
                intent.putExtra("ArrayList position", position);
                startActivity(intent);
            }
        });
    }

    // call CreateActivity by click New Counter botton
    public void newCounter(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
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
