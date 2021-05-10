package cs1302.nbatools;

import java.util.LinkedList;

import cs1302.geniustools.GeniusSong;

/** 
 * Contains information about an NBA player.
 *
 */
public class NBAPlayer {
       
    private String firstName;
    private String lastName;
    private String teamName;
    private String teamNickName;
       
    private LinkedList<GeniusSong> songList = new LinkedList<GeniusSong>();
       
    /**
     * Constructor.
     * Sets {@code firstName} to the given String name.
     * Sets {@code lastName} to the given String name.
     *
     * @param first the given first name
     * @param last the given last name
     */
    public NBAPlayer(String first, String last) {      
        this.firstName = first;
        this.lastName = last;
    }
       
    /**
     * Constructor. Creates a new {@code NBAPlayer} using the given parameters.
     * 
     * @param first the given first name
     * @param last the given last name
     * @param teamName the player's team
     * @param teamNickName the player's team's nickname
     */
    public NBAPlayer(String first, String last, String teamName, String teamNickName) {
    
        this.firstName = first;
        this.lastName = last;
        this.teamName = teamName;
        this.teamNickName = teamNickName;
    }
       
       
    /**
     * Returns {@code firstName}.
     *
     * @return the player's first name
     */
    public String getFirstName() {
        return this.firstName;
    }
       
    /**
     * Returns {@code lastName}.
     *
     * @return the player's last name
     */
    public String getLastName() {
        return this.lastName;
    }
       
    /**
     * Returns {@code teamName}.
     *
     * @return the player's team name
     */
    public String getTeamName() {
        return this.teamName;
    }
       
    /**
     * Returns {@code teamNickName}.
     *
     * @return the player's team's nickname
     */
    public String getTeamNickName() {
        return this.teamNickName;
    }
       
    /**
     * If all of the songs in {@code songList} are parsed, return true.
     * Else, return false.
     *
     * @return the parsed state of {@code songList}
     */
    public boolean getSongListParsed() {
        if (songList.size() < 1) {
            return true;
        } // if
        for (int i = 0; i < songList.size(); i++) {
            GeniusSong checkSong = songList.get(i);
            if (!checkSong.getParsed()) {
                return false;
            } // if
        } // for, traverse through song list, if song isn't parsed return false
        return true;
    }

    /**
     * Returns {@code songList}.
     *
     * @return the player's list of songs
     */
    public LinkedList<GeniusSong> getSongList() {
        return this.songList;
    }
       
    /**
     * If the given song is in {@code songList}, return true.
     * Else, return false.
     *
     * @param song the {@code GeniusSong} to check
     * @return status of song's presence in the player's song list
     */
    public boolean checkIfSongIsPresent(GeniusSong song) {
       
        String songName = song.getSongName();
        for (int i = 0; i < songList.size(); i++) {
            GeniusSong checkSong = songList.get(i);
            if (checkSong.getSongName().equals(songName)) {
                return true;
            } // if
        } // for, traverse through player list, if player has same name as newPlayer return true
        return false;
       
    }
       
    /**
     * Adds the given song to {@code songList}.
     *
     * @param song the {@code GeniusSong} to add to the list of songs
     */
    public void addSongToList(GeniusSong song) {
        songList.add(song);
    }

}
