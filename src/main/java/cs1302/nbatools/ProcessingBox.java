package cs1302.nbatools;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cs1302.geniustools.GeniusApi;
import cs1302.geniustools.GeniusSong;
import cs1302.geniustools.HTMLScraperApi;
import cs1302.geniustools.RosterSongPane;

/** 
 * Shows a loading screen, and processes the result for the given team.
 *
 */
public class ProcessingBox extends VBox {

    Label statusLabel;  
    ImageView loadingView;
   
    ProgressBar pb;  
    TeamButton team;
    
    static String configPath = "resources/config.properties";
    static String nbaApiKey;
    
    boolean keysSet = false;
    boolean parsed = false;
    int parsedNo = 0;
    TeamRoster roster;
    
    Stage mainStage;

    
    /**
     * Constructor. Uses the superclass {@code HBox} constructor, then initializes its components.
     *
     * @param team the {@code TeamButton} to use to construct this
     */
    public ProcessingBox(TeamButton team) {

        super(20);
        this.setAlignment(Pos.CENTER);
        
        this.team = team;
        
        Image loadingImg = new Image("file:resources/sprites/loading_gif.gif",
            100, 100, true, true);
        
        loadingView = new ImageView(loadingImg);
        
        statusLabel = new Label("Loading...");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
        
        pb = new ProgressBar(0);
        pb.setScaleShape(true);
        pb.setPrefWidth(this.getPrefWidth());

        this.getChildren().addAll(loadingView, statusLabel, pb);
            
        setKeys();
        runNow(() -> getRoster());
        
    }
    
    /**
     * Uses the NBA Api to get the list of players for a given team.
     * Then, sets {@code roster} to the result and calls {@code setSongsForRoster()} on it.
     * 
     */
    public void getRoster() {
       
        while (!keysSet) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // while, wait until api keys are set
       
        int teamId = team.getTeamId();
        String teamName = team.getTeamName();
        String nickName = team.getTeamNickName();
       
        Platform.runLater(() -> statusLabel.setText("Getting " + teamName + " Roster..."));
      
        Optional<JsonElement> optionalRosterList = NBAApi.getTeamRoster(teamId, nbaApiKey);
        JsonElement rosterList = optionalRosterList.get();
      
        if (rosterList != null) {
            roster =  NBAApi.parseRosterPlayers(rosterList, nickName, pb);     
        } else {
            Platform.runLater(() -> sendAlert("No players found. Please try again."));
        } // if roster has players then parse it, else send an alert
    
        try { 
            runNow(() -> setSongsForRoster());
        } catch (NoSuchElementException ioe) {
            Platform.runLater(() -> sendAlert("An API error occured. Please try again."));   
        } // try, throws exception if API call fails

    } // getRoster
    
    /**
     * Uses the Genius Api to get the list of songs associated with the players on {@code roster}.
     * Then, calls {@code parseSongsForRoster()} on {@code roster}.
     * 
     */
    public void setSongsForRoster() {
       
        Platform.runLater(() -> statusLabel.setText("Getting Lyric References..."));
       
        if (roster != null && !(roster.getPlayerList().isEmpty())) {
       
            int rosterSize = roster.getRosterSize();
            LinkedList<NBAPlayer> rosterList = roster.getPlayerList();
      
            for (int i = 0; i < rosterSize; i++) {
       
                NBAPlayer player = rosterList.get(i);
                String playerName = (player.getFirstName() + " " + player.getLastName());
                final int newI = i + 1;
       
                Platform.runLater(() -> statusLabel.setText("Getting Lyric References... (" + newI +
                    "/" + rosterSize + ")"));
       
                Optional<JsonElement> optionalSongList = GeniusApi.searchForSongs(playerName,
                    nbaApiKey);    
                if (optionalSongList == null) {
                    System.out.println("No songs found for " + playerName);
                } else {
                    JsonElement songList = optionalSongList.get();
                    if (songList != null) {
                        GeniusApi.parseSongs(songList, player, pb);
                    } else {
                        System.out.println("No songs found for " + playerName);
                    } // if player has songs then parse then

                } // if, search for songs with lyrics with player's full name
            } // for, go through player list, if player has same name as newPlayer return true 
        } // if, check if roster has players
       
        runNow(() -> parseSongsForRoster());
       
    }
    
    /**
     * Iterates through players on {@code roster}.
     * Calls {@code parseSongLyrics()} on each player.
     * After each player's songs are parsed, call {@code createSongPane()}.
     * 
     */
    public void parseSongsForRoster() {
       
        Platform.runLater(() -> statusLabel.setText("Parsing Song Lyrics..."));
       
        int rosterSize = roster.getRosterSize();
        LinkedList<NBAPlayer> rosterList = roster.getPlayerList();
       
        for (int i = 0; i < rosterSize; i++) {
      
            NBAPlayer player = rosterList.get(i);
            LinkedList<GeniusSong> songList = player.getSongList();
     
            final int newI = i + 1;
            runNow(() -> parseSongLyrics(player, songList, newI));
      
            double progress = ((1.0 * i) / (rosterSize - 1));
            updateProgress(progress); //update progressbar
   
        } // for, traverse roster and parse through songs for each player

        while (!parsed) {
            try {
                Thread.sleep(2000);
                checkRosterParsed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } // try
        } // wait until entire roster is parsed
       
        createSongPane();
       
    }
    
