package cs1302.omega;


import cs1302.nbatools.TeamPages;
import cs1302.nbatools.WesternTeamPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import static java.net.URLEncoder.encode;
import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;

import java.io.IOException;

import java.nio.charset.Charset;
import com.google.gson.JsonElement;

/**
 * NBA Genius! User selects a team.
 * The app queries for the members of that team,
 * then shows how many songs reference those members.
 */
public class OmegaApp extends Application {
    
    Label currentTeamLabel;
    
    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        
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

    } // start
    
    

} // OmegaApp
