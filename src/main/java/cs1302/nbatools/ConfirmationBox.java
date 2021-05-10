package cs1302.nbatools;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/** 
 * Allows user to confirm if they want to use the current team.
 *
 */
public class ConfirmationBox extends VBox {

    Label teamNameLabel;
    ImageView teamImageView;
    Label confirmationLabel;
    
    Button yesButton;
    Button noButton;
    
    Stage mainStage;
    Scene originScene;
    
    TeamButton team;

    
    /**
     * Constructor. Uses the superclass {@code VBox} constructor, then initializes its components.
     *
     * @param team the {@code TeamButton} to set team to
     * @param currentScene the {@code Scene} to set currentScene to
     */
    public ConfirmationBox(TeamButton team, Scene currentScene) {

        super(70);
        
        this.team = team;
        this.originScene = currentScene;
        this.setMinWidth(1280);
        this.setMinHeight(720);
        this.setAlignment(Pos.CENTER);
        
        String teamName = team.getTeamName();
        Image teamImg = team.getTeamImg();
        
        teamNameLabel = new Label(teamName);
        teamNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        
        teamImageView = new ImageView();
        teamImageView.setImage(teamImg);
        teamImageView.setSmooth(true);
        teamImageView.setScaleX(2);
        teamImageView.setScaleY(2);
        
        confirmationLabel = new Label();
        confirmationLabel.setText("Are you sure you want to select the " + teamName + "?");
        
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        
        yesButton = new Button("Yes");
        yesButton.setOnAction(this::setProcessingScene);
        
        noButton = new Button("No");
        noButton.setOnAction(this::setPagesScene);
        
        buttonBox.getChildren().addAll(yesButton, noButton);    
        this.getChildren().addAll(teamNameLabel, teamImageView, confirmationLabel, buttonBox);
        
    } // ConfirmationBox
    
    /** 
     * Sets {@code mainStage} to the given Stage.
     * 
     * @param mainStage the given Stage
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    } // setMainStage
    
    /** 
     * Changes {@code mainStage}'s scene to {@code originScene}.
     * 
     * @param e source event
     */
    private void setPagesScene(ActionEvent e) {
        if (mainStage != null) {
            if (originScene != null) {
                Platform.runLater(() -> mainStage.setScene(originScene));
            } // if
        } // if
    } // setPagesScene
    
    /** 
     * Changes {@code mainStage}'s scene to a new {@code ProcessingBox}.
     * 
     * @param e source event
     */
    private void setProcessingScene(ActionEvent e) {
        if (mainStage != null) {
            ProcessingBox processor = new ProcessingBox(team);
            processor.setMainStage(mainStage);
            Scene scene = new Scene(processor, 1280, 720);
            Platform.runLater(() -> mainStage.setScene(scene));
        } // if
    } // setProcessingScene


}
