package edu.calvin.the_b_team.calvinassassingame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

/**
 * Created by jjh35 on 11/15/2016.
 *  This class facilitates all communication between the app and the server. The methods do not
 *  return any values, rather they update the corresponding variables in shared preferences.
 */
public class ServerCommunication {
    //the base URL for our Server
    private final String baseUrl = "http://153.106.116.71:9998/calvinassassin";
    private SharedPreferences app_preferences;
    private Context context;

    /**
     *  The default constructor. In order for the class to access shared preferences, a context m
     *  must be passed from the activity requesting info from the server.
     * @param contextFromActivity
     *  The context from the activity
     */
    ServerCommunication( Context contextFromActivity ) {
        context = contextFromActivity;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
    }


    private void runQuery()
    {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

            }
        }, 1000);
    }

    /**
     * getTargetLocation will retrieve and update the coordinates of the target
     */
    public void getTargetLocation()
    {

        Log.i("the url is ", baseUrl + "/location/?playerid=" + 1 );
        try {
            app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int targetID = app_preferences.getInt("targetID",0);
            new ConnectToServer().execute( baseUrl + "/location/?playerid=" + targetID, "targetLocation" );
            Log.i("url is", baseUrl + "/location/?playerid=" + targetID );
        }
        catch (Exception e)
        {
           Log.i("error in ", "getTargetLocation" );
            Log.e("stacktrace", "exception", e);
        }
    }

    /**
     * This method will retrieve the id of the target and update.
     */
    public void getTargetID()
    {
        try {
            app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int playerID = app_preferences.getInt("playerID",0);
            new ConnectToServer().execute( baseUrl + "/targetid/?assassinID=" + playerID, "targetID" );
        }
        catch (Exception e)
        {
            //need anything
        }
    }

    /**
     * This will create a profile and save a permanent user id to shared preferences
     */
    public void createProfile()
    {
        String query;
        try {
            app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String name = app_preferences.getString("playerName","");
            int year = app_preferences.getInt("playerClass",0);
            int residence = app_preferences.getInt("playerHome",0);
            query = "/createuserprofile/?username=" + name + "&name=" + name +
                    "&residence=" + residence + "&year=" + year;
            new ConnectToServer().execute( baseUrl + query, "createProfile");
        }
        catch (Exception e)
        {
            //need anything?
        }

    }

    private class ConnectToServer extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                Log.i("the params[0] is ", params[0]);
                Log.i("the params[1] is ", params[1]);
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    //String[] answer = {result.toString(), params[1] };
                    saveInfo( new JSONArray( result.toString() ), params[1] );
                    return new JSONArray(result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
               // saveInfo( response );
            } else {
               // Toast.makeText(MainActivity.this, "invalid id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveInfo( JSONArray jsonArray, String request )
    {
        Log.i("made it", " to saveInfo()");
        ArrayList<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        }
        catch (Exception e )
        {
            //need anything?
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (request.equals("targetLocation"))
        {
            Log.i("made it", " inside of request.equals(targetLocation");
            editor.putLong("targetLatitude", Double.doubleToLongBits(Double.parseDouble(list.get(0))));
            editor.putLong("targetLongitude",  Double.doubleToLongBits(Double.parseDouble(list.get(1))) );
            editor.commit(); // Commit the changes to the preferences file

        }
        else if (request.equals("targetID"))
        {
            editor.putInt("targetID", Integer.parseInt(list.get(0)));
            editor.commit(); // Commit the changes to the preferences file
        }
        else if (request.equals("createProfile"))
        {
            editor.putInt( "playerID", Integer.parseInt( list.get(0) ));
            editor.commit();
        }

    }

}

