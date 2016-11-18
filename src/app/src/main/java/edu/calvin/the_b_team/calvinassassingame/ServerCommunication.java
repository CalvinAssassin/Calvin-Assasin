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
 *  This class facilitates all communication between the app and the server. All information will
 *  be saved in shared preferences so that all activities can access them. The methods do not
 *  return any values, rather they update the corresponding variables in shared preferences. So,
 *  one will call a method (i.e. getTargetLocation() and then check the values in shared
 *  shared preference
 */
public class ServerCommunication {
    //the base URL for our Server
    private final String baseUrl = "http://153.106.116.83:9998/calvinassassin";
    private SharedPreferences app_preferences;
    private Context context;

    /**
     *  The default constructor. In order for the class to access shared preferences, a context
     *  must be passed from the activity requesting info from the server.
     * @param contextFromActivity
     *  The context from the activity
     */
    ServerCommunication( Context contextFromActivity ) {
        context = contextFromActivity;
        //allows information to be pulled from shared preferences
        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * This method will take in an url and a type of request and call on ConnectToServer.execute
     * to send the network request
     * @param url
     * The uri resource on the server
     * @param request
     * the type of request
     */
    private void runQuery( String url, String request)
    {
        //launch the network task
        new ConnectToServer().execute( url, request );
        //delay the app so that the server has time to respond
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
        int targetID = app_preferences.getInt("targetID",0);
        runQuery(baseUrl + "/location/?playerid=" + targetID, "targetLocation");
    }

    /**
     * This method will retrieve the id of the target and update.
     */
    public void getTargetID()
    {
        int playerID = app_preferences.getInt("playerID",0);
        runQuery( baseUrl + "/targetid/?assassinID=" + playerID, "targetID"  );
    }

    /**
     * This will create a profile and save a permanent user id to shared preferences
     */
    public void createProfile()
    {
        String query;
        try {
            String name = app_preferences.getString("playerName","");
            int year = app_preferences.getInt("playerClass",0);
            int residence = app_preferences.getInt("playerHome",0);
            query = "/createuserprofile/?username=" + name + "&name=" + name +
                    "&residence=" + residence + "&year=" + year;
            runQuery( query, "createProflie" );
        }
        catch (Exception e)
        {
            //need anything?
        }

    }

    /**
     * This class launches an asynchronous task to do a network request. It will contact
     * the specified url and store the response in a JSON array
     *
     * @String url, the uri of the resource on our server
     *
     * @String request, the type of resource requested
     */
    private class ConnectToServer extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    //save the server's response to the phone
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

        //is this method needed? Will it be needed?
        @Override
        protected void onPostExecute(JSONArray response) {
            if (response != null) {

            } else {

            }
        }
    }

    /**
     * This method will convert a json array to an array list, and save the corresponding
     * information.
     *
     * @param jsonArray
     *  The Json array from the server with the server's response
     * @param request
     * the type of information requested
     */
    private void saveInfo( JSONArray jsonArray, String request )
    {
        //convert the JSON array to an arrayList
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
        switch (request) {
            case "targetLocation" :
                editor.putLong("targetLatitude", Double.doubleToLongBits(Double.parseDouble(list.get(0))));
                editor.putLong("targetLongitude",  Double.doubleToLongBits(Double.parseDouble(list.get(1))));
                break;
            case "targetID" :
                editor.putInt("targetID", Integer.parseInt(list.get(0)));
                break;
            case "createProfile" :
                editor.putInt( "playerID", Integer.parseInt(list.get(0)));
                break;
        }
        editor.commit();

    }

}

