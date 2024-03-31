
/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 8		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Copy {
	public static void copy_FCIndirectBuffer(File input, File output) {

		FileChannel src = Utility.initSourceNIO(input);
		FileChannel dest = Utility.initDestNIO(output);

		ByteBuffer buffer = ByteBuffer.allocate(16 * 1024); 
		try {
			while (src.read(buffer) != -1) {
				 buffer.flip();
				 dest.write(buffer); 
				 while (buffer.hasRemaining()) 
					 dest.write(buffer);
				 buffer.clear();
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		Utility.closeAllNIO(src, dest);

	}

	public static void copy_FCDirectBuffer(File input, File output) {
		FileChannel src = Utility.initSourceNIO(input);
		FileChannel dest = Utility.initDestNIO(output);

		ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024); 
		try {
			while (src.read(buffer) != -1) {
				buffer.flip();
				dest.write(buffer);
				while (buffer.hasRemaining())
					dest.write(buffer);
				buffer.clear();
			}
		} catch (IOException e) {
			System.err.println(e);

		}

		Utility.closeAllNIO(src, dest);
	}

	public static void copy_transferTo(File input, File output) {
		FileChannel src = Utility.initSourceNIO(input);
		FileChannel dest = Utility.initDestNIO(output);

		try {
			src.transferTo(0, src.size(), dest);
		} catch (IOException e) {
			System.err.println("Eccezione: transferTo(). " + e);
		} 

		Utility.closeAllNIO(src, dest);

	}

	public static void copy_bufferedStream(File input, File output) {
		
		InputStream src = null;
		try {
			src = new BufferedInputStream(new FileInputStream(input));
		} catch (FileNotFoundException e) {
			System.err.println("File di input non trovato");
		}
		
		OutputStream dest = null;
		try {
			dest = new BufferedOutputStream(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			System.err.println("File di output non trovato");
		}
		int i;
		try {
			while ((i = src.read()) != -1) 
				dest.write(i); 
			
		} catch (IOException e) {
			System.err.println(e);
		}

		Utility.closeAllIO(src, dest);
	}

	public static void copy_byteArrayStream(File input, File output) {
		
		InputStream src = null;
		try {
			src = new FileInputStream(input);
		} catch (FileNotFoundException e) {
			System.err.println("File di input non trovato");
		}
		
		OutputStream dest = null;
		try {
			dest = new FileOutputStream(output);
		} catch (FileNotFoundException e) {
			System.err.println("File di output non trovato");
		}

		byte[] buffer = new byte[1024];
		int i;
		try {
			while ((i = src.read(buffer)) != -1) {
				dest.write(buffer, 0, i); 
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		Utility.closeAllIO(src, dest);

	}
}
