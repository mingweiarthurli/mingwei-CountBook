package com.example.mingwei_countbook;

import java.io.Serializable;
import java.util.Date;

/**
 * This class create a counter object. Each counter object have attributes included name, date,
 * initial value, current value, and comment. User must input name of counter and initial value
 * to create a counter object. Each attribute has getter and setter. There are also methods to
 * increment and decrement current value. Method reset is used to reset current value to be
 * the initial value. If initial value or current value attempts to be negative, methods will
 * throw a exception.
 */

public class Counter implements Serializable {

    private String name;
    private Date date;
    private int currentValue;
    private int initialValue;
    private String comment;

    // constructor
    public Counter(String name, int initialValue) throws NegativeValueException {
        this.name = name;
        if (initialValue < 0) {
            throw new NegativeValueException();
        } else {
            this.initialValue = initialValue;
        }
        this.currentValue = initialValue;
        this.date = new Date();
    }

    // getters and setters
    public String getName() {return this.name;}

    public void setName(String name) {this.name = name;}

    public int getCurrentValue() {return currentValue;}

    public void setCurrentValue(int currentValue) throws NegativeValueException {
        if (currentValue < 0) {
            throw new NegativeValueException();
        } else {
            this.currentValue = currentValue;
        }
    }

    public int getInitialValue() {return this.initialValue;}

    public void setInitialValue(int initialValue) throws NegativeValueException {
        if (initialValue < 0) {
            throw new NegativeValueException();
        } else {
            this.initialValue = initialValue;
        }
    }

    public String getComment() {return comment;}

    public void setComment(String comment) {this.comment = comment;}

    public Date getDate() {return date;}

    public void setDate() {this.date = new Date();}

    // increment current value by one
    public void increment() {
        this.currentValue += 1;
    }

    // decrement current value by one
    public void decrement() throws NegativeValueException {
        if (this.currentValue == 0) {
            throw new NegativeValueException();
        } else {
            this.currentValue -= 1;
        }
    }

    // reset current value to initial value
    public void reset() {
        this.currentValue = this.initialValue;
    }
}
