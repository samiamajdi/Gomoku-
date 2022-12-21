package gomokuu;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Vector;

public class Gomoku extends GameSearch implements Serializable{
	
	private JButton btnPlay = new JButton("Play");
	private JButton niveau = new JButton("Niveau");
	private JButton sauv = new JButton();
	private GoBoard goboard = new GoBoard();
	private GomokuPosition boardpos = new GomokuPosition();
	private JCheckBox btnEasy = new JCheckBox("Easy", true);
	private JCheckBox btnNormal = new JCheckBox("Normal", false);
	private JCheckBox btnHard = new JCheckBox("Hard", false);
	private JCheckBox vsplayer = new JCheckBox("Player Vs Playe", false);
	private JCheckBox vscomputer = new JCheckBox("Player Vs Computer ", false);
	private boolean canClick = true;
	JPanel p3 = new JPanel(new GridLayout(3,1));
	
	private boolean niv = false;
	private boolean gameState = false;  // to show statue of game: true for continue, false for end.
	private boolean player = true; // true for human, false for program 

	private int playy=1;
	private boolean who=false;
	public static boolean save=false;

	
	@Override
	public String toString() {
		return "Gomoku [GoBoard=" + goboard + "]";
	}
	private int maxDepth = 5;
	
	static Thread blackTimeThread;
	static Thread whiteTimeThread;
	Gomoku a=this;
    public Gomoku() throws IOException {   	

    
    	JPanel p2 = new JPanel();
    	Color  beig  = new Color(60, 42, 33);
		p2.setBackground(beig);
		p2.setPreferredSize(new Dimension(170,580));
		p2.setLayout(null);
		
		sauv.setBackground(beig);
		sauv.setBounds(100,70,45,30);
		
		
		
		
		btnPlay.setForeground(beig);
    	btnPlay.setBounds(33,110,100,30);
    	btnPlay.setBackground(new Color(250,240,230));
    	niveau.setBounds(43,230,83,30);
    	niveau.setBackground(new Color(250,240,230));
    	JPanel p4 = new JPanel();
    	p4.setBounds(0,0,170,67);
    	
    	p4.setLayout(null);
    	Icon im= new ImageIcon("src\\gomokuu\\DFI.png");
    	Icon sau= new ImageIcon("src\\gomokuu\\hope.png");
    	JLabel image = new JLabel(im);
    	sauv.setIcon(sau);
    	image.setBounds(0,0,170,67 );
    	image.setBackground(Color.black);
    	p4.add(image);
    	
    	
    	btnEasy.setForeground(Color.white);
    	btnNormal.setForeground(Color.white);
    	btnHard.setForeground(Color.white);
    	vscomputer.setForeground(Color.white);
    	vsplayer.setForeground(Color.white);
		p3.setBackground(beig);
    	p3.setBounds(45,280,128,116);
    	p3.setVisible(false);
    	p2.add(sauv);
		
	
    	
    	p3.setLayout(null);
    	vscomputer.setBounds(10,160,150,14);
    	vscomputer.setBackground(beig);
    	vsplayer.setBounds(10,180,150,14);
    	vsplayer.setBackground(beig);
        btnEasy.setBounds(1,1,82,14);
        btnEasy.setBackground(beig);
    	btnNormal.setBounds(1,20,82,14);
    	btnNormal.setBackground(beig);
    	btnHard.setBounds(1,34,82,26);
    	btnHard.setBackground(beig);
    	p2.add(btnPlay);
    	p2.add(vscomputer);
    	p2.add(vsplayer);
    	
    	
    	p3.add(btnEasy);
    	p3.add(btnNormal);
    	p3.add(btnHard);
    	p2.add(p3);
    	p2.add(p4);
    	p2.add(niveau);
    	ButtonGroup group = new ButtonGroup();
    	group.add(btnEasy);
    	group.add(btnNormal);
    	group.add(btnHard);
    	
    	
    	ButtonGroup vsgroup = new ButtonGroup();
    	vsgroup.add(vscomputer);
    	vsgroup.add(vsplayer);
    	goboard.setBounds(0, 0, 0, 0);
    	
    	
    	add(p2, BorderLayout.EAST);
    	add(goboard, BorderLayout.CENTER);
    	
    	vsplayer.addItemListener(new ItemListener() {    
             public void itemStateChanged(ItemEvent e) { 
            
            	 if(who==false) {who=true;}
            	 
            	 else {who=false;}
                    
                System.out.println("clicked"+who);
                
                
             }    
          });   
    	
    	
    	niveau.addActionListener(new actionListe() {
    	
    	});
    	
    	
    
    	sauv.setVisible(false);
    	actionListe p= new actionListe();
    	btnPlay.addActionListener(new actionListe()
    		
    			
    		
    	);
    	
    	sauv.addActionListener(new actionListe() {
    		});
    	
    	
    }
    

