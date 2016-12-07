package edu.calvin.the_b_team.calvinassassingame;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by jjh35 on 11/26/2016.
 *
 * This game class stores information about the game. Information will be saved in
 * saved shared preferences.
 *
 */

public class Game {
    private SharedPreferences app_preferences;
    private Context context;
    //private GameInfo gameInfo;

    Game( Context con )
    {
        context = con;
    }

//    /**
//     * this class is the data structure that saves the game information
//     */
//    public class GameInfo {
//        //allows one to find out if a field exists
//        public String[] fieldNames = {"currentGame", "inPlay", "creatorID",
//                "startDate", "endDate", "players", "allGames", "currentGames",
//                "latitude", "longitude", "firstName", "lastName", "residence", "major"
//        };
//
//        //current game items
//        public String currentGame;
//        public boolean inPlay;
//        public String creatorID;
//        public String startDate;
//        public String endDate;
//        public int[] players;
//        public String latitude, longitude;
//        public String firstName, lastName, residence, major;
//
//        //information about games in general
//        public int[] allGames;
//        public int[] currentGames;
//    }


//    /**
//     * This method will find the GameInfo data structure for the class to use/manipulate
//     * @param classContext
//     */
//    Game(Context classContext ){
//        context = classContext;
//
//        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
//
//        //if the gameInfo data structure is not on the disk, create and save one
//        if( !app_preferences.contains("gameInfo") )
//        {
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//            SharedPreferences.Editor editor = preferences.edit();
//            GameInfo gameInformation = new GameInfo();
//            gameInformation.currentGame = gameInformation.creatorID = "0";
//            gameInformation.latitude = gameInformation.longitude = "0.0";
//            gameInformation.inPlay = false;
//            gameInformation.startDate = gameInformation.endDate = gameInformation.firstName =
//                    gameInformation.lastName = gameInformation.residence = gameInformation.major = "";
//            int[] emptyArray = new int[] {};
//            gameInformation.players = gameInformation.allGames = gameInformation.currentGames = emptyArray ;
//            Gson gson = new Gson();
//            String json = gson.toJson(gameInformation);
//            editor.putString("gameInfo", json);
//            editor.commit();
//            this.gameInfo = gameInformation;
//        }
//        else
//        {
//            Gson gson = new Gson();
//            String json = app_preferences.getString("gameInfo", "");
//            GameInfo gameInformation = gson.fromJson(json, GameInfo.class);
//            this.gameInfo = gameInformation;
//        }
//
//    }
//
//    /**
//     * This method allows someone to save a single value
//     * @param key
//     *  The field name of the value being saved
//     * @param value
//     *  the value being saved
//     * @return
//     */
//    public boolean saveValue( String key, String value )
//    {
//        if( Arrays.asList(this.gameInfo.fieldNames).contains(key))
//        {
//            try {
//                Field field = GameInfo.class.getField(key);
//                field.set(this.gameInfo, value);
//            }
//            catch (Exception e)
//            {
//                return false;
//            }
//
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//            SharedPreferences.Editor editor = preferences.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(this.gameInfo);
//            //save values
//            editor.putString("gameInfo", json);
//            editor.commit();
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * save information passed as a arrayList of json objects
//     * @param jsonObjectList
//     *  The arraylist containing the data
//     * @return
//     */
//    public boolean saveInfo(  ArrayList<JSONObject> jsonObjectList )
//    {
//        for (int i=0;  i< jsonObjectList.size(); i++ )
//        {
//            JSONObject jsonObject = jsonObjectList.get(i);
//            Iterator<String> iter = jsonObject.keys();
//            //go through each JSON object
//            while (iter.hasNext()) {
//                String key = iter.next();
//                //if the server responds with error, break and
//                //TODO, what should be done in the case of an error
//                if ( key.equals("err")){
//                    return false;
//                }
//                if( Arrays.asList(this.gameInfo.fieldNames).contains(key))
//                {
//                    try {
//                        Object value = jsonObject.get(key);
//                        Log.i( "the key is ", key );
//                        Log.i( " the value is ", value.toString() );
//                        //store the value
//                        Field field = GameInfo.class.getField( key );
//                        field.set(gameInfo, value );
//                    } catch (Exception e) {
//                        // Something went wrong!
//                        return false;
//                    }
//                }
//                else
//                    return false;
//
//            }
//        }
//        //initialize the editor to save values
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(this.gameInfo);
//        //save values
//        editor.putString("gameInfo", json);
//        editor.commit();
//        return true;
//    }
//
//    /**
//     * a generic accessor for the fields in the GameInfo data structure
//     * @param fieldName
//     *  the name of the field
//     * @return
//     */
//    public String getValue( String fieldName )
//    {
//        if( Arrays.asList(this.gameInfo.fieldNames).contains(fieldName) ) {
//            try {
//                Class c = this.gameInfo.getClass();
//                Field field = c.getField(fieldName);
//                Object value = field.get(this.gameInfo);
//                return value.toString();
//            } catch (Exception e) {
//                return "err";
//            }
//        }
//        return "err";
//    }
}