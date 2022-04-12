package practica;

import java.util.Random;
import java.util.Hashtable;

public class Proceso {
	static final int[] tamanios = {64, 128, 256, 512};
	
	public int insAsignadas;
	public int insEjecutadas;
	public int tamanio;
	public String nombre;
	public int id;
	public int numPaginas;	// Numero de paginas asignadas al proceso
	public int [] paginas;	// Tabla de paginas
	/*
	 * 
	 */
	
	public Proceso(String nombre, int id) {
		Random randomObj = new Random();
		this.insAsignadas = randomObj.nextInt(21) + 10;
		this.tamanio = tamanios[randomObj.nextInt(tamanios.length)];
		this.id = id;
		this.nombre = nombre;
		this.numPaginas = tamanio/16;	// 16 es el tamanio de pagina
		this.paginas = new int[numPaginas];	
	}
	
	public int EjecutarProceso() {
		this.insEjecutadas += 5;
		if (this.insEjecutadas >= this.insAsignadas) {
			this.insEjecutadas = this.insAsignadas;
			return 0;    // Si el proceso ha finalizado
		} else {
			return 1;   // Si se pudo ejecutar el proceso
		}
	}
	
	public void VerProceso() {
		System.out.println("   Proceso: " + this.id);
		System.out.println("   Nombre: " + this.nombre);
		System.out.println("   Instrucciones totales: " + this.insAsignadas);
		System.out.println("   Instrucciones ejecutadas: " + this.insEjecutadas);
		System.out.println("   Tamano: " + this.tamanio);
		VerTablaPaginas();
	}
	
	public void VerTablaPaginas() {
		System.out.println("Pagina \t Marco");
		for(int i = 0; i < numPaginas; i++) {
			System.out.println(i + "\t" + paginas[i]);
		}
	}
}
