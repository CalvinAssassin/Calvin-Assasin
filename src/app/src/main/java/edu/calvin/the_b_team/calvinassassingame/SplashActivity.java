package edu.calvin.the_b_team.calvinassassingame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This class helps to create the splash screen when the app loads
 */
public class SplashActivity extends Activity {

    private SharedPreferences app_preferences;
    private boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load the stored variables
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstRun = app_preferences.getBoolean("firstRun",true);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent;
                if (firstRun) {
                    intent = new Intent(SplashActivity.this, ProfileViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
                else {
                        //If this isnt the first time the app has been opened, go to the main Activity
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                }
            }
        }, 1500);
    }
}