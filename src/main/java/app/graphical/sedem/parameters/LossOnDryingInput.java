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

import app.data.sedemParameterData.LossOnDryingData;
import app.graphical.MainFrame;
import app.utils.DataUtils;
import app.utils.TableUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class LossOnDryingInput.
 */
public class LossOnDryingInput extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2105439747754898807L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The data. */
	private LossOnDryingData data = new LossOnDryingData();
	
	/** The table. */
	private JTable table;
	
	/** The model. */
	private DefaultTableModel model;
	
	/** The main. */
	private MainFrame main;
	
	/** The instance. */
	private LossOnDryingInput instance;
	
	/** The col num mass after. */
	private final int COL_NUM_MASS_AFTER = 2;
	
	/** The col num mass before. */
	private final int COL_NUM_MASS_BEFORE = 1;
	
	/** The col num mass delta. */
	private final int COL_NUM_MASS_DELTA = 5;
	
	/** The col num mass container. */
	private final int COL_NUM_MASS_CONTAINER = 0;
	
	/** The col num mass powder before. */
	private final int COL_NUM_MASS_POWDER_BEFORE = 3;
	
	/** The col num mass powder after. */
	private final int COL_NUM_MASS_POWDER_AFTER = 4;
	
	/** The col num percent loss. */
	private final int COL_NUM_PERCENT_LOSS = 6;
	
	/** The percent lost. */
	private float percent_lost;
	
	/** The mi. */
	private List<Float> mi = new ArrayList<Float>();
	
	/** The mf. */
	private List<Float> mf = new ArrayList<Float>();
	
	/** The dm. */
	private List<Float> dm = new ArrayList<Float>();
	
	/** The pl. */
	private List<Float> pl = new ArrayList<Float>();

	/**
	 * Instantiates a new loss on drying input.
	 *
	 * @param mainFrame the main frame
	 */
	public LossOnDryingInput(MainFrame mainFrame) {
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
		main.setLODData();
		setVisible(false);
	}

	/**
	 * Calculate.
	 */
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

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public LossOnDryingData getData() {
		return data;
	}
	
	/**
	 * Gets the percent lost.
	 *
	 * @return the percent lost
	 */
	public float getPercent_lost() {
		return percent_lost;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(LossOnDryingData data) {
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
		if (table.getRowCount() > 0) {
			calculate();
		}
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
			percent_lost = 0;
		}
	}
	
}
