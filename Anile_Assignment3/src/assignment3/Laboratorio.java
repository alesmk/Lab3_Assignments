/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 3		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package assignment3;

public class Laboratorio {

	private static final int N = 20;
	private Computer[] computers;
	private Object lock; 
	
	public Laboratorio() {
		this.computers = new Computer[N];
		this.lock = new Object();
		for(int i=0; i<N; i++) {
			this.computers[i] = new Computer();
		}
	}
	
	public static int getN() {
		return N;
	}

	// Dare l'accesso a tutti i pc
	public boolean accessoCompleto() {
		System.out.println("Un professore sta cercando di accedere al laboratorio.");
		synchronized (this.lock) {
			
			for(int i=0; i<N; i++) {
				while(this.computers[i].getStato() == Stato.IN_USO) {
					try {
						this.lock.wait(2500);
					} catch (InterruptedException e) {
						;
					}
				}
				this.computers[i].setStato(Stato.IN_USO);
			}
			System.out.println("Un professore è riuscito ad accedere al laboratorio.");
		}
		return true;
	}
	
	// Dare l'accesso all'i-esimo pc
	public boolean accesso(int i) throws IllegalArgumentException {
		
		if(i<0 || i>N) {
			throw new IllegalArgumentException();
		}
		
		synchronized (this.lock) {
			if(this.computers[i].getStato() == Stato.LIBERO) {
				this.computers[i].setStato(Stato.IN_USO);
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	// Dare l'accesso a un qualsiasi pc libero: se restituisce -1, l'accesso non è riuscito
	public int accesso() {
		int j = -1; 
		
		synchronized(this.lock) {
			for(int i=0; i<N; i++) {
				if(this.computers[i].getStato() == Stato.LIBERO) {	
					this.computers[i].setStato(Stato.IN_USO);
					j = i;
					break;
				}
			}
		}
		
		return j; 
	}
	
	
	// Liberare un pc
	public boolean rilascia(int i) throws IllegalArgumentException {
		if(i<0 || i>N) {
			throw new IllegalArgumentException();
		}
		
		synchronized (this.lock) {
			if(this.computers[i].getStato() == Stato.IN_USO) {
				this.computers[i].setStato(Stato.LIBERO);
				this.lock.notify();
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	// Liberare tutti i pc 
	public boolean rilascioCompleto() {
		System.out.println("Professore ha terminato.");
		synchronized (this.lock) {
			for(int i=0; i<N; i++) {
				if(this.computers[i].getStato() == Stato.IN_USO) {	
					this.computers[i].setStato(Stato.LIBERO);
					this.lock.notify();
				}
				else {
					// qualcosa è andato storto
					return false;
				}
			}
		}
		
		return true;
	}
}
