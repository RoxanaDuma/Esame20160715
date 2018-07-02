package it.polito.tdp.flight;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtDistanzaInput;

	@FXML
	private TextField txtPasseggeriInput;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCreaGrafo(ActionEvent event) {
		
		txtResult.clear();
		
		String distanzaMax = txtDistanzaInput.getText();
		
		//Provo a convertire il numero in intero
		try {
			 int distanzaInt = Integer.parseInt(distanzaMax);
			 
			 //Costruisco il grafo
			 model.creaGrafo(distanzaInt);
			 txtResult.appendText("Creato grafo con "+ model.getVertexSize()+" vertici e "+model.getEdgesSize()+" archi.\n");
			 
		}catch(RuntimeException e) {
			txtResult.appendText("Errore connessione al DB.\n");
			return;
		}
		
	}

	@FXML
	void doSimula(ActionEvent event) {
		
	}

	@FXML
	void initialize() {
		assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Untitled'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
