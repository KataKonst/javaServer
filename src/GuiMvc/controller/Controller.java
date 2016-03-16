package GuiMvc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import AudioLogic.Media;
import GuiMvc.view.DisplayMusic;
import GuiMvc.view.Result;
import GuiMvc.view.UploadWorker;
import GuiMvc.view.View;
import uk.co.caprica.vlcj.binding.internal.media_duration_changed;

public class Controller {
	TimerTask timerTask;
	Timer timer;
	int secs=0;
	TimerTask secTask;
	Timer sec;
	 Thread micRecorder;
	View vw;
	Controller t = this;
	public Boolean run = false;
	UploadWorker wrk = null;
	Media med = new Media();
	 boolean run1=false;;
int seconds=20;
	

	public Controller(View vw) {
		this.vw = vw;
		initActions();
	}
	
	public void setSec(int sc)
	{
		seconds=sc;
	}

	public void initActions() {


			
			

		vw.getRegister().addActionListener(new StartList(wrk, vw.getFc(), vw));

		vw.getStopUpload().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (wrk != null){
					//wrk.cancel(true);
					wrk.setWorcking(false);
					
				}

			}

		});
		vw.getStartMic().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				 run=false;
				    timerTask = new TimerTask(){
 
					 public void run() {
						  if(run){
		                         med.stop();
								String name = med.getMatch().analyze();
			                   
								Result result = new Result(name);
								 timer.cancel();
								 timer.purge();
								 sec.cancel();
								 sec.purge();
								 run=false;

								showResult(result);
								  vw.getResultSize().setText("Result Size:"+med.getMatch().getResults().size());

									vw.getResultsList().setListData(med.getMatch().getResults().toArray());
			                   
								JOptionPane.showConfirmDialog(null, name);
			                    vw.getRecordText().setText("Not recording");

								
								 secs=0;
	                            }
				                
				            }
					 
						};
						
						secTask=new TimerTask(){
							 
							 public void run() {
								  if(run){
										secs++;
					                    vw.getRecordText().setText("Recording "+secs+" seconds");

										
			                            }
						                
						            }
							 
								};
						
					
				 
				  micRecorder = new Thread(new Runnable() {
			            public void run() {
			              
                            run=true;
		                    vw.getRecordText().setText("Recording");
		                    run1=true;
		                    med.startrecordMic();
							
			            }
			            
			        });
				 
				 micRecorder.start();
					timer = new Timer(true);
					sec=new Timer(true);
					sec.scheduleAtFixedRate(secTask, 0,1000);
					
					timer.scheduleAtFixedRate(timerTask, seconds*1000, seconds*1000);

				 
				 
			}

		});
		
		vw.getStopMic().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				 if(run){ 
				med.stop();
					String name = med.getMatch().analyze();
					 timer.cancel();
					 timer.purge();
					 sec.cancel();
					 sec.purge();
					 secs=0;

					 run=false;
				   Result result = new Result(name);
				   showResult(result);
				   vw.getResultSize().setText("ResultSize:"+med.getMatch().getResults().size());

					vw.getResultsList().setListData(med.getMatch().getResults().toArray());
					JOptionPane.showConfirmDialog(null, name);
				 
                 vw.getRecordText().setText("Not recording");

				 }
					
				
				
			}
			
		});
		
		vw.getDisplayBut().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DisplayMusic a=vw.getDisMusic();
				a=new DisplayMusic();
				new Listener(a);
				List<String> ls=med.getMatch().getNames();
				
				for(String s:ls)
				a.getTxt().setText(a.getTxt().getText()+s+"\n");
				
				a.getBut().setText("Number of files: "+String.valueOf(ls.size()));
				
				
			}
			
		});
		
		vw.getClearDB().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				med.getMatch().clear();
				
			}
			
		});
		
		
		vw.addWindowListener(new java.awt.event.WindowAdapter(){
			
			 public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if(timer!=null){
					micRecorder.interrupt();
				 timer.cancel();
				 timer.purge();
				 run=false;
				System.exit(0);
				}
				med.commit();

				
				
			 }
		
		});
		
		vw.getSlider().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
			    JSlider src = (JSlider)arg0.getSource();
			    if(!run)
			    seconds=src.getValue();
			    
			    
			}
			
		});
		}
		  
	
	
	
	
	

	public void showResult(JPanel panel) {

		JFrame frame = new JFrame();
		new Listener(frame);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	class StartList implements ActionListener {

		JFileChooser fc = null;
		View frm = null;

		StartList(UploadWorker wrk, JFileChooser fc, View frm) {

			this.fc = fc;
			this.frm = frm;
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(wrk != null && wrk.isWorcking()){
				vw.getStopUpload().doClick();
			}
			
			wrk = new UploadWorker(fc, frm, med);
			wrk.execute();
		}

	}

}