package games;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import static java.lang.Math.*;

public class Mania{
	public static int op;
	public Mania(int x){
		op = x; //1: Easy, 2: Hard
		GameFrame frame = new GameFrame();
		GamePanel panel = new GamePanel(frame);
		panel.action();
		frame.add(panel);
	}
}

class GameFrame extends JFrame{
	public static int [] notes = new int[2000];
	public static int tot = 0;
	public static int stdTime = 500;
	public static long musicstart;

	public GameFrame(){
		super("usO! Mania");

		// Get notes
		String[] FILE_NAME = {"./A-SOUL-Quiet"};
		try{
			Scanner scan = new Scanner(new File(FILE_NAME[0] + "0.dat"));
			tot = scan.nextInt();
			for (int i = 0; i < tot; ++ i) notes[i] = scan.nextInt() - stdTime;
		}catch(IOException e){
			e.printStackTrace();
		}

		// Play music
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
		musicstart = System.currentTimeMillis();

		// System.out.println(tot);
		// for (int i = 0; i < tot; ++ i) System.out.println(notes[i]);
		setSize(600, 680);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class GamePanel extends JPanel{
	ArrayList<note> notelist = new ArrayList<note>();
	Image [] judge = new Image[4];
	int combo;
	public static long start;
	public JLabel combolabel, perfectlabel, greatlabel, goodlabel, misslabel;
	Image perfect, great, good, miss;
	int numgood, numgreat, numperfect, nummiss;

	public class note{
		int pos;
		long start;
		public note(int x){
			pos = x;
			start = System.currentTimeMillis();
			// System.out.println(String.valueOf(start - GamePanel.start));
		}
	}

	public GamePanel(GameFrame frame){
		Image perfect  = new ImageIcon(getClass().getResource("/pictures/perfect.jpg")).getImage();
		Image great = new ImageIcon(getClass().getResource("/pictures/great.jpg")).getImage();
		Image good = new ImageIcon(getClass().getResource("/pictures/good.jpg")).getImage();
		Image miss = new ImageIcon(getClass().getResource("/pictures/miss.jpg")).getImage();
		for (int i = 0; i < 4; ++ i) judge[i] = null;

		this.setLayout(null);
		combolabel = new JLabel("Combo: 0");
		combolabel.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
		combolabel.setBounds(410, 50, 200, 30);
		add(combolabel);
		perfectlabel = new JLabel("Perfect: 0");
		perfectlabel.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
		perfectlabel.setBounds(410, 100, 200, 30);
		add(perfectlabel);
		greatlabel = new JLabel("Great: 0");
		greatlabel.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
		greatlabel.setBounds(410, 150, 200, 30);
		add(greatlabel);
		goodlabel = new JLabel("Good: 0");
		goodlabel.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
		goodlabel.setBounds(410, 200, 200, 30);
		add(goodlabel);
		misslabel = new JLabel("Miss: 0");
		misslabel.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 30));
		misslabel.setBounds(410, 250, 200, 30);
		add(misslabel);

		frame.addKeyListener(new KeyAdapter(){ // JPanel无法监听键盘
			@Override
			public void keyTyped(KeyEvent event){
				char ch = event.getKeyChar();
				int col = 0;
				if (ch != '\n' && ch != 'd' && ch !='f' && ch != 'j' && ch != 'k') return;
				if (ch == '\n') System.exit(0);
				if (ch == 'd') col = 0;
				if (ch == 'f') col = 1;
				if (ch == 'j') col = 2;
				if (ch == 'k') col = 3;
				long now = System.currentTimeMillis();
				for (int i = 0; i < notelist.size(); ++ i){
					long err = GameFrame.stdTime - (now - notelist.get(i).start);
					if (notelist.get(i).pos == col && err < 200){
						if (abs(err) > 150){
							judge[col] = miss;
							combo = 0;
							++ nummiss;
						}else if (abs(err) > 100){
							judge[col] = good;
							++ numgood;
						}else if (abs(err) > 70){
							judge[col] = great;
							++ numgreat;
						}else{
							judge[col] = perfect;
							++ numperfect;
						}
						++ combo;
						notelist.remove(i);
						break;
					}
				}
			}
		});
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		ImageIcon icon = new ImageIcon(getClass().getResource("/pictures/bg.jpg"));
		Image bg = icon.getImage();
		g.drawImage(bg, 0, 0, 400, 700, null);
		icon = new ImageIcon(getClass().getResource("/pictures/key.jpg"));
		Image key = icon.getImage();
		long now = System.currentTimeMillis();
		for (int i = 0; i < notelist.size(); ++ i)
			if (now - notelist.get(i).start <= 500){
				g.drawImage(key, 30 + notelist.get(i).pos * 85, (int)(now - notelist.get(i).start), null);
			}
		for (int i = 0; i < 4; ++ i)
			g.drawImage(judge[i], 20 + i * 90, 525, 90, 30, null);
	}

	public void action(){
		new Painting().start();
	}

	class Painting extends Thread{
		int rand(int l, int r){
			return (int) (Math.random() * (r - l + 1)) + l;
		}
		@Override
		public void run(){
			int id = 0;
			while (true){
				long now = System.currentTimeMillis();
				if (id < GameFrame.tot){
					// add new note
					while (id < GameFrame.tot && now - GameFrame.musicstart >= GameFrame.notes[id]){
						++ id;
						int cnt = rand(1, Mania.op);
						if (cnt == 1) notelist.add(new note(rand(0, 3)));
						else{
							int x = rand(0, 2);
							int y = rand(x + 1 , 3);
							notelist.add(new note(x));
							notelist.add(new note(y));
						}
					}
				}else break;
				combolabel.setText("Combo: " + String.valueOf(combo));
				perfectlabel.setText("Perfect: " + String.valueOf(numperfect));
				greatlabel.setText("Great: " + String.valueOf(numgreat));
				goodlabel.setText("Good: " + String.valueOf(numgood));
				misslabel.setText("Miss: " + String.valueOf(nummiss));

				repaint();
				try{
					Thread.sleep(10);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
}