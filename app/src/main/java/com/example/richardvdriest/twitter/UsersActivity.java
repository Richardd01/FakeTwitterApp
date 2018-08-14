package com.example.richardvdriest.twitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter adapter;

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
