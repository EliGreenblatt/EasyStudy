package com.example.myezstudy;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a message.
 * Contains message content.
 * Also implements the Comparable interface for comparing messages.
 */
public class Message implements Comparable<Message> {
    private String theMessage;  // the message string
    // variables for meeting date
    private int createdDay, createdDayOfYear, createdMonth, createdYear,
            createdHour, createdMinute;

    /**
     * Default constructor
     */
    public Message(){}

    /**
     * Constructor for Message with text parameter.
     * Initializes message content and sets creation date information.
     * @param txt content of the message.
     */
    public Message(String txt) {
        this.theMessage = txt;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.createdMonth = cal.get(Calendar.MONTH) + 1;
        this.createdDay = cal.get(Calendar.DAY_OF_MONTH);
        this.createdDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        this.createdYear = cal.get(Calendar.YEAR);
        this.createdHour = cal.get(Calendar.HOUR_OF_DAY) + 2; // Adding 2 hours (assuming timezone correction)
        this.createdMinute = cal.get(Calendar.MINUTE);
    }

    // Getter methods for message attributes
    public int getCreatedDay(){ return createdDay;}
    public int getCreatedMonth(){ return createdMonth;}
    public int getCreatedYear(){ return createdYear;}
    public int getCreatedHour(){ return createdHour;}
    public int getCreatedMinute(){ return createdMinute;}
    // no usages but needed for database
    public int getCreatedDayOfYear(){ return createdDayOfYear;}
    public String getTheMessage(){ return theMessage;}

    /**
     * Check if the message is old (more than 7 days from creation).
     * @return True if the message is old, otherwise false.
     */
    public boolean checkIfOld(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_YEAR);
        if (year > createdYear && day + (365 - createdDayOfYear) > 7 )
            return true;
        else if (year == createdYear && day - createdDayOfYear > 7 )
            return true;
        return false;
    }

    /**
     * Override toString method to represent the Message as a string.
     * @return A formatted string representation of the message date and content.
     */
    @NonNull
    @Override
    public String toString() {
        return "Date: " + createdDay + "/" + createdMonth
                + "/" + createdYear + " " +
                createdHour + ":" + createdMinute
                + "\n" + theMessage;
    }

    /**
     * Implementation of the compareTo method for comparing messages.
     * @param to The message to compare to.
     * @return -1 if this message is 'less' than the other, 1 if 'greater', 0 if 'equal'.
     */
    @Override
    public int compareTo(Message to) {
        if (this.createdYear > to.getCreatedYear()) return -1;
        if (this.createdMonth > to.getCreatedMonth()) return -1;
        if (this.createdMonth < to.getCreatedMonth()) return 1;
        if (this.createdYear < to.getCreatedYear()) return 1;
        if (this.createdDay > to.getCreatedDay()) return -1;
        if (this.createdDay < to.getCreatedDay()) return 1;
        if (this.createdHour > to.getCreatedHour() )return -1;
        if (this.createdHour < to.getCreatedHour() ) return 1;
        if (this.createdHour > to.getCreatedMinute()) return -1;
        if (this.createdHour < to.getCreatedMinute()) return 1;
        return 0;
    }
}
