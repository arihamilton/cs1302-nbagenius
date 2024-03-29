package cs1302.nbatools;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;

import java.util.Collection;
import java.util.LinkedList;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import javafx.scene.layout.TilePane;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import javafx.application.Platform;
import cs1302.nbatools.TeamButton;

/** 
 * Pane that shows eastern teams. Contains 15 teamButtons.
 *
 */
public class EasternTeamPane extends TilePane {
       
    private final TeamButton[] easternTeams = {
        new TeamButton("file:resources/logos_eastern/hawks.png", "Atlanta Hawks", 1, "hawks"),
        new TeamButton("file:resources/logos_eastern/celtics.png", "Boston Celtics", 2, "celtics"),
        new TeamButton("file:resources/logos_eastern/nets.png", "Brooklyn Nets", 4, "nets"),
        new TeamButton("file:resources/logos_eastern/hornets.png",
        "Charlotte Hornets", 5, "hornets"),
        new TeamButton("file:resources/logos_eastern/bulls.png", "Chicago Bulls", 6, "bulls"),
        new TeamButton("file:resources/logos_eastern/cavaliers.png",
        "Cleveland Cavaliers", 7, "cavaliers"),
        new TeamButton("file:resources/logos_eastern/pistons.png",
        "Detroit Pistons", 10, "pistons"),
        new TeamButton("file:resources/logos_eastern/pacers.png", "Indiana Pacers", 15, "pacers"),
        new TeamButton("file:resources/logos_eastern/heat.png", "Miami Heat", 20, "heat"),
        new TeamButton("file:resources/logos_eastern/bucks.png", "Milwaukee Bucks", 21, "bucks"),
        new TeamButton("file:resources/logos_eastern/knicks.png", "New York Knicks", 24, "knicks"),
        new TeamButton("file:resources/logos_eastern/magic.png", "Orlando Magic", 26, "magic"),
        new TeamButton("file:resources/logos_eastern/76ers.png", "Philadelphia 76ers", 27, "76ers"),
        new TeamButton("file:resources/logos_eastern/raptors.png",
        "Toronto Raptors", 38, "raptors"),
        new TeamButton("file:resources/logos_eastern/wizards.png",
        "Washington Wizards", 41, "wizards")};
    // array of teamButtons for the eastern conference
    
    Label currentTeamLabel;
    Stage mainStage;
    Scene currentStageScene;
 
    /**
     * Constructor. Uses the superclass {@code TilePane} constructor.
     * Then initializes its components.
     *
     */
    public EasternTeamPane() {

        super();
        this.setPrefColumns(5);
        this.setPrefRows(3);
        this.setMinSize(100,100);
        this.setHgap(30);
        this.setVgap(30);
        
        this.setAlignment(Pos.CENTER);
        
        for (int i = 0; i < easternTeams.length; i++) {
            easternTeams[i].setOnMouseEntered(this::updateTeamLabelText);
            easternTeams[i].setOnAction(this::setConfirmScene);
            this.getChildren().add(easternTeams[i]);
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
    } // setConfirmScene  

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
     * @param mainStage the given Stage
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
        currentStageScene = mainStage.getScene();
        System.out.println(currentStageScene);
    }
    
  
}
