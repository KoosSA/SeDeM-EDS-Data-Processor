package app.sedem.parameters.lossondrying;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.graphical.MainFrame;
import app.utils.DataUtils;
import app.utils.TableUtils;

public class LODInput extends JFrame {

	private static final long serialVersionUID = 2105439747754898807L;
	private JPanel contentPane;
	private LODData data = new LODData();
	private JTable table;
	private DefaultTableModel model;
	private MainFrame main;
	private LODInput instance;
	private final int COL_NUM_MASS_AFTER = 2;
	private final int COL_NUM_MASS_BEFORE = 1;
	private final int COL_NUM_MASS_DELTA = 5;
	private final int COL_NUM_MASS_CONTAINER = 0;
	private final int COL_NUM_MASS_POWDER_BEFORE = 3;
	private final int COL_NUM_MASS_POWDER_AFTER = 4;
	private final int COL_NUM_PERCENT_LOSS = 6;
	private float percent_lost;
	private List<Float> mi = new ArrayList<Float>();
	private List<Float> mf = new ArrayList<Float>();
	private List<Float> dm = new ArrayList<Float>();
	private List<Float> pl = new ArrayList<Float>();

	public LODInput(MainFrame mainFrame) {
		instance = this;
		setBackground(Color.LIGHT_GRAY);
		setType(Type.UTILITY);
		main = mainFrame;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Loss on Drying Data");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().setBackground(Color.LIGHT_GRAY);
		
		table = new JTable();
		table.setBackground(Color.LIGHT_GRAY);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Container Weight (g)", "Before + Container (g)", "After + Container (g)", "Weight Before (g)", "Weight After (g)", "Delta Weight (g)", "% Loss"
			}
		) {
			private static final long serialVersionUID = -6059203927781911442L;
			Class<?>[] columnTypes = new Class[] {
				Float.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				true, true, true, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(112);
		table.getColumnModel().getColumn(1).setPreferredWidth(121);
		table.getColumnModel().getColumn(2).setPreferredWidth(115);
		table.getColumnModel().getColumn(3).setPreferredWidth(97);
		table.getColumnModel().getColumn(4).setPreferredWidth(94);
		table.getColumnModel().getColumn(5).setPreferredWidth(90);
		scrollPane.setViewportView(table);
		model = (DefaultTableModel) table.getModel();
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btn_cancel = new JButton("Cancel");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		panel.add(btn_cancel);
		
		JButton btn_addData = new JButton("Add Data");
		btn_addData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = DataUtils.getDataAmount(instance);
				TableUtils.setDataCount((DefaultTableModel) table.getModel(), count);
				data.dataSize = count;
			}
		});
		
		JButton btnNewButton = new JButton("Clear Data");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DefaultTableModel)table.getModel()).setRowCount(0);
			}
		});
		panel.add(btnNewButton);
		panel.add(btn_addData);
		
		JButton btn_calc = new JButton("Calculate");
		btn_calc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate();
			}
		});
		panel.add(btn_calc);
		
		JButton btn_save = new JButton("Save");
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSave();
			}
		});
		panel.add(btn_save);
		
		setAlwaysOnTop(true);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void onSave() {
		data.clear();
		data.dataSize = table.getRowCount();
		data.mass_after = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_AFTER);
		data.mass_before = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_BEFORE);
		data.mass_container = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_CONTAINER);
		calculate();
		main.setLODData();
		setVisible(false);
	}

	private void calculate() {
		percent_lost = 0;
		try {
			List<Float> mp = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_CONTAINER);
			List<Float> mic = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_BEFORE);
			List<Float> mfc = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_AFTER);
			mi.clear();
			mf.clear();
			dm.clear();
			pl.clear();
			for (int i = 0; i < table.getRowCount(); i++) {
				mi.add(mic.get(i) - mp.get(i));
				mf.add(mfc.get(i) - mp.get(i));
				dm.add(mi.get(i) - mf.get(i));
				pl.add(dm.get(i) / mi.get(i) * 100);
				percent_lost += pl.get(i);
			}
			percent_lost /= table.getRowCount();
			TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_POWDER_BEFORE, mi);
			TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_POWDER_AFTER, mf);
			TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_DELTA, dm);
			TableUtils.setAllValuesInColumn(table, COL_NUM_PERCENT_LOSS, pl);
		} catch (Exception e) {}
	}

	public LODData getData() {
		return data;
	}
	
	public float getPercent_lost() {
		return percent_lost;
	}

	public void setData(LODData data) {
		clearAllData(true);
		this.data = data;
		load();
	}
	
	private void load() {
		if (data == null) {
			return;
		}
		TableUtils.setDataCount(model, data.dataSize);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_AFTER, data.mass_after);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_BEFORE, data.mass_before);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_CONTAINER, data.mass_container);
		if (table.getRowCount() > 0) {
			calculate();
		}
	}
	
	private void clearAllData(boolean skip) {
		int result = JOptionPane.YES_OPTION;
		if (!skip) {
			result = JOptionPane.showConfirmDialog(instance, "To continue, all data will be cleared.");
		}
		if (result == JOptionPane.YES_OPTION) {
			model.setRowCount(0);
			percent_lost = 0;
		}
	}
	
}
