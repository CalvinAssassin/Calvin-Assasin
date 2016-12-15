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
import java.io.DataOutputStream;
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
    //private final String baseUrl = "http://153.106.116.67:8082/api";
    private final String baseUrl = "http://153.106.161.150:8082/api";
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
    private void runQuery( String url, String request, String typeOfInfo, String jsonString )
    {
        //launch the network task based on type of http request
        switch (request)
        {
            case "GET":
                try {
                    //the .get() delays the execution until the network call has finished
                    Log.i( "here", " in runQuery");
                    new GetTask().execute(url, typeOfInfo).get();
                }
                catch (Exception e)
                {
                    //if the .get() throws an exception, just run the task normally
                    new GetTask().execute(url, typeOfInfo);
                }
                break;
            case "POST":
                try {
                    new PostTask().execute(url, typeOfInfo, jsonString).get();
                }
                catch (Exception e)
                {
                    new PostTask().execute(url, typeOfInfo);
                }
                break;
            case "PUT":
                try {
                    new PutTask().execute(url, typeOfInfo, jsonString).get();
                }
                catch (Exception e)
                {
                    new PutTask().execute(url, typeOfInfo, jsonString);
                }
        }
    }

    /**
     * this method will contact the server and confirm an assasssination
     * TODO: test
     */
    public void confirmAssassination(){
        Player player = new Player(context);
        String url = baseUrl + "/profile/" + player.getID() + "/target/assassinate";
        HashMap hm = new HashMap();
        hm.put("need", "json");
        new PostTask().execute(url, "game",jsonGenerator(hm));
    }

    /**
     * This method will retrieve the values from the server and save them to shared preferences
     */
    public void getGame()
    {
        GameClass game = new GameClass(this.context);
        int gameID = game.getID();
        Log.i("the game url", baseUrl + "/game/" + gameID);
        new GetTask().execute(baseUrl + "/game/" + gameID, "game", "");
    }

    /**
     * the method will return the target's information
     */
    public void getTargetInfo()
    {
        Player player = new Player(this.context);
        String url = baseUrl + "/profile/" + player.getID() + "/target";
        new GetTask().execute(url, "game", "");
    }

    /**
     * Queries the database for profile information and saves info.
     */
    public void getUserProfile()
    {
        Player player = new Player(context);
        int playerID = player.getID();
        String url = baseUrl + "/profile/" + playerID;
        Log.i("url is ", url);
        Log.i("the url for userProfile", url);
        runQuery( url, "GET", "player", "");
    }

    /**
     * Creates a new user profile and recieves userID from server
     */
    public void createUserProfile()
    {
        HashMap hm = new HashMap();
        Player player = new Player(context);
        String firstName = player.getFirstName();
        String lastName = player.getLastName();
        String major = player.getMajor();
        String residence = player.getResidence();
        hm.put("firstName", firstName);
        hm.put("lastName", lastName);
        hm.put("major", major);
        hm.put("residence", residence);
        String url = baseUrl + "/profile";
        runQuery(url, "POST", "player", jsonGenerator(hm));
    }

    /**
     * sends an updated value for the user profile to the server
     * @param key
     *  The name of the field being updated
     * @param value
     *  the string value of the field
     */
    public void updateUserProfile( String key, String value)
    {
        HashMap hm = new HashMap();
        hm.put(key, value);
        Player player = new Player(context);
        int playerID = player.getID();
        String url = baseUrl + "/profile/" + playerID;
        Log.i("url ", url);
        runQuery(url, "PUT", "player", jsonGenerator(hm));
    }

    /**
     * gets a list of current games from the server
     * and saves it to shared preferences under "currentGames"
     */
    public void getCurrentGames()
    {
        String url = baseUrl + "/games";
        runQuery( url, "GET", "currentGames", "");
    }

    /**
     * gets a list of fture games from the server
     * and saves it to shared preferences under "futureGames"
     */
    public void getFutureGames()
    {
        String url = baseUrl + "/games/future";
        runQuery(url, "GET", "futureGames", "");
    }

    /**
     * This function can be usedexcept that it will not delay
     * the main thread until the server response has come back.
     * @param key
     * @param value
     */
    public void updateUserProfileCoordinatesWithoutDelay(String key, double value)
    {
        HashMap hm = new HashMap();
        hm.put(key, value);
        Player player = new Player(context);
        int playerID = player.getID();
        String url = baseUrl + "/profile/" + 10;
        new PutTask().execute(url, "player", jsonGenerator(hm));
    }

    /**
     * sends an updated value for the user profile to the server
     * @param key
     *  The name of the field being updated
     * @param value
     *  the int value of the field
     */
    public void updateUserProfile( String key, int value)
    {
        HashMap hm = new HashMap();
        hm.put(key, value);
        Player player = new Player(context);
        int playerID = player.getID();
        String url = baseUrl + "/profile/" + playerID;
        runQuery(url, "PUT", "player", jsonGenerator(hm));
    }

    /**
     * sends an updated value for the user profile to the server
     * @param key
     *  The name of the field being updated
     * @param value
     *  the double value of the field
     */
    public void updateUserProfile( String key, double value)
    {
        HashMap hm = new HashMap();
        hm.put(key, value);
        Player player = new Player(context);
        int playerID = player.getID();
        String url = baseUrl + "/profile/" + playerID;
        runQuery(url, "PUT", "player", jsonGenerator(hm));
    }

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
    private class GetTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            Log.i("made", " it here");
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
                    //save the values
                    Log.i("server response", result.toString());
                    save(new JSONArray( "["+ result.toString() + "]"), params[1]);
                    return new JSONArray("["+ result.toString() + "]");
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

    }

    /**
     * This inner class handles post request.
     * The skeleton for this class is borrowed from Prof VanderLinden's code. Thanks!
     *
     */
    private class PostTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
                // Hard-code json.
                //String jsonString = params[2];
                JSONObject jsonData = new JSONObject(params[2]);
                //jsonData.put("emailaddress", "kvlinden@calvin.edu");
                // Open the connection as usual.
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                // Configure the connection for a POST, including outputing streamed JSON data.
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setFixedLengthStreamingMode(jsonData.toString().length());
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(jsonData.toString());
                out.flush();
                out.close();
                // Handle the response from the (Lab09) server as usual.
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    //Log.d(TAG, jsonText.toString());
                    if (jsonText.charAt(0) == '[') {
                        Log.i("1st if", jsonText.toString());
                        save(new JSONArray(jsonText.toString()), params[1]);
                        result = new JSONArray(jsonText.toString());
                    } else if (jsonText.toString().equals("null")) {
                        Log.i("2st if", jsonText.toString());
                        result = new JSONArray();
                    } else {
                        Log.i("3st if", jsonText.toString());
                        save(new JSONArray("[" + jsonText.toString() + "]"), params[1]);
                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }


    /**
     * This inner class handles put requests.
     * The skeleton for this class is borrowed from Prof VanderLinden's code. Thanks!
     *
     */
    private class PutTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
