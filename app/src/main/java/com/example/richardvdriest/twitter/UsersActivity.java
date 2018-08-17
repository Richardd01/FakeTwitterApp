package com.example.richardvdriest.twitter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter adapter;

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.feed:
                    feedButtonHandler();
                    return true;
                case R.id.users:

                    return true;
                case R.id.tweetButton:
                    //Tweet
                    return true;
                case R.id.logout:
                    logoutButtonHandler();
                    return true;
            }
            return false;
        }
    };


    public void logoutButtonHandler(){
        ParseUser.logOut();
        startNewActivity(MainActivity.class);
    }

    public void feedButtonHandler(){
        startNewActivity(FeedActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            logoutButtonHandler();
        }else if(item.getItemId() == R.id.tweetButton){
            //moet nog gebeuren
        }else if(item.getItemId() == R.id.feed){
            feedButtonHandler();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Follow(int i){
        //Voegt naam toe bij isFollowing
        ParseUser.getCurrentUser().add("isFollowing", users.get(i));
        Log.i("username", users.get(i));
        Log.i("My username", ParseUser.getCurrentUser().getUsername());
        //Voegt naam toe bij isFollowedBy (WERKT NOG NIET))))!!!!
//        ParseQuery<ParseUser> userClickedQuery = ParseUser.getQuery();
//        userClickedQuery.whereEqualTo("username", users.get(i));
//        userClickedQuery.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if(e == null && objects.size() > 0){
//                    for(ParseUser followedUser : objects){
//                        followedUser.add("isFollowedBy", ParseUser.getCurrentUser().getUsername());
//                        Log.i("username", followedUser.getUsername());
//                        Log.i("My username", ParseUser.getCurrentUser().getUsername());
//                    }
//                }else{
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    public void startNewActivity(Class activity){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), activity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startNewActivity(FeedActivity.class);
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        final ListView userListView = findViewById(R.id.userList);
        userListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.users);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);

        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()){
                    Follow(i);
                }else{
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(i));
                    List tempUsers = ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", tempUsers);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseUser user : objects){
                        users.add(user.getUsername());
                    }

                    adapter.notifyDataSetChanged();

                    for(String username : users){
                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)){
                            userListView.setItemChecked(users.indexOf(username), true);
                        }
                    }
                }
            }
        });

    }
}
