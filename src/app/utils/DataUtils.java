package app.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DataUtils {
	
	public static int getDataAmount(Component parent) {
		String result = JOptionPane.showInputDialog(parent, "Please enter the amount of tests to average: ");
		try {
			int size = Integer.parseInt(result);
			return size;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(parent, "The data amount entered is not valid. Please try again.");
		}
		return 0;
	}
	
//	public static int getDataAmount() {
//		String result = JOptionPane.showInputDialog(null, "Please enter the amount of tests to average: ");
//		try {
//			int size = Integer.parseInt(result);
//			return size;
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(null, "The data amount entered is not valid. Please try again.");
//		}
//		return 0;
//	}

}
