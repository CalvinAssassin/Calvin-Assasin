package edu.calvin.the_b_team.calvinassassingame;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jjh35 on 11/26/2016.
 *
 * This game class stores information about the game. Information will be saved in
 * saved shared preferences.
 */

public class GameClass {
    private SharedPreferences app_preferences;
    private Context context;
    public GameInfo gameInfo;

    /**
     * this class is the data structure that saves the game information, doesn't work timeLeft and gameName
     */
    public class GameInfo {

        //used to see if types are correct when saving
        public String[] integerFieldNames = { "ID", "targetID"};
        public String[] stringFieldNames = { "gameName", "targetStartTime", "startDate","targetTimeLeft", "targetTimeoutTime", "players" };
        public String[] booleanFieldNames = {"inPlay"};

        //allows one to find out if a field exists
        public String[] fieldNames = {"ID", "gameName", "inPlay", "startDate",
                "targetStartTime", "players", "targetID", "targetTimeLeft", "targetTimeoutTime"
        };
        //current game items
        public int ID, targetID;
        public String gameName, targetTimeLeft, targetStartTime, players, targetTimeoutTime, startDate;
        public boolean inPlay;

    }

    /**
     * This method will find the GameInfo data structure for the class to use/manipulate
     * @param classContext
     */
    GameClass(Context classContext ){
        this.context = classContext;
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this.context);

