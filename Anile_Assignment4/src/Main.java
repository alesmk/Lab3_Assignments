/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 4		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ArrayList<String> pathnames = new ArrayList<String>();
		String input;
		
		while(true) {
			System.out.print("Inserisci il nome di un file o /END per terminare: ");
			input = sc.next();
			if(input.equalsIgnoreCase("/END"))
				break;
			
			if(!pathnames.contains(input))
				pathnames.add(input);
		}
		
		sc.close();
		
		Count occ = new Count();
		occ.startCount(pathnames);
		
		System.out.println(" 	*** FINE *** ");
	}

}
