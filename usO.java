import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import games.*;

class usO extends JFrame{
	public usO(){
		super("usO!");
		this.setLayout(null);
		Container con = getContentPane();

		//Icon
		Icon img = new ImageIcon("./pictures/osu.png");
		JLabel icon = new JLabel(img);
		icon.setBounds(0, 0, 400, 600);
		getLayeredPane().add(icon, Integer.valueOf(Integer.MIN_VALUE));
		((JPanel)con).setOpaque(false);

		//Titles
		JLabel title1 = new JLabel("usO!");
		JLabel title2 = new JLabel("嘘");
		title1.setFont(new Font("Dialog", Font.ITALIC + Font.BOLD, 50));
		title2.setFont(new Font("Serif", Font.BOLD, 40));
		title1.setBounds(140, 30, 150, 50); // X, Y, Width, Height
		title2.setBounds(170, 100, 40, 40);
		con.add(title1);
		con.add(title2);

		//Buttons
		JButton button1 = new JButton("usO Mania(Easy)");
		JButton button2 = new JButton("usO Mania(Hard)");
		JButton button3 = new JButton("Custom Level");
		button1.setBounds(50, 200, 300, 80);
		button2.setBounds(50, 280, 300, 80);
		button3.setBounds(50, 360, 300, 80);
		button1.addActionListener(new ButtonAction1());
		button2.addActionListener(new ButtonAction2());
		button3.addActionListener(new ButtonAction3());
		con.add(button1);
		con.add(button2);
		con.add(button3);

		setSize(400, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	class ButtonAction1 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event){
			new Mania(1);
		}
	}
	class ButtonAction2 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event){
			new Mania(2);
		}
	}
	class ButtonAction3 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event){
			new Custom();
		}
	}
	public static void main(String args[]){
		usO app = new usO();
	}
}