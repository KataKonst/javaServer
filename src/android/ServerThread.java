package android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
	Socket socket;

	public ServerThread(Socket pSocket) {
		socket = pSocket;

	}

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
		byte[] receiveData = new byte[17553];

		while (c < 20) {
			c++;
			int cs = 0;
			try {
				cs = in.read(receiveData);
			} catch (IOException e) {
				e.printStackTrace();
			}				
			bs.write(receiveData,0,cs);
			
			System.out.println("chunk"+c+" "+cs);



		}
		System.out.println(bs.size());
		byte a[] = bs.toByteArray();
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
	
	
	
	
	
	
	
	
	
	

}
