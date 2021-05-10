package cs1302.geniustools;

import static java.net.URLEncoder.encode;
import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.nio.charset.Charset;
import java.text.Normalizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cs1302.api.Tools;
import cs1302.nbatools.NBAPlayer;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.util.Pair;

/**
 * Contains methods for ScraperMonkey API.
 *
 */
public class HTMLScraperApi {

    private static final String ENDPOINT = "https://scrapingmonkey.p.rapidapi.com/byClass&";
    
    /**
     * Return an {@code Optional} describing the root element of the JSON
     * response for a "get page HTML" query.
     * 
     * @param lyricsUrl url to page
     * @param htmlApiKey key for API query
     * @return an {@code Optional} describing the root element of the response
     */
    public static Optional<JsonElement> getSongLyrics(String lyricsUrl, String htmlApiKey) {
        try {
            String url = ENDPOINT + "class=lyrics&url=" + encode(lyricsUrl, UTF8);
            return Optional.<JsonElement>ofNullable(Tools.getHttpJsonPOST(url, "POST",
                new Pair<>("content-type", "application/x-www-form-urlencoded"),
                new Pair<>("x-rapidapi-key", htmlApiKey),
                new Pair<>("x-rapidapi-host", "scrapingmonkey.p.rapidapi.com")));
        } catch (IOException ioe) {
            System.out.println(ioe.getLocalizedMessage());
            return Optional.<JsonElement>empty();
        } // try
    } // getSongLyrics
    
    /**
     * Separate the JSON response into lines.
     * Then check if the response actually contains the player's name.
     * 
     * @param lyricsElement the JSON response to separate
     * @param player the {@code NBAPlayer} to check for
     * @param song the given song
     * @param pbs list of {@code ProgressBar}s to update as method runs
     * 
     * @return true if lyrics contain name, false otherwise
     */
    public static boolean parseLyrics(JsonElement lyricsElement, NBAPlayer player
        , GeniusSong song, ProgressBar... pbs) {
        //try {
        JsonElement results = get(lyricsElement, "result");
        String fullLyrics = results.getAsString();
            
        fullLyrics = Normalizer.normalize(fullLyrics, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        fullLyrics = pattern.matcher(fullLyrics).replaceAll("");      
        // Code retrieved from https://stackoverflow.com/questions/1008802, strips accents

        fullLyrics = fullLyrics.replace("&quot;", "\"");
        fullLyrics = fullLyrics.replace("&amp;", "&");

        String[] splitLyrics = fullLyrics.split("\\s(?=[A-Z])");
        List<String> splitLyricsList = Arrays.asList(splitLyrics);
        ArrayList<String> splitLyricsArrayList = new ArrayList<String>(splitLyricsList);
        // split lyrics based on spaces
        editLyricsArray(splitLyricsArrayList, 25);
        editLyricsArray(splitLyricsArrayList, player.getFirstName(), player.getLastName());
        // if line contains last name but not first combine it with the last
            
        for (int i = 1; i < splitLyricsArrayList.size(); i++) {
            String previousString = splitLyricsArrayList.get(i - 1);
            String lastWord = previousString.substring(previousString.lastIndexOf(" ") + 1);
            char lastChar = lastWord.charAt(0);
            String currentLine = splitLyricsArrayList.get(i);
            if (Character.isUpperCase(lastChar)) {
                String newString = previousString + " " + splitLyricsArrayList.get(i);
                //System.out.println("NEW STRING: " + newString);
                splitLyricsArrayList.set(i - 1, newString);
                splitLyricsArrayList.remove(i);
                i--;
            } // if, if preceding line ends with a capital combine it with the last
        } // for
            
        editLyricsArray(splitLyricsArrayList, "[", "]"); 
        // if preceding line is a descriptor combine it with last
        String playerName = (player.getFirstName() + " " + player.getLastName());
        boolean containsName = false;
            
        for (int i = 0; i < splitLyricsArrayList.size(); i++) {
            String currentLyrics = splitLyricsArrayList.get(i);
            if (currentLyrics.contains(playerName)) {
                containsName = true;
            } // if, if lyrics actually contain player name then return true
        } // for
        if (!containsName) {
            player.getSongList().remove(song);
            return false;
        } else {
            song.setLyrics(splitLyricsArrayList);
            song.setParsed(true);
        } // if, remove song if doesn't contain player name  

        return true;

    } // search
    
    /** 
     * Updates {@code pb} with the given progress amount.
     * 
     * @param progress the given progress amount
     * @param pb the ProgressBar to update
     */
    private static void updateProgress(final double progress, ProgressBar pb) {
        Platform.runLater(() -> pb.setProgress(progress));
    } // updateProgress
    
    /**
     * Returns the lyrics about the given {@code NBAPlayer}.
     * 
     * @param player the given {@code NBAPlayer}
     * @param lyrics the given lyrics
     * 
     * @return condensed version of song lyrics
     */
    public static String getCondensedLyrics(NBAPlayer player, ArrayList<String> lyrics) {

        String condensedLyrics = "";
        String playerName = (player.getFirstName() + " " + player.getLastName());

        for (int i = 0; i < lyrics.size(); i++) {
            if (lyrics.get(i).contains(playerName)) {
                if (i > 0) {
                    condensedLyrics = condensedLyrics + lyrics.get(i - 1);
                } // if, if index isn't first then add content of first index
                condensedLyrics = condensedLyrics + ("\n" + lyrics.get(i));
                if (i < (lyrics.size() - 1)) {
                    condensedLyrics = condensedLyrics + ("\n" + lyrics.get(i + 1));
                } // if, if index isn't last then add content of first index
                break;
            } // if, if lyrics actually contain player name then return true
        } // for
        return condensedLyrics;
    }
    
    /**
     * Returns the filtered version of the lyrics.
     * 
     * @param str the string to filter
     * @param filterList the list of words to filter out
     * 
     * @return filtered version of song lyrics
     */
    public static String censorString(String str, ArrayList<String> filterList) {
 
        String newStr = str; 
        for (int i = 0; i < filterList.size(); i++) {
            String swear = filterList.get(i);
            if (newStr.toLowerCase().contains(swear)) {
                newStr = newStr.replaceAll(("(?i)" + swear), "****");
            } // if
        } // for
       
        return newStr;
    }
    
    /**
     * Edits the given lyrics based on the line's length.
     * 
     * @param list the list of lyrics to edit
     * @param length the length to edit based on
     * 
     */
    private static void editLyricsArray(ArrayList<String> list, int length) {

        for (int i = 1; i < list.size(); i++) {
            String previousString = list.get(i - 1);
            if (previousString.length() < length) {
                String newString = previousString + " " + list.get(i);
                //System.out.println("NEW STRING: " + newString);
                list.set(i - 1, newString);
                list.remove(i);
                i--;
            } // if, if preceding line is too short combine it with the last
        } // for
    }
    
    /**
     * Edits the given lyrics based what the lines contain.
     * 
     * @param list the list of lyrics to edit
     * @param prevContains what to check for the previous lines
     * @param nextContains what to check for the current lines
     * 
     */
    private static void editLyricsArray(ArrayList<String> list, String prevContains
        , String nextContains) {
        for (int i = 1; i < list.size(); i++) {
            String previousString = list.get(i - 1);
            String currentLine = list.get(i);
            if (previousString.contains(prevContains) && currentLine.contains(nextContains)) {
                String newString = previousString + " " + list.get(i);
                list.set(i - 1, newString);
                list.remove(i);
                i--;
            } // if, if previous line and next line contain certain strings then combine them
        } // for
    }
  
} // OpenLibrarySearchApi
