package app.utils;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUtil.
 */
public class FileUtil {

	/** The gson. */
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/** The json filter. */
	private static FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("Json (*.json)", "json");


	/**
	 * Save a object to file.
	 *
	 * @param parent the parent
	 * @param obj the Object to be saved
	 */
	public static void saveFile(Component parent, Object obj) {
		JFileChooser fc = new JFileChooser();
		String data = gson.toJson(obj);
        fc.addChoosableFileFilter(jsonFilter);
        fc.setFileFilter(jsonFilter);
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
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
	}

	/**
	 * Open file.
	 *
	 * @param <T> the generic type
	 * @param parent the parent
	 * @param type the type
	 * @return the object represented by T
	 */
	public static <T> T openFile(Component parent, Class<T> type) {
		JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(jsonFilter);
        fc.setFileFilter(jsonFilter);
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if(!file.getName().endsWith(".json")) {
				System.err.println("FileType error.");
			}
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				T obj = gson.fromJson(reader, type);
				return obj;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
	}

}
