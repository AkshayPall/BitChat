package com.example.akshaypall.bitchat;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;


public class ChatActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ArrayList<Message> messageArrayList = new ArrayList<>();
        messageArrayList.add(new Message("Test", ContactDataSource.getCurrentUser().getmPhoneNumber()));
        messageArrayList.add(new Message("Test2", "4168483178"));

        ListView messagesListView = (ListView)findViewById(R.id.messages_list);
        messagesListView.setAdapter(new MessagesAdapter(messageArrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages){
            super(ChatActivity.this, R.layout.messages_list_item, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            Message message = getItem(position);
            TextView messageView = (TextView)v.findViewById(R.id.message);
            messageView.setText(message.getmText());

            if (message.getmSender().equals(ContactDataSource.getCurrentUser().getmPhoneNumber())){
                messageView.setBackgroundColor(Color.GREEN);
            } else {
                messageView.setBackgroundColor(Color.BLUE);
            }

            return v;
        }
    }
}
