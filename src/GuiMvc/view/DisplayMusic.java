package GuiMvc.view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DisplayMusic extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea txt;
	
	private JPanel pan;
	private JLabel but;
	public DisplayMusic()
	{
		initGui();
	}
	
	public void initGui()
	{

		 pan=new JPanel();
		 but=new JLabel();
		 txt=new JTextArea();
		 but.setMaximumSize(new Dimension(300,20));
		 but.setMinimumSize(new Dimension(300,20));
		 but.setPreferredSize(new Dimension(300,20));
		 
		 

		
		 this.setSize(new Dimension(400,400));
		 pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));
	     pan.add(but);
	     //pan.add(txt);
	     this.setVisible(true);
	     JScrollPane jsp=new JScrollPane(txt);
		 jsp.setMaximumSize(new Dimension(350,350));
		 jsp.setMinimumSize(new Dimension(350,350));
		 jsp.setPreferredSize(new Dimension(350,300));
	     pan.add(jsp);
	     this.getContentPane().add(pan);
		
	}
	
	
	public JTextArea getTxt() {
		return txt;
	}
	public JPanel getPan() {
		return pan;
	}
	public JLabel getBut() {
		return but;
	}
	

}