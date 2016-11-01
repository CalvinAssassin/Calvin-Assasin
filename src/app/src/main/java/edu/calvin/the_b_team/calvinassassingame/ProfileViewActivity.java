package edu.calvin.the_b_team.calvinassassingame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageView;
import android.net.Uri;

public class ProfileViewActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private Button choosePhotoButton;
    private Button finalizeButton;
    private EditText playerNameEditable;
    private EditText playerClassEditable;
    private EditText playerHomeEditable;

    private boolean finalized = false;

    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        //Set up the menu drawer and its items
        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setTitle(getResources().getText(R.string.profile_activity_title));

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        profileImage = (ImageView) findViewById(R.id.profile_picture);
        choosePhotoButton = (Button) findViewById(R.id.choose_photo_button);
        finalizeButton = (Button) findViewById(R.id.finalize_profile_button);
        playerNameEditable = (EditText) findViewById(R.id.player_name_editable);
        playerClassEditable = (EditText) findViewById(R.id.player_class_editable);
        playerHomeEditable = (EditText) findViewById(R.id.player_home_editable);

        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.finalize_message);
        dlgAlert.setTitle("Finalize?");
        dlgAlert.setPositiveButton("Finalize",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finalized = true;
                        //Disable the info fields
                        playerNameEditable.setEnabled(false);
                        playerClassEditable.setEnabled(false);
                        playerHomeEditable.setEnabled(false);
                        choosePhotoButton.setEnabled(false);
                    }
                });
        dlgAlert.setNegativeButton("Not yet",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss confirmation message and return
                    }
                });
        dlgAlert.setCancelable(true);

        //Open the photo gallery browser to allow the user to choose a profile photo
        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });
        finalizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAlert.create().show();
            }
        });

    }

    //Set the profile picture after returning from the photo gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profileImage.setImageURI(selectedImage);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profileImage.setImageURI(selectedImage);
                }
                break;
        }
    }

    // Beginning of menu drawer configuration

    private void addDrawerItems() {
        String[] menuPages = { "Home", "Map", "Standings", "Settings" };
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
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, GPSmap.class);
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

    //End of menu drawer configuration



}
