// This class is the main java class for the standings activity.
// It shows game progress, stats on players, and who's left alive
//
// @author: Christiaan Hazlett, The B Team

package edu.calvin.the_b_team.calvinassassingame;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.games.Game;

import java.util.ArrayList;

public class StandingsViewActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    //Player List Variables
    private ProgressBar progressBar;
    private TextView remainingPlayersText;
    private ListView playerList;
    private ArrayList<String> playerListItems;
    ArrayAdapter playerListArrayAdapter;

    private SharedPreferences app_preferences;

    // BEGIN DEMO VARIABLE DECLARATION
    private boolean joinedGameThree;
    private boolean javinDead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings_view);


        //Load the stored variables
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        joinedGameThree = app_preferences.getBoolean("joinedGameThree",false);
        javinDead = app_preferences.getBoolean("javinDead",false);

        //Load Layout Items
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        remainingPlayersText = (TextView)findViewById(R.id.remainingPlayersText);

        //Set up the menu drawer and its items
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getResources().getText(R.string.standings_activity_title));

        //Games that will start
        String[] playerObjects = {
                //TODO: This is hardcoded until we can retrieve the list of players
                "Nate Bender",
                "Jesse Hulse",
                "Paige Brinks",
                "Frank Boye",
                "Javin Unger",
                "Tupac Shakur",
                "Keither Vander Linden",
                "Ozzy Osbourne",
                "Brad Pitt",
                "James Brown",
                "Lars Ulrich",
                "Axl Rose",
        };
        playerList = (ListView)findViewById(R.id.playerList);
        playerListItems = new ArrayList<String>();
        playerListArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, playerObjects);
        playerList.setAdapter(playerListArrayAdapter);

        //DEMO LOGIC
        if (!joinedGameThree){
            playerList.setVisibility(View.INVISIBLE);
            progressBar.setProgress(0);
            remainingPlayersText.setText("Join a game to view player standings.");
        }
        else{
            if (!javinDead){
                remainingPlayersText.setText("0/12 Players Assassinated");
                progressBar.setProgress(0);
            }
            else{
                remainingPlayersText.setText("1/12 Players Assassinated");
                progressBar.setProgress(1);
            }
        }

    }
    // Beginning of menu drawer configuration

    private void addDrawerItems() {
        String[] menuPages = { "Home", "Profile", "Map", "Join a Game", "Settings" };
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
                getSupportActionBar().setTitle(R.string.standings_activity_title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


//    This is an experiment. It adds the '...' to the action bar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

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
                intent = new Intent(this, GameSelectActivity.class);
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