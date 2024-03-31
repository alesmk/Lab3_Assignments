/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 9		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */
package server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class TerminationHandler extends Thread {
	
	private ServerSocketChannel serverSocket;
	private ArrayList<SocketChannel> list;
	
	public TerminationHandler(ServerSocketChannel serverSocket, ArrayList<SocketChannel> connectedClients) {
		this.serverSocket = serverSocket;
		this.list = connectedClients;
	}
	
	public void run(){
		try {
			for(SocketChannel client : this.list) {
				client.close();
			}
	
			this.serverSocket.close();
		
		} catch (IOException e) {
			System.err.println("[SERVER] " + e.getMessage());
		}
		System.out.println(" *** SERVER CHIUSO *** ");
	}
	
}
