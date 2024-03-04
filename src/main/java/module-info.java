module module_name {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.base;
	requires java.desktop;
	requires com.google.gson;
	requires org.jfree.jfreechart;
	requires javafx.swing;
	
	exports app;
	exports koossa.sedem.app;
	exports koossa.sedem.app.graphics;
	exports koossa.sedem.app.utils;
	exports koossa.sedem.app.sedemData;
	
	opens koossa.sedem.app to javafx.fxml, javafx.graphics, javafx.swing;
	opens koossa.sedem.app.graphics to javafx.fxml, javafx.graphics, javafx.swing;
}