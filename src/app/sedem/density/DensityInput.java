package app.sedem.density;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import app.utils.DataUtils;
import app.utils.MainFrame;
import app.utils.TableUtils;

public class DensityInput extends JFrame {

	private static final long serialVersionUID = 2105439747754898807L;
	private JPanel contentPane;
	private DensityData data = new DensityData();
	private JTable table;
	private DefaultTableModel model;
	private MainFrame main;
	private final int COL_NUM_VOLUME_INITIAL = 1;
	private final int COL_NUM_VOLUME_AFTER = 2;
	private final int COL_NUM_MASS = 0;
	private float tappedDensity;
	private float bulkDensity;

	public DensityInput(MainFrame mainFrame) {
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
				"Weight (g)", "Initial Volume (ml)", "Volume After (ml)"
			}
		) {
			private static final long serialVersionUID = -2335450417166994732L;
			Class<?>[] columnTypes = new Class[] {
				Float.class, Float.class, Float.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
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
				TableUtils.setDataCount((DefaultTableModel) table.getModel(), DataUtils.getDataAmount());
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
	}
	
	private void onSave() {
		data.volume_after = TableUtils.getAllValuesInColumn(table, COL_NUM_VOLUME_AFTER);
		data.volume_before = TableUtils.getAllValuesInColumn(table, COL_NUM_VOLUME_INITIAL);
		data.mass = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS);
		data.dataSize = table.getRowCount();
		bulkDensity = calculate(data.mass, data.volume_before);
		tappedDensity = calculate(data.mass, data.volume_after);
		main.setDensityData();
		setVisible(false);
	}

	private float calculate(List<Float> mass, List<Float> volume) {
		float d = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			d += mass.get(i) / volume.get(i);
		}
		d /= table.getRowCount();
		return d;
	}

	public DensityData getData() {
		return data;
	}
	
	public float getTappedDensity() {
		return tappedDensity;
	}
	
	public float getBulkDensity() {
		return bulkDensity;
	}
	
	public void setData(DensityData data) {
		clearAllData(true);
		this.data = data;
		load();
	}
	
	private void load() {
		if (data == null) {
			return;
		}
		TableUtils.setDataCount(model, data.dataSize);
		TableUtils.setAllValuesInColumn(table, COL_NUM_MASS, data.mass);
		TableUtils.setAllValuesInColumn(table, COL_NUM_VOLUME_AFTER, data.volume_after);
		TableUtils.setAllValuesInColumn(table, COL_NUM_VOLUME_INITIAL, data.volume_before);
		bulkDensity = calculate(data.mass, data.volume_before);
		tappedDensity = calculate(data.mass, data.volume_after);
	}
	
	private void clearAllData(boolean skip) {
		int result = JOptionPane.YES_OPTION;
		if (!skip) {
			result = JOptionPane.showConfirmDialog(null, "To continue, all data will be cleared.");
		}
		if (result == JOptionPane.YES_OPTION) {
			model.setRowCount(0);
			tappedDensity = 0;
			bulkDensity = 0;
		}
	}
}
