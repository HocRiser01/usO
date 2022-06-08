package games;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;

public class Custom extends JFrame{
	JLabel label;
	int[] notes;
	int tot;
	public Custom(){
		super("Custom Level");
		this.setLayout(null);
		Container con = getContentPane();

		String[] FILE_NAME = {"./A-SOUL-Quiet"};

		try{
			Clip music = AudioSystem.getClip();
			File sourceFile = new File(FILE_NAME[0] + ".wav");
			AudioInputStream ais = AudioSystem.getAudioInputStream(sourceFile);
			music.open(ais);
			music.start();
		}catch (LineUnavailableException e){
			e.printStackTrace();
		}catch (UnsupportedAudioFileException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}

		setFocusable(true);
		long start = System.currentTimeMillis();
		label = new JLabel("Haven't created any note");
		label.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
		label.setBounds(100, 100, 800, 30);
		con.add(label);
		notes = new int[2000];
		tot = 0;

		addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent event){
				// System.out.println(event.getKeyChar());
				if (event.getKeyChar() == '\n'){
					try{
						FileWriter fw = new FileWriter("./" + FILE_NAME[0] + ".dat");
						fw.write(tot + "\n");
						for (int i = 0; i < tot; ++ i) fw.write(notes[i] + "\n");
						fw.close();
					}catch(IOException e){
						e.printStackTrace();
					}
					System.exit(0);
				}
				if (event.getKeyChar() != ' ') return;
				long now = System.currentTimeMillis();
				label.setText("Created a new note at: " + (now - start) + " ms.");
				label.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
				con.add(label);
				notes[tot ++] = (int)(now - start);
			}
		});

		setSize(1000, 600);
		setVisible(true);
	}

	public static void main(String args[]){
		Custom app = new Custom();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}