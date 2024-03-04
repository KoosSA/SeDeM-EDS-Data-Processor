package koossa.sedem.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("SeDeM EDS Calculator");
		
		
		VBox cec = FXMLLoader.load(getClass().getResource("graphics/mainApplicationWindow.fxml"));
//		VBox cec = FXMLLoader.load(getClass().getResource("graphics/sedemParameterView.fxml"));
		Scene correctiveExcipientScene = new Scene(cec);
		
		stage.setScene(correctiveExcipientScene);
		
		stage.show();
		
//		VBox cec = FXMLLoader.load(getClass().getResource("correctiveExcipientCalculation.fxml"));
//		Scene correctiveExcipientScene = new Scene(cec);
//		Stage s1 = new Stage();
//		s1.setTitle("Corrective Excipient Calculator");
//		s1.setScene(correctiveExcipientScene);
//		s1.show();
	}

}
