package practica;

import java.util.Scanner;
import java.util.*;

import practica.Administrador;
import practica.LocMem.Nodo;

public class Sistema {
	static int idCont = 1;   // El id comienza en 1
	static Proceso procesoActual = null;
	
	public static void main(String[] args) {
		boolean menu = true;
		int op;
		while (menu) {
			System.out.println("\t SIMULADOR DE PROCESOS EN SISTEMA OPERATIVO");
			System.out.println("1) Crear proceso nuevo");
			System.out.println("2) Ver estado actual del sistema");
			System.out.println("3) Imprimir cola de procesos");
			System.out.println("4) Ver proceso actual");
			System.out.println("5) Ejecutar proceso actual");
			System.out.println("6) Pasar al proceso siguiente");
			System.out.println("7) Matar al proceso actual");
			System.out.println("8) Desfragmentar");
			System.out.println("9) Salir del programa");
			op = ControlExcepciones.IntInput();
			switch(op) {
				case 1:
					if (Sistema.CrearProceso() == 1) {
						System.out.println("-> Proceso creado");
						idCont++;
					} else {
						System.out.println("-> No se pudo crear al proceso por falta de memoria");
					}
					break;
					
				case 2:
					Sistema.EstadoActual();
					break;
					
				case 3:
					if (Administrador.ProcesosListos.size() > 0) {
						Administrador.ImprimeProcesosListos();
					} else {
						System.out.println("-> La cola de procesos esta vacia");
					}
					break;
					
				case 4:
					if (procesoActual != null) {
						System.out.println("\t PROCESO ACTUAL");
						procesoActual.VerProceso();
					} else {
						System.out.println("-> No hay proceso actual");
					}
					break;
					
				case 5:
					if (procesoActual != null) {
						Sistema.EjecutarProceso();
					} else {
						System.out.println("-> No hay proceso actual");
					}
					break;
					
				case 6:
					Proceso nuevoActual = SiguienteProcesoActual();
					if (nuevoActual != null) {
						if (nuevoActual != procesoActual){
							procesoActual = nuevoActual;
						} else {
							System.out.println("-> Solo hay un proceso en la lista de procesos");
						}
					} else {
						System.out.println("-> No se pudo realizar el cambio de procesos");
					}
					break;
					
				case 7:
					if (procesoActual != null) {
						System.out.println("-> El proceso ha sido terminado antes de tiempo");
						FinalizarProceso(procesoActual.id, Administrador.ProcesosFinalizadosNE);
					} else {
						System.out.println("-> No hay proceso actual");
					}
					break;
				case 8:
					if (Sistema.desfragmentar() == 1) {
						System.out.println("-> Desfragmentacion exitosa");
						Administrador.LLlocMem.VerLista();
					} else {
						System.out.println("-> No fue posible desfragmentar");
					}
					break;
				case 9:
					Sistema.finMenu();
					menu = false;
					break;
				default:
					System.out.println("Opcion invalida");
					break;
							
			}
			System.out.println("Presione cualquier tecla para continuar");
			try {
				System.in.read();
				System.out.print("\033[H\033[2J");
				System.out.flush();
			} catch (Exception e) {
				
			}
		}
	}
	
	public static void finMenu() {
		System.out.println("-> Numero de procesos listos: " + Administrador.ProcesosListos.size());
		if (Administrador.ProcesosListos.size() > 0) {
			System.out.println("\t LISTA DE PROCESOS LISTOS");
			Administrador.ImprimeProcesosListos();
		}
		System.out.println("-> Numero de procesos finalizados con exito: " + Administrador.ProcesosFinalizadosE.size());
		if (Administrador.ProcesosFinalizadosE.size() > 0) {
			System.out.println("\t LISTA DE PROCESOS FINALIZADOS CON EXITO");
			Administrador.ImprimeProcesosFinalizadosE();
		}
		
		System.out.println("-> Numero de procesos finalizados sin exito: " + Administrador.ProcesosFinalizadosNE.size());
		if (Administrador.ProcesosFinalizadosNE.size() > 0) {
			System.out.println("\t LISTA DE PROCESOS FINALIZADOS SIN EXITO");
			Administrador.ImprimeProcesosFinalizadosNE();
		}
	}
	
