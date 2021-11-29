package app.sedem.lossondrying;

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

import app.utils.DataUtils;
import app.utils.MainFrame;
import app.utils.TableUtils;

public class LODInput extends JFrame {

	private static final long serialVersionUID = 2105439747754898807L;
	private JPanel contentPane;
	private LODData data = new LODData();
	private JTable table;
	private DefaultTableModel model;
	private MainFrame main;
	private final int COL_NUM_MASS_AFTER = 1;
	private final int COL_NUM_MASS_BEFORE = 0;
	private float percent_lost;

	public LODInput(MainFrame mainFrame) {
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
				"Weight Initial (g)", "Weight After (g)"
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
				int count = DataUtils.getDataAmount();
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
		
		JButton btn_save = new JButton("Save");
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSave();
			}
		});
		panel.add(btn_save);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void onSave() {
		data.clear();
		data.dataSize = table.getRowCount();
		data.mass_after = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_AFTER);
		data.mass_before = TableUtils.getAllValuesInColumn(table, COL_NUM_MASS_BEFORE);
		calculate();
		main.setLODData();
		setVisible(false);
	}

	private void calculate() {
		percent_lost = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			percent_lost += (data.mass_before.get(i) - data.mass_after.get(i)) / data.mass_before.get(i);
		}
		percent_lost /= table.getRowCount();
		percent_lost *= 100;
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
		calculate();
	}
	
	private void clearAllData(boolean skip) {
		int result = JOptionPane.YES_OPTION;
		if (!skip) {
			result = JOptionPane.showConfirmDialog(null, "To continue, all data will be cleared.");
		}
		if (result == JOptionPane.YES_OPTION) {
			model.setRowCount(0);
			percent_lost = 0;
		}
	}
	
}
