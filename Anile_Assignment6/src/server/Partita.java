/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 6		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Partita extends Thread{
	private Socket socket;
	private Giocatore player;
	private Mostro monster;
	public Partita() {
		this.player = new Giocatore();
		this.monster = this.player.getMostro();
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public void run() {
		System.out.println("**Inizio partita**");
		int comando, lG, pG, sM;
		String esito; 
		
		// Leggo il comando dal client
		try(BufferedReader reader =	new BufferedReader(new InputStreamReader(socket.getInputStream()))){
			try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
					
				// ROUND 
				System.out.println("Aspetto il primo comando");
				
				while((comando = reader.read()) != -1){
					esito = "";				
					
					if(comando == 4) {
						System.out.println("Avvio di una nuova partita...");
						player.setSalute(player.getX());
						player.setPozione(player.getY());
						monster.setSalute(monster.getZ());
						comando = reader.read();
					}
					if(comando == 1) combatti();
					else if(comando == 2) cura();
					else if(comando == 3) player.setSalute(-1);
					
					lG = player.getSalute();
					pG = player.getPozione();
					sM = monster.getSalute();

					// Calcolo l'esito e lo invio al client
					esito = lG + ";" + pG + ";" + sM + "\n";
					System.out.println("Invio esito: "+esito);

					writer.write(esito);
					writer.flush();
					
				}
				System.out.println("*Fine*");
			}catch(IOException e) {
				System.err.println("Errore apertura BufferedWriter server");
			}
		}catch(IOException e) {
			System.err.println("Errore apertura BufferedReader server" + e.getMessage());
		}
		
		try {
			this.socket.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	// Combattere con il mostro 
	private void combatti() {
		
		int lG = player.getSalute();
		int sM = monster.getSalute();
		int xG, xM;
		
		xG = ThreadLocalRandom.current().nextInt(0, lG)+1;
		xM = ThreadLocalRandom.current().nextInt(0, sM)+1;
		
		player.setSalute(lG - xG);
		monster.setSalute(sM - xM);
		System.out.printf("Danni inflitti: [G: %d], [M: %d]\n", xG, xM);
	}
	
	// Il giocatore usa la pozione
	private void cura() {
		int y;
		int pG = player.getPozione();
		
		if(pG == 0) {
			System.out.println("Pozione esaurita :(");
			return;
		}
		else y = ThreadLocalRandom.current().nextInt(0, pG)+1;
		
		System.out.println("Il giocatore si cura... +" + y + " punti vita!...");
		player.setSalute(player.getSalute() + y);
		player.setPozione(pG - y);
	}
	
}
