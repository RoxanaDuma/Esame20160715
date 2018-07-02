package it.polito.tdp.flight.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Route;

public class FlightDAO {

	public List<Airline> getAllAirlines() {
		String sql = "SELECT * FROM airline";
		List<Airline> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Airline(res.getInt("Airline_ID"), res.getString("Name"), res.getString("Alias"),
						res.getString("IATA"), res.getString("ICAO"), res.getString("Callsign"),
						res.getString("Country"), res.getString("Active")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Route> getAllRoutes() {
		String sql = "SELECT * FROM route";
		List<Route> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Route(res.getString("Airline"), res.getInt("Airline_ID"), res.getString("Source_airport"),
						res.getInt("Source_airport_ID"), res.getString("Destination_airport"),
						res.getInt("Destination_airport_ID"), res.getString("Codeshare"), res.getInt("Stops"),
						res.getString("Equipment")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Airport> getAllAirports() {
		String sql = "SELECT * FROM airport";
		List<Airport> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Airport(res.getInt("Airport_ID"), res.getString("name"), res.getString("city"),
						res.getString("country"), res.getString("IATA_FAA"), res.getString("ICAO"),
						res.getDouble("Latitude"), res.getDouble("Longitude"), res.getFloat("timezone"),
						res.getString("dst"), res.getString("tz")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public int getRottaDaAeroporti(int idAirport1, int idAirport2) {
		
		String sql ="select count(*) as c " + 
				"from route " + 
				"where Source_airport_ID = ? " + 
				"and Destination_airport_ID = ? " ;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idAirport1);
			st.setInt(2, idAirport2);
			
			
			ResultSet res = st.executeQuery();

			res.first();
			 
			int risultato = res.getInt("c");
			
			conn.close();
			return risultato;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}

//	public static void main(String args[]) {
//		FlightDAO dao = new FlightDAO();
//
//		List<Airline> airlines = dao.getAllAirlines();
//		System.out.println(airlines);
//
//		List<Airport> airports = dao.getAllAirports();
//		System.out.println(airports);
//
//		List<Route> routes = dao.getAllRoutes();
//		System.out.println(routes);
//	}

	
	public List<CoppiaAirportId> getAirportsConnected(int number){
		
		String sql ="select distinct Source_airport_ID as a1 ,Destination_airport_ID as a2 " + 
				"from route, airport ai1 ,airport ai2 " + 
				"where route.Source_airport_ID = ai1.Airport_ID " + 
				"and route.Destination_airport_ID = ai2.Airport_ID " + 
				"and ai1.Airport_ID<> ai2.Airport_ID " +
				"and " + 
				"acos(cos(ai1.Longitude * (PI()/180)) * " + 
				"cos(ai1.Latitude * (PI()/180)) * " + 
				"cos(ai2.Longitude * (PI()/180)) * " + 
				"cos(ai2.Latitude * (PI()/180)) " + 
				"+ " + 
				"cos(ai1.Longitude * (PI()/180)) * " + 
				"sin(ai1.Latitude * (PI()/180)) * " + 
				"cos(ai2.Longitude * (PI()/180)) * " + 
				"sin(ai2.Latitude * (PI()/180)) " + 
				"+ " + 
				"sin(ai1.Longitude * (PI()/180)) * " + 
				"sin(ai2.Longitude * (PI()/180)) " + 
				") * 6378 < ? " ;
		
		List<CoppiaAirportId> result = new ArrayList<CoppiaAirportId>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, number);
			
			
			ResultSet res = st.executeQuery();

			while(res.next()) {
				
				result.add(new CoppiaAirportId(res.getInt("a1"),res.getInt("a2")));
				
			}
		
			
			conn.close();
			return result;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		
	}

	public int getFiumicinoAirport() {
		
		String sql ="select Airport_ID as airportId " + 
				"from airport " + 
				"where Name='Allgau' " ;
		
		int airportId;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();

			res.first();
			
			airportId = res.getInt("airportId");
			
			conn.close();
			
			return airportId;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	
	}
	
	
}
