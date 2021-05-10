package cs1302.geniustools;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;

import java.util.Collection;
import java.util.LinkedList;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import java.util.Random;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javafx.scene.layout.TilePane;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import javafx.application.Platform;
import cs1302.nbatools.ConfirmationBox;
import cs1302.nbatools.NBAPlayer;
import cs1302.nbatools.TeamButton;
import cs1302.nbatools.TeamPages;
import cs1302.nbatools.TeamRoster;

/** 
 * TabPane, displays a tab for each player in the given roster.
 * Creates a {@code SongBox} for each song in each player's song list.
 *
 */
public class RosterSongPane extends TabPane {
    
    Label currentTeamLabel;
    Stage mainStage;
    
    /**
     * Constructor. Uses the superclass {@code TabPane} constructor, then initializes its components.
     * Then sets the given {@code Stage}'s scene to the pane.
     * 
     * @param roster the given team roster
     * @param mainStage the given stage
     */
    public RosterSongPane(TeamRoster roster, Stage mainStage) {

        super();
        this.mainStage = mainStage;
        
        LinkedList<NBAPlayer> playerList = roster.getPlayerList();
        
        for (int i = 0; i < playerList.size(); i++) {
        	NBAPlayer currentPlayer = playerList.get(i);
        	if (currentPlayer.getSongList().size() > 0) {
        		String playerName = currentPlayer.getFirstName() + " " + currentPlayer.getLastName();
        		Tab tab = new Tab();
        		tab.setText(playerName);
        		
        		LinkedList<GeniusSong> songList = currentPlayer.getSongList();
        		
        		Pagination pagination = new Pagination(songList.size(), 0);
        		
        		pagination.setPageFactory((index) -> {
        				
        			GeniusSong currentSong = songList.get(index);
        			String condensedLyrics = HTMLScraperApi.getCondensedLyrics(currentPlayer, currentSong.getLyrics());
        	        
        			return new SongBox(currentPlayer, currentSong, condensedLyrics);
        			
        	        });
        		
        		tab.setContent(pagination);
        		this.getTabs().add(tab);

        	} // if, if player has songs then create pages for them
        	
        } // for, check roster and add tab if player has songs
        
        Button goBackButton = new Button("Return to Selection Screen");
        goBackButton.setOnAction(this::refreshStage);
        goBackButton.setStyle("-fx-background-color: aliceblue; -fx-background-radius: 8,7,5");
        VBox completeBox = new VBox(15);
        completeBox.setAlignment(Pos.CENTER);
        
        completeBox.getChildren().addAll(this, goBackButton);
        completeBox.setStyle("-fx-background-color: #f7f7f7");
   
        Scene scene = new Scene(completeBox, 1280, 720);
        mainStage.setScene(scene);
     
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
     * Refreshes {@code mainStage}'s scene.=.
     * 
     * @param e source event
     */
    private void refreshStage(ActionEvent e) {

    	if (mainStage != null) {

        Platform.runLater(() -> refresh(mainStage));
    	}
    } // updateTeamLabelText
    
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
        
        currentTeamLabel = new Label();
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
