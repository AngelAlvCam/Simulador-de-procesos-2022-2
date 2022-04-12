package practica;

import java.util.*;
import practica.LocMem.*;

public class Administrador {
	static public LinkedHashMap<Integer, Proceso> ProcesosListos = new LinkedHashMap<Integer, Proceso>();
	static public Set<Integer> ProcesosListosKeyset = ProcesosListos.keySet();
	static public LinkedList<Proceso> ProcesosFinalizadosE = new LinkedList<Proceso>();
	static public LinkedList<Proceso> ProcesosFinalizadosNE = new LinkedList<Proceso>();
	static public int[] memoria = new int[2048];
	
	static public ListaLigadaNodos LLlocMem = new ListaLigadaNodos();
	static public int numPaginasLibres = 128;	// 2048/16 = 128
	
	static public void InsertarProcesoMemoria(Proceso proceso) {
		numPaginasLibres -= proceso.numPaginas;
		for (int i = 0; i < proceso.numPaginas; i++) {
			int locInicial = proceso.paginas[i]*16;
			if (locInicial >= 0) {
				for(int j = locInicial; j < locInicial + 16; j++) {
					memoria[j] = proceso.id;
				}
			}
		}
	}
	
	static public void SuprimirProcesoMemoria(Proceso proceso) {
		numPaginasLibres += proceso.numPaginas;
		for (int i = 0; i < proceso.numPaginas; i++) {
			int locInicial = proceso.paginas[i]*16;
			if (locInicial >= 0) {
				for(int j = locInicial; j < locInicial + 16; j++) {
					memoria[j] = 0;
				}
			}
		}
	}
	
	static public void ImprimeMemoria() {	// Imprime el estado de la memoria
		int localidad = -1;
		String nombreProceso = null;
		System.out.println("Localidad\t   Proceso");
		for (int i = 0; i < memoria.length; i++) {
			if (memoria[i] != 0) {
				localidad = i;
				nombreProceso = ProcesosListos.get(memoria[localidad]).nombre;
				System.out.println("\t" + localidad + "\t" + nombreProceso);
			}
		}
	}
	
	static public void ImprimeProcesosListos() {
		Proceso proceso;
		int insPendientes;
		System.out.println("Nombre \t ID \t Instrucciones pendientes");
		for (Integer i : ProcesosListosKeyset) {
			proceso = ProcesosListos.get(i);
			insPendientes = proceso.insAsignadas - proceso.insEjecutadas;
			System.out.println(proceso.nombre + "\t" + proceso.id + "\t" + insPendientes);
		}
	}
	
	static public void ImprimeProcesosFinalizadosE() {
		Proceso proceso;
		System.out.println("Nombre \t ID");
		for (int i = 0; i < ProcesosFinalizadosE.size(); i++) {
			proceso = ProcesosFinalizadosE.get(i);
			System.out.println(proceso.nombre + " \t " + proceso.id);
		}
	}
	
	static public void ImprimeProcesosFinalizadosNE() {
		Proceso proceso;
		int insPendientes;
		System.out.println("Nombre \t ID \t Instrucciones sin ejecutar");
		for (int i = 0; i < ProcesosFinalizadosNE.size(); i++) {
			proceso = ProcesosFinalizadosNE.get(i);
			insPendientes = proceso.insAsignadas - proceso.insEjecutadas;
			System.out.println(proceso.nombre + " \t " + proceso.id + " \t " + insPendientes);
		}
	}
}
