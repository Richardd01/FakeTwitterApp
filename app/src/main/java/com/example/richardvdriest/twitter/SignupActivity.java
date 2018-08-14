package com.example.richardvdriest.twitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void signup(View view){
        emailEditText = findViewById(R.id.editTextSignupEmail);
        usernameEditText = findViewById(R.id.editTextSignupUsername);
        passwordEditText = findViewById(R.id.editTextSignupPassword);


        ParseUser user = new ParseUser();
        user.setEmail(emailEditText.getText().toString());
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Intent feedIntent = new Intent();
                    feedIntent.setClass(getApplicationContext(), FeedActivity.class);
                    feedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(feedIntent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
