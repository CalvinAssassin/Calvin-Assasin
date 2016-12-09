//package edu.calvin.the_b_team.calvinassassingame;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//
///**
// * Created by jjh35 on 11/26/2016.
// *
// * This game class stores information about the game. Information will be saved in
// * saved shared preferences.
// */
//
//public class GameClass {
//
//    private SharedPreferences app_preferences;
//    private Context context;
//    private GameInfo gameInfo;
//
//    /**
//     * this class is the data structure that saves the game information, doesn't work timeLeft and gameName
//     */
//    public class GameInfo {
//
//        public String[] hashmapFieldNames = { "players"};
//
//        //allows one to find out if a field exists
//        public String[] fieldNames = {"gameID", "gameName", "inPlay", "creatorID",
//              "startDate", "endDate", "latitude", "timeLeft", "longitude", "firstName",
//                "lastName", "residence", "players", "major", "games", "active",
//                "alive", "dead"
//        };
//        //used to see if types are correct when saving
//        public String[] doubleFieldNames = { "latitude", "longitude"};
//        public String[] integerFieldNames = { "currentGame", "creatorID"};
//        public String[] stringFieldNames = { "gameID", "startDate", "endDate", "firstName",
//                "lastName", "residence", "major", "timeLeft" };
//        public String[] intArrayFieldNames = { "games", "active", "alive","dead"};
//        public String[] booleanFieldNames = {"inPlay"};
//
//        //current game items
//        public int gameID ;
//        public String gameName;
//        public boolean inPlay;
//        public int creatorID;
//        public String startDate;
//        public String endDate;
//
//        public Double latitude, longitude;
//        public String firstName, lastName, residence, major, timeLeft;
//
//        public int[]  games, active,alive,dead;
//
//        public ArrayList<HashMap<String, Object>> players;
//
//
//    }
//
//    /**
//     * This method will find the GameInfo data structure for the class to use/manipulate
//     * @param classContext
//     */
//    GameClass(Context classContext ){
//        context = classContext;
//        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
//
//        //if the gameInfo data structure is not on the disk, create and save one
//        if( !app_preferences.contains("gameInfo") )
//        {
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//            SharedPreferences.Editor editor = preferences.edit();
//            GameInfo gameInformation = new GameInfo();
//            gameInformation.gameID = gameInformation.creatorID = 0;
//            gameInformation.latitude = gameInformation.longitude = 0.0;
//            gameInformation.inPlay = false;
//            gameInformation.startDate = gameInformation.endDate = gameInformation.firstName =
//                    gameInformation.lastName = gameInformation.residence =
//                    gameInformation.major = gameInformation.timeLeft = gameInformation.gameName = "";
//            int[] emptyArray = new int[] {};
//            gameInformation.games = gameInformation.active =
//                    gameInformation.alive = gameInformation.dead = emptyArray;
//            gameInformation.players = new  ArrayList<HashMap<String, Object>>();
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
//     * This method will refresh the memory by pulling the latest from
//     * the disk and saving it to the current object.
//     */
//    public void refreshMemory()
//    {
//        Gson gson = new Gson();
//        String json = app_preferences.getString("gameInfo", "");
//        GameInfo gameInformation = gson.fromJson(json, GameInfo.class);
//        this.gameInfo = gameInformation;
//    }
//
//    /**
//     * The following methods are overloads of public boolean save(...) for different types
//     */
//
//
//
//    //will this work for a hashmap?
//    public boolean save( String key, HashMap map ) {
//        if (Arrays.asList(this.gameInfo.hashmapFieldNames).contains(key)) {
//            this.gameInfo.players.add( map );
//            saveToDrive();
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * This method allows someone to save a single value
//     * @param key
//     *  The field name of the value being saved
//     * @param value
//     *  the value being saved
//     * @return
//     */
//    public boolean save( String key, String value ) {
//        if (Arrays.asList(this.gameInfo.stringFieldNames).contains(key)) {
//            try {
//                Field field = GameInfo.class.getField(key);
//                field.set(this.gameInfo, value);
//            } catch (Exception e) {
//                return false;
//            }
//            saveToDrive();
//            return true;
//        }
//        return false;
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
//    public boolean save( String key, int value ) {
//        if (Arrays.asList(this.gameInfo.integerFieldNames).contains(key)) {
//            try {
//                Field field = GameInfo.class.getField(key);
//                field.set(this.gameInfo, value);
//            } catch (Exception e) {
//                return false;
//            }
//            saveToDrive();
//            return true;
//        }
//        return false;
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
//    public boolean save( String key, double value ) {
//        if (Arrays.asList(this.gameInfo.doubleFieldNames).contains(key)) {
//            try {
//                Field field = GameInfo.class.getField(key);
//                field.set(this.gameInfo, value);
//            } catch (Exception e) {
//                return false;
//            }
//            saveToDrive();
//            return true;
//        }
//        return false;
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
//    public boolean save( String key, boolean value ) {
//        if (Arrays.asList(this.gameInfo.booleanFieldNames).contains(key)) {
//            try {
//                Field field = GameInfo.class.getField(key);
//                field.set(this.gameInfo, value);
//            } catch (Exception e) {
//                return false;
//            }
//            saveToDrive();
//            return true;
//        }
//        return false;
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
//    public boolean save( String key, int[] value ) {
//        if (Arrays.asList(this.gameInfo.intArrayFieldNames).contains(key)) {
//            try {
//                Field field = GameInfo.class.getField(key);
//                field.set(this.gameInfo, value);
//            } catch (Exception e) {
//                return false;
//            }
//            saveToDrive();
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * This method will save the game data structure to the hard drive
//     * @return
//     *  true if save was a success, false otherwise
//     */
//    public boolean saveToDrive() {
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
////                        Log.i( "the key is ", key );
////                        Log.i( " the value is ", value.toString() );
//
//                        if( key.equals("players"))
//                        {
//                            Log.i("value of players is ", value.toString());
//                            //savePlayers( value );
//                        }
//
//                        else {
//                            //store the value
//                            Field field = GameInfo.class.getField(key);
//                            field.set(gameInfo, value);
//                        }
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
//        saveToDrive();
//        return true;
//    }
//
//    public void savePlayers( JSONObject jsonObject )
//    {
//
//    }
//
//    /**
//     * a generic accessor for the fields in the GameInfo data structure
//     * @param fieldName
//     *  the name of the field
//     * @return
//     *  an object containing the value being retrieved
//     */
//    public Object getValue( String fieldName )
//    {
//        refreshMemory();
//        if( Arrays.asList(this.gameInfo.fieldNames).contains(fieldName) ) {
//            try {
//                Class c = this.gameInfo.getClass();
//                Field field = c.getField(fieldName);
//                Object value = field.get(this.gameInfo);
//                return value;
//                //return value.toString();
//            } catch (Exception e) {
//                return "err";
//            }
//        }
//        return "err";
//    }
//
//    /**
//     * the following are accessors for the different info in game
//     * @return
//     *  the value being retrieved
//     */
//    public int getGameID()
//    {
//        if ( !getValue( "currentGame" ).equals("err") )
//            return (int) getValue("currentGame");
//        return 0;
//    }
//
//    public String getGameName()
//    {
//        if ( !getValue( "gameName" ).equals("err") )
//            return (String) getValue("gameName");
//        return "error!";
//    }
//
//    public boolean inPlay()
//    {
//        if ( !getValue( "inPlay" ).equals("err") )
//            return (boolean) getValue("inPlay");
//        return false;
//    }
//
//    public int getCreatorID()
//    {
//        if ( !getValue( "creatorID" ).equals("err") )
//            return (int) getValue("creatorID");
//        return 0;
//    }
//
//    public String getStartDate()
//    {
//        if ( !getValue( "startDate" ).equals("err") )
//            return (String) getValue("startDate");
//        return "error";
//    }
//
//    public String getEndDate()
//    {
//        if ( !getValue( "endDate" ).equals("err") )
//            return (String) getValue("endDate");
//        return "error";
//    }
//
//    public double getLatitude()
//    {
//        if ( !getValue( "latitude" ).equals("err") )
//            return (double) getValue("latitude");
//        return 0.0;
//    }
//
//    public double getLongitude()
//    {
//        if ( !getValue( "longitude" ).equals("err") )
//            return (double) getValue("longitude");
//        return 0.0;
//    }
//
//    public String getFirstName()
//    {
//        if ( !getValue( "firstName" ).equals("err") )
//            return (String) getValue("firstName");
//        return "error";
//    }
//
//    public String getLastName()
//    {
//        if ( !getValue( "lastName" ).equals("err") )
//            return (String) getValue("lastName");
//        return "error";
//    }
//
//    public String getResidence()
//    {
//        if ( !getValue( "residence" ).equals("err") )
//            return (String) getValue("residence");
//        return "error";
//    }
//
//    public String getMajor()
//    {
//        if ( !getValue( "major" ).equals("err") )
//            return (String) getValue("major");
//        return "error";
//    }
//
//    public String getTimeLeft()
//    {
//        if ( !getValue( "timeLeft" ).equals("err") )
//            return (String) getValue("timeLeft");
//        return "error";
//    }
//
//    public int[] getGames()
//    {
//        return (int[]) getValue("games");
//    }
//    public int[] getActive()
//    {
//        return (int[]) getValue("active");
//    }
//    public int[] getAlive()
//    {
//        return (int[]) getValue("alive");
//    }
//    public int[] getDead()
//    {
//        return (int[]) getValue("dead");
//    }
//
//
//}
