/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 3		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package assignment3;

import java.util.concurrent.PriorityBlockingQueue;

public class Tesista extends Utente {

	private int postazione;
	
	public Tesista(int id, Laboratorio lab, PriorityBlockingQueue<Utente> coda, int pos) {
		super(id, lab, coda);
		this.postazione = pos;
	}

	public int getPostazione() {
		return this.postazione;
	}

	public void esci() {
		if(this.laboratorio.rilascia(this.getPostazione())) {
			System.out.println("Rilascio della postazione " + this.getPostazione() + " andato a buon fine");
		}
		else {
			System.out.println("Rilascio della postazione " + this.getPostazione() + "fallito.");
		}
		
		System.out.println("[T] Utente " + this.id + " ha finito " + this.getPostazione());
	}
}
