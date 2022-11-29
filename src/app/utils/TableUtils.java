/*
 *
 */
package app.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// TODO: Auto-generated Javadoc
/**
 * {@link TableUtils} provides methods for altering datatables quickly.
 * @author Koos
 *
 */
public class TableUtils {

	/**
	 * Gets all values in a table column.
	 *
	 * @param table - Table to get values from
	 * @param index - Specify the column to get from
	 * @return list - The values in the specified column
	 */
	public static List<Float> getAllValuesInColumn(JTable table, int index) {
		List<Float> list = new ArrayList<Float>();
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getValueAt(i, index) != null) {
				list.add((float) table.getValueAt(i, index));
			}
		}
		return list;
	}


	/**
	 * Resize the table.
	 *
	 * @param tableModel the table model
	 * @param count the new size
	 */
	public static void setDataCount(DefaultTableModel tableModel, int count) {
		for (int i = 0; i < count; i++) {
			tableModel.addRow(new Object[] {});
		}
	}


	/**
	 * Sets the values in specified column.
	 *
	 * @param table the table
	 * @param index the column index
	 * @param list the list of new values
	 */
	public static void setAllValuesInColumn(JTable table, int index, List<Float> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < table.getRowCount(); i++) {
				table.setValueAt(list.get(i), i, index);
			}
		}
	}

}
