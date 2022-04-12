
package practica.LocMem;

/*
 * 	Esta clase refiere a un nodo de la lista ligada de memoria
 */
public class Nodo {
	public char tipo;
	public int locInicio;
	public int tamanio;
	public Nodo SiguienteNodo;
	public Nodo AnteriorNodo;
	public int idProcesoAsociado;
	
	public Nodo() {	// Nodo inicial
		tipo = 'H';
		locInicio = 0;
		tamanio = 2048;
		SiguienteNodo = null;
		AnteriorNodo = null;
		idProcesoAsociado = 0;
	}
	
	public Nodo(char tipo, int locInicio, int tamanio) {
		this.tipo = tipo;
		this.locInicio = locInicio;
		this.tamanio = tamanio;
		SiguienteNodo = null;
		AnteriorNodo = null;
	}
	
	public void verNodo() {
		System.out.printf("[Tipo: %c, Loc: %d, Tam: %d, idP: %d]\n", tipo,locInicio,tamanio,idProcesoAsociado);
	}
}