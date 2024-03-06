package com.example.myezstudy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * Represents a meeting between users.
 */
public class Meeting implements Comparable<Meeting>{

    private String partnerUsername;
    private User partner;
    private int day;
    private int month;
    private int year;
    private int startHour;
    private int endHour;
    private int price;
    private String Available;

    public Meeting(){} // for database

    public Meeting(int day ,int month, int year, int startHour, int endHour, int price) {
        this.partner = null;
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.endHour = endHour;
        this.price = price;
        this.Available = "true";
    }
    public Meeting(User partner, String partnerUsername,int day ,int month, int year, int startHour, int endHour, int price) {
        this.partner = new User(partner.getName(), partner.getAge(), partner.getPhone(),partner.getEmail());
        this.day = day;
        this.partnerUsername = partnerUsername;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.endHour = endHour;
        this.price = price;
        this.Available = "false";
    }
    public Meeting(User partner, String partnerUsername, Meeting M) {
        this.partner = new User(partner.getName(), partner.getAge(), partner.getPhone(),partner.getEmail());;
        this.day = M.getDay();
        this.partnerUsername = partnerUsername;
        this.month = M.getMonth();
        this.year = M.getYear();
        this.startHour = M.getStartHour();
        this.endHour = M.getEndHour();
        this.price = M.getPrice();
        this.Available = "false";
    }

    // getters
    public int getPrice(){return price;}
    public String getPartnerUsername(){return partnerUsername;}
    public int getDay(){return day;}
    public int getMonth(){return month;}
    public int getYear(){return year;}
    public int getStartHour(){return startHour;}
    public int getEndHour(){return endHour;}
    public User getPartner() {return partner;}


    // string representation of the meeting details.
    public String printDetails() {
        if(this.ifAvailable()){
            return
                    "Available\n"+
                            "Date: " + day + "/" + month +"/" + year + "\n"
                             + "from " + startHour +
                            " to " + endHour +
                            "\nprice: " + price;
        }
        return
                "Partner: Username " + partnerUsername + "\n" + partner.toString() +
                        "Date: " + day + "/" + month +"/" + year + "\n"
                        + "from " + startHour +
                        " to " + endHour +
                        "\nprice: " + price;

    }

    // string representation of the meeting.
    public String toString() {
        if(this.ifAvailable()){
            return
                    "Available\n"+
                            "Date: " + day + "/" + month +"/" + year +
                            " from " + startHour +
                            " to " + endHour;
        }
        return
                "Partner: " + partner.getName() +
                        ", Date: " + day + "/" + month +"/" + year +
                        " from " + startHour +
                        " to " + endHour;

    }

    /**
     * Checks if the meeting is equal to another meeting
     * based on date and time.
     * @param M Another Meeting object to compare.
     * @return True if the meetings are equal, false otherwise.
     */
    public boolean equalTo(Meeting M){
        if(this.day == M.getDay() && this.month == M.getMonth()
                &&this.year == M.getYear())
            for(int i = this.startHour; i< this.endHour; i++){
                if(M.getStartHour() <= i && i< M.getEndHour())
                    return true;
            }
        return false;
    }

    /**
     * Adds a partner to the meeting.
     * @param partner         User object representing the partner.
     * @param partnerUsername Username of the partner.
     */
    public void addPartner(User partner, String partnerUsername) {
        if(!ifAvailable()) return;
        this.partnerUsername = partnerUsername;
        this.Available = "false";
        this.partner = partner;
    }

    // Cancels the meeting, making it available.
    public void CancelMetting() {
        this.Available = "true";
        this.partnerUsername = null;
        this.partner = null;
    }

    // returns if meeting is available
    public boolean ifAvailable(){
        if(this.partner == null){
            this.Available = "true";
            return true;
        }
        this.Available = "false";
        return false;
    }

    // True if the meeting can be canceled, false otherwise.
    public boolean checkIfCanCancel(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(this.year == cal.get(Calendar.YEAR) &&
                this.month == (cal.get(Calendar.MONTH) + 1) &&
                this.day== cal.get(Calendar.DAY_OF_MONTH) &&
                (this.startHour - (cal.get(Calendar.HOUR_OF_DAY) + 2))
            <= 4 )
            return false;
        return true;
    }

    // Compares two meetings for sorting based on date and time.
    @Override
    public int compareTo(Meeting to) {
        if (this.year < to.getYear()) return -1;
        if (this.year > to.getYear()) return 1;
        if (this.month < to.getMonth()) return -1;
        if (this.month > to.getMonth()) return 1;
        if (this.day < to.getDay()) return -1;
        if (this.day > to.getDay()) return 1;
        if (this.startHour < to.getStartHour()) return -1;
        if (this.startHour > to.getStartHour()) return 1;
        if (this.endHour < to.getEndHour()) return -1;
        if (this.endHour > to.getEndHour()) return 1;
        return 0;
    }
}
