package android;

import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

class Server {

	AudioInputStream audioInputStream;
	static AudioInputStream ais;
	static AudioFormat format;
	static boolean status = true;
	static int port = 50005;
	static int sampleRate = 44100;

	public static void main(String args[]) throws Exception {

		ServerSocket serverSocket = new ServerSocket(50005);

		while (true) {

			Socket clientSocket = serverSocket.accept();

			new Thread(new ServerThread(clientSocket)).start();

		}

	}

}