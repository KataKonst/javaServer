package AudioLogic;

import java.nio.ByteBuffer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 *
 * @author Kata
 */
public class LoadFile {

	DoubleFFT_1D fft;
	double[] dataChunk;
	long pt[] ;
    int size;
    int[] range;
	public LoadFile(int size,int pointsNumber,int[] Range)
	{
		this.size=size;
		dataChunk=new double[2*size];
		this.range=Range;
		pt=new long[pointsNumber];

	}
	
	public static short[] shortMe(byte[] bytes) {
	    short[] out = new short[bytes.length / 2]; // will drop last byte if odd number
	    ByteBuffer bb = ByteBuffer.wrap(bytes);
	    for (int i = 0; i < out.length; i++) {
	        out[i] = bb.getShort();
	    }
	    return out;
	}
	
	
	public static double[] floatMe(short[] pcms) {
		double[] floaters = new double[2*pcms.length];
	    for (int i = 0; i < pcms.length; i++) {
	        floaters[i] = pcms[i];
	    }
	    return floaters;
	}
	
	
	public void loadMic(byte data[], Matcher match, int pz, boolean mic,
			String nume) {
           		

		dataChunk=floatMe(shortMe(data));
		fft=new DoubleFFT_1D(dataChunk.length/2);

		fft.realForwardFull(dataChunk);
		int pos = 0;
		int ct = 0;
		int mx = -1;
		//System.out.println(range[range.length-1]+2);
		for (int i = range[0]; i < range[range.length-1]+2; i++) {
			double ampl =(Math.sqrt(dataChunk[2 * i] * dataChunk[2 * i]
					+ dataChunk[2 * i + 1] * dataChunk[2 * i + 1]) + 1);
			
			if (i > range[ct]) {
				pt[ct] = pos;
				ct++;
				mx = -999999999;
			} else {
				if (mx < ampl) {
					mx = (int) ampl;
					pos = i;
				}
			}

		}
		if (mic) {
			match.get(pt, pz);
		} else {
			match.insert(pt, new PointPOJO(pz, nume));
		}
	}

}