/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 10		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */


import java.net.DatagramSocket;

public class TerminationHandler extends Thread {
	
	private DatagramSocket server;
	
	public TerminationHandler(DatagramSocket server) {
		this.server = server;
	}
	
	public void run(){
		this.server.close();
		System.out.println(" *** SERVER CHIUSO *** ");
	}
	
}
