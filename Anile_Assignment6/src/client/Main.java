/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 6		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

// esito = "vitagiocatore;pozionegiocatore;salutemostro\n"
public class Main {
	// CLIENT
	public static void main(String[] args) {
		
		System.out.println("Avvio partita...");
		try(Socket socket = new Socket("localhost", 1313)){
			
			try(BufferedReader reader =	new BufferedReader(new InputStreamReader(socket.getInputStream()))){
				try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
					execute(reader, writer);
				}catch(IOException e){
					System.err.println("Errore apertura BufferedWriter client");
				}				
			}catch(IOException e){
				System.err.println("Errore apertura BufferedReader client");
			}
					
		}catch(IOException e){
			System.err.println("Connessione fallita");
		}
		
	}
	
	private static void execute(BufferedReader reader, BufferedWriter writer) {
		int comando;
		int lG, pG, sM;
		int esitoPartita;
		String[] esito; 
		String nuovaPartita, _e = "";
		
		Scanner sc = new Scanner(System.in);
		
		do{
			System.out.printf("Possibili comandi:\n1-Combatti\n2-Curati\n3-Fuggi\n");			
			
			//Leggo il comando da eseguire
			do{
				System.out.print("Prossimo comando > ");
				try{
					comando = sc.nextInt();
				}catch(InputMismatchException e){
					comando = -1;
					sc.next();
				}
			}while(comando<1 || comando > 3); 
			
			//Invio il comando al server
			try { writer.write(comando);
			} catch (IOException e1) { System.err.println("[client] Eccezione: scrittura"); }

			try { writer.flush(); } catch (IOException e1) { System.err.println("[client] Eccezione: flush"); }
			
			System.out.println("Inviato il comando "  + comando);
			
			// Leggo l'esito dal server 
			try { _e = reader.readLine(); } catch (IOException e) { System.err.println("[client] Eccezione: lettura");	}

			esito = _e.split(";");
			lG = Integer.parseInt(esito[0]);
			pG = Integer.parseInt(esito[1]);
			sM = Integer.parseInt(esito[2]);
			
			if(lG == -1)
				System.out.println("Il giocatore è scappato...");
			else
				System.out.println("GIOCATORE: [salute: " + lG + "; pozione: " + pG + "]");
			System.out.println("MOSTRO: [salute: " + sM + "]");
			
		}while(lG > 0 && sM > 0); 
	
		// Calcolo dell'esito finale. -1: ha perso il giocatore | 0: pareggio | 1: ha vinto il giocatore
		if(lG == 0 && sM == 0) {
			esitoPartita = 0;
			System.out.println(" --- PAREGGIO --- ");
		}
		else if(sM > 0) {
			esitoPartita = -1;
			System.out.println(" --- IL MOSTRO HA VINTO  --- ");
		}
		else {
			esitoPartita = 1;
			System.out.println(" --- IL GIOCATORE HA VINTO --- ");
		}
		
		// Vuoi giocare ancora?
		if(esitoPartita == 0 || esitoPartita == 1) { 
			
			do {
				System.out.print("Vuoi giocare ancora? [y/n]> "); 
				nuovaPartita = sc.next();
			}while(!nuovaPartita.equalsIgnoreCase("y") && !nuovaPartita.equalsIgnoreCase("n"));
			
			if(nuovaPartita.equalsIgnoreCase("y")) { 
				try { writer.write(4); } catch (IOException e) { System.err.println("[client] Eccezione: scrittura"); }
				System.out.println("Avvio di nuova partita.");
				execute(reader,writer);
			}
			else{
				System.out.println("*** PARTITA TERMINATA ***");
			}
			
		}
		sc.close();

	}
}
