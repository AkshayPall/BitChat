package com.example.akshaypall.bitchat;

/**
 * Created by Akshay Pall on 19/07/2015.
 */
public class Message {
    private String mText;
    private String mSender;

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
}
