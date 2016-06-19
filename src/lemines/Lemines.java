package lemines;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.InputEvent;

public class Lemines extends JFrame implements ActionListener{
	
	MinePanel mainMP;
	JMenuBar mainJMB;
	JMenu gameJM, gameModeJM, flagJM;
	JMenuItem newLeminesJMI;
	//JMenuItem customJMI;
	JCheckBox showNumberJCB;
	JRadioButton smallJRB, normalJRB, expertJRB;
	boolean isPlaying;
	int[] gameMode;
	int time;
	Thread clock;
	/**
	 * 
	 */
	public Lemines(){
		getContentPane().setBackground(new Color(102, 102, 102));
		
		progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.SOUTH);
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		progressBar.setString("Time");
		progressBar.setName("asdf");
		progressBar.setForeground(new Color(51, 153, 0));
		progressBar.setToolTipText("");
		// Initialization
		this.setSize(300, 400);
		this.setBackground(new Color(51, 51, 51));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainJMB = new JMenuBar();
		this.setJMenuBar(mainJMB);
		gameJM = new JMenu("Lemines");
		gameJM.setMnemonic('g');
		gameModeJM = new JMenu("Mode");
		
		
		// Build Lemines Menu
		newLeminesJMI = new JMenuItem("New Lemines");
		newLeminesJMI.addActionListener(this);
		newLeminesJMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
		// Build modeLeminesJM
		ButtonGroup bg = new ButtonGroup();
		smallJRB = new JRadioButton("Small");  	    bg.add(smallJRB);
		normalJRB = new JRadioButton("Normal"); 	bg.add(normalJRB);
		expertJRB = new JRadioButton("Expert");		bg.add(expertJRB);
		//customJRB = new JRadioButton("Custom...");  bg.add(customJRB);
		
		gameModeJM.add(smallJRB); 		 smallJRB.addActionListener(this);
		gameModeJM.add(normalJRB);		 normalJRB.addActionListener(this);
		gameModeJM.add(expertJRB);       expertJRB.addActionListener(this);
 
		gameMode = NORMAL; 		// Set default mode game is NORMAL
		
		gameJM.add(newLeminesJMI);
		gameJM.add(gameModeJM);
		
		// Build Main menu
		mainJMB.add(gameJM);
		
		mntmPause = new JMenuItem("Freeze");
		mntmPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mntmPause.setMnemonic('f');
		mntmPause.setMnemonic(KeyEvent.VK_CONTROL);
		mntmPause.setIcon(new ImageIcon(Lemines.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPause.png")));
		gameJM.add(mntmPause);
		mntmPause.addActionListener(this);
		//mainJMB.add(infoJM);
		
		
		// Build Flag counter
		
		flagJM = new JMenu("Flag: 40");
		
		// Build Clock
		clock = new Thread(new TimeInLemines());
	}
	class TimeInLemines implements Runnable{
		@Override
		public void run() {
			while(true)
				try{
					Thread.sleep(100);
					mseconds -= 100;
					progressBar.setValue(mseconds);
					if ( mseconds == 0 ){
						endLemines();
						return;
					}
				} catch (InterruptedException e){}
			}
		}
	public static void main(String args[]){
		
		Lemines mine = new Lemines();
		mine.setVisible(true);
	} 
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if( e.getSource() == newLeminesJMI){
			startLemines();
			return;
		}
	
		if( e.getSource() == smallJRB ) {
			gameMode = SMALL;
			startLemines();
			return;
		}
		if( e.getSource() == normalJRB) {
			gameMode = NORMAL; 
			startLemines();
			return;
		}
		if( e.getSource() == expertJRB) {
			gameMode = EXPERT;
			gameModeJM.setSize(1, 1);
			startLemines();
			return;
		}
		if ( e.getSource() == mntmPause){
			if ( mntmPause.getText().equalsIgnoreCase("freeze"))
				freeze();
			else 
				melt();
		}
		
	}
	
	public void startLemines(){
		
		mseconds = 20000;
		progressBar.setMaximum(mseconds);
		progressBar.setValue(mseconds);
		initBoard();
		clock.start();
		
	}
	
	private void initBoard(){
		
		if( mainMP != null){
			mainMP.setVisible(false);
			this.getContentPane().remove(mainMP);
		}
		
		mainMP = new MinePanel(gameMode[0], gameMode[1], gameMode[2]);
		this.getContentPane().add(mainMP,BorderLayout.CENTER);
		flagJM.setVisible(false);
		flagJM = mainMP.getFlag();
		mainJMB.add(flagJM);
		isPlaying = true;
		progressBar.setMaximum(mseconds);
		this.setSize(gameMode[1]*40, gameMode[0]*40 + progressBar.getHeight());
	}
	
	private void endLemines(){
		mainMP.explode();
		freeze();
	}
	
	private void freeze(){
		if ( mainMP == null ) return ;
		try { 
			clock.wait(100000000);
		} catch (InterruptedException ie){}
		mainMP.setVisible(false);
		mntmPause.setText("Melt");
		mntmPause.setIcon(new ImageIcon(Lemines.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png")));
	}
	
	private void melt(){
		if ( mainMP == null ) return ;
		clock.notify();
		mainMP.setVisible(true);
		mntmPause.setText("Freeze");
		mntmPause.setIcon(new ImageIcon(Lemines.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPause.png")));
	}
	
	private static final int []
							 SMALL     = {9,9,10},
							 NORMAL    = {16,16,40},
							 EXPERT    = {16,30,99};	
	private JProgressBar progressBar;
	private int mseconds, score;
	private JMenuItem mntmPause;
}
