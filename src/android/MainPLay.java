package android;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.tritonus.sampled.convert.PCM2PCMConversionProvider;

import AudioLogic.LoadFile;
import AudioLogic.Matcher;

public class MainPLay {

    private final static int BUFFER_SIZE = 128000;
    private static File soundFile;
    private static AudioInputStream audioStream;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine;

    /**
     * @param filename the name of the file that is going to be played
     */
    public static String  testPlay(byte[] a)
    {
    	String result="";
    	
      try {
        //File file = new File(filename);
        AudioInputStream in= AudioSystem.getAudioInputStream(new ByteArrayInputStream(a));
        System.out.println("Ass");

        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        System.out.println(in.getFormat());
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
                                                                                      baseFormat.getSampleRate(),
                                                                                      16,
                                                                                      baseFormat.getChannels(),
                                                                                      baseFormat.getChannels() * 2,
                                                                                      baseFormat.getSampleRate(),
                                                                                      true);
        din = AudioSystem.getAudioInputStream(getAudioFormat(), in);
        
        // Play now. 
        result=   rawplay(decodedFormat, din);
        in.close();
      } catch (Exception e)
        {
            //Handle exception.
        }
	return result;
    } 

    public static String  rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException,                                                                                                LineUnavailableException
    {
      byte[] data = new byte[4096];
      SourceDataLine line = getLine(getAudioFormat()); 
     AudioInputStream musicStream = new PCM2PCMConversionProvider().getAudioInputStream(getAudioFormat(), din);
String result="";
System.out.println(musicStream.getFormat()+"ss");
      if (line != null)
      {
        // Start
        line.start();
        int nBytesRead = 0, nBytesWritten = 0;
        
        Matcher match = new Matcher();
		byte[] buffer = new byte[(int) 4096];
		  int[] Range=new int[]{40,80,160};
		  LoadFile fl = new LoadFile(4096, Range.length, Range);
        
        
	        int d = 0;
        
        while (nBytesRead != -1)
        {
            nBytesRead = din.read(buffer, 0, buffer.length);
System.out.println("ss");
			 fl.loadMic(buffer, match, d, true, "sadsad");
d++;
            
           if (nBytesRead != -1) nBytesWritten = line.write(buffer, 0, nBytesRead);
        }
        
        
		
		
        
        
        result=match.analyze();
        // Stop
        line.drain();
        line.stop();
        line.close();
        din.close();

      } 
		return result;

    }

    private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
      SourceDataLine res = null;
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
      res = (SourceDataLine) AudioSystem.getLine(info);
      res.open(audioFormat);
      return res;
    } 
    
    public static void main(String[] args)
    {
    	try {
			testPlay(Files.readAllBytes(Paths.get("test.mp3")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    static AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);
    }
    
}