	public static void EjecutarProceso() {
		if (procesoActual.EjecutarProceso() == 1) {
			// El proceso concluye
			System.out.println("-> El proceso se ejecuto con exito: 5 instrucciones ejecutadas");
			procesoActual = Sistema.SiguienteProcesoActual();
		} else {
			System.out.println("-> El proceso ha concluido con su ejecucion");
			FinalizarProceso(procesoActual.id, Administrador.ProcesosFinalizadosE);
		}
	}
	
	public static void FinalizarProceso(int idProceso, LinkedList<Proceso> listaProcesosFinalizados) {
		Proceso procesoFinalizado = Administrador.ProcesosListos.remove(idProceso);
		listaProcesosFinalizados.add(procesoFinalizado);
		Administrador.ProcesosListosKeyset = Administrador.ProcesosListos.keySet();
		Administrador.SuprimirProcesoMemoria(procesoFinalizado);
		ActualizaListaLigadaRemove(procesoFinalizado);
		procesoActual = Sistema.ProcesoActual();
	}
	
	public static Proceso SiguienteProcesoActual() {
		if (Administrador.ProcesosListos.size() > 1) {
			int removerKey = procesoActual.id;
			Proceso removerProceso = Administrador.ProcesosListos.get(removerKey);
			
			Administrador.ProcesosListos.remove(removerKey);
			Administrador.ProcesosListos.put(removerKey, removerProceso);
			Administrador.ProcesosListosKeyset = Administrador.ProcesosListos.keySet();
			
			return ProcesoActual();
		} if (Administrador.ProcesosListos.size() == 1){
			return ProcesoActual();
		}
		return null;
	}
	
	public static void EstadoActual() {
		System.out.println("-> Numero de procesos listos: " + Administrador.ProcesosListos.size());
		if (Administrador.ProcesosListos.size() > 0) {
			System.out.println("\t LISTA DE PROCESOS LISTOS");
			Administrador.ImprimeProcesosListos();
		}
		System.out.println("-> Numero de procesos finalizados con exito: " + Administrador.ProcesosFinalizadosE.size());
		if (Administrador.ProcesosFinalizadosE.size() > 0) {
			System.out.println("\t LISTA DE PROCESOS FINALIZADOS CON EXITO");
			Administrador.ImprimeProcesosFinalizadosE();
		}
		
		System.out.println("-> Numero de procesos finalizados sin exito: " + Administrador.ProcesosFinalizadosNE.size());
		if (Administrador.ProcesosFinalizadosNE.size() > 0) {
			System.out.println("\t LISTA DE PROCESOS FINALIZADOS SIN EXITO");
			Administrador.ImprimeProcesosFinalizadosNE();
		}
		
		System.out.println("\tLISTA LIGADA DE MEMORIA");
		Administrador.LLlocMem.VerLista();
		
	}
	
	public static Proceso ProcesoActual() {
		Proceso procesoActual;
		for (int key : Administrador.ProcesosListosKeyset) {
			procesoActual = Administrador.ProcesosListos.get(key);
			return procesoActual;
		}
		return null;
	}
	
	
	
	public static int CrearProceso() {
		System.out.println("Ingrese un nombre para el proceso: ");
		String nombre = ControlExcepciones.nameInput();
		Proceso procesoNuevo = new Proceso(nombre, idCont);
		int tamanioProceso = procesoNuevo.tamanio;
		
		if (Administrador.numPaginasLibres >= procesoNuevo.numPaginas) {
			// Si hay suficientes paginas
			ActualizaListaLigadaAdd(procesoNuevo);
			
			// insertar en memoria directamente
			Administrador.InsertarProcesoMemoria(procesoNuevo);
			
			// Insertar en cola de procesos
			Administrador.ProcesosListos.put(idCont, procesoNuevo);
			if (Administrador.ProcesosListos.size() == 1) {
				procesoActual = procesoNuevo;
			}
			return 1;
		} else {
			// Si no hay suficientes paginas
			return 0;
		}
	}
	
