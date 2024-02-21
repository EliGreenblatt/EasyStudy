package com.example.myezstudy;

import java.util.List;

public class Meeting implements Comparable<Meeting>{


    private User partner;
    private String day;
    private String month;
    private String year;
    private String startHour;
    private String endHour;
    private String Available;
    public Meeting(){}
    public Meeting(String day ,String month, String year, String startHour, String endHour) {
        this.partner = null;
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.endHour = endHour;
        this.Available = "true";
    }
    public Meeting(User partner,String day ,String month, String year, String startHour, String endHour) {
        this.partner = new User(partner.getName(), partner.getAge(), partner.getPhone(),partner.getEmail());
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.endHour = endHour;
        this.Available = "false";
    }
    public Meeting(User partner,Meeting M) {
        this.partner = new User(partner.getName(), partner.getAge(), partner.getPhone(),partner.getEmail());;
        this.day = M.getDay();
        this.month = M.getMonth();
        this.year = M.getYear();
        this.startHour = M.getStartHour();
        this.endHour = M.getEndHour();
        this.Available = "false";
    }
    public String getDay(){return day;}
    public String getMonth(){return month;}
    public String getYear(){return year;}

    public String getStartHour(){return startHour;}

    public String getEndHour(){return endHour;}


    public String toString() {
        if(this.ifAvailable()){
            return
                    "Available\n"+
                            "Date: " + day + "/" + month +"/" + year +
                            " from " + startHour +
                            " to " + endHour;
        }
        return
                "Partner: " + partner.toString() +
                        "Date: " + day + "/" + month +"/" + year +
                        " from " + startHour +
                        " to " + endHour;
    }
    public User getPartner() {return partner;}
    public boolean CompareTo(Meeting M){
        if(this.day.equals(M.getDay()) && this.month.equals(M.getMonth())
                &&this.year.equals(M.getYear()) &&
                this.startHour.equals(M.getStartHour())
                && this.endHour.equals(M.getEndHour()))
            return true;
        return false;
    }

    public void addPartner(User partner) {
        if(!ifAvailable()) return;
        this.Available = "false";
        this.partner = partner;
    }
    public void CancelMetting() {
        this.Available = "true";
        this.partner = null;
    }
    public boolean ifAvailable(){
        if(this.partner == null){
            this.Available = "true";
            return true;
        }
        this.Available = "false";
        return false;
    }

    @Override
    public int compareTo(Meeting o) {
        if (Integer.parseInt(this.year) < Integer.parseInt(o.getYear())) return 0;
        else if (Integer.parseInt(this.year) > Integer.parseInt(o.getYear())) return 1;
        else if (Integer.parseInt(this.month) < Integer.parseInt(o.getMonth())) return 0;
        else if (Integer.parseInt(this.month) > Integer.parseInt(o.getMonth())) return 1;
        else if (Integer.parseInt(this.day) < Integer.parseInt(o.getDay())) return 0;
        return 1;
    }
}
