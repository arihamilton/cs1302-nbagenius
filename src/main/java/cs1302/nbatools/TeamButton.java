package cs1302.nbatools;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.application.Platform;

/** 
 * Button representation of a given team.
 *
 */
public class TeamButton extends Button {
	
	private Image teamLogo;
	private String teamName;
	private int teamId;
	private String teamNickName;
	
	/**
     * Constructor. Uses the superclass {@code Button} constructor. 
     * Then initializes its components using the given parameters.
     *
     */
	public TeamButton(String imgUrl, String teamName, int teamId, String teamNickName) {

        super();
        
        teamLogo = new Image(imgUrl, 100, 100, true, true);
        this.teamName = teamName;
        this.teamId = teamId;
        this.teamNickName = teamNickName;
        
        ImageView teamLogoView = new ImageView(teamLogo);
        
        this.setGraphic(teamLogoView);
        
    }
	
	/** 
     * Sets {@code teamName} to the given String.
     * 
     * @param teamName the given String
     */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
     * Returns {@code teamName}.
     *
     * @return the team's name
     */
	public String getTeamName() {
		return this.teamName;
	}
	
	/**
     * Returns {@code teamId}.
     *
     * @return the team's id
     */
	public int getTeamId() {
		return this.teamId;
	}
	
	/**
     * Returns {@code teamLogo}.
     *
     * @return the team's logo
     */
	public Image getTeamImg() {
		return this.teamLogo;
	}
	
	
	
}
