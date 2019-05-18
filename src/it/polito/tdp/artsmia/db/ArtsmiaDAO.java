package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void popola(SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo) {
		// TODO Auto-generated method stub
		String sql = "SELECT o1.object_id id1, o2.object_id id2, COUNT(*) AS peso\n" + 
				"FROM exhibition_objects AS eo1, exhibition_objects AS eo2, exhibitions e, objects o1, objects o2 \n" + 
				"WHERE o1.object_id = eo1.object_id\n" + 
				"AND o2.object_id = eo2.object_id \n" + 
				"AND e.exhibition_id = eo1.exhibition_id  \n" + 
				"AND e.exhibition_id = eo2.exhibition_id \n" + 
				"AND o1.object_id > o2.object_id\n" + 
				"GROUP BY id1, id2 ";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject a1 = new ArtObject(res.getInt("id1"));
				ArtObject a2 = new ArtObject(res.getInt("id2"));
				
				if(!grafo.containsVertex(a1)) {
					grafo.addVertex(a1);
				}
				
				if(!grafo.containsVertex(a2)) {
					grafo.addVertex(a2);
				}
				
				if(!grafo.containsEdge(a1, a2) || !grafo.containsEdge(a2,a1)) {
					DefaultWeightedEdge edge = grafo.addEdge(a1, a2);
					grafo.setEdgeWeight(edge, res.getInt("peso")); // peso = quante volte 2 opere sono state esposte insieme
				}
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
