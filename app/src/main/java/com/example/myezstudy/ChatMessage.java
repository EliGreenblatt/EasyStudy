package com.example.myezstudy;

import androidx.annotation.NonNull;

/**
 * This class represents a chat message between the users.
 * Extends Message class, that class contains basic message information.
 */
public class ChatMessage extends Message {

    private String partner; // The other user of the meeting

    /**
     * Default constructor.
     */
    public ChatMessage(){}

    /**
     * Constructor with partner and text parameters.
     * @param partner other user in the communication.
     * @param txt text message content.
     */
    public ChatMessage(String partner, String txt){
        super(txt);
        this.partner = partner;
    }

    /**
     * Get the partner of the chat message.
     * @return username.
     */
    public String getPartner() {return this.partner;}

    /**
     * Set the partner of the chat message.
     * @param partner username to set.
     */
    public void setPartner(String partner) { this.partner = partner;}

    /**
     * @return string represent the message date and partner.
     */
    @Override
    public String toString() {
        return "Date: " + super.getCreatedDay() + "/" + super.getCreatedMonth()
                + "/" + super.getCreatedYear() + " " +
                super.getCreatedHour() + ":" + super.getCreatedMinute()
                + "\nFrom: " + partner;
    }

    /**
     * print the information of the chat message.
     * @return string including date, partner, and message content.
     */
    public String printMsg() {
        return "Date: " + super.getCreatedDay() + "/" + super.getCreatedMonth()
                + "/" + super.getCreatedYear() + " " +
                super.getCreatedHour() + ":" + super.getCreatedMinute()
                + "\nFrom: " + partner + ":\n"
                + super.getTheMessage();
    }
}
