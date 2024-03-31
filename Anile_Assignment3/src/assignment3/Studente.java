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

public class Studente extends Utente {
	
	private int postazione;
	
	public Studente(int id, Laboratorio lab, PriorityBlockingQueue<Utente> coda) {
		super(id, lab, coda);
		this.postazione = -1;
	}
		
	public int getPostazione() {
		return this.postazione;
	}

	public void setPostazione(int postazione) {
		this.postazione = postazione;
	}

	public void esci() {
		if(this.laboratorio.rilascia(this.getPostazione())) {
			System.out.println("Rilascio della postazione " + this.getPostazione() + " andato a buon fine");
		}
		else {
			System.out.println("Rilascio della postazione " + this.getPostazione() + "fallito.");
		}
		
		System.out.println("[S] Utente " + this.id + " ha finito sulla postazione " + this.getPostazione());
	}

}
