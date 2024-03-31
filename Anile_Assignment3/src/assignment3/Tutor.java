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

// Gestisce la coda delle richieste di accesso al laboratorio
public class Tutor implements Runnable {

	private Laboratorio laboratorio;
	private PriorityBlockingQueue<Utente> queue;
	
	public Tutor(Laboratorio lab, PriorityBlockingQueue<Utente> coda) {
		laboratorio = lab;
		this.queue = coda;
	}
	
	public void run() {
		System.out.println("Tutor in esecuzione...");
		Utente user;
		
		while (!Thread.currentThread().isInterrupted()) {
		    try {
		    	user = queue.poll();
				if(user != null) {
					autorizza(user);
				}
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		    }
		}
		
	}
	
	private void autorizza(Utente user) {
		
		// Il tutor si chiede a che tipo di utente deve fornire l'accesso.
		
		if(user instanceof Professore) {
			if(laboratorio.accessoCompleto()) {
				user.autorizza();
			}
			else {
				System.out.println("Il professore non è riuscito ad accedere.");
				this.queue.add(user);
			}
		}
		else if(user instanceof Tesista) {
			int i = ((Tesista)user).getPostazione(); 
			if(laboratorio.accesso(i)) {
				user.autorizza();
			}
			else {
				System.out.println("Il tesista non è riuscito ad accedere.");
				this.queue.add(user);
			}
		}
		else {
			int i = laboratorio.accesso();
			if(i != -1) {
				((Studente)user).setPostazione(i);
				user.autorizza();
			}
			else {
				System.out.println("Lo studente non è riuscito ad accedere.");
				this.queue.add(user);
			}
		}
				
	}
}
