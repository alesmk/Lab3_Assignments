/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 6		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		System.out.println("Server in esecuzione...");
			
		try(ServerSocket server = new ServerSocket(1313) ) {
			
			// Inizia una partita
			while(true){
				try{
					Socket clientSocket = server.accept();
					Partita partita = new Partita();
					partita.setSocket(clientSocket); // gestisce la partita lato server
					partita.start();		
				}catch(IOException e){
					return; //
				}
			}		
		} catch(BindException e) {
			System.err.println("Porta occupata");
		} catch(IOException e) {
			System.err.println(e);
		}
	}

}
