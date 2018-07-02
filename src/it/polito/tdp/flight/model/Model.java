package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.CoppiaAirportId;
import it.polito.tdp.flight.db.FlightDAO;

public class Model {
	
	
	private FlightDAO dao ;
	private SimpleDirectedWeightedGraph <Airport,DefaultWeightedEdge> grafo ;
	private List<Airport> allAirports;
	private List<Route> allRoutes;
	private Map<Integer,Airport> airportIdMap;
	
	
	public Model() {
		this.dao = new FlightDAO();
		this.allAirports = dao.getAllAirports();
		this.allRoutes = dao.getAllRoutes();
		
		this.airportIdMap= new HashMap<>();
		for(Airport a : allAirports) {
			this.airportIdMap.put(a.getAirportId(), a);
		}
	}
	
	public void creaGrafo(int distanzaInt) {
		
		this.dao = new FlightDAO();
		this.grafo = new SimpleDirectedWeightedGraph <Airport,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Inserisco i vertici : aeroporti
		this.allAirports = dao.getAllAirports();
		this.airportIdMap= new HashMap<>();
		
		for(Airport a : allAirports) {
			airportIdMap.put(a.getAirportId(), a);
		}
		
		Graphs.addAllVertices(this.grafo, this.allAirports);
		
		//System.out.println("Creato grafo con "+this.grafo.vertexSet().size()+" vertici.\n");
		
		//Inserisco gli archi : un arco collega 2 aeroporti solo se la loro distanza è inferiore a quella selezionata
		// e almeno una compagnia compie tale rotta
		
		for(Route r : this.allRoutes ) {
			
			Airport sourceA = this.airportIdMap.get(r.getSourceAirportId());
			Airport destinationA = this.airportIdMap.get(r.getDestinationAirportId());
			
			if(!sourceA.equals(destinationA)) {
				
				double distanza = LatLngTool.distance(new LatLng(sourceA.getLatitude(), 
						sourceA.getLongitude()), new LatLng(destinationA.getLatitude(), 
								destinationA.getLongitude()), LengthUnit.KILOMETER);
				
				if(distanza < distanzaInt) {
					
					if(esisteRotta(sourceA,destinationA)) {
						
						double weight =  distanza / 50 ;
						
						Graphs.addEdge(this.grafo, sourceA, destinationA, weight);
						
					}
					
				}
				
			}
			
			
		}
	
		
		
	
	/*	List<CoppiaAirportId> archi = dao.getAirportsConnected(distanzaInt);
		
		for(CoppiaAirportId cai : archi) {
			
			if(cai.getAirportiId1()!= cai.getAirportId2()) {
				
			//	if(esisteRotta(cai.getAirportiId1(),cai.getAirportId2())) {
				double durata = 800 * distanza(cai.getAirportiId1(),cai.getAirportId2());
				
					//this.grafo.addEdge(this.airportIdMap.get(cai.getAirportiId1()), this.airportIdMap.get(cai.getAirportId2()));
					
					Graphs.addEdge(this.grafo,this.airportIdMap.get(cai.getAirportiId1()), this.airportIdMap.get(cai.getAirportId2()), durata);
			//}
		}
	}*/
		
		System.out.print("Creato grafo con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi.\n");
	///	System.out.println(this.grafo.edgeSet().toString());
	//	for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
		//	System.out.println("Peso dell'arco "+e+" :"+this.grafo.getEdgeWeight(e));
		}
	

	
	public void printStats() {
		
		//Se ci.connectedSets().size è maggiore di 1 significa che non tutti gli aeroporti sono collegati.
		ConnectivityInspector <Airport,DefaultWeightedEdge> ci = new ConnectivityInspector<Airport,DefaultWeightedEdge>(this.grafo);
		
		System.out.println(ci.connectedSets());
		
	}

	public List<Airport> getAirportFromFiumicino() {
		
		List<Airport> vicini =new ArrayList<Airport>();
		int fiumicinoIdA = dao.getFiumicinoAirport();
		
		Airport fiumicino = this.airportIdMap.get(fiumicinoIdA);
		
		if(fiumicino!=null) {
			
			vicini = Graphs.neighborListOf(this.grafo, fiumicino);
			
			//ConnectivityInspector <Airport,DefaultWeightedEdge> ci = new ConnectivityInspector<Airport,DefaultWeightedEdge>(this.grafo);
			
		//	a = ci.connectedSetOf(fiumicino);
			
			//System.out.println("Aeroporti connessi a fiumicino "+a);
			
			
		}
		return vicini;
	}
	
	public Airport getAeroportoRaggDaFiumicino() {
		
		/*int fiumicinoIdA=dao.getFiumicinoAirport();
		Airport fiumicino = this.airportIdMap.get(fiumicinoIdA);
		
		//System.out.println(fiumicinoIdA);
		
		DepthFirstIterator<Airport,DefaultWeightedEdge> bfv = new DepthFirstIterator<Airport, DefaultWeightedEdge>(this.grafo,fiumicino);
		
		List<Airport> ragg = new ArrayList<Airport>();
		
		bfv.next();
		
		while(bfv.hasNext()) {
			ragg.add(bfv.next());
		}
		*/
		
		
		return null;

	}

	private boolean esisteRotta(Airport sourceA, Airport destinationA) {

		this.dao = new FlightDAO();
		
		int result = dao.getRottaDaAeroporti(sourceA.getAirportId(), destinationA.getAirportId());
		
		if(result>0) {
			return true;
		}
		return false;
	}

	public int getVertexSize() {
	
		if(this.grafo!=null)
		return this.grafo.vertexSet().size();
		
		return 0;
		
	}

	public int getEdgesSize() {
	
		if(this.grafo!= null)
		return this.grafo.edgeSet().size();
		
		return 0;
	}
	
	

	//CALCOLARE DISTANZA TRA DUE AEROPORTI
	//double weight = LatLngTool.distance(new LatLng(a1.getLatitude(), 
		//	a1.getLongitude()), new LatLng(a2.getLatitude(), 
			//	a2.getLongitude()), LengthUnit.KILOMETER);
	
	
	
		
		
	}
	
	

