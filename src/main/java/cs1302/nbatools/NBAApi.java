package cs1302.nbatools;

import static java.net.URLEncoder.encode;
import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.nio.charset.Charset;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.util.Pair;

/**
 * Contains methods for NBA-API.
 * 
 */
public class NBAApi {

    private static final String ENDPOINT = "https://api-nba-v1.p.rapidapi.com";
    
    /**
     * Return an {@code Optional} describing the root element of the JSON
     * response for a "get players by team id" query.
     * 
     * @param teamNo teamId
     * @param nbaApiKey key for API query
     * @return an {@code Optional} describing the root element of the response
     */
    public static Optional<JsonElement> getTeamRoster(int teamNo, String nbaApiKey) {
        try {
            String url = ENDPOINT + "/players/teamId/" + teamNo;
            return Optional.<JsonElement>ofNullable(getJson(url, "GET",
            		new Pair<>("x-rapidapi-key", nbaApiKey),
            		new Pair<>("x-rapidapi-host", "api-nba-v1.p.rapidapi.com")));
        } catch (IOException ioe) {
        	System.out.println(ioe.getLocalizedMessage());
            return Optional.<JsonElement>empty();
        } // try
    } // getTeamRoster
    
    
    /**
     * For all players listed in the JSON response, create an {@code NBAPlayer} representation.
     * Add these players to a {@code TeamRoster}, then return that roster.
     * 
     * @param roster the JSON response to iterate through
     * @param nickName the nickname for the roster's team
     * @param pbs list of {@code ProgressBar}s to update as method runs
     * @return a {@code TeamRoster} that contains the players on the roster
     */
    public static TeamRoster parseRosterPlayers(JsonElement roster, String nickName, ProgressBar... pbs) {
        //try {
            JsonElement results = get(roster, "api");
            JsonElement players = get(results, "players");          
            JsonArray playersArray = players.getAsJsonArray();
            
            TeamRoster activeRoster = new TeamRoster(); 
            int numPlayerResults = playersArray.size();
            
            String firstName; String lastName;
            String teamName = activeRoster.getRosterName();
            
            for (int i = 0; i < numPlayerResults; i++) {

                JsonObject currentPlayer = playersArray.get(i).getAsJsonObject();
                
                if (currentPlayer.get("leagues") == null) {
                	continue;
                } // if
                JsonObject leagues = currentPlayer.get("leagues").getAsJsonObject();
                if (leagues.get("standard") == null) {
                	continue;
                } // if
                JsonObject standard = leagues.get("standard").getAsJsonObject();
                if (standard.get("active") == null) {
                	continue;
                } // if
                // check if active status is present. if not, move on to the next player
                
                String activeString = standard.get("active").getAsString();
                
                if (activeString != null && !activeString.isEmpty()) {   	
                   
                	firstName = currentPlayer.get("firstName").getAsString();
                	lastName = currentPlayer.get("lastName").getAsString();
                	
                	NBAPlayer newPlayer = new NBAPlayer(firstName, lastName, teamName, nickName);
                	
                	if (!activeRoster.checkIfPlayerIsPresent(newPlayer)) {
                		activeRoster.addPlayer(newPlayer);
                	} // if, check if player is already in list
                	
                } // if, check if player is active
                
                for (ProgressBar pb : pbs) {
                	double progress = ((1.0 * i) / (numPlayerResults - 1));
                	updateProgress(progress, pb); //update progressbar
                } // for, update all ProgressBars
            }
            System.out.println(activeRoster);
            return activeRoster;
    } // parseRosterPlayers
    
    /** 
     * Updates {@code pb} with the given progress amount.
     * 
     * @param progress the given progress amount
     */
    private static void updateProgress(final double progress, ProgressBar pb) {
    	//System.out.println(progress);
        Platform.runLater(() -> pb.setProgress(progress));
    } // updateProgress

    
} // NBAApi
