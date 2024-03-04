package koossa.sedem.app.graphics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import koossa.sedem.app.sedemData.ExcipientSeDeMData;
import koossa.sedem.app.utils.FxFileUtil;

public class MainApplicationWindowController {
	
	@FXML
	TabPane tabPanel;
	ObservableList<Tab> tabs;
	Map<String, ExcipientSeDeMData> openExcipients;
	
	public MainApplicationWindowController() {
		openExcipients = new HashMap<String, ExcipientSeDeMData>();
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
	
	public void onSaveExcipient() {
		FxFileUtil.saveFile(tabPanel, new ExcipientSeDeMData());
	}

}
