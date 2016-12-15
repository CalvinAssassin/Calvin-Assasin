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
 *
 * This class stores and gives access to all information about the user and saves
 * it to shared preferences
 */

public class Player {

    private SharedPreferences app_preferences;
    private Context context;
    private PlayerInfo playerInfo;

    /**
     * this class is a data structure that will save the information about the user. It will be
     * saved to shared preferences
     */
    public class PlayerInfo{
        public String[] fieldNames = { "ID", "firstName", "lastName", "residence", "major",
                "latitude", "longitude", "locUpdateTime", "isAlive", "currentGameID"
        };

        public String[] intFieldNames = { "ID", "currentGameID" };
        public String[] doubleFieldNames = { "latitude", "longitude" };
        public String[] stringFieldNames = { "firstName", "lastName", "residence", "major", "locUpdateTime" };
        public String[] booleanFieldNames = { "isAlive" };

        public int ID, currentGameID;
        public Double latitude, longitude;
        public String firstName, lastName, residence, major, locUpdateTime;
        public boolean isAlive;

    }

    /**
     * This method will find the playerInfo data structure for the class to use/manipulate
     * @param classContext
     *  a context so that one can save values to the drive
     */
    Player( Context classContext )
    {
        this.context = classContext;
        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //if the gameInfo data structure is not on the disk, create a default and save
        if( !app_preferences.contains("playerInfo") ) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.ID = playerInfo.currentGameID = 1;
            playerInfo.latitude = playerInfo.longitude = 0.0;
            playerInfo.firstName = "first name";
            playerInfo.lastName = "last name";
            playerInfo.residence = "residence";
            playerInfo.major = "major";
            playerInfo.locUpdateTime = "0000-00-00 00:00:00";
            playerInfo.isAlive = false;
            Gson gson = new Gson();
            String json = gson.toJson(playerInfo);
            editor.putString("playerInfo", json);
            editor.commit();
            this.playerInfo = playerInfo;
        }
        else
        {//pull from shared preferences
            Gson gson = new Gson();
            String json = app_preferences.getString("playerInfo", "");
            PlayerInfo playerInformation = gson.fromJson(json, PlayerInfo.class);
            this.playerInfo = playerInformation;
        }
    }

    /**
     * This method will refresh the memory by pulling the latest from
     * the disk and saving it to the current object.
     */
    public void refreshMemory()
    {
        Gson gson = new Gson();
        String json = app_preferences.getString("playerInfo", "");
        PlayerInfo gameInformation = gson.fromJson(json, PlayerInfo.class);
        this.playerInfo = gameInformation;
    }

    /**
     * The following methods are overloads of public boolean save(...) for different types
     */

    /**
     * This method allows someone to save a single value
     * @param key
     *  The field name of the value being saved
     * @param value
     *  the value being saved
     * @return
     */
    public boolean save( String key, String value ) {
        if (Arrays.asList(this.playerInfo.stringFieldNames).contains(key)) {
            try {
                Field field = PlayerInfo.class.getField(key);
                field.set(this.playerInfo, value);
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
    public boolean save( String key, int value ) {
        if (Arrays.asList(this.playerInfo.intFieldNames).contains(key)) {
            try {
                Field field = PlayerInfo.class.getField(key);
                field.set(this.playerInfo, value);
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
    public boolean save( String key, double value ) {
        if (Arrays.asList(this.playerInfo.doubleFieldNames).contains(key)) {
            try {
                Field field = PlayerInfo.class.getField(key);
                field.set(this.playerInfo, value);
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
        if (Arrays.asList(this.playerInfo.booleanFieldNames).contains(key)) {
            try {
                Field field = PlayerInfo.class.getField(key);
                field.set(this.playerInfo, value);
            } catch (Exception e) {
                return false;
            }
            saveToDrive();
            return true;
        }
        return false;
    }

    /**
     * This method will save the player data structure to the hard drive
     * @return
     *  true if save was a success, false otherwise
     */
    public boolean saveToDrive() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.playerInfo);
        //save values
        editor.putString("playerInfo", json);
        editor.commit();
        return true;
    }

    /**
     * save information passed as a arrayList of json objects, used by ServerCommunication
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
//                        Log.i( "the key is ", key );
//                        Log.i( " the value is ", value.toString() );
                        //store the value
                        Field field = PlayerInfo.class.getField( key );
                        field.set(this.playerInfo, value );
                    } catch (Exception e) {
                        // Something went wrong!
                        e.printStackTrace();
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        saveToDrive();
        return true;
    }

    /**
     * a generic accessor for the fields in the PlayerInfo data structure
     * @param fieldName
     *  the name of the field
     * @return
     *  an object with the value being retrieved
     */
    public Object getValue( String fieldName )
    {
        refreshMemory();
        if( Arrays.asList(this.playerInfo.fieldNames).contains(fieldName) ) {
            try {
                Class c = this.playerInfo.getClass();
                Field field = c.getField(fieldName);
                Object value = field.get(this.playerInfo);
                return value;

            } catch (Exception e) {
                return e.toString();
            }
        }
        return "err";
    }

    /**
     * thre following accessors give access to the saved info
     * @return
     *  the value from the disk
     */
    public int getID()
    {
        if ( !getValue( "ID" ).equals("err") )
            return (int) getValue( "ID" );
        return 0;

    }
    public double getLatitude()
    {
        if ( !getValue( "latitude" ).equals("err") )
            return (double) getValue("latitude");
        return 0.1;
    }

    public double getLongitude()
    {
        if ( !getValue( "longitude" ).equals("err") )
            return (double) getValue("longitude");
        return 0.0;

    }
    public String getFirstName()
    {
        if ( !getValue( "firstName" ).equals("err") )
            return (String) getValue("firstName");
        return "error";
    }
    public String getLastName()
    {
        if ( !getValue( "lastName" ).equals("err") )
            return (String) getValue("lastName");
        return "error";
    }
    public String getMajor()
    {
        if ( !getValue( "major" ).equals("err") )
            return (String) getValue("major");
        return "error";
    }
    public String getResidence()
    {
        if ( !getValue( "ID" ).equals("err") )
            return (String) getValue("residence");
        return "error";
    }
    public String getLocUpdateTime()
    {
        if( !getValue( "locUpdateTime").equals("err"))
            return (String) getValue("locUpdateTime");
        return "error";
    }
    public int getCurrentGameID()
    {
        if ( !getValue( "currentGameID" ).equals("err") )
            return (int) getValue( "currentGameID" );
        return 0;
    }

    public boolean getIsAlive()
    {
        return (boolean) getValue("isAlive");
    }

}
