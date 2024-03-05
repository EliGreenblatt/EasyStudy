package com.example.myezstudy;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Message implements Comparable<Message> {
        private String theMessage;
        private int createdDay, createdDayOfYear, createdMonth, createdYear,
                createdHour, createdMinute;

        public Message(){
        }
        public Message(String txt) {
            this.theMessage = txt;
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            this.createdMonth = cal.get(Calendar.MONTH) + 1;
            this.createdDay = cal.get(Calendar.DAY_OF_MONTH);
            this.createdDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            this.createdYear = cal.get(Calendar.YEAR);
            this.createdHour = cal.get(Calendar.HOUR_OF_DAY) + 2;
            this.createdMinute = cal.get(Calendar.MINUTE);
        }

        public int getcreatedDay(){ return createdDay;}
        public int getcreatedMonth(){ return createdMonth;}
        public int getcreatedYear(){ return createdYear;}
        public int getcreatedHour(){ return createdHour;}
        public int getcreatedMinute(){ return createdMinute;}
        public int getCreatedDayOfYear(){ return createdDayOfYear;}
        public String gettheMessage(){ return theMessage;}

        public boolean checkIfOld(){
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_YEAR);
            if (year > createdYear &&
                    day + (365 - createdDayOfYear) > 7 )
                return true;
            else if (year == createdYear &&
                    day - createdDayOfYear > 7 )
                return true;
            return false;
        }
        @NonNull
        public String toString() {
            return
                    "Date: " + createdDay + "/" + createdMonth
                            + "/" + createdYear + " " +
                            createdHour + ":" + createdMinute
                            + "\n" + theMessage;
        }


        @Override
        public int compareTo(Message o) {
            if (this.createdYear > o.getcreatedYear()) return -1;
            else if (this.createdYear < o.getcreatedYear()) return 1;
            else if (this.createdMonth > o.getcreatedMonth()) return -1;
            else if (this.createdMonth < o.getcreatedMonth()) return 1;
            else if (this.createdDay > o.getcreatedDay()) return -1;
            else if (this.createdDay < o.getcreatedDay()) return 1;
            else if (this.createdHour > o.getcreatedHour() )return -1;
            else if (this.createdHour < o.getcreatedHour() ) return 1;
            else if (this.createdHour > o.getcreatedMinute()) return -1;
            else if (this.createdHour < o.getcreatedMinute()) return 1;
            return 0;
        }
    }


