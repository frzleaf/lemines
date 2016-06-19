package lemines;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

public class Cell extends JButton{
	private int number;
	private boolean flag, availb;
	public Cell(){
		this.setBorder( BorderFactory.createEtchedBorder());
		reset();
		this.setHideActionText(true);
	}
	
	public void setNumber(int n){
		if( n < 10 && n >= 0) 
			number = n;
	}
	
	public int getNumber(){
		return number;
	}
	
	/*
	 * Represent the cell with the number or bom and paint with correspontive color
	 */
	public boolean grow(boolean show){
		if( !isAvailable() || flag){
			return false;
		}
		if( number == BOM){
			//setText("X");
			setBackground(BOMCOLOR);
			setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED));
			availb = false;
			return false;
		}
		if ( number == 0){
			hideText();
			this.setEnabled(false);
			setBackground(STONE);

			availb = false;
			return true;
		}
		
		// Number = 1 -> 8
		if( show){
			showText();
		}
		paint();
		availb = false;
		return true;
	}
	public void paint(){
		switch( number){
		case 2: {
			setBackground(TWO);
			break;
		}
		case 1: {
			setBackground(ONE);
			break;
		}
		case 3: {
			setBackground(THREE);
			break;
		}
		case 4: {
			setBackground(FOUR);
			break;
		}
		case 5: {
			setBackground(FIVE);
			break;
		}
		case 6: {
			setBackground(SIX);
			break;
		}
		case 7: {
			setBackground(SEVEN);
			break;
		}
		case 8: {
			setBackground(EIGHT);
			break;
		}}
	}
	/*
	 * Check flagged status
	 */
	public void hideText(){
		setText("");
	}
	public void showText(){
		if(number > 0 && number < 9)
			setText(number + "");
	}
	public boolean isFlagged(){
		return flag;
	}
	public boolean isAvailable(){
		return availb;
	}
	
	/*
	 * Flag the cell
	 */
	public void flag(){
		if(!isAvailable()) return;
		if( flag){
			setBackground(Color.WHITE);
			//setText(null);
		} else {
			setBackground(FLAG);
			//setText(FLAG);
		}
		flag = !flag;
	}
	
	/*
	 *  Check 
	 */
	public boolean explode(){
		if( !flag && number == BOM)
			return true;
		else 
			return false;
	}
	
	/*
	 * The cell contains mine?
	 */
	public boolean isExplosible(){
		return ( number == BOM);
	}
	/*
	 *  Warning the cell that there is a nearby bom
	 */
	public void isDanger(){
		if( number < BOM)
			number++;
	}
	public void reset(){
		number = 0;
		flag = false;
		setText(null);
		setBackground(Color.WHITE);
		availb = true;
	}
	public static final int BOM = 9;
	private static final Color ONE = new Color(112, 146, 190),
							   TWO = new Color(0,128,128),
							   THREE = new Color(163,73,164),
							   FOUR = new Color(150,0,0),
							   FIVE = new Color(30,160,70),
							   SIX = new Color(132, 132,0),
							   SEVEN = new Color(185,122,87),
							   EIGHT = new Color(64,0,64),
							   STONE = new Color(128,128,128),
							   BOMCOLOR = new Color(68, 30, 68),
							   FLAG = new Color(255, 255, 0);
							
	
	/* Original style
	 * ONE = new Color(0, 128, 192),
							   TWO = new Color(0,70,200),
							   THREE = new Color(0,217,108),
							   FOUR = new Color(255,255,0),
							   FIVE = new Color(255,128,0),
							   SIX = new Color(255,0,0),
							   SEVEN = new Color(255,128,0),
							   EIGHT = new Color(128,0,128),
							   STONE = new Color(128,128,128),
							   BOMCOLOR = new Color(106, 0, 53),
							   FLAG = new Color(255, 0, 128);
							   
	 */
}
