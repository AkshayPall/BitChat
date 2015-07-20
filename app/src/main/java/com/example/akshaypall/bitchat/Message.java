package com.example.akshaypall.bitchat;

import java.util.Date;

/**
 * Created by Akshay Pall on 19/07/2015.
 */
public class Message {
    private String mText;
    private String mSender;
    private Date mDate;

    Message(String text, String sender) {
        mText = text;
        mSender = sender;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }
}
