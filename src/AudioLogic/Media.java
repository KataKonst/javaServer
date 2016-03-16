package AudioLogic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.tritonus.sampled.convert.PCM2PCMConversionProvider;
import org.tritonus.sampled.convert.SampleRateConversionProvider;

/**
 *
 * @author Kata
 */
public class Media {

    private boolean runing;
    TargetDataLine line = null;
    ByteArrayOutputStream micRecord = null;
    ByteArrayOutputStream loadRecord = null;
    private Matcher match = new Matcher();

    public Matcher getMatch() {
		return match;
	}

	public void startrecordMic() {
    	match.matching.clear();
    	match.namesMus.clear();
        initMic();
        int d=1;
        micRecord = new ByteArrayOutputStream();
        int n = 0;
        byte[] buffer = new byte[(int) 1024];
        int[] Range=new int[]{40,80,160};
        LoadFile fl = new LoadFile(4096,Range.length,Range);
runing=true;
DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getAudioFormat());
SourceDataLine sourceDataLine = null;
try {
	sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	sourceDataLine.open(getAudioFormat());
	sourceDataLine.start();
} catch (LineUnavailableException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}


// FloatControl volumeControl = (FloatControl)
// sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
// volumeControl.setValue(100.0f);

// sourceDataLine.start();

        while (runing) {
            
           


            int count = line.read(buffer, 0, 1024);
          //  sourceDataLine.write(buffer, 0, 1024);
            
            n++;
             if (count == 1024) {
                micRecord.write(buffer, 0, count);

            }
            if (count == 1024 && n % 4 == 0) {
                
                fl.loadMic(micRecord.toByteArray(), match, d, true, "sadsad");
                d++;
                micRecord.reset();
            }

        }
       
    }


    public void stop() {
        runing = false;
        line.close();
        line.flush();
    }

    private void initMic() {
        try {

            final AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);
    }
    
    AudioFormat getAudioFormat3() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);
    }
    
    AudioFormat getAudioFormat2(){
    	
    	 float sampleRate = 44100;
         int sampleSizeInBits = 16;
         int channels = 2; // mono
         boolean signed = true;
         boolean bigEndian = true;
         return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                 bigEndian);
    	
    }
    

    File file = null;

    public String testPlay(String filename) {
        try {
            file = new File(filename);
            try (AudioInputStream in = AudioSystem.getAudioInputStream(file)) {
                AudioFormat baseFormat = in.getFormat();

                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false);
                AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
                
                play(din);
            }
        } catch (Exception e) {
            
            return e.toString();
        }
        return "completed";
    }

    private void play(AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[1024];
        if(din.getFormat().getSampleRate()!=44100){
        din=new SampleRateConversionProvider.SampleRateConverterStream(din, getAudioFormat2());
        }
       
        AudioInputStream musicStream = new PCM2PCMConversionProvider().getAudioInputStream(getAudioFormat(), din);
        loadRecord = new ByteArrayOutputStream();
        int n = 0;
        int bytesRead = 0;
        int position=1;
        int[] Range=new int[]{40,80,160};
        LoadFile fl = new LoadFile(4096,Range.length,Range);
        String name=match.getMusicName(file.getName());
        if(match.addMusic(file.getName()))
        {
        while (bytesRead != -1) {
           

            bytesRead = musicStream.read(data, 0, 1024);
            

            if (bytesRead == 1024) {
                loadRecord.write(data, 0, bytesRead);
                n++;

            }
            if (bytesRead == 1024 && n % 4 == 0) {
                fl.loadMic(loadRecord.toByteArray(), match, position, false,name);
                position++;
                loadRecord.reset();
            }

           

        }

        din.close();
        }

    }
    public void commit()
    {
        match.db.commit();
    }
}