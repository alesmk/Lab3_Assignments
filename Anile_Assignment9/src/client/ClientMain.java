/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 9		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientMain {

	public static void main(String[] args) {
		
		System.out.println("[CLIENT]");
		
        SocketChannel client = null;
        Scanner sc;
        String request, newMessage="";
        ByteBuffer buffer;
        byte[] reqBytes;
        
		try {
			client = SocketChannel.open(new InetSocketAddress("localhost", 1313));
			client.configureBlocking(true);
		}
		catch(IOException e) {
			System.err.println("[CLIENT] Eccezione configurazione client - " + e.getMessage());
		}

		try {
			while(!client.finishConnect() ){ System.out.println("Connessione in corso"); }
		} catch (IOException e) {
			System.err.println("[CLIENT] " + e.getMessage());		} 
		
		System.out.println("[CLIENT] Client connesso.");
		
		sc = new Scanner(System.in);
		
		while(true) {
			try {
				System.out.print("[CLIENT] Inserisci il messaggio: ");
				request = sc.nextLine();	
				if(request.isEmpty()) continue;
				
				// write
				reqBytes = request.getBytes(StandardCharsets.UTF_8);
	            buffer = ByteBuffer.allocate(1024);
	            buffer.put(reqBytes);
	            buffer.flip();
	            while (buffer.hasRemaining()) {
	                client.write(buffer);
	            }
	            buffer.clear();
	            newMessage = "";
	            
	            if(request.trim().equalsIgnoreCase("END")){
            		try {
            			client.close();
					} catch (IOException e) {
						System.err.println("[CLIENT] Eccezione chiusura");
					}
            		
					break;
				}	  
	            
	            // read
	            while (client.read(buffer) > 0) {
	            	buffer.flip();
	            	newMessage += StandardCharsets.UTF_8.decode(buffer).toString();
	            	if(newMessage.endsWith("\n"))
	            		break;
	            	
	            	buffer.clear();
	            }
	            
	            newMessage = newMessage.substring(0, newMessage.length()-1);
	            
	            System.out.println("Ricevuto --> " + newMessage);
	            
			}catch(IOException e) {
				System.err.println("[CLIENT] " + e.getMessage() + " - Chiusura in corso.");
				try {
					client.close();
				} catch (IOException e1) {
					System.err.println("[CLIENT] " + e.getMessage());
				}
				break;
			}
			
		}
			
		sc.close();
		System.out.println("[CLIENT] Connessione terminata");

	}

}
