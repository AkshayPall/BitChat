package com.example.akshaypall.bitchat;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignInActivity extends ActionBarActivity {

    private EditText mNumberEdittext;
    private EditText mPasswordEdittext;
    private EditText mNameEdittext;
    private static final String TAG = "TAG";

    //List of extra attributes/columns used in User data table
    private static final String NAME_COLUMN = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TelephonyManager telephonyManager = (TelephonyManager)this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();

        mNumberEdittext = (EditText)findViewById(R.id.user_number);
        mNumberEdittext.setText(phoneNumber);
        mPasswordEdittext = (EditText)findViewById(R.id.user_password);
        mNameEdittext = (EditText)findViewById(R.id.user_name);

        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(mNumberEdittext.getText().toString()); //have to user # for username as username MUST BE UNIQUE, and this may not be true for real names
                user.setPassword(mPasswordEdittext.getText().toString());
                user.put(NAME_COLUMN, mNameEdittext.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d(TAG, "User successfully signed up");
                            SignInActivity.this.finish();
                        } else {
                            Toast signupFailedToast = Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_LONG);
                            signupFailedToast.show();
                        }
                    }
                });
            }
        });

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(mNumberEdittext.getText().toString(),
                        mPasswordEdittext.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    //TODO: SUCCESS
                                    SignInActivity.this.finish();
                                } else {
                                    Toast loginFailedToast = Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG);
                                    loginFailedToast.show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
}
