/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 3		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package assignment3;

public class Computer {
	private Stato stato;
	
	public Computer() {
		this.stato = Stato.LIBERO;
	}

	public Stato getStato() {
		return stato;
	}

	protected void setStato(Stato stato) { 
		this.stato = stato;
	}
	
}
