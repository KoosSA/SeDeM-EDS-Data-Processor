package app.sedem.parameters.homogeneity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import app.graphical.MainFrame;
import app.utils.FileUtil;
import app.utils.TableUtils;

public class HomogenityInput extends JFrame {
	
	private static final long serialVersionUID = -5710938278410004076L;
	private HomogenityInput instance;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextArea textArea;
	private final int COL_NUM_RANGE_HIGH = 0;
	private final int COL_NUM_RANGE_LOW = 1;
	private final int COL_NUM_RANGE_MEAN = 2;
	private final int COL_NUM_PERCENT_RETAINED = 3;
	public static float Fm, dm, deltaFm, homogenityIndex;
	private HomogenityData data;
	private MainFrame main;

	public HomogenityInput(MainFrame mainFrame) {
		this.main = mainFrame;
		setType(Type.UTILITY);
		instance = this;
		getContentPane().setLayout(new BorderLayout(0, 0));
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setTitle("Homogeneity Index Calculator");
		
		JPanel panel_bottom = new JPanel();
		panel_bottom.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_bottom.setBackground(Color.GRAY);
		getContentPane().add(panel_bottom, BorderLayout.SOUTH);
		FlowLayout fl_panel_bottom = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panel_bottom.setLayout(fl_panel_bottom);
		
		JButton btn_clearAll = new JButton("Clear All");
		panel_bottom.add(btn_clearAll);
		
		JButton btn_addData = new JButton("Add Data");
		
		panel_bottom.add(btn_addData);
		
		JButton btn_saveTemplate = new JButton("Save Template");
		panel_bottom.add(btn_saveTemplate);
		
		JButton btn_loadTemplate = new JButton("Load Template");
		panel_bottom.add(btn_loadTemplate);
		
		JButton btn_calcMean = new JButton("Calculate Mean Ranges");
		panel_bottom.add(btn_calcMean);
		
		JButton btn_calcHomogeniety = new JButton("Calculate Homogenity");
		panel_bottom.add(btn_calcHomogeniety);
		
		/*JButton btn_loadData = new JButton("Load Data");
		panel_bottom.add(btn_loadData);*/
		
		JButton btn_saveData = new JButton("Save Data");
		
		panel_bottom.add(btn_saveData);
		
		JPanel panel_center = new JPanel();
		getContentPane().add(panel_center, BorderLayout.CENTER);
		panel_center.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.BLACK));
		//panel_center.add(textArea, BorderLayout.CENTER);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_center.add(scrollPane_1, BorderLayout.CENTER);
		scrollPane_1.setViewportView(textArea);
		
		JPanel panel_left = new JPanel();
		panel_left.setBackground(Color.GRAY);
		getContentPane().add(panel_left, BorderLayout.WEST);
		panel_left.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_left.add(scrollPane);
		
		table = new JTable();
		table.setRowSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, Color.BLACK));
		table.setBackground(Color.LIGHT_GRAY);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Upper Range", "Lower Range", "Mean Range", "% Retained"
			}
		) {
			private static final long serialVersionUID = 5867470767042593706L;
			Class<?>[] columnTypes = new Class[] {
				Float.class, Float.class, Float.class, Float.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
		tableModel = (DefaultTableModel) table.getModel();
		btn_addData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane.showInputDialog(instance, "Please enter the number of data ranges: ");
				try {
					int size = Integer.parseInt(result);
					TableUtils.setDataCount(tableModel, size);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(instance, "The data amount entered is not valid. Please try again.");
				}
			}
		});
		
		btn_saveTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomogenityTemplate template = new HomogenityTemplate();
				template.DATA_SIZE = table.getRowCount();
				template.RANGES_UPPER = TableUtils.getAllValuesInColumn(table, COL_NUM_RANGE_HIGH);
				template.RANGES_LOWER = TableUtils.getAllValuesInColumn(table, COL_NUM_RANGE_LOW);
				template.RANGES_MEAN = TableUtils.getAllValuesInColumn(table, COL_NUM_RANGE_MEAN);
				FileUtil.saveFile(instance, template);
				log("Template saved.");
			}
		});
		
		btn_loadTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomogenityTemplate template = FileUtil.openFile(instance, HomogenityTemplate.class);
				if (template == null) {
					return;
				}
				clearAllData(false);
				TableUtils.setDataCount(tableModel, template.DATA_SIZE);
				TableUtils.setAllValuesInColumn(table, COL_NUM_RANGE_HIGH, template.RANGES_UPPER);
				TableUtils.setAllValuesInColumn(table, COL_NUM_RANGE_LOW, template.RANGES_LOWER);
				TableUtils.setAllValuesInColumn(table, COL_NUM_RANGE_MEAN, template.RANGES_MEAN);
				log("Template loaded.");
			}

		});
		
		btn_calcMean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateMeanRanges(COL_NUM_RANGE_MEAN);
				log("Mean ranges calculated.");
			}
		});
		
		btn_clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllData(false);
			}
		});
		
		btn_calcHomogeniety.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				
				doCalculation();
			}
		});
		
		btn_saveData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//data = new HomogenityData();
				data.template.DATA_SIZE = table.getRowCount();
				data.template.RANGES_UPPER = TableUtils.getAllValuesInColumn(table, COL_NUM_RANGE_HIGH);
				data.template.RANGES_LOWER = TableUtils.getAllValuesInColumn(table, COL_NUM_RANGE_LOW);
				data.template.RANGES_MEAN = TableUtils.getAllValuesInColumn(table, COL_NUM_RANGE_MEAN);
				data.PERCENTAGES_RETAINED = TableUtils.getAllValuesInColumn(table, COL_NUM_PERCENT_RETAINED);
				data.smallerThan45 = getSmallerThan(45);
				main.setHomogenityData();
				setVisible(false);
			}
		});
		
		/**btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data = FileUtil.openFile(HomogenityData.class);
				if (data == null) {
					return;
				}
				clearAllData(true);
				TableUtils.setDataCount(tableModel, data.template.DATA_SIZE);
				setAllValuesInColumn(COL_NUM_RANGE_HIGH, data.template.RANGES_UPPER);
				setAllValuesInColumn(COL_NUM_RANGE_LOW, data.template.RANGES_LOWER);
				setAllValuesInColumn(COL_NUM_RANGE_MEAN, data.template.RANGES_MEAN);
				setAllValuesInColumn(COL_NUM_PERCENT_RETAINED, data.PERCENTAGES_RETAINED);
				log("Data loaded.");
				doCalculation();
			}
		});**/
		
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		pack();

	}
	
	private float getSmallerThan(float value) {
		float v = 0;
		for (int i = 0; i < data.PERCENTAGES_RETAINED.size(); i++) {
			if (data.template.RANGES_MEAN.get(i) < value) {
				v += data.PERCENTAGES_RETAINED.get(i);
			}
		}
		return v;
	}
	
	private void calculateMeanRanges(int columnMeans) {
		int numRows = table.getRowCount();
		float value = 0;
		for (int r = 0; r < numRows; r++) {
			value = ((float) table.getValueAt(r, 0) + (float) table.getValueAt(r, 1)) / 2;
			table.setValueAt(value, r, columnMeans);
		}
	}
	
	private void log(String text) {
		textArea.append("  >> " + text + "\n");
	}
	
	private void clearAllData(boolean skip) {
		int result = JOptionPane.YES_OPTION;
		if (!skip) {
			result = JOptionPane.showConfirmDialog(null, "To continue, all data will be cleared.");
		}
		if (result == JOptionPane.YES_OPTION) {
			tableModel.setRowCount(0);
			Fm = 0;
			dm = 0;
			deltaFm = 0;
			homogenityIndex = 0;
			textArea.setText("");;
			log("All data erased.");
		}
	}
	
	private void doCalculation() {
		try {
			log("Starting calculation.");
			log("");
			List<Float> percentages = TableUtils.getAllValuesInColumn(table, COL_NUM_PERCENT_RETAINED);
			int Fm_ref = getHighestValue(percentages);
			Fm = percentages.get(Fm_ref);
			dm = (float) table.getValueAt(Fm_ref, COL_NUM_RANGE_MEAN);
			log("");
			log("% in majority range: " + Fm);
			log("Majority range: " + dm);
			int availableUp = Fm_ref;
			int availablleDown = table.getRowCount() - (Fm_ref + 1);
			int numStudy = Math.max(availableUp, availablleDown);
			log("");
			log("# of fractions: " + numStudy);
			List<Float> dm_up = new ArrayList<Float>(numStudy);
			List<Float> dm_down = new ArrayList<Float>(numStudy);
			List<Float> fm_up = new ArrayList<Float>(numStudy);
			List<Float> fm_down = new ArrayList<Float>(numStudy);
			for (int i = Fm_ref - 1; i >= Fm_ref - numStudy; i--) {
				if (i < 0) {
					dm_up.add(0.0f);
					fm_up.add(0.0f);
				} else {
					dm_up.add((float) table.getValueAt(i, COL_NUM_RANGE_MEAN));
					fm_up.add((float) table.getValueAt(i, COL_NUM_PERCENT_RETAINED));
				}
			}
			for (int i = Fm_ref + 1; i <= Fm_ref + numStudy; i++) {
				if (i < table.getRowCount()) {
					dm_down.add((float) table.getValueAt(i, COL_NUM_RANGE_MEAN));
					fm_down.add((float) table.getValueAt(i, COL_NUM_PERCENT_RETAINED));
				} else {
					dm_down.add(0.000f);
					fm_down.add(0.000f);
				}
			}
			
			//System.out.println(dm_down);
			deltaFm = 0;
			log("delta = ");
			for (int i = 0; i < numStudy; i++) {
				log("[" + fm_down.get(i) + "(" + dm + "-" + dm_down.get(i) + ") + " + fm_up.get(i) + "(" + dm_up.get(i) + " - " + dm + ")] + ");
				deltaFm += (((dm - dm_down.get(i)) * (fm_down.get(i))) + ((dm_up.get(i) - dm) * (fm_up.get(i))));
			}
			log("");
			log("Delta Fm: " + deltaFm);
			log("");
			homogenityIndex = Fm / (100 + deltaFm);
			log("Homogenity Index: " + homogenityIndex);
	
			log("");
			log("Calculation finished.");
		} catch (Exception calcEx) {
			System.err.println("Homogeneity calculation failed.");
		}
	}
	
	private int getHighestValue(List<Float> percentages) {
		int index = 0;
		for (int i = 0; i < percentages.size(); i++) {
			if (percentages.get(i) > percentages.get(index)) {
				index = i;
			}
		}
		return index;
	}
	
	public float getHomogenityIndex() {
		return homogenityIndex;
	}
	
	public float getPercentSmallerThan(float value) {
		return getSmallerThan(value);
	}
	
	public HomogenityData getData() {
		return data;
	}

	public void setData(HomogenityData data) {
		clearAllData(true);
		this.data = data;
		load();
	}

	private void load() {
		if (data == null) {
			return;
		}
		TableUtils.setDataCount(tableModel, data.template.DATA_SIZE);
		TableUtils.setAllValuesInColumn(table, COL_NUM_RANGE_HIGH, data.template.RANGES_UPPER);
		TableUtils.setAllValuesInColumn(table, COL_NUM_RANGE_LOW, data.template.RANGES_LOWER);
		TableUtils.setAllValuesInColumn(table, COL_NUM_RANGE_MEAN, data.template.RANGES_MEAN);
		TableUtils.setAllValuesInColumn(table, COL_NUM_PERCENT_RETAINED, data.PERCENTAGES_RETAINED);
		log("Data loaded.");
		doCalculation();
	}

}
