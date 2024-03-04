package koossa.sedem.app.graphics;


import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class CorrectiveExcipientController {
	
	@FXML
	ChoiceBox<?> excipient1;
	@FXML
	ChoiceBox<?> excipient2;
	@FXML
	TextArea text;
	
	public void initialize() {
		
	}
	
	public void onCancel() {
		text.setText("Cancel pressed");
	}
	
	public void saveCalculation() {
		text.setText("Save pressed /r /n hallo daar");
	}
	

}
