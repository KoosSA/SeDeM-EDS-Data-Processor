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

import app.data.sedemParameterData.PowderFlowData;
import app.graphical.MainFrame;
import app.utils.DataUtils;
import app.utils.TableUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class PowderFlowInput.
 */
public class PowderFlowInput extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2105439747754898807L;

	/** The content pane. */
	private JPanel contentPane;

	/** The data. */
	private PowderFlowData data = new PowderFlowData();

	/** The table. */
	private JTable table;

	/** The model. */
	private DefaultTableModel model;

	/** The main. */
	private MainFrame main;

	/** The col num time. */
	private final int COL_NUM_TIME = 1;

	/** The col num mass. */
	private final int COL_NUM_MASS = 0;

	/** The col num cone height. */
	private final int COL_NUM_CONE_HEIGHT = 2;

	/** The col num cone radius. */
	private final int COL_NUM_CONE_RADIUS = 3;

	/** The angle of response. */
	private float angle_of_response;

	/** The flowability. */
	private float flowability;

	/** The instance. */
	private PowderFlowInput instance;

	/**
	 * Instantiates a new powder flow input.
	 *
	 * @param mainFrame the main frame
	 */
	public PowderFlowInput(MainFrame mainFrame) {
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

		JLabel lblNewLabel = new JLabel("Powder-Flow Data");
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
				"Weight (g)", "Time (s)", "Cone Height (mm)", "Cone Radius (mm)"
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
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(102);
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


		pack();
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
	}

	/**
	 * On save.
	 */
	private void onSave() {
		data.clear();
		data.time = TableUtils.getAllValuesInColumn(table, COL_NUM_TIME);
		data.mass = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS);
		data.cone_height = TableUtils.getAllValuesInColumn(table, COL_NUM_CONE_HEIGHT);
		data.cone_radius = TableUtils.getAllValuesInColumn(table, COL_NUM_CONE_RADIUS);
		data.dataSize = data.mass.size();
		calculate();
		main.setFlowData();
		setVisible(false);
	}

	/**
	 * Calculate.
	 */
	private void calculate() {
		flowability = 0;
		angle_of_response = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			flowability += data.time.get(i);
			angle_of_response += Math.toDegrees( Math.atan(data.cone_height.get(i) / data.cone_radius.get(i)) );
			//System.out.println(angle_of_response);
		}
		flowability /= table.getRowCount();
		angle_of_response /= table.getRowCount();
		//System.out.println(flowability);
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public PowderFlowData getData() {
		return data;
	}

	/**
	 * Gets the angle of response.
	 *
	 * @return the angle of response
	 */
	public float getAngle_of_response() {
		return angle_of_response;
	}

	/**
	 * Gets the flowability.
	 *
	 * @return the flowability
	 */
	public float getFlowability() {
		return flowability;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(PowderFlowData data) {
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
		TableUtils.setAllValuesInColumn(table, COL_NUM_CONE_HEIGHT, data.cone_height);
		TableUtils.setAllValuesInColumn(table, COL_NUM_CONE_RADIUS, data.cone_radius);
		TableUtils.setAllValuesInColumn(table, COL_NUM_TIME, data.time);
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
			flowability = 0;
			angle_of_response = 0;
		}
	}

}
