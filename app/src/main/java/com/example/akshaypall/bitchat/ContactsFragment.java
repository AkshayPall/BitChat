package com.example.akshaypall.bitchat;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.akshaypall.bitchat.ContactsFragment.Listener} interface
 * to handle interaction events.
 */
public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener, ContactDataSource.Listener {

    private static final String TAG = "ContactsFragment";
    private String[] mProjectionsFilter =
            {ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
    private Listener mListener;
    private ArrayList<Contact> mContacts = new ArrayList<>();
    private ContactAdapter mAdapter;

    //Parse Column/Attribute Strings(Names)
    private static final String USERNAME_COLUMN = "username";
    private static final String NAME_COLUMN = "name";

    public ContactsFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts,null);

        ListView listView = (ListView)v.findViewById(R.id.list);

        ContactDataSource dataSource = new ContactDataSource(getActivity(),this);

        mAdapter = new ContactAdapter(mContacts);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, dataSource);

        return v;
    }

    @Override
    public void onFetchedContacts(ArrayList<Contact> contacts) {
        mContacts.clear();
        mContacts.addAll(contacts);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contactPressed = mContacts.get(position);
        Log.d(TAG, contactPressed.getmName()+" "+contactPressed.getmPhoneNumber());
        mListener.onContactSelected(contactPressed);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Listener {
        public void onContactSelected(Contact contact);
    }

    private class ContactAdapter extends ArrayAdapter<Contact>{
        ContactAdapter(ArrayList<Contact> contacts){
            super(getActivity(), R.layout.contact_list_item, R.id.list_item_name, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            Contact contact = getItem(position);

            TextView contactName = (TextView)v.findViewById(R.id.list_item_name);
            contactName.setText(contact.getmName());
            TextView contactNumber = (TextView)v.findViewById(R.id.list_item_number);
            contactNumber.setText(contact.getmPhoneNumber());
            return v;
        }
    }
}