package com.lasergiraffe.rideshare;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.initialize;


public class MainActivity extends ListActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    public static List<Note> posts;
    static boolean started = false; //don't reinitialize parse if already done once

    @Override
    protected void onResume() {
        super.onResume();
        refreshPostList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* sets up parse */
        initializeParse();

        /* prevent from this activity if not logged on */
        checkLoggedOn();

        /* sets up layout */
        setupPosts();

        //BUTTONS :D
        Button switchtonewpage = (Button) findViewById(R.id.newpost_button);
        //Button clear = (Button) findViewById(R.id.clear_button);
        Button logout = (Button) findViewById(R.id.logout_button);
        Button myGroups = (Button) findViewById(R.id.myGroups);

        //listview is the layout for this page
        ListView list = getListView();

        //Click the "New Post" button to make a new post
        switchtonewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPost = new Intent(v.getContext(), newpost.class);
                startActivityForResult(newPost, 1);
                refreshPostList();
                //finish();
            }
        });

        // Click an item on the action bar to show the description
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, OpenedPostActivity.class);

                String noteTitle = ((Note) parent.getItemAtPosition(position)).getTitle();
                String noteDetails = ((Note) parent.getItemAtPosition(position)).getDetails();
                String noteKey = ((Note) parent.getItemAtPosition(position)).getId();
                String noteName = ((Note) parent.getItemAtPosition(position)).getName();
                int noteCapacity = ((Note) parent.getItemAtPosition(position)).getCapacity();
                int noteCurrNumRiders = ((Note) parent.getItemAtPosition(position)).getCurrNumRiders();
                //String noteToDest = ((Note)parent.getItemAtPosition(position)).getToDest();
                //String noteFromDest = ((Note)parent.getItemAtPosition(position)).getFromDest();
                String notePrice = ((Note) parent.getItemAtPosition(position)).getPrice();
                String notePhone = ((Note) parent.getItemAtPosition(position)).getPhone();
                String noteTime = ((Note) parent.getItemAtPosition(position)).getTime();
                String noteDate = ((Note) parent.getItemAtPosition(position)).getDate();
                String noteTimeDate = noteTime + "          " + noteDate;
                String noteUsername = ((Note) parent.getItemAtPosition(position)).getUsername();
                boolean noteIsDriver = ((Note) parent.getItemAtPosition(position)).getDriver();

                i.putExtra(getString(R.string.noteTitle), noteTitle);
                i.putExtra(getString(R.string.noteDetails), noteDetails);
                i.putExtra(getString(R.string.noteKey), noteKey);
                i.putExtra(getString(R.string.noteName), noteName);
                i.putExtra(getString(R.string.noteCapacity), noteCapacity);
                i.putExtra(getString(R.string.noteCurrNumRiders), noteCurrNumRiders);
                //i.putExtra(getString(R.string.noteToDest), noteToDest);
                //i.putExtra(getString(R.string.noteFromDest), noteFromDest);
                i.putExtra(getString(R.string.notePrice), notePrice);
                i.putExtra(getString(R.string.notePhone), notePhone);
                i.putExtra(getString(R.string.noteTimeDate), noteTimeDate);
                i.putExtra(getString(R.string.noteUsername), noteUsername);
                i.putExtra(getString(R.string.noteIsDriver), noteIsDriver);

                startActivity(i);
                refreshPostList();
                //finish();
            }
        });

        /*
        // "Clear" Button (We will get rid of this button)
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ParseQuery<ParseObject> query = ParseQuery.getQuery("notes");
                query.findInBackground(new FindCallback<ParseObject>() {


                    @Override
                    public void done(List<ParseObject> postList, ParseException e) {
                        if (e == null) {
                            // If there are results, update the list of posts
                            // and notify the adapter
                            posts.clear();
                            ParseObject.deleteAllInBackground(postList);
                            ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                        } else {
                            Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                        }
                    }
                });
                refreshPostList();
            }
        });
        */
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                refreshPostList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // "Log out" Button for users to log out
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                //finish();
            }
        });

        //"My Group" Button to show users' own groups
        myGroups.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, MyGroups.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //System.out.println("Created?");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //System.out.println("Created options");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                //finish();
                return true;
        }
        return false;
    }

    // Method that refreshes the action bar from Parse.
    private void refreshPostList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("notes");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    posts.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(), post.getString("noteTitle"),
                                post.getString("noteName"), post.getString("notePhone"),
                                post.getString("noteDetails"), post.getInt("noteCapacity"),
                                post.getInt("noteCurrNumRiders"), post.getString("noteToDest"),
                                post.getString("noteFromDest"), post.getString("notePrice"),
                                post.getString("noteTime"), post.getString("noteDate"),
                                post.getString("noteUsername"), post.getBoolean("noteIsDriver"));
                        note.getObjectId();
                        posts.add(note);
                    }
                    ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    private void checkLoggedOn(){
        final ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            //finish();
        }
    }

    private void setupPosts(){
        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, posts);
        setListAdapter(adapter);
        refreshPostList();
    }

    private void initializeParse(){
        ParseObject.registerSubclass(Note.class);
        ParseObject.registerSubclass(Message.class);


        initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //started = true;
    }
}
