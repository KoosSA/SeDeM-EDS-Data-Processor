package app.graphical.sedem.parameters;

import java.awt.BorderLayout;
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

import app.data.sedemParameterData.DensityData;
import app.graphical.MainFrame;
import app.utils.DataUtils;
import app.utils.TableUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DensityInput.
 */
public class DensityInput extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2105439747754898807L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The data. */
	private DensityData data = new DensityData();
	
	/** The table. */
	private JTable table;
	
	/** The model. */
	private DefaultTableModel model;
	
	/** The main. */
	private MainFrame main;
	
	/** The col num bulk density. */
	private final int COL_NUM_BULK_DENSITY = 3;
	
	/** The col num tapped density. */
	private final int COL_NUM_TAPPED_DENSITY = 4;
	
	/** The col num volume initial. */
	private final int COL_NUM_VOLUME_INITIAL = 1;
	
	/** The col num volume after. */
	private final int COL_NUM_VOLUME_AFTER = 2;
	
	/** The col num mass. */
	private final int COL_NUM_MASS = 0;
	
	/** The tapped density. */
	private float tappedDensity;
	
	/** The bulk density. */
	private float bulkDensity;
	
	/** The instance. */
	private DensityInput instance;
	
	/** The bd. */
	private List<Float> bd = new ArrayList<Float>();
	
	/** The td. */
	private List<Float> td = new ArrayList<Float>();

	/**
	 * Instantiates a new density input.
	 *
	 * @param mainFrame the main frame
	 */
	public DensityInput(MainFrame mainFrame) {
		instance = this;
		setType(Type.UTILITY);
		main = mainFrame;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Density Data");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Weight (g)", "Initial Volume (ml)", "Volume After (ml)", "Bulk Density (g/ml)", "Tapped Density (g/ml)"
			}
		) {
			private static final long serialVersionUID = -2335450417166994732L;
			Class<?>[] columnTypes = new Class[] {
				Float.class, Float.class, Float.class, Float.class, Float.class
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
		table.getColumnModel().getColumn(1).setPreferredWidth(97);
		table.getColumnModel().getColumn(2).setPreferredWidth(98);
		scrollPane.setViewportView(table);
		
		model = (DefaultTableModel) table.getModel();
		
		JPanel panel = new JPanel();
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
				TableUtils.setDataCount((DefaultTableModel) table.getModel(), DataUtils.getDataAmount(instance));
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
		
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
	}
	
	/**
	 * On save.
	 */
	private void onSave() {
		data.volume_after = TableUtils.getAllValuesInColumn(table, COL_NUM_VOLUME_AFTER);
		data.volume_before = TableUtils.getAllValuesInColumn(table, COL_NUM_VOLUME_INITIAL);
		data.mass = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS);
		data.dataSize = table.getRowCount();
//		bulkDensity = calculate(data.mass, data.volume_before);
//		tappedDensity = calculate(data.mass, data.volume_after);
		calculate();
		main.setDensityData();
		setVisible(false);
	}

	/**
	 * Calculate.
	 */
	private void calculate() {
		tappedDensity = 0;
		bulkDensity = 0;
		bd.clear();
		td.clear();
		List<Float> m = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS);
		List<Float> vi = TableUtils.getAllValuesInColumn(table, COL_NUM_VOLUME_INITIAL);
		List<Float> vf = TableUtils.getAllValuesInColumn(table, COL_NUM_VOLUME_AFTER);
		for (int i = 0; i < table.getRowCount(); i++) {
			bd.add(m.get(i) / vi.get(i));
			td.add(m.get(i) / vf.get(i));
			tappedDensity += td.get(i);
			bulkDensity += bd.get(i);
		}
		TableUtils.setAllValuesInColumn(table, COL_NUM_BULK_DENSITY, bd);
		TableUtils.setAllValuesInColumn(table, COL_NUM_TAPPED_DENSITY, td);
		tappedDensity /= table.getRowCount();
		bulkDensity /= table.getRowCount();
		
	}

	/*private float calculate(List<Float> mass, List<Float> volume) {
		float d = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			d += mass.get(i) / volume.get(i);
		}
		d /= table.getRowCount();
		return d;
	}*/

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public DensityData getData() {
		return data;
	}
	
	/**
	 * Gets the tapped density.
	 *
	 * @return the tapped density
	 */
	public float getTappedDensity() {
		return tappedDensity;
	}
	
	/**
	 * Gets the bulk density.
	 *
	 * @return the bulk density
	 */
	public float getBulkDensity() {
		return bulkDensity;
	}
	
	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(DensityData data) {
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
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS, data.mass);
		TableUtils.setAllValuesInColumn(table, COL_NUM_VOLUME_AFTER, data.volume_after);
		TableUtils.setAllValuesInColumn(table, COL_NUM_VOLUME_INITIAL, data.volume_before);
//		bulkDensity = calculate(data.mass, data.volume_before);
//		tappedDensity = calculate(data.mass, data.volume_after);
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
			tappedDensity = 0;
			bulkDensity = 0;
		}
	}
}
