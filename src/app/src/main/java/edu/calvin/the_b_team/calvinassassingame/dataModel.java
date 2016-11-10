//          _____      _       _                                      _
//         / ____|    | |     (_)         /\                         (_)
//        | |     __ _| |_   ___ _ __    /  \   ___ ___  __ _ ___ ___ _ _ __
//        | |    / _` | \ \ / / | '_ \  / /\ \ / __/ __|/ _` / __/ __| | '_ \
//        | |___| (_| | |\ V /| | | | |/ ____ \\__ \__ \ (_| \__ \__ \ | | | |
//         \_____\__,_|_| \_/ |_|_| |_/_/    \_\___/___/\__,_|___/___/_|_| |_|
//
package edu.calvin.the_b_team.calvinassassingame;

import java.security.Timestamp;

/**
 * This class will hold all the data for the game, and include the methods for loading and saving to
 * disk as well as syncing with the server.
 *
 * Created by cdh24 on 11/2/16.
 */

public class dataModel {

    // Personal user traits
    private String userName;
    private String firstName;
    private String lastName;
    private Timestamp year;
    private String major;


    // Game information (this is assuming that the user may only be participating in one game at a
    //                   time, per our meeting on this)
    private int gameID;
    private String gameName;
    private boolean gameIsInProgress;
    private int gameCreatorID;
    private Timestamp startDate;
    private int gameRound;

    // Current Target's Information
    private String targetFirstName;
    private String targetLastName;
    private Timestamp targetYear;
    private String targetMajor;
    // figure out how to store position data, then add vars here

    // Player ranking variables
//    private Map<String, String>


    //
    // Accessor Methods
    //

    // Personal traits accessors
    public String getPlayerUserName() { return userName; }
    public String getPlayerFirstName() { return firstName; }
    public String getPlayerLastName() { return lastName; }
    public String getPlayerFullName() { return firstName + " " + lastName; }
    public Timestamp getPlayerYear() { return year; }
    public String getPlayerMajor() { return major; }

    // Game information accessors
    public int getGameID() { return gameID; }
    public String getGameName() { return gameName; }
    public boolean getIsGameInProgress() { return gameIsInProgress; }
    public int getGameCreatorID() { return gameCreatorID; }
    public Timestamp getGameStartDate() { return startDate; }
    public int getGameRound() { return gameRound; }

    // Target information accessors
    public String getTargetFirstName() { return targetFirstName; }
    public String getTargetLastName() { return targetLastName; }
    public String getTargetFullName() { return targetFirstName + " " + targetLastName; }
    public Timestamp getTargetYear() { return targetYear; }
    public String getTargetMajor() { return targetMajor; }



    //
    // Data sync methods
    //
    public boolean syncDataWithServer() {
        // Add this after creating server
        // Probably this will do a diff and then call separate update methods
        return true;
    }
}
