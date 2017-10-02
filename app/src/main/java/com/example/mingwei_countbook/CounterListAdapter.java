package com.example.mingwei_countbook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is a custom adaptor to adapt ArrayList of Counter object to ListView.
 * This code are originally from:
 * https://www.youtube.com/watch?v=YMJSBHAZsso
 * and modified by mingwei.
 */

public class CounterListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Counter> counterList;

    // Counstructor
    public CounterListAdapter(Context context, ArrayList<Counter> counterList) {
        this.context = context;
        this.counterList = counterList;
    }

    @Override
    public int getCount() {
        return counterList.size();
    }

    @Override
    public Object getItem(int position) {
        return counterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v= View.inflate(context, R.layout.item_counter_list, null);
        TextView tvName = (TextView) v.findViewById(R.id.tv_name);
        TextView tvCurrentValue = (TextView) v.findViewById(R.id.tv_currentValue);
        TextView tvInitialValue = (TextView) v.findViewById(R.id.tv_initialValue);

        // Set text for TextView
        tvName.setText(counterList.get(position).getName());
        tvCurrentValue.setText("Current value: " +
                String.valueOf(counterList.get(position).getCurrentValue()));
        tvInitialValue.setText("Initial value: " +
                String.valueOf(counterList.get(position).getInitialValue()));

        return v ;
    }
}
