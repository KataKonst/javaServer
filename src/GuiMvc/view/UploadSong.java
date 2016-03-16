package GuiMvc.view;

import javax.swing.JFrame;

import GuiMvc.controller.Listener;

public class UploadSong {

	public UploadSong(){
		JFrame frame = new JFrame();
		new Listener(frame);
		frame.pack();
		frame.setVisible(true);
	}
}