package koossa.sedem.app.graphics;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import koossa.sedem.app.sedemData.ExcipientSeDeMData;
import koossa.sedem.app.utils.FxFileUtil;

public class MainApplicationWindowController {
	
	@FXML
	TabPane tabPanel;
	ObservableList<Tab> tabs;
	
	public void initialize() {
		tabs = tabPanel.getTabs();
	}
	
	private void createNewTab(String name) {
		Tab t = new Tab(name);
		tabs.add(t);
		try {
			t.setContent(FXMLLoader.load(getClass().getResource("sedemParameterView.fxml")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onNewItem() {
		TextInputDialog tid = new TextInputDialog();
		tid.setHeaderText("Enter the excipient name:");
		Optional<String> input = tid.showAndWait();
		if (input.isPresent() && input.get().length() > 0) {
			createNewTab(input.get());
		}
	}
	
	public void onLoadItem() {
		ExcipientSeDeMData data = FxFileUtil.openFile(tabPanel, ExcipientSeDeMData.class);
		createNewTab(data.excipientName);
		SedemParameterViewController.updateData(data);
	}
	
	public void onSaveExcipient() {
		FxFileUtil.saveFile(tabPanel, new ExcipientSeDeMData());
	}

}
