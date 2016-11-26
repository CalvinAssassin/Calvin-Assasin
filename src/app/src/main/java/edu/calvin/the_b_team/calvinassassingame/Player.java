package edu.calvin.the_b_team.calvinassassingame;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by jjh35 on 11/26/2016.
 * This class stores and gives access to all information about the user
 */
public class Player {

    private SharedPreferences app_preferences;
    private Context context;
    private PlayerInfo playerInfo;

    /**
     * this class is a data structure that will save the information about the user
     */
    public class PlayerInfo{
        public String[] fieldNames = { "ID", "firstName", "lastName", "residence", "major",
                "latitude", "longitude"
        };

        public int ID;
        public double latitude, longitude;
        public String firstName, lastName, residence, major;

    }

    /**
     * This method will find the playerInfo data structure for the class to use/manipulate
     * @param classContext
     */
    Player( Context classContext )
    {
        this.context = classContext;
        //if the gameInfo data structure is not on the disk, create and save one
        if( !app_preferences.contains("playerInfo") ) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.ID = 0;
            playerInfo.latitude = playerInfo.longitude = 0.0;
            playerInfo.firstName = playerInfo.lastName = playerInfo.residence = playerInfo.major = "";

            Gson gson = new Gson();
            String json = gson.toJson(playerInfo);
            editor.putString("playerInfo", json);
            editor.commit();

            this.playerInfo = playerInfo;

        }
        else
        {
            Gson gson = new Gson();
            String json = app_preferences.getString("playerInfo", "");
            PlayerInfo playerInformation = gson.fromJson(json, PlayerInfo.class);
            this.playerInfo = playerInformation;
        }
    }


    /**
     * This method allows someone to save a single value
     * @param key
     *  The field name of the value being saved
     * @param value
     *  the value being saved
     * @return
     */
    public boolean saveValue( String key, String value ) {
        if (Arrays.asList(this.playerInfo.fieldNames).contains(key)) {
            try {
                Field field = PlayerInfo.class.getField(key);
                field.set(this.playerInfo, value);
            } catch (Exception e) {
                return false;
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(this.playerInfo);
            //save values
            editor.putString("playerInfo", json);
            editor.commit();
            return true;
        }
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
                //if the server responds with error, break and
                //TODO, what should be done in the case of an error
                if ( key.equals("err")){
                    return false;
                }
                if( Arrays.asList(this.playerInfo.fieldNames).contains(key))
                {
                    try {
                        Object value = jsonObject.get(key);
                        Log.i( "the key is ", key );
                        Log.i( " the value is ", value.toString() );
                        //store the value
                        Field field = PlayerInfo.class.getField( key );
                        field.set(playerInfo, value );
                    } catch (Exception e) {
                        // Something went wrong!
                        return false;
                    }
                }
                else
                    return false;

            }
        }
        //initialize the editor to save values
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.playerInfo);
        //save values
        editor.putString("gameInfo", json);
        editor.commit();
        return true;
    }

    /**
     * a generic accessor for the fields in the PlayerInfo data structure
     * @param fieldName
     *  the name of the field
     * @return
     */
    public String getValue( String fieldName )
    {
        if( Arrays.asList(this.playerInfo.fieldNames).contains(fieldName) ) {
            Gson gson = new Gson();
            String json = app_preferences.getString("gameInfo", "");
            this.playerInfo = gson.fromJson(json, PlayerInfo.class);
            try {
                Class<?> c = this.playerInfo.getClass();
                Field field = c.getDeclaredField(fieldName);
                Object value = field.get(fieldName);
                return value.toString();
            } catch (Exception e) {
                return "err";
            }
        }

        return "err";
    }
}
