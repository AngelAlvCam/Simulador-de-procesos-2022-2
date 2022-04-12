
package practica.LocMem;

public class ListaLigadaNodos {
	public Nodo nodoInicio;
	public int tamanio;
	public int numProcesos;
	public int numHuecos;
	
	public ListaLigadaNodos() {
		nodoInicio = new Nodo();
		tamanio = 1;
		numProcesos = 0;
		numHuecos = 1;
	}
	
	public void VerLista() {
		System.out.println("Lista ligada de nodos");
		System.out.printf("Num nodos de procesos: %d\n", numProcesos);
		System.out.printf("Num nodos de huecos: %d\n", numHuecos);
		System.out.printf("Num de nodos totales: %d\n", tamanio);
		Nodo nodoActual = this.nodoInicio;
		while(nodoActual != null) {
			System.out.printf("[%c, %d, %d, IDP: %d]\n", nodoActual.tipo, 
					nodoActual.locInicio, nodoActual.tamanio, nodoActual.idProcesoAsociado);
			nodoActual = nodoActual.SiguienteNodo;
		}
	}
}
