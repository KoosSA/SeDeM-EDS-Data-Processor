package app.graphical.sedem.parameters;

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

import app.data.sedemParameterData.HygroscopicityData;
import app.graphical.MainFrame;
import app.utils.DataUtils;
import app.utils.TableUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class HygroscopicityInput.
 */
public class HygroscopicityInput extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2105439747754898807L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The data. */
	private HygroscopicityData data = new HygroscopicityData();
	
	/** The table. */
	private JTable table;
	
	/** The model. */
	private DefaultTableModel model;
	
	/** The main. */
	private MainFrame main;
	
	/** The col num mass container. */
	private final int COL_NUM_MASS_CONTAINER = 0;
	
	/** The col num mass before. */
	private final int COL_NUM_MASS_BEFORE = 1;
	
	/** The col num mass after. */
	private final int COL_NUM_MASS_AFTER = 2;
	
	/** The col num mass powder before. */
	private final int COL_NUM_MASS_POWDER_BEFORE = 3;
	
	/** The col num mass powder after. */
	private final int COL_NUM_MASS_POWDER_AFTER = 4;
	
	/** The col num mass delta. */
	private final int COL_NUM_MASS_DELTA = 5;
	
	/** The col num percentage gain. */
	private final int COL_NUM_PERCENTAGE_GAIN = 6;
	
	/** The percent gain. */
	private float percent_gain;
	
	/** The instance. */
	private HygroscopicityInput instance;
	
	/** The mi. */
	private List<Float> mi = new ArrayList<Float>();
	
	/** The mf. */
	private List<Float> mf = new ArrayList<Float>();
	
	/** The dm. */
	private List<Float> dm = new ArrayList<Float>();
	
	/** The pl. */
	private List<Float> pl = new ArrayList<Float>();

	/**
	 * Instantiates a new hygroscopicity input.
	 *
	 * @param mainFrame the main frame
	 */
	public HygroscopicityInput(MainFrame mainFrame) {
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
		
		JLabel lblNewLabel = new JLabel("Hygroscopicity Data");
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
				"Weight Container (g)", "Container & Substance Initial Mass (g)", "Container & Substance Final Mass (g)", "Substance Initial Mass (g)", "Substance Final Mass (g)", "Delta Mass (g)", "% Gain (%)"
			}
		) {
			private static final long serialVersionUID = 8002144398192845301L;
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
		
		pack();
		setLocationRelativeTo(null);
		
		setAlwaysOnTop(true);
	}
	
	/**
	 * On save.
	 */
	private void onSave() {
		data.clear();
		data.dataSize = table.getRowCount();
		data.mass_after = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_AFTER);
		data.mass_before = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_BEFORE);
		data.mass_container = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_CONTAINER);
		calculate();
		main.setHygroData();
		setVisible(false);
	}

	/**
	 * Calculate.
	 */
	private void calculate() {
		percent_gain = 0;
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
				dm.add(mf.get(i) - mi.get(i));
				pl.add(dm.get(i) / mi.get(i) * 100);
				percent_gain += pl.get(i);
			}
			percent_gain /= table.getRowCount();
			TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_POWDER_BEFORE, mi);
			TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_POWDER_AFTER, mf);
			TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_DELTA, dm);
			TableUtils.setAllValuesInColumn(table, COL_NUM_PERCENTAGE_GAIN, pl);
		} catch (Exception e) {}
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public HygroscopicityData getData() {
		return data;
	}
	
	/**
	 * Gets the percent gain.
	 *
	 * @return the percent gain
	 */
	public float getPercent_gain() {
		return percent_gain;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(HygroscopicityData data) {
		clearAllData(true);
		this.data = data;
		load();
	}
	
	/**
	 * Load.
	 */
	private void load() {
		if (data == null) {
			return;
		}
		TableUtils.setDataCount(model, data.dataSize);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_AFTER, data.mass_after);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_BEFORE, data.mass_before);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS_CONTAINER, data.mass_container);
		calculate();
	}
	
	/**
	 * Clear all data.
	 *
	 * @param skip the skip
	 */
	private void clearAllData(boolean skip) {
		int result = JOptionPane.YES_OPTION;
		if (!skip) {
			result = JOptionPane.showConfirmDialog(instance, "To continue, all data will be cleared.");
		}
		if (result == JOptionPane.YES_OPTION) {
			model.setRowCount(0);
			percent_gain = 0;
		}
	}
	
}
