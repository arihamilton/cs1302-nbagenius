package cs1302.geniustools;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.animation.Timeline;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import cs1302.nbatools.NBAPlayer;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/** 
 * Visual representation of a {@code GeniusSong}.
 *
 */
public class SongBox extends VBox {

    Label songName;
    ImageView songImageView;
    Label songLyric;   
    
    /**
     * Constructor. Uses the superclass {@code VBox} constructor, then initializes its components.
     *
     * @param player the player the song is for
     * @param song the song to visualize
     * @param songLyrics the song's lyrics
     */
    public SongBox(NBAPlayer player, GeniusSong song, String songLyrics) {

        super(15);
        
        this.setAlignment(Pos.CENTER);
        
        String artistName = (song.getArtistName() + " - " + song.getSongName());
        
        ArrayList<String> swearList = new ArrayList<String>();
    	try {
    		FileReader txtFile = new FileReader("resources/filterwords.txt");
    		Scanner scan = new Scanner(txtFile);
            while(scan.hasNextLine()) {
            	swearList.add(scan.nextLine());
            } // while, get list of swears
            scan.close();
    	} catch (FileNotFoundException ioe) {
            System.err.println("Filter list not found");
        } // try
        
        artistName = HTMLScraperApi.censorString(artistName, swearList);
        songLyrics = HTMLScraperApi.censorString(songLyrics, swearList);
        
        songName = new Label(artistName);
        songName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        songImageView = new ImageView(song.getSongPicture());
        songImageView.setFitHeight(300);
        songImageView.setFitWidth(300);
        songImageView.setStyle("-fx-background-color: black; -fx-padding: 5");
        
        songLyric = new Label(songLyrics);
        songLyric.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        songLyric.setAlignment(Pos.CENTER);
        songLyric.setTextAlignment(TextAlignment.CENTER);
        songLyric.setStyle("-fx-background-color: #f7e672");
        
        this.setStyle("-fx-background-color: #ffffff");
        this.getChildren().addAll(songName, songImageView, songLyric);
        
    } // SongBox
    
  
}
