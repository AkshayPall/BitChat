package com.example.akshaypall.bitchat;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay Pall on 19/07/2015.
 */
public class ContactDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "TAG";
    private Context mContext;
    private Listener mListener;

    //Parse Column/Attribute Strings(Names)
    private static final String USERNAME_COLUMN = "username";
    private static final String NAME_COLUMN = "name";

    ContactDataSource(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Cursor Loaded!");
        List<String> numbers = new ArrayList<>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            String phoneNumber = data.getString(1);
            phoneNumber = phoneNumber.replaceAll("-", "");
            phoneNumber = phoneNumber.replaceAll(" ", "");
            phoneNumber = phoneNumber.replaceAll("\\(", "");
            phoneNumber = phoneNumber.replaceAll("\\)", "");
            numbers.add(phoneNumber);
            data.moveToNext();
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn(USERNAME_COLUMN, numbers);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    //TODO: swap cursors now to load data
                    Log.d(TAG, "Contacts query successful");
//                    mContacts.clear();
                    ArrayList<Contact> contacts = new ArrayList<Contact>();
                    for(ParseUser user: list){
                        Contact contact = new Contact();
                        contact.setmName((String)user.get(NAME_COLUMN));
                        contact.setmPhoneNumber(user.getUsername());
                        contacts.add(contact);
                    }
                    if (mListener != null) {
                        mListener.onFetchedContacts(contacts);
                    }
                } else {
                    Toast queryFailedToast = Toast.makeText(mContext, "Query Failed", Toast.LENGTH_LONG);
                    queryFailedToast.show();
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mCursorAdapter.swapCursor(null);
    }

    public interface Listener{
        public void onFetchedContacts(ArrayList<Contact> contacts);
    }
}
