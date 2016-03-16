/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GuiMvc.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import AudioLogic.Media;

/**
 *
 * @author Kata
 */
public class UploadWorker extends SwingWorker {

	Boolean run;
	JFileChooser fc;
	View frm;
	Media md = null;
	boolean isWorcking;

	public UploadWorker(JFileChooser fc, View frm, Media md) {
		this.fc = fc;
		this.frm = frm;
		this.md = md;
		
	}

	@Override
	protected Object doInBackground() throws Exception {
		int a = fc.showOpenDialog(frm);

		if (a == JFileChooser.APPROVE_OPTION) {

			File[] files = fc.getSelectedFiles();
			
			int mxValProgressBar = 0;
			for (File file : files) {
				mxValProgressBar += DfsCountItems(file);
			}
			System.out.println(mxValProgressBar);
			
			frm.resetRegister(mxValProgressBar);
			isWorcking = true;

			for (File file : files) {
				if(isWorcking){
					Dfs(file);
				}else{
					break;
				}
				
			}
			md.commit();

		}

		return 0;
	}
	
	
	public int DfsCountItems(File dir) {

		int count=0;
		if (dir.isDirectory()) {
			
			for (File listFile : dir.listFiles()) {
				
				if (listFile.isDirectory()) {
					count += DfsCountItems(listFile);
				} else {
					String a = listFile.getName();
					if (checkExt(a, "mp3")) {
					   count++;
					}
				}
			}
		
		} else {
			if (checkExt(dir.getName(), "mp3")) {
				count++;
			}
		}
		return count;

	}
	
	

	public void Dfs(File dir) {

		if (dir.isDirectory()) {
			for (File listFile : dir.listFiles()) {
				if(isWorcking){
					if (listFile.isDirectory()) {
						Dfs(listFile);
					} else {
						String a = listFile.getName();
						if (checkExt(a, "mp3")) {
							frm.getSongUploaded().append(listFile.getName() + "  " + md.testPlay(listFile.toString())+"\n");
							frm.updateProgress();
						}
					}
				}else{
					break;
				}
				
			}
		} else {
			if (checkExt(dir.getName(), "mp3")) {
				frm.getSongUploaded().append(dir.getName() + "  " + md.testPlay(dir.toString())+"\n");
				frm.updateProgress();

			}
		}

	}

	boolean checkExt(String name, String ext) {
		int poz = name.length() - ext.length() - 1;

		if (name.length() - poz - 1 != ext.length()) {
			return false;

		}
		for (int k = poz + 1; k < name.length(); k++) {
			if (ext.charAt(k - (poz + 1)) != name.charAt(k)) {
				return false;

			}

		}

		return true;
	}

	public boolean isWorcking() {
		return isWorcking;
	}

	public void setWorcking(boolean isWorcking) {
		this.isWorcking = isWorcking;
	}

}