	public static void paginar(Proceso proceso, int localidadInicio, int numDePagina) {
		int marco = localidadInicio/16;	// Relacion entre marcos y localidades
		proceso.paginas[numDePagina] = marco;
	}
	
	
	public static void ActualizaListaLigadaRemove(Proceso proceso) {
		Nodo nodoActual = Administrador.LLlocMem.nodoInicio;	// Cabeza la lista ligada
		while(nodoActual != null) {
			if (nodoActual.idProcesoAsociado == proceso.id) {
				nodoActual.tipo = 'H';
				nodoActual.idProcesoAsociado = 0;
				Administrador.LLlocMem.numHuecos++;
				Administrador.LLlocMem.numProcesos--;	// Convierte a los P en H
			}
			nodoActual = nodoActual.SiguienteNodo;
		}
		unirHuecos();
	}
	
	public static boolean unirHuecos() {
		Nodo nodoActual = Administrador.LLlocMem.nodoInicio;
		Nodo nodoSiguiente = nodoActual.SiguienteNodo;
		boolean merge = false;
		boolean mergeAux = false;
		while(nodoSiguiente != null) {
			merge = false;
			if (nodoActual.tipo == 'H' && nodoSiguiente.tipo == 'H') {
				// mecanismo merge
				// [...]->[H]->[H]->[...]
				// [...]->[  HH  ]->[...]
				nodoActual.tamanio += nodoSiguiente.tamanio;
				nodoActual.SiguienteNodo = nodoSiguiente.SiguienteNodo;
				if (nodoSiguiente.SiguienteNodo != null) {
					nodoSiguiente.SiguienteNodo.AnteriorNodo = nodoActual;
				}
				Administrador.LLlocMem.numHuecos--;
				Administrador.LLlocMem.tamanio--;
				merge = true;
				mergeAux = true;
			} if (merge == false) {
				nodoActual = nodoActual.SiguienteNodo;
			}
			nodoSiguiente = nodoActual.SiguienteNodo;
		}
		return mergeAux;
	}
	
	public static boolean unirProcesos() {
		Nodo nodoActual = Administrador.LLlocMem.nodoInicio;
		Nodo nodoSiguiente = nodoActual.SiguienteNodo;
		boolean merge = false;
		boolean mergeAux = false;
		while(nodoSiguiente != null) {
			merge = false;
			if (nodoActual.tipo == 'P' && nodoSiguiente.tipo == 'P' && nodoActual.idProcesoAsociado == nodoSiguiente.idProcesoAsociado) {
				// mecanismo merge
				// [...]->[H]->[H]->[...]
				// [...]->[  HH  ]->[...]
				nodoActual.tamanio += nodoSiguiente.tamanio;
				nodoActual.SiguienteNodo = nodoSiguiente.SiguienteNodo;
				if (nodoSiguiente.SiguienteNodo != null) {
					nodoSiguiente.SiguienteNodo.AnteriorNodo = nodoActual;
				}
				Administrador.LLlocMem.numProcesos--;
				Administrador.LLlocMem.tamanio--;
				merge = true;
				mergeAux = true;
			} if (merge == false) {
				nodoActual = nodoActual.SiguienteNodo;
			}
			nodoSiguiente = nodoActual.SiguienteNodo;
		}
		return mergeAux;
	}
	
	public static void ActualizaListaLigadaAdd(Proceso proceso) {
		Nodo nodoActual = Administrador.LLlocMem.nodoInicio;
		boolean insertado = false;
		int numDePag = 0;
		int nodosExaminados = 0;
		int tamanioInsertar = proceso.tamanio;
		
		while (insertado == false) {
			if (nodoActual.tipo == 'H') {
				if (nodoActual.tamanio > tamanioInsertar) {
					Nodo nuevoNodo = new Nodo('P', nodoActual.locInicio, tamanioInsertar);
					nuevoNodo.SiguienteNodo = nodoActual;
					nuevoNodo.AnteriorNodo = nodoActual.AnteriorNodo;
					nuevoNodo.idProcesoAsociado = proceso.id;
					
					if (nodoActual.AnteriorNodo != null) {
						nodoActual.AnteriorNodo.SiguienteNodo = nuevoNodo;
					}
					
					if (nodosExaminados == 0) {
						Administrador.LLlocMem.nodoInicio = nuevoNodo;
					}
					
					int localidadInicio = nodoActual.locInicio;
					for (int i = 0; i<tamanioInsertar/16; i++) {
						//System.out.printf("%d %d %d\n", i, localidadInicio, numDePag);
						paginar(proceso, localidadInicio, numDePag);
						localidadInicio+=16;
						numDePag++;
					}
					
					nodoActual.AnteriorNodo = nuevoNodo;
					nodoActual.locInicio+=tamanioInsertar;
					nodoActual.tamanio-=tamanioInsertar;
					Administrador.LLlocMem.numProcesos++;
					Administrador.LLlocMem.tamanio++;
					
					insertado = true;
					
				} else {
					nodoActual.tipo = 'P';
					nodoActual.idProcesoAsociado = proceso.id;
					Administrador.LLlocMem.numHuecos--;
					Administrador.LLlocMem.numProcesos++;
					
					int localidadInicio = nodoActual.locInicio;
					for (int i = 0; i<nodoActual.tamanio/16; i++) {
						paginar(proceso, localidadInicio, numDePag);
						localidadInicio+=16;
						numDePag++;
					}
					
					if (nodoActual.tamanio == tamanioInsertar) {
						insertado = true;
					} else {
						insertado = false;
						tamanioInsertar-=nodoActual.tamanio;
					}
				}
			}
			nodosExaminados++;
			nodoActual = nodoActual.SiguienteNodo;
		}
	}
	
