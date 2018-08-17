package com.example.richardvdriest.twitter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    ParseUser user;
    ParseObject tweet;
    BottomNavigationView navigation;
    RecyclerView mRecylclerView;
    FeedAdapter feedAdapter;
    List<Tweet> feedListItems;

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.feed:
                    return true;
                case R.id.users:
                    findUserButtonHandler();
                    return true;
                case R.id.tweetButton:
                    tweetButtonHandler();
                    return true;
                case R.id.logout:
                    logoutButtonHandler();
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void startNewActivity(Class activity){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), activity);
        startActivity(intent);
    }

    public void logoutButtonHandler(){
        ParseUser.logOut();
        startNewActivity(MainActivity.class);
    }

    public void findUserButtonHandler(){
        startNewActivity(UsersActivity.class);
        finish();
    }

    public void tweetButtonHandler(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send a Tweet");
        final EditText tweetEditText = new EditText(this);
        builder.setView(tweetEditText);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                navigation.setSelectedItemId(R.id.feed);
                if(tweetEditText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill in your tweet", Toast.LENGTH_SHORT).show();
                }else {
                    tweet.put("tweet", tweetEditText.getText().toString());
                    tweet.put("username", ParseUser.getCurrentUser().getUsername());

                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(FeedActivity.this, "Tweet sent!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(FeedActivity.this, "Tweet Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                Log.i("Send button hit", tweetEditText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                navigation.setSelectedItemId(R.id.feed);
                Log.i("Info", "Tweet cancelled");
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            logoutButtonHandler();
        }else if(item.getItemId() == R.id.users){
            findUserButtonHandler();
        }else if(item.getItemId() == R.id.tweetButton){
            tweetButtonHandler();
        }else if(item.getItemId() == R.id.feed){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        user = ParseUser.getCurrentUser();

        tweet = new ParseObject("Tweet");

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


        mRecylclerView = findViewById(R.id.listView);
        feedListItems = new ArrayList<>();
        mRecylclerView.setLayoutManager(new LinearLayoutManager(this));




        final List<Map<String, String>> tweetData = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject tweet : objects){
                    Map<String, String> tweetInfo = new HashMap<>();
                    tweetInfo.put("content", tweet.getString("tweet"));
                    tweetInfo.put("username", tweet.getString("username"));
                    tweetData.add(tweetInfo);
                    Log.i("Ïnfo", "Tweet succesvol opgehaald");
                    feedListItems.add(new Tweet(R.drawable.ic_launcher_background, tweetInfo.get("username"), tweetInfo.get("content")));
                    feedAdapter = new FeedAdapter(FeedActivity.this, feedListItems);
                    //SimpleAdapter simpleAdapter = new SimpleAdapter(FeedActivity.this, tweetData, android.R.layout.simple_list_item_2, new String[] {"content", "username"}, new int[] {android.R.id.text1, android.R.id.text2});
                    mRecylclerView.setAdapter(feedAdapter);
                }
            }
        });
    }
}
