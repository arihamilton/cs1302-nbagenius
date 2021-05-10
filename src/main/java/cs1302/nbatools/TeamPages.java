package cs1302.nbatools;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** 
 * Contains pages of the western and eastern team panes.
 *
 */
public class TeamPages extends Pagination {
	
	WesternTeamPane westPane;
	EasternTeamPane eastPane;
	
	/**
     * Constructor. Uses the superclass {@code Pagination} constructor, then initializes its components.
     *
     */
	public TeamPages() {

		super();
		this.setPageCount(2);
		
		westPane = new WesternTeamPane();
		eastPane = new EasternTeamPane();
		
		Label westTitleLabel = new Label("Western Conference");
		westTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		westTitleLabel.setTextFill(Paint.valueOf("Red"));
		
		Label eastTitleLabel = new Label("Eastern Conference");
		eastTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		eastTitleLabel.setTextFill(Paint.valueOf("Blue"));
		
		VBox pane1 = new VBox(westTitleLabel, westPane);
		pane1.setSpacing(15);
		pane1.setAlignment(Pos.CENTER);
		
		VBox pane2 = new VBox(eastTitleLabel, eastPane);
		pane2.setSpacing(15);
		pane2.setAlignment(Pos.CENTER);
		
		this.setPageFactory((pageIndex) -> { if (pageIndex == 0) return pane1; return pane2;});
	}
	
	/**
     * Returns {@code westPane}.
     *
     * @return {@code westPane}
     */
	public WesternTeamPane getWestPane() {
		return westPane;
	}
	
	/**
     * Returns {@code eastPane}.
     *
     * @return {@code eastPane}
     */
	public EasternTeamPane getEastPane() {
		return eastPane;
	}

}
