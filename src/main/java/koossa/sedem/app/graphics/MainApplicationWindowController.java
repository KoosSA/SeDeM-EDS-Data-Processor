package koossa.sedem.app.graphics;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import koossa.sedem.app.sedemData.ExcipientSeDeMData;
import koossa.sedem.app.utils.FxFileUtil;

public class MainApplicationWindowController {
	
	@FXML
	TabPane tabPanel;
	ObservableList<Tab> tabs;
	Map<String, ExcipientSeDeMData> openExcipients;
	Map<String, String> openPaths;
	
	public MainApplicationWindowController() {
		openExcipients = new HashMap<String, ExcipientSeDeMData>();
		openPaths = new HashMap<String, String>();
	}
	
	public void initialize() {
		tabs = tabPanel.getTabs();
	}
	
	private void createNewTab(String name) {
		Tab t = new Tab(name);
		tabs.add(t);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("sedemParameterView.fxml"));
		Node n;
		try {
			n = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		SedemParameterViewController controller = loader.getController();
		t.setContent(n);
		t.setUserData(controller);
		t.setStyle("-fx-background-colour: #444444ff;");
		t.getTabPane().setStyle("-fx-background-colour: #444444ff;");
		tabPanel.getStyleClass().add("floating");
		ExcipientSeDeMData data = openExcipients.getOrDefault(name, null);
		if (data != null) {
			controller.updateData(data);
		}
		t.setOnSelectionChanged(event -> {
			ExcipientSeDeMData dataToShow = openExcipients.getOrDefault(tabPanel.getSelectionModel().getSelectedItem().getText(), null);
			System.out.println(dataToShow.parameters);
			if (dataToShow != null) {
				((SedemParameterViewController)tabPanel.getSelectionModel().getSelectedItem().getUserData()).updateData(dataToShow);
			}
		});
		
	}
	
	public void onNewItem() {
		TextInputDialog tid = new TextInputDialog();
		tid.setHeaderText("Enter the excipient name:");
		Optional<String> input = tid.showAndWait();
		if (input.isPresent() && input.get().length() > 0) {
			if (openExcipients.putIfAbsent(input.get(), new ExcipientSeDeMData()) == null) {
				createNewTab(input.get());
			}
		}
		
	}
	
	public void onLoadItem() {
		ExcipientSeDeMData data = FxFileUtil.openFile(tabPanel, ExcipientSeDeMData.class);
		if (data == null) return;
		if (openExcipients.putIfAbsent(data.excipientName, data) == null) {
			createNewTab(data.excipientName);
		}
	}
	
	public void onOpenProject() {
		List<ExcipientSeDeMData> list = FxFileUtil.openMultiFiles(tabPanel, ExcipientSeDeMData.class);
		if (list == null) return;
		list.forEach(data -> {
			if (data == null) return;
			if (openExcipients.putIfAbsent(data.excipientName, data) == null) {
				createNewTab(data.excipientName);
			}
		});
	}
	
	public void onSaveExcipient() {
		try {
			FxFileUtil.saveFile(tabPanel, openExcipients.get(tabPanel.getSelectionModel().getSelectedItem().getText()));
		} catch (Exception e) {
			System.err.println("Cannot save excipient.");
		}
	}
	

}
