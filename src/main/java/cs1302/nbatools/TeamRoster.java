package cs1302.nbatools;

import java.util.LinkedList;

/** 
 * Roster, contains a list of NBAPlayers in the team.
 *
 */
public class TeamRoster {
	
    private LinkedList<NBAPlayer> playerList = new LinkedList<NBAPlayer>();
    private String rosterName;
    private String teamNickName;
	
    /**
     * Default Constructor. Sets {@code rosterName} to "none".
     *
     */
    public TeamRoster() {
        this.rosterName = "none";
    }
	
    /**
     * Overloaded Constructor. Sets {@code rosterName} to the given String.
     *
     * @param teamName the given string
     */
    public TeamRoster(String teamName) {
        this.rosterName = teamName;
    }
	
    /**
     * Overloaded Constructor. Sets {@code rosterName} to the given String.
     * Sets {@code teamNickName} to the given String.
     *
     * @param teamName the given string
     * @param teamNickName the given string
     */
    public TeamRoster(String teamName, String teamNickName) {
        this.rosterName = teamName;
        this.teamNickName = teamNickName;
    }
	
    /**
     * Sets {@code rosterName} to the given String.
     *
     * @param newName the given string
     */
    public void setRosterName(String newName) {
        this.rosterName = newName;
    }
	
    /**
     * Adds the given NBAPlayer to {@code playerList}.
     *
     * @param newPlayer the given NBAPlayer
     */
    public void addPlayer(NBAPlayer newPlayer) {
        playerList.add(newPlayer);
    }
	
    /**
     * Returns {@code playerList.size()}.
     *
     * @return the size of {@code playerList}
     */
    public int getRosterSize() {
        return playerList.size();
    }
	
    /**
     * Returns {@code playerList}.
     *
     * @return the list of players on the team
     */
    public LinkedList<NBAPlayer> getPlayerList() {
        return playerList;
    }
	
    /**
     * Returns {@code rosterName}.
     *
     * @return the team's name
     */
    public String getRosterName() {
        return this.rosterName;
    }
	
    /**
     * Returns {@code teamNickName}.
     *
     * @return the team's nickname
     */
    public String getTeamNickName() {
        return this.teamNickName;
    }
	
    /**
     * If {@code playerList} contains the given NBAPlayer, return {@code true}.
     * Else, return {@code false}.
     * 
     * @param newPlayer the given NBAPlayer
     * @return boolean representation of player's presence
     */
    public boolean checkIfPlayerIsPresent(NBAPlayer newPlayer) {
		
        String first = newPlayer.getFirstName();
        String last = newPlayer.getLastName();
        for (int i = 0; i < playerList.size(); i++) {
            NBAPlayer player = playerList.get(i);
            if (player.getFirstName().equals(first) && player.getLastName().equals(last)) {
                return true;
            }
        } // for, traverse through player list, if player has same name as newPlayer return true
        return false;
		
    }
	
    /**
     * Returns a string representation of {@code this}.
     * 
     * @return String representation of current roster
     */
    @Override
    public String toString() {
        String str = "";
        NBAPlayer currentPlayer;
        for (int i = 0; i < playerList.size(); i++) {
            currentPlayer = playerList.get(i);
            str = str + (currentPlayer.getFirstName() + " " + currentPlayer.getLastName() + "\n");
        } // for, traverse through the json array, if result has an image then add img url to list
        return str;
    }

}
