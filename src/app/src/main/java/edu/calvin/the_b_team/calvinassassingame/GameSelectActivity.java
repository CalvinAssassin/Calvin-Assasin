package edu.calvin.the_b_team.calvinassassingame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.games.Game;

import java.util.ArrayList;

public class GameSelectActivity extends AppCompatActivity {

    //Initialize Drawer and Layout things
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView upcomingGameList;
    private ArrayList<String> upcomingListItems;
    ArrayAdapter upcomingGameListArrayAdapter;

    private ListView activeGameList;
    private ArrayList<String> activeListItems;
    ArrayAdapter activeGameListArrayAdapter;

    //Runtime Variables
    private SharedPreferences app_preferences;
    boolean settingsFinalized;

    // BEGIN DEMO VARIABLE DECLARATION
    private boolean joinedGameThree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        //Load runtime variables from app_preferences
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        settingsFinalized = app_preferences.getBoolean("settingsFinalized",false);
        joinedGameThree = app_preferences.getBoolean("joinedGameThree",false);

        //Set up the menu drawer and its items
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Set up other layout items

        //Define the confirmation dialogue to join a game
        final AlertDialog.Builder joinConfirmAlert  = new AlertDialog.Builder(this);
        joinConfirmAlert.setMessage(R.string.join_confirm_message);
        joinConfirmAlert.setTitle("Join this game?");
        joinConfirmAlert.setCancelable(true);
        joinConfirmAlert.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            /* TODO: Join the game & draw a checkmark by that game in the list to indicate that the user will participate
             * in this game. The checkmark will also be visible when the game is active.
             */
                joinedGameThree = true;
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putBoolean("joinedGameThree", joinedGameThree);
                editor.commit(); // Commit the changes to the preferences file
            }
        });
        joinConfirmAlert.setNegativeButton("Maybe not...", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss confirmation message and return
            }
        });

        //Define the confirmation dialogue to leave a game
        final AlertDialog.Builder leaveConfirmAlert  = new AlertDialog.Builder(this);
        leaveConfirmAlert.setMessage(R.string.leave_confirm_message);
        leaveConfirmAlert.setTitle("Leave this game?");
        leaveConfirmAlert.setCancelable(true);
        leaveConfirmAlert.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            /* TODO: Leave the game & remove the checkmark by that game in the list to indicate that the user will no longer participate
             * in this game. If this game has already started and is in the active list, this will be a forfeit
             */
                joinedGameThree = false;
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putBoolean("joinedGameThree", joinedGameThree);
                editor.commit(); // Commit the changes to the preferences file
                Toast.makeText(getBaseContext(), "Game Left", Toast.LENGTH_SHORT).show();
            }
        });
        joinConfirmAlert.setNegativeButton("Maybe not...", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss confirmation message and return
            }
        });

        //Games that will start
        String[] upcomingGameObjects = {
                //TODO: This is hardcoded until we can retrieve the list of games
                "Game 3 - Begins: 6 December 2016 - 10 Players",
        };
        upcomingGameList = (ListView)findViewById(R.id.upcomingList);
        upcomingListItems = new ArrayList<String>();
        upcomingGameListArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, upcomingGameObjects);
        upcomingGameList.setAdapter(upcomingGameListArrayAdapter);

        String[] activeGameObjects = {
                //TODO: This is hardcoded until we can retrieve the list of games
                "Game 1 - Began: 22 November 2016 - 19 Players",
                "Game 2 - Began: 29 November 2016 - 22 Players"
        };
        activeGameList = (ListView)findViewById(R.id.activeList);
        activeListItems = new ArrayList<String>();
        activeGameListArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, activeGameObjects);
        activeGameList.setAdapter(activeGameListArrayAdapter);


        //User selects a game to join from the upcomingGameList
        upcomingGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                //Toast.makeText(getBaseContext(), "Join game clicked", Toast.LENGTH_SHORT).show();
                if (!settingsFinalized){
                    Toast.makeText(getBaseContext(), "You must finalize your profile before joining a game!", Toast.LENGTH_LONG).show();
                }
                else {
                     if (!joinedGameThree){
                        joinConfirmAlert.show();
                    }
                    else {
                        leaveConfirmAlert.show();
                    }
                }
            }
        });
    }

    // Beginning of menu drawer configuration

    private void addDrawerItems() {
        String[] menuPages = { "Home", "Profile", "Map", "Standings", "Settings" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuPages);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Choose one!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.profile_activity_title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {

        // Handle Navigation Options
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, ProfileViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, MapCompactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, StandingsViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
        }
    }
    //End of menu drawer configuration
}
