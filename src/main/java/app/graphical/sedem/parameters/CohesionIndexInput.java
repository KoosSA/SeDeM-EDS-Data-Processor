package app.graphical.sedem.parameters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import app.data.sedemParameterData.CohesionIndexData;
import app.graphical.MainFrame;
import app.utils.DataUtils;
import app.utils.TableUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class CohesionIndexInput.
 */
public class CohesionIndexInput extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2105439747754898807L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The data. */
	private CohesionIndexData data = new CohesionIndexData();
	
	/** The table. */
	private JTable table;
	
	/** The model. */
	private DefaultTableModel model;
	
	/** The main. */
	private MainFrame main;
	
	/** The instance. */
	private CohesionIndexInput instance;
	
	/** The col num hardness. */
	private final int COL_NUM_HARDNESS = 0;
	
	/** The cohesion index. */
	private float cohesionIndex;

	/**
	 * Instantiates a new cohesion index input.
	 *
	 * @param mainFrame the main frame
	 */
	public CohesionIndexInput(MainFrame mainFrame) {
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
		
		JLabel lblNewLabel = new JLabel("Cohesion Data");
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
				"Hardness (N)"
			}
		) {
			private static final long serialVersionUID = 8002144398192845301L;
			Class<?>[] columnTypes = new Class[] {
				Float.class, Float.class, Float.class, Float.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
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
		data.clear();
		data.hardness = TableUtils.getAllValuesInColumn(table, COL_NUM_HARDNESS);
		data.dataSize = data.hardness.size();
		calculate();
		main.setCohesionIndexData();
		setVisible(false);
	}

	/**
	 * Calculate.
	 */
	private void calculate() {
		cohesionIndex = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			cohesionIndex += data.hardness.get(i);
		}
		cohesionIndex /= table.getRowCount();
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public CohesionIndexData getData() {
		return data;
	}
	
	/**
	 * Gets the cohesion index.
	 *
	 * @return the cohesion index
	 */
	public float getCohesionIndex() {
		return cohesionIndex;
	}
	
	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(CohesionIndexData data) {
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
		TableUtils.setAllValuesInColumn(table, COL_NUM_HARDNESS, data.hardness);
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
			cohesionIndex = 0;
		}
	}
	
}
