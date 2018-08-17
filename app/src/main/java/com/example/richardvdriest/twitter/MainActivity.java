package com.example.richardvdriest.twitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    EditText editTextSigninUsername;
    EditText editTextSigninPassword;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.signup){
            Intent signupIntent = new Intent();
            signupIntent.setClass(getApplicationContext(), SignupActivity.class);
            startActivity(signupIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view){

        if(editTextSigninPassword.getText().toString().matches("") || editTextSigninUsername.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show();
        }else {
            ParseUser.logInInBackground(editTextSigninUsername.getText().toString(), editTextSigninPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null || e == null) {
                        //login succesfull
                        Intent feedIntent = new Intent();
                        feedIntent.setClass(getApplicationContext(), FeedActivity.class);
                        feedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(feedIntent);
                        finish();
                    } else {
                        //login fail
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            Intent feedIntent = new Intent();
            feedIntent.setClass(getApplicationContext(), FeedActivity.class);
            feedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(feedIntent);
            finish();
        }

        editTextSigninUsername = findViewById(R.id.editTextLoginUsername);
        editTextSigninPassword = findViewById(R.id.editTextLoginPassword);
    }
}
