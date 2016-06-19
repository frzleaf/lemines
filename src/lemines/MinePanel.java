

/*
 * le_mines
 */


package lemines;

import java.awt.GridLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MinePanel extends JPanel implements MouseListener{
	
	private Cell [][] cells;
	private int m,n,mines,remain, flags;
	private boolean fClik, end, showText;
	private int[] coord = new int [2];
	JMenu flagJM;
	// Other items in the menu

	
	public MinePanel(int m, int n, int mines ){
		
		this.m = m;
		this.n = n;
		this.mines = mines;
		flags = mines;
		remain = m*n - mines;
		end = false;
		fClik = true;
		cells = new Cell[m][n];
		showText = true;
		
		// Build Flag counter
		flagJM = new JMenu(" Flags: " + flags);
		flagJM.setEnabled(false);
		
		this.setLayout(new GridLayout(m,n));
		
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				cells[i][j] = new Cell();
				this.add(cells[i][j]);
				cells[i][j].addMouseListener(this);
			}
		
		
	}
	
	/*public static void main(String args[]){
		
		MinePanel mf = new MinePanel(17, 30, 99);
		
		JFrame a = new JFrame();
		a.getContentPane().add(mf);
		a.setSize(1300, 700);
		a.setVisible(true);
		a.setDefaultCloseOperation(3);
	} */
	
	private void roll( int x, int y){
		
		ArrayList<Integer> celist = new ArrayList<>();
		Integer [][] mineInt = new Integer[m][n];
		
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				if( (i)*(n) + (j + 1) <= mines)
					mineInt[i][j] = Cell.BOM;
				else 
					mineInt[i][j] = 0;
				celist.add(mineInt[i][j]);
		}
		
		int leng = m*n;
		int ind = 0;
		
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				if( leng > 0)
					ind = (int)(Math.random()*(leng--));
					mineInt[i][j] = celist.get(ind);
					celist.remove(ind);
		}
		
		int randInd;
		while ( mineInt[x][y] == Cell.BOM ){  // Swap clicking-mine with another
			randInd = (int)(Math.random()*m*n);
			if( mineInt[ (int)(randInd/n)][randInd % n] != Cell.BOM){
				mineInt[ (int)(randInd/n)][randInd % n] = Cell.BOM;
				mineInt[x][y] = 0;
			}
		}
		
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				cells[i][j].setNumber( mineInt[i][j]);
			}
		
	}
	private void loadMines(){
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				if ( cells[i][j].isExplosible() ){
					for( int t = i - 1; t <= i + 1; ++t)
						for( int h = j - 1; h <= j + 1; ++h){
							if( t >= 0 && h >= 0 && t < m && h < n ){
								cells[t][h].isDanger();
							}
						}
				}
			}
		
	}
	private void init(int x, int y){
		roll(x,y);
		loadMines();
		dig(x,y);
	}
	private void flag(int x, int y){
		cells[x][y].flag();
		
		if ( cells[x][y].isFlagged() )
			--flags;
		else 
			++flags;
		
		flagJM.setText("Flags: " + flags);
		
	}
	/*
	 *  Dig the clicked-cell:
	 *  	Check the cell: is flagged? is Explode
	 *  	Show the cell with its representation
	 *  If the cell is safety (number of bom = 0) dig all around it
	 */
	private void dig(int x, int y){
			if( !cells[x][y].isAvailable()) return;
			//System.out.println(cells[x][y].getNumber()); // Show in console the coordinate
		
			if( !cells[x][y].explode()){
				if(cells[x][y].grow(showText)){
					--remain;
					//System.out.println(remain);
				if( cells[x][y].getNumber() == 0)
					digAround(x,y);
				complete();
				}
			} else {
				explode();
				return;
			}
	}
	private void digAround(int x, int y){
		for( int t = x - 1; t <= x + 1; ++t){
			for ( int h = y - 1; h <= y + 1; ++h){
				if( t >= 0 && h >= 0 && t < m && h < n){
					dig(t,h);
			}
			}
		}
	}
	/*
	 *  Burning is like auto dig,
	 *  This method invoke digAround if the following coditions are satisfied:
	 *  	- The around flags is equal with the number of bom in this cell
	 *  	- Unflagged
	 *   	
	 */
	private void burn(int x, int y){
		
		if( cells[x][y].isAvailable()) return;
		int flags = 0;
		for( int t = x - 1; t <= x + 1; ++t){
			for ( int h = y - 1; h <= y + 1; ++h){
				if( t >= 0 && h >= 0 && t < m && h < n)
					if( cells[t][h].isFlagged()) flags++;
			}
		}
		if( flags == cells[x][y].getNumber()) digAround(x,y);
	}
	/**
	 * End the match and showing all cells
	 */
	public void explode(){
		end = true;
		for ( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				cells[i][j].setEnabled(false);
				//if( cells[i][j].getNumber() == Cell.BOM )
				cells[i][j].grow(showText);
			}
	}
	private boolean complete(){
		if( remain == 0 ) 
			{
			end = true;
			JOptionPane.showInputDialog(null, "Your name: ", "Victory",0);
			}
		return ( remain == 0);
		
	}
	private void showAll(){
		for ( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				//if( cells[i][j].getNumber() == Cell.BOM )
				cells[i][j].grow(showText);
			}
	}
	
	private void showInConsole(){
		for ( int i = 0; i < m; ++i){
			System.out.println();
			for( int j = 0; j < n; ++j){
				System.out.printf("%2d",cells[i][j].getNumber());
			}
		}
	}
	
	public void resetGame(){
		// Reset all mines
		for( int i = 0; i < m; ++i){
			for( int j = 0; j < n; ++j){
				cells[i][j].reset();
			}
		}
		remain = m*n - mines;
		end = false;
		fClik = true;
		
	}
	
	// Repaint the number of all cells
	public void repainTable(){
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				if( !cells[i][j].isAvailable()){
					if( showText )cells[i][j].showText();
					else cells[i][j].hideText();
				}
			}
	}
	public void setShowNumber(boolean s){
		showText = s;
	}
	
	public JMenu getFlag(){
		return flagJM;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if( end) return;
		/*
		 * Find the cell is digged
		 * 
		 */
		coord[0] = 0; coord[1] = 0;
		for( int i = 0; i < m; ++i)
			for( int j = 0; j < n; ++j){
				if( e.getSource() == cells[i][j]){
					coord[0] = i;
					coord[1] = j;
					break;
				}
			}
		if( !cells[coord[0]][coord[1]].isAvailable()){
			burn(coord[0],coord[1]);
			return;
		}
		/*
		 * To avoid explosion at the first dig;
		 */
		if( fClik){
			init(coord[0], coord[1]);
			fClik = false;
			//showInConsole();
			return;
		}
		if( e.isMetaDown() || e.isAltDown() ){
			flag(coord[0], coord[1]);
			return;
		}
		dig( coord[0], coord[1]);
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {

	}
	
}