        //if the gameInfo data structure is not on the disk, create and save one
        if( !app_preferences.contains("gameInfo") )
        {
            //initialize default values
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            GameInfo gameInfo = new GameInfo();
            gameInfo.ID = gameInfo.targetID = 1;
            gameInfo.inPlay = false;
            gameInfo.targetStartTime = "";
            gameInfo.players = "[{}]";
            gameInfo.startDate = "";
            gameInfo.targetTimeoutTime = "";
            gameInfo.gameName = "g1";
            gameInfo.targetTimeLeft = "0";
            Gson gson = new Gson();
            String json = gson.toJson(gameInfo);
            editor.putString("gameInfo", json);
            editor.commit();
            this.gameInfo = gameInfo;
        }
        else
        {
            Gson gson = new Gson();
            String json = app_preferences.getString("gameInfo", "");
            GameInfo gameInformation = gson.fromJson(json, GameInfo.class);
            this.gameInfo = gameInformation;
        }

    }

    /**
     * This method will refresh the memory by pulling the latest from
     * the disk and saving it to the current object.
     */
    public void refreshMemory()
    {
        Gson gson = new Gson();
        String json = app_preferences.getString("gameInfo", "");
        GameInfo gameInformation = gson.fromJson(json, GameInfo.class);
        this.gameInfo = gameInformation;
    }

    /**
     * This method allows someone to save a single value
     * @param key
     *  The field name of the value being saved
     * @param value
     *  the value being saved
     * @return
     */
    public boolean save( String key, String value ) {
        if (Arrays.asList(this.gameInfo.stringFieldNames).contains(key)) {
            try {
                Field field = GameInfo.class.getField(key);
                field.set(this.gameInfo, value);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            saveToDrive();
            return true;
        }
        Log.i("bad value in save", key);
        return false;
    }

    /**
     * This method allows someone to save a single value
     * @param key
     *  The field name of the value being saved
     * @param value
     *  the value being saved
     * @return
     */
    public boolean save( String key, int value ) {
        if (Arrays.asList(this.gameInfo.integerFieldNames).contains(key)) {
            try {
                Field field = GameInfo.class.getField(key);
                field.set(this.gameInfo, value);
            } catch (Exception e) {
                return false;
            }
            saveToDrive();
            return true;
        }
        return false;
    }


    /**
     * This method allows someone to save a single value
     * @param key
     *  The field name of the value being saved
     * @param value
     *  the value being saved
     * @return
     */
    public boolean save( String key, boolean value ) {
        if (Arrays.asList(this.gameInfo.booleanFieldNames).contains(key)) {
            try {
                Field field = GameInfo.class.getField(key);
                field.set(this.gameInfo, value);
            } catch (Exception e) {
                return false;
            }
            saveToDrive();
            return true;
        }
        return false;
    }

    /**
     * This method will save the game data structure to the hard drive
     * @return
     *  true if save was a success, false otherwise
     */
    public boolean saveToDrive() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.gameInfo);
        //save values
        editor.putString("gameInfo", json);
        editor.commit();
        return true;
    }

    /**
     * save information passed as a arrayList of json objects
     * @param jsonObjectList
     *  The arraylist containing the data
     * @return
     */
    public boolean saveInfo(  ArrayList<JSONObject> jsonObjectList )
    {
        for (int i=0;  i< jsonObjectList.size(); i++ )
        {
            JSONObject jsonObject = jsonObjectList.get(i);
            Iterator<String> iter = jsonObject.keys();
            //go through each JSON object
            while (iter.hasNext()) {
                String key = iter.next();
                //Log.i("key in player class ", key );
                //if the server responds with error, break and
                //TODO, what should be done in the case of an error
                if ( key.equals("err")){
                    return false;
                }
                //the server will return "gameID" or "playerID" for queries for target ID
                //these keys are not valid, but the rest of the info is, so just skip it
                if( key.equals("gameID") || key.equals("playerID"))
                {
                    continue;
                }
                if(Arrays.asList(this.gameInfo.fieldNames).contains(key) )
                {
                    try {
                        Object value = jsonObject.get(key);
                        Log.i( "the key is ", key );
                        Log.i( " the value is ", value.toString() );
                        //store the value
                        if( key.equals("players")) {
                            //before saving the players JSONArray, change it to a string
                            String playersString = value.toString();
                            Field field = GameInfo.class.getField(key);
                            field.set(this.gameInfo, playersString);
                        }
                        else {
                            Field field = GameInfo.class.getField(key);
                            field.set(this.gameInfo, value);
                        }

                    } catch (Exception e) {
                        // Something went wrong!
                        e.printStackTrace();
                       // Log.i( "exception in saveInfo", e.toString());
                        return false;
                    }
                }
                else {
                    Log.i("bad name", " here 1");
                    return false;
                }

            }
        }
        saveToDrive();
        return true;
    }

    /**
     * a generic accessor for the fields in the GameInfo data structure
     * @param fieldName
     *  the name of the field
     * @return
     *  an object containing the value being retrieved
     */
    public Object getValue( String fieldName )
    {
        refreshMemory();
        if( Arrays.asList(this.gameInfo.fieldNames).contains(fieldName) ) {
            try {
                Class c = this.gameInfo.getClass();
                Field field = c.getField(fieldName);
                Object value = field.get(this.gameInfo);
                return value;
                //return value.toString();
            } catch (Exception e) {
                return "err";
            }
        }
        return "err";
    }

    /**
     * the following are accessors for the different info in game
     * @return
     *  the value being retrieved
     */
    public int getID()
    {
        if ( !getValue( "ID" ).equals("err") )
            return (int) getValue("ID");
        return 0;
    }

    public String getGameName()
    {
        if ( !getValue( "gameName" ).equals("err") )
            return (String) getValue("gameName");
        return "error!";
    }

    public boolean getInPlay()
    {
        if ( !getValue( "inPlay" ).equals("err") )
            return (boolean) getValue("inPlay");
        return false;
    }

    public String getTargetStartTime()
    {
        if ( !getValue( "targetStartTime" ).equals("err") )
            return (String) getValue("targetStartTime");
        return "error!";
    }
    public String getTargetTimeLeft()
    {
        if ( !getValue( "targetTimeLeft" ).equals("err") )
            return (String) getValue("targetTimeLeft");
        return "error!";
    }

    public String getTargetTimeoutTime()
    {
        if ( !getValue( "targetTimeoutTime" ).equals("err") )
            return (String) getValue("targetTimeoutTime");
        return "error!";
    }

    public int getTargetID()
    {
        if ( !getValue( "targetID" ).equals("err") )
            return (int) getValue("targetID");
        return 0;
    }

    public String getStartDate()
    {
        if ( !getValue( "startDate" ).equals("err") )
            return (String) getValue("startDate");
        return "error!";
    }

    public JSONArray getPlayers()
    {
        try {
            Log.i("json string from mem", this.gameInfo.players);
            return new JSONArray(this.gameInfo.players);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    /**
     * This will return the jsonObject that corrolates with the ID
     * @param ID
     *  The ID of the player being retreived
     * @return
     *  The playeri info
     */
    public JSONObject getPlayerInfo( int ID ) {
        //first convert jsonArray String to arrayList
        ArrayList<JSONObject> list = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(this.gameInfo.players);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonObject);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //iterate through the arrayList and looking for a matching ID
        for(int i = 0; i<list.size();i++)
        {
            try {
                //return the JSONObject if match is found
                if (list.get(i).getInt("ID") == ID) {
                    return list.get(i);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        JSONObject error = new JSONObject();
        try {
            //no matching ID, return error object
            return error.put("error", "error");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject();
        }
    }

}