    public void setDefaultStatue() {
    	boardpos.DefaultState();
    	
    	gameState = false;
    	
    	player = true;
    	
    
    	
    	
    	btnPlay.setText("Play");
    	
    	btnEasy.setSelected(true);
    	maxDepth = 5;
    }
    
 
    
    public boolean drawnPosition(Position p){
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int i = 0; i < pos.board.length; i++)
    		for(int j = 0; j < pos.board[0].length; j++)
    			if(pos.board[i][j] == GomokuPosition.BLANK) return false;
    	return true;
    }
    
    public boolean wonPosition(Position p, boolean player){
    	GomokuPosition pos = (GomokuPosition)p;
    	short b;
    	if(player) b = GomokuPosition.HUMAN;
    	else b = GomokuPosition.PROGRAM;
    	
    	for (int i = 0; i < boardpos.board.length; i++) {
			for (int j = 0; j < boardpos.board[0].length; j++) {
				if (i < (boardpos.board.length - 4))
					if (boardpos.board[i][j] == b 
					 && boardpos.board[i][j] == boardpos.board[i + 1][j]
					 && boardpos.board[i][j] == boardpos.board[i + 2][j]
					 && boardpos.board[i][j] == boardpos.board[i + 3][j]
					 && boardpos.board[i][j] == boardpos.board[i + 4][j])
						return true;
			
				if (j < (boardpos.board[0].length - 4))
					if (boardpos.board[i][j] == b
					 && boardpos.board[i][j] == boardpos.board[i][j + 1]
					 && boardpos.board[i][j] == boardpos.board[i][j + 2]
					 && boardpos.board[i][j] == boardpos.board[i][j + 3]
					 && boardpos.board[i][j] == boardpos.board[i][j + 4])
						return true;
				
				if (i < (boardpos.board.length - 4)  && j < (boardpos.board[0].length - 4))
					if (boardpos.board[i][j] == b
					 && boardpos.board[i][j] == boardpos.board[i + 1][j + 1]
					 && boardpos.board[i][j] == boardpos.board[i + 2][j + 2]
					 && boardpos.board[i][j] == boardpos.board[i + 3][j + 3]
					 && boardpos.board[i][j] == boardpos.board[i + 4][j + 4])
						return true;
				
				if (i < (boardpos.board.length - 4) && j >= 4)
					if (boardpos.board[i][j] != 0
					 && boardpos.board[i][j] == boardpos.board[i + 1][j - 1]
					 && boardpos.board[i][j] == boardpos.board[i + 2][j - 2]
					 && boardpos.board[i][j] == boardpos.board[i + 3][j - 3]
					 && boardpos.board[i][j] == boardpos.board[i + 4][j - 4])
						return true;
				}
			}
		
		return false;
    }
  
    public float positionEvaluation(Position p, boolean player){
    	int[][] myConnects = connexion(p, player, 5);
    	int[][] enemyConnects = connexion(p, !player, 5);
    	
        // if there is a connect 5 or two open connect 4, then return positive infinity of negative infinity
    	if(myConnects[3][0] > 0 || myConnects[2][2] > 0)
    		return Float.POSITIVE_INFINITY;
    	if(enemyConnects[3][0] > 0 || enemyConnects[2][2] > 0)
    		return Float.NEGATIVE_INFINITY;
    	
    	int ret = 0;
    	GomokuPosition pos = (GomokuPosition)p;
    	
    	int[] score = {10, 100, 1000};
    	
    	short myChessman;
		short enemyChessman;
		if(player) {
			myChessman = GomokuPosition.HUMAN;
			enemyChessman = GomokuPosition.PROGRAM;
		}
		else {
			myChessman = GomokuPosition.PROGRAM;
			enemyChessman = GomokuPosition.HUMAN;
		}
    	
    	for(int i = 0; i < pos.board.length; i++) {
    		for(int j = 0; j < pos.board[0].length; j++) {
    			if(pos.board[i][j] == myChessman) {
    				if(distanceToBoundary(i, j) == 0)
    					ret += 1;
    				else if(distanceToBoundary(i, j) == 1)
    					ret += 2;
    				else if(distanceToBoundary(i, j) == 2)
    					ret += 4;
    				else
    					ret += 8;
    			}
    			else if(pos.board[i][j] == enemyChessman) {
    				if(distanceToBoundary(i, j) == 0)
    					ret -= 1;
    				else if(distanceToBoundary(i, j) == 1)
    					ret -= 2;
    				else if(distanceToBoundary(i, j) == 2)
    					ret -= 4;
    				else
    					ret -= 8;
    			}
    		}
    	}
    	
    	ret += myConnects[0][1] * score[0];
    	ret += myConnects[0][2] * (int)(score[1] * 0.9);
    	ret += myConnects[1][1] * score[1];
    	ret += myConnects[1][2] * (int)(score[2] * 0.9);
    	ret += myConnects[2][1] * score[2];
    	
    	ret -= enemyConnects[0][1] * score[0];
    	ret -= enemyConnects[0][2] * (int)(score[1] * 0.9);
    	ret -= enemyConnects[1][1] * score[1];
    	ret -= enemyConnects[1][2] * (int)(score[2] * 0.9);
    	ret -= enemyConnects[2][1] * score[2];
    	
    	return ret;
    }
 
    private int[][] connexion(Position p, boolean player, int number) {
    	GomokuPosition pos = (GomokuPosition)p;
    	short b;
    	if(player) b = GomokuPosition.HUMAN;
    	else b = GomokuPosition.PROGRAM;
    	
    	int count[][] = new int[number-1][3];
    	for(int i = 0; i < count.length; i++)
    		for(int j = 0; j < count[0].length; j++)
    			count[i][j] = 0;
    	
    	for(int n = 2; n <= number; n++) {
    		for(int i = 0; i < pos.board.length; i++) {
        		for(int j = 0; j < pos.board[0].length; j++) {
        			if(i+n-1 < pos.board.length) {
        				if(downSame(p, i, j, n-1, b)) {
        					while(true) {
        						if(i+n < pos.board.length)
        							if(pos.board[i+n][j] == b) break;
        						if(i-1 >= 0)
        							if(pos.board[i-1][j] == b) break;
        						
        						count[n-2][0]++;
        						
        						if(i-1 >= 0 && i+n >= pos.board.length) {
            	    				if(pos.board[i-1][j] == GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    			}
            	    			else if(i-1 < 0 && i+n < pos.board.length) {
            	    				if(pos.board[i+n][j] == GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    			}
            	    			else if(i-1 >= 0 && i+n < pos.board.length) {
            	    				if(pos.board[i-1][j] == GomokuPosition.BLANK 
            	    						&& pos.board[i+n][j] != GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    				if(pos.board[i+n][j] == GomokuPosition.BLANK
            	    						&& pos.board[i-1][j] != GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    				
            	    				if(pos.board[i-1][j] == GomokuPosition.BLANK
            	    						&& pos.board[i+n][j] == GomokuPosition.BLANK)
            	    					count[n-2][2]++;
            	    			}
            	    			break;
        					}
        				}
        				
        				if(j+n-1 < pos.board[0].length) {
        					if(rightSame(p, i, j, n-1, b)) {
        						while(true) {
            						if(j+n < pos.board[0].length)
            							if(pos.board[i][j+n] == b) break;
            						if(j-1 >= 0)
            							if(pos.board[i][j-1] == b) break;
            						count[n-2][0]++;
                					
                					if(j-1 >= 0 && j+n >= pos.board[0].length) {
                						if(pos.board[i][j-1] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if(j-1 < 0 && j+n < pos.board[0].length) {
                						if(pos.board[i][j+n] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if(j-1 >= 0 && j+n < pos.board[0].length) {
                						if(pos.board[i][j-1] == GomokuPosition.BLANK 
                	    						&& pos.board[i][j+n] != GomokuPosition.BLANK)
                	    					count[n-2][1]++;
                	    				if(pos.board[i][j+n] == GomokuPosition.BLANK
                	    						&& pos.board[i][j-1] != GomokuPosition.BLANK)
                	    					count[n-2][1]++;
                	    				
                	    				if(pos.board[i][j-1] == GomokuPosition.BLANK
                	    						&& pos.board[i][j+n] == GomokuPosition.BLANK)
                	    					count[n-2][2]++;
                					}
                					break;
            					}
        					}
        				}
        				
        				if(i+n-1 < pos.board.length && j+n-1 < pos.board[0].length) {
        					if(rightDownSame(p, i, j, n-1, b)) {
        						while(true) {
            						if(i+n < pos.board.length && j+n < pos.board[0].length)
            							if(pos.board[i+n][j+n] == b) break;
            						if(i-1 >= 0 && j-1 >= 0)
            							if(pos.board[i-1][j-1] == b) break;
            						count[n-2][0]++;
                					
                					if((i-1 >= 0 && j-1 >= 0) && (i+n >= pos.board.length || j+n >= pos.board[0].length)) {
                						if(pos.board[i-1][j-1] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-1 < 0 || j-1 < 0) && (i+n < pos.board.length && j+n < pos.board[0].length)) {
                						if(pos.board[i+n][j+n] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-1 >= 0 && j-1 >= 0) && (i+n < pos.board.length && j+n < pos.board[0].length)) {
                						if(pos.board[i-1][j-1] == GomokuPosition.BLANK
                								&& pos.board[i+n][j+n] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.board[i+n][j+n] == GomokuPosition.BLANK
                								&& pos.board[i-1][j-1] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.board[i-1][j-1] == GomokuPosition.BLANK && pos.board[i+n][j+n] == GomokuPosition.BLANK)
                							count[n-2][2]++;
                					}
                					break;
        						}
        					}
        				}
        				
        				if(i-n+1 >= 0 && j+n-1 < pos.board[0].length) {
        					if(rightUpSame(p, i, j, n-1, b)) {
        						while(true) {
            						if(i-n >= 0 && j+n < pos.board[0].length)
            							if(pos.board[i-n][j+n] == b) break;
            						if(i+1 < pos.board.length && j-1 >= 0)
            							if(pos.board[i+1][j-1] == b) break;
            						count[n-2][0]++;
                					
                					if((i-n >= 0 && j+n < pos.board[0].length) && (i+1 >= pos.board.length || j-1 < 0)) {
                						if(pos.board[i-n][j+n] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-n < 0 || j+n > pos.board[0].length) && (i+1 < pos.board.length && j-1 >= 0)) {
                						if(pos.board[i+1][j-1] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-n >= 0 && j+n < pos.board[0].length) && (i+1 < pos.board.length && j-1 >= 0)) {
                						if(pos.board[i-n][j+n] == GomokuPosition.BLANK
                								&& pos.board[i+1][j-1] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.board[i+1][j-1] == GomokuPosition.BLANK
                								&& pos.board[i-n][j+n] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.board[i-n][j+n] == GomokuPosition.BLANK && pos.board[i+1][j-1] == GomokuPosition.BLANK)
                							count[n-2][2]++;
                					}
                					break;
            					}
        					}
        				}
        			}
        		}
        	}
    	}
    	
    	
    	
    	return count;
    }
    
    private boolean downSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.board[i+k][j] != b) return false;
    	return true;
    }
    
    private boolean rightSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.board[i][j+k] != b) return false;
    	return true;
    }
    
    private boolean rightDownSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.board[i+k][j+k] != b) return false;
    	return true;
    }
    
    private boolean rightUpSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.board[i-k][j+k] != b) return false;
    	return true;
    }
    
    
    
    private int distanceToBoundary(int x, int y) {
		int xToUp = x;
		int xToDown = boardpos.board.length - 1 - x;
		int yToLeft = y;
		int yToRight = boardpos.board[0].length - 1 - y;
		
		xToUp = Math.min(xToUp, xToDown);
		xToUp = Math.min(xToUp, yToLeft);
		xToUp = Math.min(xToUp, yToRight);
		return xToUp;
	}

	public Position [] possibleMoves(Position p, boolean player){
    	GomokuPosition pos = (GomokuPosition)p;
    	int count = 0;
    	for(int i = 0; i < pos.board.length; i++)
    		for(int j = 0; j < pos.board[0].length; j++)
    			if(pos.board[i][j] == GomokuPosition.BLANK && isInSize(p, i, j)) 
    				count++;
    	
    	Position[] ret = new Position[count];
    	count = 0;
    	for(int i = 0; i < pos.board.length; i++) {
    		for(int j = 0; j < pos.board[0].length; j++) {
                    // next move must be in 3 boards away from current boards
    			if(pos.board[i][j] == GomokuPosition.BLANK && isInSize(p, i, j)) {
    				GomokuPosition pos2 = new GomokuPosition();
    				pos2 = pos.getNewPosition();
    				if(player)
    					pos2.board[i][j] = GomokuPosition.HUMAN;
    				else
    					pos2.board[i][j] = GomokuPosition.PROGRAM;
    				ret[count++] = pos2;
    			}
    		}
    	}
    	return ret;
    }
	
	private boolean isInSize(Position p, int x, int y) {
		GomokuPosition pos = (GomokuPosition)p;
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		
		boolean firstChessman = true;
		for(int i = 0; i < pos.board.length; i++) {
			for(int j = 0; j < pos.board[0].length; j++) {
				if(pos.board[i][j] != GomokuPosition.BLANK) {
					if(firstChessman) {
						maxX = minX = i;
						maxY = minY = j;
						firstChessman = false;
					}
					else {
						
                                                if(minX > i) minX = i;
						if(maxX < i) maxX = i;
						if(minY > j) minY = j;
						if(maxX < j) maxX = j;
					}
				}
			}
		}
		
		if(x >= minX-3 && x <= maxX+3 && y >= minY-3 && y <= maxY+3) return true;
		else return false;
	}
    
    public boolean reachedMaxDepth(Position p, int depth){
    
    	if(depth >= maxDepth) return true;
    	return false;
    }
    
    
    
    
   
    public class Mouse extends MouseAdapter implements Serializable {
    	private static final long serialVersionUID = 1L;

		public void mouseClicked(MouseEvent e) {
    		if (gameState && canClick && who==false)  {
				int mouseX = 0;
		    	int mouseY = 0;
		    	int boardX = 0;
		    	int boardY = 0;
		    	
			    mouseX = e.getX();
			    mouseY = e.getY();
			    if (mouseX >= goboard.xStart && mouseX <= goboard.xEnd && mouseY >= goboard.yStart && mouseY <= goboard.yEnd) {
			        boardX = (mouseX - goboard.xStart) / goboard.widthStep;
			        boardY = (mouseY - goboard.yStart) / goboard.heightStep;
			        
			       if(boardpos.board[boardX][boardY] == GomokuPosition.BLANK) {
			    	   boardpos.board[boardX][boardY] = GomokuPosition.HUMAN;
			    	   repaint();
			    	  
			    	   if(wonPosition(boardpos, HUMAN)) {
			    		   JOptionPane.showMessageDialog(null, "Human win!");
			    		   gameState = false;
			    		   return;
			    	   }
			    	   else if(drawnPosition(boardpos)) {
			    		   JOptionPane.showMessageDialog(null, "Draw game!");
			    		   gameState = false;
			    		   return;
			    	   }
			    	   
			    	   player = PROGRAM;
			    	   canClick = false;
			    	   if(vscomputer.isSelected()) {
			    	   Thread thread2 = new Thread(new Runnable() {
			    		   public void run() {
			    			   Vector v = alphaBeta(0, (Position)boardpos, PROGRAM);
			    			   boardpos = (GomokuPosition)v.elementAt(1);
			    			   repaint();
			    			   
    				    	   player = HUMAN;
    				    	   canClick = true;
    				    	   if(wonPosition(boardpos, PROGRAM)) {
    				    		   JOptionPane.showMessageDialog(null, "Computer win!");
    				    		   gameState = false;
    				    		   return;
    				    	   }
    				    	   else if(drawnPosition(boardpos)) {
    				    		   JOptionPane.showMessageDialog(null, "Draw game!");
    				    		   gameState = false;
    				    		   return;
    				    	   }
                                        }
			    	    }); 
			    	   
			    	   thread2.setPriority(Thread.MAX_PRIORITY);
			    	   thread2.start();
			       }}
			    }
			}
			else if(vsplayer.isSelected()){
			if( gameState && canClick) {
				System.out.println("playy "+playy);
				System.out.println("player vs player");

				canClick = true;
					int mouseX = 0;
			    	int mouseY = 0;
			    	int boardX = 0;
			    	int boardY = 0;
			    	
				    mouseX = e.getX();
				    mouseY = e.getY();
				    if (mouseX >= goboard.xStart && mouseX <= goboard.xEnd && mouseY >= goboard.yStart && mouseY <= goboard.yEnd) {
				        boardX = (mouseX - goboard.xStart) / goboard.widthStep;
				        boardY = (mouseY - goboard.yStart) / goboard.heightStep;
				        
				       if(boardpos.board[boardX][boardY] == GomokuPosition.BLANK) {
				    	   boardpos.board[boardX][boardY] = playy;
				    	   System.out.println("before verif "+playy);
				    	   System.out.println("clicked case : "+boardpos.board[boardX][boardY]);
				    	   System.out.println("entered");
				    	   repaint();
				    	    
				    	   if(playy==1) {
							    playy=-1;}
				    	   else {
			    				    playy=1;}
							    System.out.println("after verif "+playy);
				       }
				    	   if(playy==1) {
				    	   if(wonPosition(boardpos, HUMAN)) {
				    		   JOptionPane.showMessageDialog(null, "PLAYER 1 win!");
				    		   gameState = false;
				    		   return;
				    		   
				    	   }
				    	   else if(drawnPosition(boardpos)) {
				    		   JOptionPane.showMessageDialog(null, "Draw game!");
				    		   gameState = false;
				    		   return;
				    		   
				    	   }
				    	   
				    	   player = PROGRAM;
				    	   canClick = true;
//				    	  

				
			}   if(playy==-1) {
				if(wonPosition(boardpos, PROGRAM)) {
		    		   JOptionPane.showMessageDialog(null, "PLAYER 2 win!");
		    		   gameState = false;
		    		   return;
		    		   
		    	   }
		    	   else if(drawnPosition(boardpos)) {
		    		   JOptionPane.showMessageDialog(null, "Draw game!");
		    		   gameState = false;
		    		   return;
		    		   
		    	   }
				
			}

		   
		}
	}}
    		
    		
    	}
    }
    
    public class actionListe implements Serializable , ActionListener{

    	/**
    	 * 
    	 */
    	private static final long serialVersionUID = 1L;

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if (e.getSource()==sauv) {
    			try {
    				
        			FileOutputStream fos = new FileOutputStream ("D:\\matrice.txt");
        			ObjectOutputStream out = new ObjectOutputStream(fos);
        			out.writeObject (a) ;
        			out.flush () ;
        			out.close () ;
        			Gomoku uer = null ;
        			FileInputStream file = new FileInputStream
        					("D:\\matrice.txt") ;
        			ObjectInputStream in = new ObjectInputStream (file);
        			uer = (Gomoku) in.readObject() ;
        			if (uer != null) {
        				System.out.println (uer) ;
        			}
        			if(e.getSource()==sauv){
        	    		
        	    		System.out.println("waahya sidna ");
        	    		save=true; 
        	    	}
        			}
        			
        			catch(Exception e1) {
        				e1.printStackTrace();
        			}
    			
    			
    		}else if (e.getSource()==btnPlay) {
    			
    			if (!gameState) {
    				gameState = true;
    				btnPlay.setText("Replay");
    				if(btnEasy.isSelected()) maxDepth = 2;
    				else if(btnNormal.isSelected()) maxDepth = 4;
    				else if(btnHard.isSelected()) maxDepth = 5;
    				sauv.setVisible(true);
    				
    			}
    			else {
    				setDefaultStatue();
        			goboard.repaint();
        			sauv.setVisible(false);
    			}
    			
    		} else if (e.getSource()==niveau) {
    			if( !niv){
    				niv = true;
    			p3.setVisible(true);  
    			  
    			}else {
    				p3.setVisible(false);
    				niv = false;
    			}
    		}
    	}
    	
    	
    	
    	
   

    }

    
    class GoBoard extends JPanel implements Serializable {
    	private  int width;
    	private  int height;
    	private  int widthStep;
    	private  int heightStep;
		
    	private  int xStart;
    	private  int yStart;
    	private  int xEnd;
    	private  int yEnd;
        
    	@Override
		public String toString() {
			return "GoBoard [width=" + width + ", height=" + height + ", widthStep=" + widthStep + ", heightStep="
					+ heightStep + ", xStart=" + xStart + ", yStart=" + yStart + ", xEnd=" + xEnd + ", yEnd=" + yEnd
					+ "]";
		}

		public GoBoard() {
    		
    		Color  beig  = new Color(204, 149, 96);
    		setBackground(beig);
    		
    		addMouseListener(new Mouse() 
    		); 
    	}
    	
    	protected void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		
    	
    		width = (int)(getWidth() * 0.98);
        	height = (int)(getHeight() * 0.98);
        	widthStep = width / GomokuPosition.size_board;
        	heightStep = height / GomokuPosition.size_board;
    		
        	xStart = (int)(width * 0.01);
        	yStart = (int)(height * 0.01);
        	xEnd = xStart + GomokuPosition.size_board * widthStep;
        	yEnd = yStart + GomokuPosition.size_board * heightStep;
        	
    		// draw goboard
    		g.setColor(Color.BLACK);
    		g.drawLine(xStart, yStart, xStart, yEnd);
    		g.drawLine(xStart, yStart, xEnd, yStart);
    		g.drawLine(xEnd, yStart, xEnd, yEnd);
    		g.drawLine(xStart, yEnd, xEnd, yEnd);
    		for (int i = 1; i < GomokuPosition.size_board; i++) {
    			g.drawLine(xStart + i * widthStep, yStart, xStart + i * widthStep, yEnd);
    			g.drawLine(xStart, yStart + i * heightStep, xEnd, yStart + i * heightStep);
    		}
    		
    		// draw  goboard
    		int chessRadius = (int)(Math.min(widthStep, heightStep) * 0.9 *0.5);
    		for (int i = 0; i < boardpos.board.length; i++) {
    			for (int j = 0; j < boardpos.board[0].length; j++) {
    				if (boardpos.board[i][j] == GomokuPosition.HUMAN) {
    				    g.setColor(Color.BLACK);
    				    int xCenter = (int)(xStart + (i + 0.5) * widthStep);
    				    int yCenter = (int)(yStart + (j + 0.5) * heightStep);
    				    g.fillOval(xCenter - chessRadius, yCenter - chessRadius, 2 * chessRadius, 2 * chessRadius);
    				    
    				}
    				else if (boardpos.board[i][j] == GomokuPosition.PROGRAM) {
    					g.setColor(Color.WHITE);
    					int xCenter = (int)(xStart + (i + 0.5) * widthStep);
    				    int yCenter = (int)(yStart + (j + 0.5) * heightStep);
    				    g.fillOval(xCenter - chessRadius, yCenter - chessRadius, 2 * chessRadius, 2 * chessRadius);
    				}
    			}
    		}
    	}
    }
   
    
    
}


