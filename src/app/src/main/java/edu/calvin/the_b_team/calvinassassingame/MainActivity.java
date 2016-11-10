package edu.calvin.the_b_team.calvinassassingame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button killedButton;
    private AlertDialog.Builder assassinationSentAlert;
    private AlertDialog.Builder targetConfirmationAlert;
    private TextView count_down_textView;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getText(R.string.main_activity_title));

        //Set up the menu drawer and its items
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        killedButton = (Button)findViewById(R.id.killedButton);
        count_down_textView = (TextView)findViewById(R.id.count_down_textView);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        getSupportActionBar().setHomeButtonEnabled(true);

        countDownStart();

        // when killedTarget button is clicked,
        // show a pop up which says a confirmation message has been sent
        // then get response from target and alert the user of their targets response
        assassinationSentAlert = new AlertDialog.Builder(MainActivity.this);
        targetConfirmationAlert = new AlertDialog.Builder(MainActivity.this);

        assassinationSentAlert.setTitle("Target Assassinated");
        assassinationSentAlert.setMessage("Confirmation message has been sent to your target.");

        targetConfirmationAlert.setTitle("Target Assassinated");
        targetConfirmationAlert.setMessage("Target has confirmed assassination! You will be assigned a new target when the next round starts.");

        targetConfirmationAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog2, int id2) {
                Intent intent;
                intent = new Intent(MainActivity.this, StandingsViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        assassinationSentAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int id) {
                // for demo, this just sends the confirmation message 5 secs after
                // assassination request has been sent
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(this!=null && !isFinishing()){
                            // show pop up that target has confirmed the assassination
                            targetConfirmationAlert.show();
                        }
                    }
                }, 5000);
            }
        });
        killedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when this button is clicked, show the alert
                assassinationSentAlert.show();
            }
        });

    }       // onCreate

    /** countDownStart() starts the count down until the end of the round
     * if there is less than one day left, the text will turn red
     */
    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    // Date of end of round
                    Date futureDate = dateFormat.parse("2016-11-16");   // in the future this date will not be hard coded
                    Date currentDate = new Date();
                    // get difference in time from now until futureDate
                    // update it every second
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime() - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        // if there is less than one day left in the round,
                        // turn timer color to red
                        if(days < 1) {
                            count_down_textView.setTextColor(Color.RED);
                        }
                        count_down_textView.setText(String.format("%02d", days) + ":" + "" + String.format("%02d", hours)
                                + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    } else {
                        count_down_textView.setText("TIME UP");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }


    //Beginning of menu drawer configuration
    private void addDrawerItems() {
        String[] menuPages = { "Profile", "Map", "Standings", "Settings" };
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
                getSupportActionBar().setTitle(R.string.main_activity_title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


//    This is an expiriment. It adds the '...' to the action bar
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
                intent = new Intent(this, ProfileViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this,MapCompactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, StandingsViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
        }
    }
    // End of menu drawer configuration



}
