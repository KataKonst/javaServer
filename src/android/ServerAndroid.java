package android;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioPlayer;

public class ServerAndroid implements Runnable {
	Socket socket;

	public ServerAndroid(Socket pSocket) {
		socket = pSocket;

	}
	  static AudioFormat    format = new AudioFormat(44100, 16, 1, true, false);
		SourceDataLine sourceDataLine=null;

	@Override
	public void run() {

		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		int c = 0;
		InputStream in = null;
		OutputStream out = null;
		try {
			out = (socket.getOutputStream());
			in = (socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		c = 0;
		final byte[] receiveData = new byte[4096];
		  DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
	        try {
				 sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				 sourceDataLine.open();
				 sourceDataLine.start();
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		while (c < 1000) {
			c++;
			int cs = 0;
			try {
				cs = in.read(receiveData);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bs.write(receiveData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		        // A thread solve the problem of chunky audio 
		        new Thread(new Runnable() {
		            @Override
		            public void run() {
		                toSpeaker(receiveData,sourceDataLine);
		            }
		        }).start();
			
			
			System.out.println("chunk"+c);



		}
		 ByteArrayInputStream baiss = new ByteArrayInputStream(
				 receiveData);

		 

		 
			byte a[] = bs.toByteArray();

		 AudioInputStream      ais = new AudioInputStream(baiss, format, a.length);
		 Clip clip;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = ais;         
	         clip.open(inputStream);
	         clip.start(); 
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         

		System.out.println(bs.size());
		System.out.println("ss");

		String res = MainPLay.testPlay(a);
		System.out.println(res);
		try {
			out.write(res.getBytes());
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	

public static void toSpeaker(byte soundbytes[],SourceDataLine sourceDataLine ) {
    try {

      


      

       // sourceDataLine.open(format);

        
        sourceDataLine.write(soundbytes, 0, soundbytes.length);
        sourceDataLine.drain();
        System.out.println(soundbytes.toString());
        
    } catch (Exception e) {
        System.out.println("Not working in speakers...");
        e.printStackTrace();
    }
}

	
	
	
	
	

}
