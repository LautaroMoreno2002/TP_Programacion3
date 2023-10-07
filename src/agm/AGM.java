package agm;

import java.util.ArrayList;
import java.util.HashSet;

import modelo.Grafo;

public class AGM {
	private static ArrayList<Integer> _verticesAGM;
	private static ArrayList<Arista> _aristasAGM;

	/*
	 	VT := {u} (u cualquier vertice de G)
		ET := ∅
		i := 1
		mientras i ≤ n − 1 hacer
			elegir e = (u, v) ∈ E tal que l(e) sea mınima
			entre las aristas que tienen un extremo
			u ∈ VT y el otro v ∈ V \ VT
			ET := ET ∪ {e}
			VT := VT ∪ {v}
			i := i + 1
		retornar T = (VT , ET)
	 */
	
	public static Grafo prim(Grafo grafoCompleto) {
		if (grafoCompleto.cantidadVertices()<2) return grafoCompleto;
		
		_verticesAGM = new ArrayList<Integer>();
		_aristasAGM = new ArrayList<Arista>();
		_verticesAGM.add(0);
		int iteraciones = 1;
		while (iteraciones <= grafoCompleto.cantidadVertices()-1) {
			Arista e = aristaConPesoMinimo(grafoCompleto,_verticesAGM);
			System.out.println("Arista:" + e.getDesde() + " " +e.getHasta()+ " "+ e.getPesoEntreAmbos() + "\n");
			_aristasAGM.add(e); // le pasa la arista (x,y) con el peso entre ambas
			System.out.println("aristasAGM: "+_aristasAGM.toString());
			_verticesAGM.add(e.getHasta());
			System.out.println("verticesAGM: "+_verticesAGM.toString());
			iteraciones++;
		}
		Grafo agm = construirGrafo(_verticesAGM, _aristasAGM);
		esConexo(agm); // Valida si el grafo es conexo
		return agm; // retorna un nuevo grafo
	}
	
	private static void esConexo(Grafo gr) {
		if (gr == null) throw new IllegalArgumentException("No se puede hacer un agm de un grafo nulo.");
		if (!BFS.esConexo(gr)) throw new IllegalArgumentException("Un grafo debe ser conexo para ser un árbol.");
	}
	
	private static Arista aristaConPesoMinimo(Grafo grafoCompleto, ArrayList<Integer> verticesAGM) {
		Arista arista = new Arista(0,0,20);
		for (int vertice : verticesAGM) {
			Arista candidatoArista = conseguirAristaMinimaEntreLosVecinosDelVertice(vertice,grafoCompleto,verticesAGM);
			if (candidatoArista.getPesoEntreAmbos()<arista.getPesoEntreAmbos()) {
				arista.setDesde(vertice);
				arista.setHasta(candidatoArista.getHasta());
				arista.setPesoEntreAmbos(candidatoArista.getPesoEntreAmbos());
			}
			System.out.println("\nDentro del for:" + "Arista:" + arista.getDesde() + " " +arista.getHasta()+ " "+ arista.getPesoEntreAmbos() );
		}
		return arista;
	}
	
	private static Arista conseguirAristaMinimaEntreLosVecinosDelVertice(int vertice, Grafo grafoCompleto, ArrayList<Integer> verticesAGM) {
		Arista candidatoArista = new Arista(vertice,vertice,20);
		HashSet<Integer> vecinosDelVertice = grafoCompleto.vecinosDelVertice(vertice);
		for (int vecino : vecinosDelVertice) {
			if (!verticesAGM.contains(vecino) && grafoCompleto.pesoEntreDosVecinos(vertice, vecino) < candidatoArista.getPesoEntreAmbos()) {
				candidatoArista.setHasta(vecino);
				candidatoArista.setPesoEntreAmbos(grafoCompleto.pesoEntreDosVecinos(vertice, vecino));
			}
		}
		return candidatoArista;
	}
	
	private static Grafo construirGrafo(ArrayList<Integer> _verticesAGM, ArrayList<Arista> _aristasAGM) {
        Grafo gAux = new Grafo(_verticesAGM.size());
        for (Arista arista: _aristasAGM)
            gAux.agregarArista(arista.getDesde(), arista.getHasta(), arista.getPesoEntreAmbos());
        return gAux;
    }
}
