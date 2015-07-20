package com.example.akshaypall.bitchat;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Akshay Pall on 19/07/2015.
 */
public class MessageDataSource {
    private static final String MESSAGES_CLASS_NAME = "Messages";
    private static final String SENDER_COLUMN = "sender";
    private static final String RECIPIENT_COLUMN = "recipient";
    private static final String TEXT_COLUMN = "text";

    public static void sendMessage(String sender, String recipient, String text) {
        ParseObject newMessage = new ParseObject(MESSAGES_CLASS_NAME);
        newMessage.put(SENDER_COLUMN, sender);
        newMessage.put(RECIPIENT_COLUMN, recipient);
        newMessage.put(TEXT_COLUMN, text);
        newMessage.saveInBackground();
    }

    public static void fetchMessagesAfter (String sender, String recipient, Date afterDate, final Listener listener){
        ParseQuery<ParseObject> mainQuery = messagesQuery(sender, recipient);
        mainQuery.whereGreaterThan("createdAt", afterDate);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    ArrayList<Message> messages = new ArrayList<Message>();
                    for (ParseObject object : list) {
                        Message message = new Message(object.getString(TEXT_COLUMN), object.getString(SENDER_COLUMN));
                        message.setmDate(object.getCreatedAt());
                        messages.add(message);
                    }
                    listener.onAddMessages(messages);
                }
            }
        });
    }

    public static void fetchMessages (String sender, String recipient, final Listener listener){
        ParseQuery<ParseObject> mainQuery = messagesQuery(sender, recipient);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    ArrayList<Message> messages = new ArrayList<Message>();
                    for (ParseObject object : list) {
                        Message message = new Message(object.getString(TEXT_COLUMN), object.getString(SENDER_COLUMN));
                        message.setmDate(object.getCreatedAt());
                        messages.add(message);
                    }
                    listener.onFetchedMessages(messages);
                }
            }
        });
    }

    private static ParseQuery<ParseObject> messagesQuery (String sender, String recipient) {
        ParseQuery querySent = new ParseQuery(MESSAGES_CLASS_NAME);
        querySent.whereEqualTo(SENDER_COLUMN, sender);
        querySent.whereEqualTo(RECIPIENT_COLUMN, recipient);

        ParseQuery queryReceived = new ParseQuery(MESSAGES_CLASS_NAME);
        queryReceived.whereEqualTo(RECIPIENT_COLUMN, sender);
        queryReceived.whereEqualTo(SENDER_COLUMN, recipient);

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(querySent);
        queries.add(queryReceived);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending("createdAt");
        return mainQuery;
    }

    public interface Listener {
        public void onFetchedMessages(ArrayList<Message> messages);
        public void onAddMessages(ArrayList<Message> messages);
    }
}
