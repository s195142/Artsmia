package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> graph;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
	}
	
	
	public void creaGrafo() {
		
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		//aggiungo vertici
		Graphs.addAllVertices(graph, idMap.values()); // se ho lista passo direttamente lista al posto di valori mappa
		
		//aggiungo archi
		List<Adiacenza> adj = dao.listAdiacenze();
		
		for(Adiacenza a : adj) {
			// per ogni coppia ho già il peso.
			//qui mi serve l idmap xk io nelle adiacenze ho salvato l'id
			ArtObject source = idMap.get(a.getO1()); // no controlli, sono sicuro che ci sia l oggetto
			ArtObject dest = idMap.get(a.getO2());
			try {
				Graphs.addEdge(graph, source, dest, a.getPeso());
			}catch(Throwable t) {}
		}
		
		System.out.println("Grafo creato! " + graph.vertexSet().size()+ " vertici e "+ graph.edgeSet().size()+ " archi");
	}


	public int getVertexSize() {
		return graph.vertexSet().size();
	}


	public int getEdgeSize() {
		return graph.edgeSet().size();
	}


	public boolean isDigit(String id) {
		return id.matches("\\d");
	}


	public String doComponente(String id) {
		String res = "";
		this.creaGrafo();
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(graph);
		res+=inspector.connectedSetOf(id);
		
		return res;
	}
	
	
	
	
	
}
