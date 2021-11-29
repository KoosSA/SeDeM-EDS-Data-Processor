package app.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableUtils {
	
	public static List<Float> getAllValuesInColumn(JTable table, int index) {
		List<Float> list = new ArrayList<Float>();
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getValueAt(i, index) != null) {
				list.add((float) table.getValueAt(i, index));
			}
		}
		return list;
	}
	
	public static void setDataCount(DefaultTableModel tableModel, int count) {
		for (int i = 0; i < count; i++) {
			tableModel.addRow(new Object[] {});
		}
	}
	
	public static void setAllValuesInColumn(JTable table, int index, List<Float> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < table.getRowCount(); i++) {
				table.setValueAt(list.get(i), i, index);
			}
		}
	}

}
