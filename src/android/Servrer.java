package android;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import it.sauronsoftware.jave.Encoder;

class Servrer {

AudioInputStream audioInputStream;
static AudioInputStream ais;
static AudioFormat format;
static boolean status = true;
static int port = 50005;
static int sampleRate = 44100;

public static void main(String args[]) throws Exception {

	
	

    DatagramSocket serverSocket = new DatagramSocket(50005);


    byte[] receiveData = new byte[4096]; 
    // ( 1280 for 16 000Hz and 3584 for 44 100Hz (use AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat) to get the correct size)

    format = new AudioFormat(sampleRate, 16, 1, true, false);
final ByteArrayOutputStream bs=new ByteArrayOutputStream();
int c=0;
    while (c<600) {
    	c++;
    	System.out.println(c);
        final DatagramPacket receivePacket = new DatagramPacket(receiveData,
                receiveData.length);

        serverSocket.receive(receivePacket);

       
        bs.write(receivePacket.getData());


        // A thread solve the problem of chunky audio 
      
    }
    ByteArrayInputStream baiss = new ByteArrayInputStream(
    		bs.toByteArray());
    ais = new AudioInputStream(baiss, format, bs.toByteArray().length);
    System.out.println(MainPLay.rawplay(format, ais));
    new Thread(new Runnable() {
        @Override
        public void run() {
         //  toSpeaker(bs.toByteArray());
        }
    }).start();
}

public static void toSpeaker(byte soundbytes[]) {
    try {

        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);


        
        sourceDataLine.open(format);

        sourceDataLine.start();

        System.out.println("format? :" + sourceDataLine.getFormat()+" "+format);

        sourceDataLine.write(soundbytes, 0, soundbytes.length);
        System.out.println(soundbytes.toString());
        sourceDataLine.drain();
        sourceDataLine.close();
    } catch (Exception e) {
        System.out.println("Not working in speakers...");
        e.printStackTrace();
    }
}
}