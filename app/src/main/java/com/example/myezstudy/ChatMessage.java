package com.example.myezstudy;

import androidx.annotation.NonNull;

public class ChatMessage extends Message {
    private String partner;

    public ChatMessage(){}
    public ChatMessage(String partner, String txt){
        super(txt);
        this.partner = partner;
    }
    public String getPartner(){return this.partner;}
    public void setPartner(String partner){ this.partner = partner;}

    @Override
    public String toString() {
        return
                "Date: " + super.getcreatedDay() + "/" + super.getcreatedMonth()
                        + "/" + super.getcreatedYear() + " " +
                        super.getcreatedHour() + ":" + super.getcreatedMinute()
                        + "\nFrom: " + partner;
    }
    public String prineMsg() {
        return
                "Date: " + super.getcreatedDay() + "/" + super.getcreatedMonth()
                        + "/" + super.getcreatedYear() + " " +
                        super.getcreatedHour() + ":" + super.getcreatedMinute()
                        + "\nFrom: " + partner + ":\n"
                    + super.gettheMessage();
    }

}
