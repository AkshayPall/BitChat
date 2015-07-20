package com.example.akshaypall.bitchat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import android.os.Handler;


public class ChatActivity extends ActionBarActivity implements View.OnClickListener, MessageDataSource.Listener{

    public static final String CONTACT_NUMBER = "CONTACT_NUMBER";
    public static final String TAG = "TAG";

    private ArrayList<Message> mMessageArrayList;
    private MessagesAdapter mAdapter;
    private String mCurrentRecipientNumber;
    private ListView mMessagesListView;
    private Date mLastMessageDate;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Polled");
            MessageDataSource.fetchMessagesAfter(ContactDataSource.getCurrentUser().getmPhoneNumber(),
                    mCurrentRecipientNumber,
                    mLastMessageDate,
                    ChatActivity.this);
            mHandler.postDelayed(mRunnable, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mCurrentRecipientNumber = getIntent().getStringExtra(CONTACT_NUMBER);

        mMessageArrayList = new ArrayList<>();
        MessageDataSource.fetchMessages(ContactDataSource.getCurrentUser().getmPhoneNumber(), mCurrentRecipientNumber, this);

        mMessagesListView = (ListView)findViewById(R.id.messages_list);
        mAdapter = new MessagesAdapter(mMessageArrayList);
        mMessagesListView.setAdapter(mAdapter);

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
            MessageDataSource.sendMessage(newMessage.getmSender(), mCurrentRecipientNumber, newMessage.getmText());
        }
    }

    @Override
    public void onFetchedMessages(ArrayList<Message> messages) {
        mMessageArrayList.clear();
        addMessages(messages);
        mHandler.postDelayed(mRunnable, 2000);
    }

    private void addMessages(ArrayList<Message> messages) {
        mMessageArrayList.addAll(messages);
        mAdapter.notifyDataSetChanged();
        if (mMessageArrayList.size() > 0){
            mMessagesListView.setSelection(messages.size()-1);
            mLastMessageDate = messages.get(messages.size()-1).getmDate();
        }
    }

    @Override
    public void onAddMessages(ArrayList<Message> messages) {
        addMessages(messages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
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
