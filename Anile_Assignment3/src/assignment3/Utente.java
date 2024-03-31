/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 3		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package assignment3;

import java.time.LocalDateTime;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

// Task

public abstract class Utente implements Runnable{

	private final int minDelay = 1;
	private final int maxDelay = 700;
	
	protected int id;
	private int k;
	private long intervallo;
	protected Laboratorio laboratorio;
	private PriorityBlockingQueue<Utente> queue;
	private LocalDateTime ultimoUtilizzo;
	private boolean autorizzazione;
	
	public Utente(int id, Laboratorio lab, PriorityBlockingQueue<Utente> coda) {
		this.id = id;
		this.k = ThreadLocalRandom.current().nextInt(1, 15);
		this.intervallo = ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1);
		this.laboratorio = lab;
		this.queue = coda;
		this.ultimoUtilizzo = null;
		this.autorizzazione = false;
	}
	
	public LocalDateTime getUltimoUtilizzo() {
		return this.ultimoUtilizzo;
	}

	public void run() {
		System.out.println("Utente " + this.id + " ha richiesto l'accesso...");
		for(int i=0; i<this.k; i++) {
			
			// L'utente si mette in coda 
			this.accoda(i, k);
			
			// Quando è il suo turno, l'utente usa il PC per un periodo di tempo generato casualmente
			long tempoUtilizzo = ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1);
			
			try {
				Thread.sleep(tempoUtilizzo);
				System.out.println("Utente " + this.id + " ha completato un utilizzo");
			} catch(InterruptedException e) {
				System.err.println("Utente " + this.id + "interrotto...");
			}
			
			// rilascia il (o i) pc
			this.esci();
			System.out.println("Utente " + this.id + " è uscito...");
			
			// Aspetta un certo intervallo di tempo prima di usare di nuovo il pc
			try {
				Thread.sleep(intervallo);
			} catch(InterruptedException e) {
				System.err.println("...interruzione sleep");
			}
		}
		System.out.println(" 	UTENTE " + this.id + " HA COMPLETATO LA SUA ESECUZIONE.");
	}
	
	private synchronized void accoda(int i, int k) {
		System.out.printf("(%d/%d) Utente %d in coda...\n", i, k, this.id);
		this.ultimoUtilizzo = LocalDateTime.now();
		this.queue.add(this);
		
		while(!this.autorizzazione) {
			try {
				this.wait();
			}
			catch(InterruptedException e) {
				;
			}
		}
		this.autorizzazione = false;
	}
	
	public synchronized void autorizza() {
		System.out.println("Utente " + this.id + " autorizzato :)");
		this.autorizzazione = true;
		this.notify();
	}
	
	protected abstract void esci();
	
}
