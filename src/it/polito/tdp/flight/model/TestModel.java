package it.polito.tdp.flight.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo(50);
		
		model.printStats();
		
		System.out.print(model.getAirportFromFiumicino());
		
		
		
	}

}
