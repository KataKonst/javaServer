package GuiMvc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import vlc.Utils;

public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelShazam;
	private JButton register;
	private JButton stopUpload;
	private JButton startMic;
	private JFileChooser fc;
	private JTextArea songUploaded;
	private JProgressBar progressBar;
	private DisplayMusic DisMusic;
	private JButton displayBut;
	private JLabel progressLabel;
	private JButton ClearDB;
	private JButton stopMic;
	private JLabel recordText;
	private JList resultsList;
	private JLabel ResultSize;
	private JSlider slider;
	
	
	
	


	

	public JSlider getSlider() {
		return slider;
	}

	public JList getResultsList() {
		return resultsList;
	}

	public JLabel getRecordText() {
		return recordText;
	}

	public JButton getStopMic() {
		return stopMic;
	}

	public View() {

		initGui();
	}

	public void initGui() {
		this.setLayout(new BorderLayout());
		
		progressBar = new JProgressBar();
		
		progressLabel=new JLabel("Progress: ");
	
		
		progressBar.setMinimumSize(new Dimension(200,20));
		progressBar.setPreferredSize(new Dimension(200,20));
		progressBar.setMaximumSize(new Dimension(100,40));
		progressBar.setMinimum(0);
		progressBar.setStringPainted(true);
		
		panelShazam = new JPanel();
		songUploaded = new JTextArea("Song uploaded\n\n");
		songUploaded.setEditable(false);

		register = new JButton("Upload");
		stopUpload = new JButton("StopUpload");
		startMic = new JButton("StartMic");
		displayBut=new JButton("Music");
		ClearDB=new JButton("ClearDB");
		stopMic=new JButton("StopMic");
		
		slider=new JSlider(JSlider.HORIZONTAL,0,100,20);
		
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMaximumSize(new Dimension(100,50));
		slider.setPreferredSize(new Dimension(100,50));
		slider.setMinimumSize(new Dimension(100,50));;
		Font font = new Font("Serif", Font.ITALIC, 10);
		slider.setFont(font);
		

		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Music File", "mp3");
		fc.setFileFilter(filter);

		
		
		JScrollPane jsp=new JScrollPane(songUploaded);
		jsp.setMinimumSize(new Dimension(300,400));
		jsp.setMaximumSize(new Dimension(300,400));
		jsp.setPreferredSize(new Dimension(300,400));
		
		resultsList=new JList();
		JScrollPane jsp2=new JScrollPane(resultsList);
		jsp2.setMinimumSize(new Dimension(400,400));
		jsp2.setMaximumSize(new Dimension(400,400));
		jsp2.setPreferredSize(new Dimension(400,400));
		
		recordText=new JLabel("Not Recording   ");
		recordText.setMinimumSize(new Dimension(130,20));
		recordText.setMaximumSize(new Dimension(130,20));
		recordText.setPreferredSize(new Dimension(130,20));
		
		ResultSize=new JLabel("ResultSize:0");
		ResultSize.setMinimumSize(new Dimension(100,20));
		ResultSize.setMaximumSize(new Dimension(100,20));
		ResultSize.setPreferredSize(new Dimension(100,20));
		
		
       
		GridBagLayout gbl=new GridBagLayout();

		this.getContentPane().setLayout(gbl);
		this.getContentPane().add(register,new GridBagConstraints(0,0,1,1,1.0,1.0,
				                      GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		
		this.getContentPane().add(stopUpload,new GridBagConstraints(1,0,1,1,1.0,1.0,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(startMic,new GridBagConstraints(2,0,1,1,1.0,1.0,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(stopMic,new GridBagConstraints(3,0,1,1,1.0,1.0,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(displayBut,new GridBagConstraints(4,0,1,1,1.0,1.0,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(ClearDB,new GridBagConstraints(5,0,1,1,1.0,1.0,
                GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(progressLabel,new GridBagConstraints(0,1,1,1,1.0,1.0,
	               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(progressBar,new GridBagConstraints(1,1,3,1,1.0,1.0,
	               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(jsp,new GridBagConstraints(0,2,3,5,1.0,1.0,
               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		
		this.getContentPane().add(jsp2,new GridBagConstraints(3,2,3,5,1.0,1.0,
               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		this.getContentPane().add(recordText,new GridBagConstraints(3,1,3,1,1.0,1.0,
	               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		this.getContentPane().add(slider,new GridBagConstraints(4,1,1,1,1.0,1.0,
	               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		this.getContentPane().add(ResultSize,new GridBagConstraints(5,1,1,1,1.0,1.0,
	               GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		
	
		
	
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public JLabel getResultSize() {
		return ResultSize;
	}

	public JButton getClearDB() {
		return ClearDB;
	}

	public void setMxValProgressBar(int n){
		progressBar.setMaximum(n);
	}
	
	public void setMnValProgressBar(int n){
		progressBar.setMinimum(n);
	}
	
	public void updateProgress(){
		progressBar.setValue(progressBar.getValue()+1);
		progressLabel.setText("Progress:"+(progressBar.getValue())+"\\"+progressBar.getMaximum());

	}

	public JPanel getPanelShazam() {
		return panelShazam;
	}

	

	public JButton getRegister() {
		return register;
	}



	public JFileChooser getFc() {
		return fc;
	}

	public JButton getStopUpload() {
		return stopUpload;
	}

	public JButton getStartMic() {
		return startMic;
	}
	
	public JTextArea getSongUploaded() {
		return songUploaded;
	}

	public void setSongUploaded(JTextArea songUploaded) {
		this.songUploaded = songUploaded;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public void resetRegister(int mxValProgressBar) {
		progressBar.setMinimum(0);
		progressBar.setMaximum(mxValProgressBar);
		progressBar.setValue(0);
		progressLabel.setText("Progress:"+0+"\\"+mxValProgressBar);
		
		songUploaded.setText("");
		
	}
	public JButton getDisplayBut() {
		return displayBut;
	}

	public DisplayMusic getDisMusic() {
		return DisMusic;
	}

}