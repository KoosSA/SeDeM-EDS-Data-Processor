package koossa.sedem.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class FxFileUtil {

	
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static FileChooser fc = new FileChooser();
	private static ExtensionFilter jsonFilter = new ExtensionFilter("Json (*.json)", "*.json", "*.JSON");
	private static ExtensionFilter projectFilter = new ExtensionFilter("Project (*.edsProject)", "*.edsProject", "*.edsproject", "*.EDSPROJECT");
	
	static {
		fc.getExtensionFilters().add(jsonFilter);
		fc.getExtensionFilters().add(projectFilter);
	}

	public static void saveFile(Node node, Object obj) {
		fc.setSelectedExtensionFilter(jsonFilter);
		String data = gson.toJson(obj);
		File file = fc.showSaveDialog(node.getScene().getWindow());
		if(!file.getName().contains(".")) {
			file = new File(file.toString() + ".json");
		}
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T> T openFile(Node node, Class<T> type) {
		fc.setSelectedExtensionFilter(jsonFilter);
		File file = fc.showOpenDialog(node.getScene().getWindow());
		if (file == null) return null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			T obj = gson.fromJson(reader, type);
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static <T> List<T> openMultiFiles(Node node, Class<T> type) {
		fc.setSelectedExtensionFilter(jsonFilter);
		List<File> files = fc.showOpenMultipleDialog(node.getScene().getWindow());
		if (files == null || files.size() <= 0) return null;
		
		List<T> list = new ArrayList<T>();
		files.forEach(file -> {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			T obj = gson.fromJson(reader, type);
			list.add(obj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return list;
	}
	
	public static <T> T loadFile(String path, Class<T> type) {
		File file = new File(path);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			T obj = gson.fromJson(reader, type);
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}

}