//                String jsonString = params[2];
//                JSONObject jsonData = new JSONObject(jsonString);
                JSONObject jsonData = new JSONObject(params[2]);
                //jsonData.put("emailaddress", "new@calvin.edu");
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setFixedLengthStreamingMode(jsonData.toString().length());
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(jsonData.toString());
                out.flush();
                out.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    //Log.d(TAG, jsonText.toString());
                    if (jsonText.charAt(0) == '[') {
                        Log.i("1st if", jsonText.toString());
                        result = new JSONArray(jsonText.toString());
                        save(new JSONArray( jsonText.toString()), params[1]);
                    } else if (jsonText.toString().equals("null")) {
                        Log.i("2st if", jsonText.toString());
                        result = new JSONArray();
                    } else {
                        Log.i("3st if", jsonText.toString());
                        save(new JSONArray( "["+ jsonText.toString() + "]"), params[1]);
                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    /**
     * Inner class for DELETEing
     * From professor VanderLinden,Thanks!
     * TODO: DOES NOT WORK!!!
     */
//    private class DeleteTask extends AsyncTask<String, Void, JSONArray> {
//
//        @Override
//        protected JSONArray doInBackground(String... params) {
//            HttpURLConnection connection = null;
//            StringBuilder jsonText = new StringBuilder();
//            JSONArray result = null;
//            try {
//                URL url = new URL(params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("DELETE");
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(connection.getInputStream()));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        jsonText.append(line);
//                    }
//                    //Log.d(TAG, jsonText.toString());
//                    if (jsonText.charAt(0) == '[') {
//                        Log.i("1st if", jsonText.toString());
//                        result = new JSONArray(jsonText.toString());
//                    } else if (jsonText.toString().equals("null")) {
//                        Log.i("2st if", jsonText.toString());
//                        result = new JSONArray();
//                    } else {
//                        Log.i("3st if", jsonText.toString());
//                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
//                    }
//                } else {
//                    throw new Exception();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray players) {
//            playerList.clear();
//            if (players == null) {
//                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
//            } else if (players.length() == 0) {
//                Toast.makeText(MainActivity.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
//            } else {
//                convertJSONtoArrayList(players);
//            }
//            MainActivity.this.updateDisplay();
//        }
//
//    }

    /**
     * save will save the data that has been received from the server
     * @param jsonArray
     *  The data from the server
     * @param type
     *  Whether the data is for the Player or Game data structure
     */
    public void save( JSONArray jsonArray, String type )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Log.i("the type is ", type);
        //convert the jsonArray to an array list of json objects
        ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObjectList.add(jsonObject);

            } catch (Exception e) {

            }
        }
        Log.i("parsed json response", jsonObjectList.toString());
        if( type.equals("game")) {
            GameClass game = new GameClass(context);
            game.saveInfo(jsonObjectList);
        }
        else if (type.equals("player"))
        {
            Player player = new Player(context);
            player.saveInfo(jsonObjectList);
        }
        else if (type.equals("currentGames"))
        {
            editor.putString("currentGames", jsonArray.toString());
            editor.commit();
        }
        else if (type.equals("futureGames"))
        {
            editor.putString("futureGames", jsonArray.toString());
            editor.commit();
        }
    }

}
