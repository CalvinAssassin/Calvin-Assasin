package edu.calvin.the_b_team.calvinassassingame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private final String baseUrl = "http://153.106.116.66:9998/api";
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
     * the type of http request
     * @param typeOfInfo
     *  The type of information (player or game)
     */
    private void runQuery( String url, String request, String typeOfInfo )
    {
        //launch the network task
        new ConnectToServer().execute( url, request, typeOfInfo );
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
//    public void getTargetLocation()
//    {
//        int targetID = app_preferences.getInt("targetID",0);
//        runQuery(baseUrl + "/location/?playerid=" + targetID, "GET");
//    }

    /**
     * This method will retrieve the id of the target and update.
     */
//    public void getTargetID()
//    {
//        int playerID = app_preferences.getInt("playerID",0);
//        runQuery( baseUrl + "/targetid/?assassinID=" + playerID, "GET"  );
//    }


    /**
     * Queries the database for profile information.
     */
    public void getProfile()
    {
        HashMap hm = new HashMap();
        Player player = new Player(context);
        String playerID = player.getValue( "ID" );
        hm.put("id", playerID );
        String url = baseUrl + "/profile/" + jsonGenerator(hm);
        runQuery( url, "GET", "player");
    }

    /**
     * This method will update the a profile via a PUT request
     */
//    public void updateProfile( )
//    {
//
//    }

    /**
     * This will create a profile and save a permanent user id to shared preferences
     */
    public void createProfile()
    {
        HashMap hm = new HashMap();
        Player player = new Player(context);
        String firstName = player.getValue( "firstName" );
        String lastName = player.getValue( "lastName" );
        String major = player.getValue( "major" );
        String residence = player.getValue( "residence" );
        hm.put("firstName", firstName);
        hm.put("lastName", lastName);
        hm.put("major", major);
        hm.put("residence", residence);
        String url = baseUrl + "/profile/" + jsonGenerator( hm );
        runQuery(url, "POST", "player");

    }

//    //a test function. Will this be needed?
//    public void getPlayers()
//    {
//        runQuery( "http://153.106.116.65:9998/calvinassassin/players", "players" );
//    }

    //the following queries are for game info

    /**
     * createGame will create a game via a POST request
     *
     * @param gameName
     *  The name of the game to be created
     */
    public void createGame( String gameName )
    {
        HashMap hm = new HashMap();
        hm.put("gameName", gameName);
        hm.put("inPlay", false);
        hm.put("creatorID", "");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        hm.put("StartDate", dateFormat.format(date));
        //TODO, let serer handle end date?
        hm.put("endDate", "");
        runQuery(baseUrl + "/games/" + jsonGenerator(hm), "POST", "game");
    }

    /**
     * update an already existing game
     * TODO
     */
    public void updateGame()
    {

    }

    //get a list all of game IDs
    public void getAllGames()
    {
        runQuery(baseUrl + "/games/all", "GET", "game");
    }



    /**
     *
     */

    /**
     * This method creates json and returns it as a string
     * @param values
     *  A hashmap with a key/value for the json
     */
    public String jsonGenerator(HashMap values)
    {
        Iterator iterator = values.entrySet().iterator();
        JSONObject jsonObject = new JSONObject();
        while ( iterator.hasNext())
        {
            Map.Entry pair = (Map.Entry)iterator.next();
            try {
                jsonObject.put(pair.getKey().toString(), pair.getValue().toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
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
                //change the request type (i.e. "Post", "Put", "Get")
                connection.setRequestMethod( params[1] );
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    //save the values
                    save(new JSONArray( result.toString()), params[2]);
                    return new JSONArray(result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                assert connection != null;
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
     * save will save the data that has been received from the server
     * @param jsonArray
     *  The data from the server
     * @param type
     *  Whether the data is for the Player or Game data structure
     */
    public void save( JSONArray jsonArray, String type )
    {
        //convert the jsonArray to an array list of json objects
        ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObjectList.add(jsonObject);

            } catch (Exception e) {

            }
        }
        if( type.equals("game")) {
            Game game = new Game(context);
            game.saveInfo(jsonObjectList);
        }
        else if (type.equals("player"))
        {
            Player player = new Player(context);
            player.saveInfo(jsonObjectList);
        }
    }

    /**
     * This function will save the values from the server's response to shared preferences
     * @param jsonArray
     */
    private void saveFunction( JSONArray jsonArray )
    {
        Log.i("in save ", " function...");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        //convert the jsonArray to an array list of json objects
        ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObjectList.add(jsonObject);

            } catch (Exception e) {

            }
        }
        Log.i( "jsonObjectList.size is", Integer.toString(jsonObjectList.size()));
        //iterate through each jsonObject in the list
        for (int i=0;  i< jsonObjectList.size(); i++ )
        {
            JSONObject jsonObject = jsonObjectList.get(i);
            Iterator<String> iter = jsonObject.keys();
            //go through each JSON object
            while (iter.hasNext()) {
                String key = iter.next();
                //if the server responds with error, break and
                //TODO, what should be done in the case of an error
                if ( key.equals("err")){
                    break;
                }
                try {
                    Object value = jsonObject.get(key);
                    //to implement: save the values to shared preferences;
                    Log.i( "the key is ", key );
                    Log.i( " the value is ", value.toString() );
                    editor.putString(key, value.toString());
                    editor.commit();
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }
        }

    }

}