    /**
     * Creates and immediately starts a new daemon thread that executes
     * {@code target.run()}. This method, which may be called from any thread,
     * will return immediately its the caller.
     * @param target the object whose {@code run} method is invoked when this
     *               thread is started
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runNow
    
    /** 
     * Loads the file at {@code configPath} and gets the keys inside.
     * Then sets {@code nbaApiKey to the received key.}
     * 
     */
    private void setKeys() {
        try (FileInputStream configFileStream = new FileInputStream(configPath)) {
            Properties config = new Properties();
            config.load(configFileStream);
            nbaApiKey = config.getProperty("apinba.apikey");         // get apinba.apikey
            keysSet = true;
        } catch (IOException ioe) {
            Platform.runLater(() -> sendAlert("API keys not found. Please try again."));
        } // try, get keys from properties file in resources   
    }
    
    /** 
     * Creates a {@code Alert} with the given message.
     * 
     * @param msg the given message
     */
    private void sendAlert(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (mainStage != null) {
                    refresh(mainStage);
                } // if
            } // if
        });
    } // sendAlert
    
    /** 
     * Updates {@code pb} with the given progress amount.
     * 
     * @param progress the given progress amount
     */
    private void updateProgress(final double progress) {
        Platform.runLater(() -> pb.setProgress(progress));
    } // updateProgress
    
    /** 
     * Creates a new {@code RosterSongPane} using {@code roster}.
     * 
     */
    private void createSongPane() {
        Platform.runLater(() -> {
            RosterSongPane songPane = new RosterSongPane(roster,
                mainStage);
        } );
    } // updateProgress
 
    /** 
     * Sets {@code mainStage} to the given Stage.
     * 
     * @param mainStage the given Stage
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
    
    /** 
     * Iterates through {@code roster} and checks if its players are parsed.
     * If they are parsed, set {@code parsed} to true.
     * 
     */
    private void checkRosterParsed() {
        int rosterSize = roster.getRosterSize();
        LinkedList<NBAPlayer> rosterList = roster.getPlayerList();
        boolean rosterChecked = true;
        
        for (int i = 0; i < rosterSize; i++) { 
            NBAPlayer player = rosterList.get(i);
            if (!player.getSongListParsed()) {
                System.out.println((player.getFirstName() + " isn't parsed"));
                rosterChecked = false;
                parsed = false;
            }
        } // for, traverse through player list, if player song list is parsed return true
       
        if (rosterChecked) {
            parsed = true;
        } // if
    }

    /** 
     * Uses the HTML Api to parse the given NBAPlayer's {@code songList}'s lyrics.
     * 
     * @param player the given NBAPlayer
     * @param songList player's list of songs
     * @param newI the player's position in their roster
     */
    private void parseSongLyrics(NBAPlayer player, LinkedList<GeniusSong> songList, int newI) {
       
        for (int j = 0; j < songList.size(); j++) {
            
            GeniusSong song = songList.get(j);
     
            Optional<JsonElement> songOptionalFullLyrics = HTMLScraperApi.getSongLyrics(
                song.getSongUrl(), nbaApiKey); 
      
            if (songOptionalFullLyrics == null) {
                System.out.println(song.getSongUrl());
                System.out.println("Song has no lyrics");
                songList.remove(song);
                j--;
            } else {
      
                try { 
                    JsonElement lyricsElement = songOptionalFullLyrics.get();
                    JsonObject lyricsObject = lyricsElement.getAsJsonObject();
                    if (lyricsObject.get("result") != null) {
                        JsonElement lyricsResult = lyricsObject.get("result");
                        JsonArray lyricsArray = lyricsResult.getAsJsonArray();
                        while (lyricsArray == null || (lyricsArray.size() <= 0)) {
                            songOptionalFullLyrics = HTMLScraperApi.getSongLyrics(song.getSongUrl(),
                            nbaApiKey);
                            lyricsElement = songOptionalFullLyrics.get();
                            lyricsObject = lyricsElement.getAsJsonObject();
                            lyricsResult = lyricsObject.get("result");
                            lyricsArray = lyricsResult.getAsJsonArray();
                        } // while, query until html call returns lyrics
    
                        final JsonElement finallyricsElement = lyricsElement;
                        boolean result = HTMLScraperApi.parseLyrics(finallyricsElement, player,
                            song, pb);
                        if (!result) {
                            j--;
                        } // if
       
                    } else {
                        System.out.println(song.getSongUrl());
                        System.out.println("Song has no lyrics");
                        songList.remove(song);
                        j--;
                    } // if, if lyrics aren't empty then parse them, else remove song from list
       
                } catch (NoSuchElementException ioe) {
                    songList.remove(song);
                    j--;
                } // try, if song contains lyrics then parse them, else remove song from list
            } // if
       
        } // for, go through song list
      
        parsedNo++;
        Platform.runLater(() -> statusLabel.setText("Parsing Song Lyrics... (" + parsedNo + "/"
            + roster.getRosterSize() + ")"));
       
    }
    
    /** 
     * Sets the given {@code Stage}'s scene to a replica of the beginning screen.
     * 
     * @param stage the given Stage
     */
    private void refresh(Stage stage) {
       
        stage.hide();

        VBox pane = new VBox(30);
        TeamPages teamsPages = new TeamPages();
        
        pane.setAlignment(Pos.CENTER);
        
        Label currentTeamLabel = new Label();
        teamsPages.getWestPane().setTeamLabel(currentTeamLabel);
        teamsPages.getEastPane().setTeamLabel(currentTeamLabel);

        pane.getChildren().addAll(teamsPages, currentTeamLabel);
       
        Scene scene = new Scene(pane, 1280, 720);

        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.setTitle("NBA Genius");
        stage.setScene(scene);
        teamsPages.getWestPane().setMainStage(stage);
        teamsPages.getEastPane().setMainStage(stage);
        //stage.sizeToScene();
        stage.show();
      
    }

}
