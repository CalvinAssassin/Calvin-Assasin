package edu.calvin.the_b_team.calvinassassingame;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.location.LocationListener;

import com.google.android.gms.games.Game;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


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
    private ImageView splashImage;
    private Handler handler;
    private Runnable runnable;
    private boolean targetEliminated;
    private SharedPreferences savedValues;

    private LocationManager locationManager;
    private LocationListener listener;
    Context context = this;


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
        targetEliminated = false;
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

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
        targetConfirmationAlert.setMessage("Target has confirmed assassination! You will be assigned a new target soon.");

        targetConfirmationAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog2, int id2) {
                targetEliminated = true;
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
        //get the gps coordinates
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        configure();
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
                    //TODO change the futureDate to load from a sharedPreferences that can sync to the server
                    Date futureDate = dateFormat.parse("2016-12-25");   // in the future this date will not be hard coded
                    Date currentDate = new Date();
                    // get difference in time from now until futureDate
                    // update it every second
                    if (currentDate.after(futureDate)) {
                        count_down_textView.setText("TIME UP");
                    } else if (targetEliminated) {
                        count_down_textView.setTextColor(Color.RED);
                        count_down_textView.setText("TARGET\nELIMINATED");
                    } else {
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
                        if (days < 1) {
                            count_down_textView.setTextColor(Color.RED);
                        }
                        count_down_textView.setText(String.format("%02d", days) + ":" + "" + String.format("%02d", hours)
                                + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    /**
     * configue will check to see if the proper permissions are in place and prompt the user if not.
     * then it will retrieve the user's coordinates and send them to the server
     * @return
     *  returns the location
     */
    public Location configure(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return null;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        //noinspection MissingPermission
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null)
        {
            Log.i("the location is", location.toString());
            Toast.makeText(context, "coordinates" + location.getLongitude() + " " + location.getLatitude(), Toast.LENGTH_LONG).show();
            ServerCommunication server = new ServerCommunication(context);
            Player player = new Player(context);
            player.save("latitude", location.getLatitude());
            player.save("longitude", location.getLongitude());;
            server.updateUserProfileCoordinatesWithoutDelay( "latitude",  location.getLatitude());
            server.updateUserProfileCoordinatesWithoutDelay("longitude", location.getLongitude());
            return location;
        }
        return null;
    }

    /**
     * this function is called when requestPermission is called.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure();
                break;
            default:
                break;
        }
    }


    //Beginning of menu drawer configuration
    private void addDrawerItems() {
        String[] menuPages = { "Profile", "Map", "Standings", "Join a Game", "Settings" };
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
    // End of menu drawer configuration

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putBoolean("targetEliminatedBool", targetEliminated);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        targetEliminated = savedValues.getBoolean("targetEliminatedBool", false);
    }



}