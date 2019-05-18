package it.polito.tdp.artsmia.model;

import org.jgrapht.alg.*;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.*;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;

	public void creaGrafo() {
		
		if(grafo==null) { // !!
			ArtsmiaDAO dao = new ArtsmiaDAO();
			grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
			dao.popola(grafo);
		}
		
		
	}

	public boolean isDigit(String idinput) {
		return idinput.matches("\\d+"); // se string \\w

	}

	public String calcolaComponente(String idinput) {
		if(grafo==null) {
			creaGrafo();
		}
		String res = "";
		
		for(ArtObject ao : grafo.vertexSet()) {
			res+=ao.toString()+" "+grafo.degreeOf(ao)+"\n";
		}
		
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(grafo);
		res+=inspector.connectedSets().size();
		
		return res;
		
	}

}
