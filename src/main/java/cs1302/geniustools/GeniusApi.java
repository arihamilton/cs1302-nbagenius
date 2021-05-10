package cs1302.geniustools;

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

import cs1302.nbatools.NBAPlayer;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.util.Pair;

/**
 * Contains methods for Genius API.
 * 
 */
public class GeniusApi {

    private static final String ENDPOINT = "https://genius.p.rapidapi.com";
    
    /**
     * Return an {@code Optional} describing the root element of the JSON
     * response for a "search" query.
     * 
     * @param searchTerm teamId
     * @param geniusApiKey key for API query
     * @return an {@code Optional} describing the root element of the response
     */
    public static Optional<JsonElement> searchForSongs(String searchTerm, String geniusApiKey) {
        try {
            String url = ENDPOINT + "/search?q=" + encode(searchTerm, UTF8);
            return Optional.<JsonElement>ofNullable(getJson(url, "GET",
            		new Pair<>("x-rapidapi-key", geniusApiKey),
            		new Pair<>("x-rapidapi-host", "genius.p.rapidapi.com")));
        } catch (IOException ioe) {
        	System.out.println(ioe.getLocalizedMessage());
            return Optional.<JsonElement>empty();
        } // try
    } // searchForSongs
    
    /**
     * Separate the JSON response into different songs
     * If song meets qualifications, then turn it into a {@code GeniusSong}.
     * 
     * @param songList the JSON response to iterate through
     * @param player the {@code NBAPlayer} to add song to
     * @param pbs list of {@code ProgressBar}s to update as method runs
     * 
     */
    public static void parseSongs(JsonElement songList, NBAPlayer player, ProgressBar... pbs) {
            JsonElement results = get(songList, "response");
            JsonElement hits = get(results, "hits");   
            JsonArray hitsArray = hits.getAsJsonArray();
            int numSongResults = hitsArray.size();
            
            String songArtistName; String songName ; String songImageUrl; String songPageUrl;
            
            for (int i = 0; i < numSongResults; i++) {

                JsonObject currentSong = hitsArray.get(i).getAsJsonObject();
                
                if (currentSong.get("result") == null) { continue; } // if
                JsonObject songResult = currentSong.get("result").getAsJsonObject();
                if (songResult.get("stats") == null) { continue; } // if
                if (songResult.get("primary_artist") == null) { continue; } // if
                JsonObject songStats = songResult.get("stats").getAsJsonObject();
                JsonObject songArtist = songResult.get("primary_artist").getAsJsonObject();
        
                // check if song has stats and artist name. if not, move on to the next player

                if (songStats != null && songArtist != null) {   	
                   
                	songArtistName = songArtist.get("name").getAsString();
                	songName = songResult.get("title").getAsString();
                	songImageUrl = songResult.get("header_image_url").getAsString();
                	Image songImage = new Image("file:resources/placeholder_image.png", 200, 200, true, true);
                	
                	if (songImageUrl != null) { songImage = new Image(songImageUrl); } 
                	// if, check if song has image
                	if (songArtistName.equals("NBA") || songArtistName.contains("Rap Genius") || songArtistName.equals("Genius")
                			|| songArtistName.contains("NBA (Archives)") || songArtistName.equals("NFL")
                			|| songArtistName.contains("Sports Genius")) {
                		continue;
                	} // if, check if song has a valid artist
                	if (songName.contains("Resignation Letter") || songName.contains("Roster") || songName.contains("Athlete References")
                			|| songName.contains("NBA (Archives)") || songName.contains("NBA Draft") 
                			|| songName.contains("Sponsored Athletes")
                			|| songName.contains("Release Calendar") || songName.contains("Sports Genius")) {
                		continue;
                	} // if, check if song is actually a song with lyrics
                	if ((songStats.get("pageviews") == null) || (songStats.get("pageviews").getAsInt() < 100)) {
                		continue;
                	} // if, check if the song is actually popular
                	
                	songPageUrl = songResult.get("url").getAsString();
                	GeniusSong newSong = new GeniusSong(songArtistName, songName, songImage, songPageUrl);
                	
                	if (!player.checkIfSongIsPresent(newSong)) {
                		player.addSongToList(newSong);
                		System.out.println(newSong);
                	} // if, check if song is already in list       	
                } // if, check if song is valid
                
                for (ProgressBar pb : pbs) {
                	double progress = ((1.0 * i) / (numSongResults - 1));
                	updateProgress(progress, pb); //update progressbar
                } // for, update given ProgressBars
            }
    } // parseSongs
    
    /** 
     * Updates {@code pb} with the given progress amount.
     * 
     * @param progress the given progress amount
     */
    private static void updateProgress(final double progress, ProgressBar pb) {
    	//System.out.println(progress);
        Platform.runLater(() -> pb.setProgress(progress));
    } // updateProgress

    
} // GeniusApi
