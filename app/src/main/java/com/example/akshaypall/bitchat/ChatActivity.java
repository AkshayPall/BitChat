package com.example.akshaypall.bitchat;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;


public class ChatActivity extends ActionBarActivity implements View.OnClickListener{

    private ArrayList<Message> mMessageArrayList;
    private MessagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageArrayList = new ArrayList<>();
        mMessageArrayList.add(new Message("Test", ContactDataSource.getCurrentUser().getmPhoneNumber()));
        mMessageArrayList.add(new Message("Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2 Test2 Test2Test2 Test2Test2Test2  ",
                "4168483178"));
        mMessageArrayList.add(new Message("success", "4168483178"));

        ListView messagesListView = (ListView)findViewById(R.id.messages_list);
        mAdapter = new MessagesAdapter(mMessageArrayList);
        messagesListView.setAdapter(mAdapter);

        Button sendMessageButton = (Button)findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText messageField = (EditText)findViewById(R.id.new_message_field);
        String text = messageField.getText().toString();
        if (!text.equals("")) {
            Message newMessage = new Message(text, ContactDataSource.getCurrentUser().getmPhoneNumber());
            mMessageArrayList.add(newMessage);
            mAdapter.notifyDataSetChanged();
            messageField.setText("");
        }
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

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)messageView.getLayoutParams();

            if (message.getmSender().equals(ContactDataSource.getCurrentUser().getmPhoneNumber())){
                messageView.setBackground(getDrawable(R.drawable.bubble_right_green));
                layoutParams.gravity = Gravity.RIGHT;
            } else {
                messageView.setBackground(getDrawable(R.drawable.bubble_left_grey));
                layoutParams.gravity = Gravity.LEFT;
            }
            messageView.setLayoutParams(layoutParams);
            return v;
        }
    }
}
