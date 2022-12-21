package gomokuu;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

//import util.Info;

public class test {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		EventQueue.invokeLater(new Thread() {
            public void run() {
                Gomoku frame;
				try {
					FileInputStream file = new FileInputStream
							("D:\\matrice.txt") ;
					ObjectInputStream in = new ObjectInputStream (file);
					
					try {
					//	if (Gomoku.save) {
					frame=(Gomoku) in.readObject();
					frame.setTitle("Gomoku Game");
					System.out.println("madaghach9ach");
	                frame.setSize(750, 580);
	                frame.setLocationRelativeTo(null); 
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.setVisible(true); 
					
						
						}
					catch(Exception e2) { e2.printStackTrace();}
					
					
					
					
					
				}  
				catch (IOException e) {
					try {
						frame = new Gomoku();
						frame.setTitle("Gomoku Game");
		                frame.setSize(750, 580);
		                frame.setLocationRelativeTo(null);
		                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		                frame.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				     
		    	
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           
            }
        });

	}
}