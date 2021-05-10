package cs1302.nbatools;

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
import javafx.stage.Stage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javafx.scene.layout.TilePane;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import javafx.application.Platform;
import cs1302.nbatools.TeamButton;

/** 
 * Pane that shows western teams. Contains 15 teamButtons.
 *
 */
public class WesternTeamPane extends TilePane {
	
	private final TeamButton[] westernTeams = {
			new TeamButton("file:resources/logos_western/mavericks.png", "Dallas Mavericks", 8, "mavericks"),
		    new TeamButton("file:resources/logos_western/nuggets.png", "Denver Nuggets", 9, "nuggets"),
		    new TeamButton("file:resources/logos_western/warriors.png", "Golden State Warriors", 11, "warriors"),
		    new TeamButton("file:resources/logos_western/rockets.png", "Houston Rockets", 14, "rockets"),
		    new TeamButton("file:resources/logos_western/clippers.png", "Los Angeles Clippers", 16, "clippers"),
		    new TeamButton("file:resources/logos_western/lakers.png", "Los Angeles Lakers", 17, "lakers"),
		    new TeamButton("file:resources/logos_western/grizzlies.png", "Memphis Grizzlies", 19, "grizzlies"),
		    new TeamButton("file:resources/logos_western/timberwolves.png", "Minnesota Timberwolves", 22, "timberwolves"),
		    new TeamButton("file:resources/logos_western/pelicans.png", "New Orleans Pelicans", 23, "pelicans"),
		    new TeamButton("file:resources/logos_western/thunder.png", "OKC Thunder", 25, "thunder"),
		    new TeamButton("file:resources/logos_western/suns.png", "Phoenix Suns", 28, "suns"),
		    new TeamButton("file:resources/logos_western/trailblazers.png", "Portland Trail Blazers", 29, "trail blazers"),
		    new TeamButton("file:resources/logos_western/kings.png", "Sacramento Kings", 30, "kings"),
		    new TeamButton("file:resources/logos_western/spurs.png", "San Antonio Spurs", 31, "spurs"),
		    new TeamButton("file:resources/logos_western/jazz.png", "Utah Jazz", 40, "jazz")};
	// array of teamButtons for the western conference
    
    Label currentTeamLabel;
    Stage mainStage;
    Scene currentStageScene;
 
    /**
     * Constructor. Uses the superclass {@code TilePane} constructor, then initializes its components.
     *
     */
    public WesternTeamPane() {

        super();
        this.setPrefColumns(5);
        this.setPrefRows(3);
        this.setMinSize(100,100);
        this.setHgap(30);
        this.setVgap(30);
        
        this.setAlignment(Pos.CENTER);
        
        for (int i = 0; i < westernTeams.length; i++) {
        	westernTeams[i].setOnMouseEntered(this::updateTeamLabelText);
        	westernTeams[i].setOnAction(this::setConfirmScene);
            this.getChildren().add(westernTeams[i]);
        } // for, add all imageviews to this (TilePane)
   
     
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
     * Sets {@code currentTeamLabel} to the name of the teamButton that called it.
     * 
     * @param e source event
     */
    private void updateTeamLabelText(MouseEvent e) {
    	TeamButton sourceImg = (TeamButton)e.getSource();
        Platform.runLater(() -> currentTeamLabel.setText(sourceImg.getTeamName()));
    } // updateTeamLabelText
    
    /** 
     * Changes {@code mainStage}'s scene to a new ConfirmationBox.
     * 
     * @param e source event
     */
    private void setConfirmScene(ActionEvent e) {
    	TeamButton sourceImg = (TeamButton)e.getSource();
    	
    	if (mainStage != null) {
    	ConfirmationBox confirmationBox = new ConfirmationBox(sourceImg, currentStageScene);
    	confirmationBox.setMainStage(mainStage);
    	Scene scene = new Scene(confirmationBox, 1280, 720);
        Platform.runLater(() -> mainStage.setScene(scene));
    	}
    } // updateTeamLabelText
    

    /** 
     * Sets {@code currentTeamLabel} to the given Label.
     * 
     * @param label the given Label
     */
    public void setTeamLabel(Label label) {
        this.currentTeamLabel = label;
    } // setProgressBar
    
    /** 
     * Sets {@code mainStage} to the given Stage.
     * 
     * @param stage the given Stage
     */
    public void setMainStage(Stage mainStage) {
		this.mainStage = mainStage;
		currentStageScene = mainStage.getScene();
		System.out.println(currentStageScene);
	}
    
  
}
