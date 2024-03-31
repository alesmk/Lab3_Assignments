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

public class Professore extends Utente {

	public Professore(int id, Laboratorio lab, PriorityBlockingQueue<Utente> coda) {
		super(id, lab, coda);
	}
	
	public void esci() {
		this.laboratorio.rilascioCompleto();
		System.out.println("[P] Utente " + this.id + " ha finito.");
	}

}
