/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 3		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package assignment3;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		// Input:  #studenti, #tesisti, #professori.
		Scanner sc = new Scanner(System.in);
		int nStudenti, nTesisti, nProfessori;
		
		System.out.println("Inserisci il numero di studenti: ");
		nStudenti = sc.nextInt();

		System.out.println("Inserisci il numero di tesisti: ");
		nTesisti = sc.nextInt();

		System.out.println("Inserisci il numero di professori: ");
		nProfessori = sc.nextInt();
	
		sc.close();
		System.out.printf("Ho %d studenti, %d tesisti e %d professori\n", nStudenti, nTesisti, nProfessori);
		
		ExecutorService utenti = Executors.newCachedThreadPool();
		
		Laboratorio lab = new Laboratorio();
		PriorityBlockingQueue<Utente> queue = new PriorityBlockingQueue<Utente>(Laboratorio.getN(), new Comparatore());
		
		int i, postazione;
		
		for(i=0; i<nProfessori; i++) {	
			utenti.execute(new Professore(i, lab, queue));
		}
		for( ; i<nTesisti+nProfessori; i++) {
			postazione = ThreadLocalRandom.current().nextInt(0, Laboratorio.getN());
			System.out.println("Il tesista ha scelto il computer " + postazione);
			utenti.execute(new Tesista(i, lab, queue, postazione));
		}
		for( ; i<nStudenti+nTesisti+nProfessori; i++) {
			utenti.execute(new Studente(i, lab, queue));
		}
		
		Tutor _tutor = new Tutor(lab, queue);
		Thread tutor = new Thread(_tutor);
		tutor.start();
		
		// Terminazione
		utenti.shutdown();
		System.out.println("Il laboratorio sta per chiudere.");
		
		while(!utenti.isTerminated()) { 
			try {
				utenti.awaitTermination(3000, TimeUnit.MILLISECONDS);
			}catch(InterruptedException e) {
				System.out.println("Eccezione awaitTermination");
			}
		}
		
		tutor.interrupt();
		try {
			tutor.join();
		}catch(InterruptedException e) {
			System.out.println("Tutor: eccezione join");
		}
		
		System.out.flush();
		System.out.println("	*** Laboratorio chiuso ***");
	}

}