	public static int desfragmentar() {
	
		if (Administrador.LLlocMem.tamanio > 1) {	// Si tamanio = 1 -> existe un unico hueco o proceso
			Nodo nodoActual = Administrador.LLlocMem.nodoInicio;
			while(nodoActual != null) {
				if (nodoActual.tipo == 'H') {	// Si se encuentra un hueco
					Nodo nodoSiguiente = nodoActual.SiguienteNodo;
					if (nodoSiguiente != null) {	// Si el hueco no es el fin de la lista
						// [nodoActual]->[Siguiente]
						int tamanioHueco = nodoActual.tamanio;
						int locInicioHueco = nodoActual.locInicio;
						
						int idpNodoSiguiente = nodoSiguiente.idProcesoAsociado;
						int tamanioSiguiente = nodoSiguiente.tamanio;
						
						nodoActual.tipo = 'P';	// El hueco se convierte en P
						nodoActual.tamanio = tamanioSiguiente;	// Se le asigna al hueco el tamano de P siguiente
						nodoActual.idProcesoAsociado = idpNodoSiguiente;
						
						nodoSiguiente.tipo = 'H';	// El P se convierte en H
						nodoSiguiente.tamanio = tamanioHueco;	// Se le asigna el tamano del hueco
						nodoSiguiente.idProcesoAsociado = 0;
						nodoSiguiente.locInicio = locInicioHueco + tamanioSiguiente;
						 
						// Volver a paginar
						Proceso proceso = Administrador.ProcesosListos.get(idpNodoSiguiente);
						Administrador.SuprimirProcesoMemoria(proceso);
						paginarDesfragmentacion(proceso, nodoActual);
						Administrador.InsertarProcesoMemoria(proceso);
						
					}
				}
				if (unirHuecos() == false || unirProcesos() == false) {
					nodoActual = nodoActual.SiguienteNodo;
				}
				
			}
			return 1;
		} else {
			// No es posible desfragmentar
			return 0;
		}
	}
	
	public static void paginarDesfragmentacion(Proceso proceso, Nodo nodo) {
		int numPagsMarcadas = 0;
		int numPagsEnNodo = nodo.tamanio/16;	// Numero de paginas en el nodo en cuestion
		
		for (int i = 0; i < proceso.numPaginas; i++) {
			if (proceso.paginas[i] == -1) {
				numPagsMarcadas++;
			}
		}
		
		int localidadInicial = nodo.locInicio;
		if (numPagsMarcadas > 0) {	// Si existen paginas marcadas
			int pagInicial = proceso.numPaginas - numPagsMarcadas;
			for (int j = pagInicial; j < proceso.numPaginas; j++) {
				paginar(proceso,localidadInicial,j);
				localidadInicial+=16;
			}
			
		} else {
			for (int j = 0; j < proceso.numPaginas; j++) {
				if (j < numPagsEnNodo) {
					// paginar normal
					paginar(proceso,localidadInicial,j);
					localidadInicial+=16;
				} else {
					// paginar con -1
					paginar(proceso,-16,j);
				}
			}
		}
	}
}
