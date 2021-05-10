package cs1302.geniustools;

import java.util.ArrayList;

import cs1302.nbatools.NBAPlayer;
import javafx.scene.image.Image;

/** 
 * Contains information about a song.
 *
 */
public class GeniusSong {
	
	String artistName;
	String songName;
	
	Image songPicture;
	ArrayList<String> lyrics = new ArrayList<String>();
	String songUrl;
	boolean parsed = false;

	/**
     * Default Constructor. Sets variables to default values.
     * 
     */
	public GeniusSong() {
		
		artistName = "default";
		songName = "default";
		
		songPicture = new Image("file:resources/placeholder_image.png");
		songUrl = "none";
		
	}
	
	/**
     * Constructor. Creates a new {@code GeniusSong} using the given parameters.
     * 
     * @param artist the song artist's name
     * @param song the song's name
     * @param pic the song's picture on Genius
     * @param url the url of the song on Genius
     */
	public GeniusSong(String artist, String song, Image pic, String url) {	
		this.artistName = artist;
		this.songName = song;
		
		this.songPicture = pic;
		this.songUrl = url;
	}
	
	/**
     * Returns {@code songName}.
     *
     * @return the song's name
     */
	public String getSongName() {
		return this.songName;
	}
	
	/**
     * Returns {@code songUrl}.
     *
     * @return the song's url
     */
	public String getSongUrl() {
		return this.songUrl;
	}
	
	/**
     * Returns {@code artistName}.
     *
     * @return the song artist's name
     */
	public String getArtistName() {
		return this.artistName;
	}
	
	/**
     * Returns {@code songPicture}.
     *
     * @return the song's picture on Genius
     */
	public Image getSongPicture() {
		return this.songPicture;
	}
	
	/**
     * Sets {@code lyrics} to the given list.
     *
     * @param lyrics the given list
     */
	public void setLyrics(ArrayList<String> lyrics) {
		this.lyrics = lyrics;
	}
	
	/**
     * Sets {@code parsed} to the given boolean.
     *
     * @param val the given boolean
     */
	public void setParsed(boolean val) {
		this.parsed = val;
	}
	
	/**
     * Returns {@code parsed}.
     *
     * @return whether or not the song is parsed
     */
	public boolean getParsed() {
		return this.parsed;
	}
	
	/**
     * Returns {@code lyrics}.
     *
     * @return list of song's lyrics
     */
	public ArrayList<String> getLyrics() {
		return this.lyrics;
	}
	
	/**
     * Returns the song's artist name and name.
     * 
     * @return String representation of song
     */
	@Override
	public String toString() {
		return (artistName + " - " + songName);
	}

}
