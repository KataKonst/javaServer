package GuiMvc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;


import GuiMvc.controller.Listener;

public class Result extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainList;
	private JTextField strToSearch;
	private JButton search;
	private JScrollPane scroll;

	public Result(String arg){
		
		initLayout();
		match(arg);
		
	}
	
	private void initLayout() {
		// TODO Auto-generated method stub
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		strToSearch = new JTextField();
		search = new JButton("Serach");
		search.addActionListener(this);
		
		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.add(strToSearch,BorderLayout.CENTER);
		infoPanel.add(search,BorderLayout.EAST);
		
        add(infoPanel);
        
        initList();
	}
	
	private void initList(){
		mainList = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainList.add(new JPanel(), gbc);
        
        scroll = new JScrollPane(mainList);
        add(scroll);
	}

	
	private JLabel makeImage(String string) {

		URL url = null;
		ImageIcon image = null;
		try {
			url = new URL(string);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image = new ImageIcon(url);
		
		JLabel panel = new JLabel();
		panel.setIcon(image);
		
		return panel;
	}

	private void match(String arg) {

		String clientID = "Shazam";
        String textQuery = arg;// "Knorkator - Ich hasse Musik";
        int maxResults = 10;
        boolean filter = true;
        int timeout = 2000;
        
  
   
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		this.remove(scroll);
		initList();
		match(strToSearch.getText());
		
	}
	
	
		
	
}