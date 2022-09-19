package app.graphical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import app.data.ProjectData;
import app.data.SeDeMData;
import app.data.lists.Result;
import app.data.lists.SeDeMParameters;
import app.data.sedemParameterData.CohesionIndexData;
import app.data.sedemParameterData.DensityData;
import app.data.sedemParameterData.HomogenityData;
import app.data.sedemParameterData.HygroscopicityData;
import app.data.sedemParameterData.LossOnDryingData;
import app.data.sedemParameterData.PowderFlowData;
import app.graphical.comparisonGraph.ComparisonFrameSelective;
import app.graphical.correctiveExcipient.CorrectiveExcipientFrame;
import app.graphical.sedem.parameters.CohesionIndexInput;
import app.graphical.sedem.parameters.DensityInput;
import app.graphical.sedem.parameters.HomogenityInput;
import app.graphical.sedem.parameters.HygroscopicityInput;
import app.graphical.sedem.parameters.LossOnDryingInput;
import app.graphical.sedem.parameters.PowderFlowInput;
import app.utils.Calculator;
import app.utils.FileUtil;
import app.utils.TableUtils;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2801820765057303648L;
	private JPanel contentPane;
	private Map<String, JTable> table_parameters_list = new HashMap<String, JTable>();
	private Map<String, JTable> table_indices_list = new HashMap<String, JTable>();
	private JTabbedPane tabs = new JTabbedPane();
	private static MainFrame instance;

	public static final int COL_NUM_PARAM_TABLE_GRAPH_VALUE = 4;
	public static final int COL_NUM_PARAM_TABLE_PROCESSED_VALUE = 3;
	public static final int COL_NUM_PARAM_TABLE_ACTUAL_VALUE = 2;

	private HomogenityInput homogenity = new HomogenityInput(this);
	private DensityInput density = new DensityInput(this);
	private PowderFlowInput flow = new PowderFlowInput(this);
	private CohesionIndexInput cohIndex = new CohesionIndexInput(this);
	private LossOnDryingInput lod = new LossOnDryingInput(this);
	private HygroscopicityInput hygro = new HygroscopicityInput(this);

	//private SeDeMData data = new SeDeMData();
	private Map<String, SeDeMData> data = new HashMap<>();
	//private JPanel panel_chart;

	public MainFrame() {
		MainFrame.instance = this;
		setTitle("SeDeM EDS Data Processor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showOptionDialog(instance, "Do you really want to quit? All unsaved data will be lost.", "Confirm Quit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
					dispose();
					System.exit(0);
				}
			}
		});

		JMenuItem mntmNewMenuItem = new JMenuItem("New Data Tab");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(instance, "Enter name for new data:");
				if (name != null && name.length() > 0) {
					tabs.add(name, addDataTab(name));
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Open/Add Existing");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectData pd = FileUtil.openFile(instance, ProjectData.class);
				if (pd != null) {
					//data = pd.data;
					data.putAll(pd.data);
					pd.data.keySet().forEach(key -> {
						Component c = addDataTab(key);
						tabs.add(c);
						tabs.setSelectedComponent(c);
						doCalcs();
					});
				}
			}

		});
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Save Project");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectData d = new ProjectData();
				d.data = data;
				FileUtil.saveFile(instance, d);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_del = new JMenuItem("Delete Current Tab");
		mntmNewMenuItem_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String name = tabs.getSelectedComponent().getName();
					if (JOptionPane.showConfirmDialog(instance, "Delete: " + name) == JOptionPane.OK_OPTION) {
						tabs.remove(tabs.getSelectedComponent());
						data.remove(name);
					}
				} catch (Exception ex) {}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_del);

		JMenu mnNewMenu_1 = new JMenu("Data");
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Density");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabs.getSelectedComponent() != null) {
					DensityData d = data.get(tabs.getSelectedComponent().getName()).densityData;
					if (d != null) {
						density.setData(d);
					} else {
						density.setData(new DensityData());
					}
					density.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Cohesion Index");
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabs.getSelectedComponent() != null) {
					CohesionIndexData d = data.get(tabs.getSelectedComponent().getName()).cohesionIndexData;
					if (d != null) {
						cohIndex.setData(d);
					} else {
						cohIndex.setData(new CohesionIndexData());
					}
					cohIndex.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_8);

		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Powder Flow");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabs.getSelectedComponent() != null) {
					PowderFlowData d = data.get(tabs.getSelectedComponent().getName()).flowData;
					if (d != null) {
						flow.setData(d);
					} else {
						flow.setData(new PowderFlowData());
					}
					flow.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_6);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Loss On Drying");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabs.getSelectedComponent() != null) {
					LossOnDryingData d = data.get(tabs.getSelectedComponent().getName()).lossOnDryingData;
					if (d != null) {
						lod.setData(d);
					} else {
						lod.setData(new LossOnDryingData());
					}
					lod.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);

		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Hygroscopicity");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabs.getSelectedComponent() != null) {
					HygroscopicityData d = data.get(tabs.getSelectedComponent().getName()).hygroscopicityData;
					if (d != null) {
						hygro.setData(d);
					} else {
						hygro.setData(new HygroscopicityData());
					}
					hygro.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_5);

		JMenuItem mntmNewMenuItem_11 = new JMenuItem("Homogeneity");
		mntmNewMenuItem_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabs.getSelectedComponent() != null) {
					HomogenityData d = data.get(tabs.getSelectedComponent().getName()).homogenietyData;
					if (d != null) {
						homogenity.setData(d);
					} else {
						homogenity.setData(new HomogenityData());
					}
					homogenity.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_11);

		JMenu mnNewMenu_utilities = new JMenu("Utilities");
		menuBar.add(mnNewMenu_utilities);

		JMenuItem mntmNewMenuItem_Comparison = new JMenuItem("Comparison");
		mnNewMenu_utilities.add(mntmNewMenuItem_Comparison);
		mntmNewMenuItem_Comparison.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//String name = JOptionPane.showInputDialog("Enter name for new comparison:");
				//if (name != null && name.length() > 0) {

				//}
				//comp.setVisible(true);
				new ComparisonFrameSelective("Data Comparison", instance);
			}
		});

		JMenuItem mntmNewMenuItem_Corrective_Excipient = new JMenuItem("Corrective Excipient Calculation");
		mnNewMenu_utilities.add(mntmNewMenuItem_Corrective_Excipient);
		mntmNewMenuItem_Corrective_Excipient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CorrectiveExcipientFrame("Corrective Excipient Calculation", instance);
			}
		});



		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));


		contentPane.add(tabs);

		setLocationRelativeTo(null);

	}

	private JScrollPane addDataTab(String name){
		data.putIfAbsent(name, new SeDeMData());
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setName(name);
		JPanel panel_tab = new JPanel();
		panel_tab.setName(name);
		panel_tab.setBackground(Color.LIGHT_GRAY);
		panel_tab.setLayout(new BoxLayout(panel_tab, BoxLayout.Y_AXIS));

		JScrollPane parameter_panel = new JScrollPane();
		JTable table_parameters = createParameterTable(name, parameter_panel);
		panel_tab.add(parameter_panel);

		panel_tab.add(Box.createVerticalStrut(50));

		JScrollPane indices = new JScrollPane();
		createIndicesTable(name, indices);
		panel_tab.add(indices);

		JPanel panel_chart = new JPanel();
		panel_chart.add(SeDeMPolygon.createChartPanel(TableUtils.getAllValuesInColumn(table_parameters, COL_NUM_PARAM_TABLE_GRAPH_VALUE), name));
		table_parameters.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				panel_chart.remove(0);
				panel_chart.add(SeDeMPolygon.createChartPanel(TableUtils.getAllValuesInColumn(table_parameters, COL_NUM_PARAM_TABLE_GRAPH_VALUE), name));
				updateIndices();
			}

		});
		panel_tab.add(panel_chart);

		scrollPane.setViewportView(panel_tab);
		return scrollPane;
	}

	private void updateIndices() {
		String name = tabs.getSelectedComponent().getName();
		List<Float> processedParameters = TableUtils.getAllValuesInColumn(table_parameters_list.get(name), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		JTable indexTable = table_indices_list.get(name);

		indexTable.setValueAt(Calculator.calculateDimensionIndex(processedParameters), 0, 2);
		indexTable.setValueAt(Calculator.calculateCompresibilityIndex(processedParameters), 1, 2);
		indexTable.setValueAt(Calculator.calculateFlowabilityIndex(processedParameters), 2, 2);
		indexTable.setValueAt(Calculator.calculateLubricityStabilityIndex(processedParameters), 3, 2);
		indexTable.setValueAt(Calculator.calculateLubricityDosageIndex(processedParameters), 4, 2);

		data.get(name).dimension_index = Calculator.calculateDimensionIndex(processedParameters);
		data.get(name).compressibility_index = Calculator.calculateCompresibilityIndex(processedParameters);
		data.get(name).flowability_index = Calculator.calculateFlowabilityIndex(processedParameters);
		data.get(name).lubricity_dosage_index = Calculator.calculateLubricityDosageIndex(processedParameters);
		data.get(name).lubricity_stability_index = Calculator.calculateLubricityStabilityIndex(processedParameters);
		
		float igc = Calculator.calculateIGC(processedParameters);
		float ipp = Calculator.calculateIPP(processedParameters);
		float ip = Calculator.calculateIP(processedParameters);

		indexTable.setValueAt(ip, 5, 2);
		indexTable.setValueAt(ipp, 6, 2);
		indexTable.setValueAt(igc , 7, 2);
		
		data.get(name).IGC = igc;
		data.get(name).IP = ip;
		data.get(name).IPP = ipp;

		List<Float> results = TableUtils.getAllValuesInColumn(indexTable, 2);
		for (int i = 0; i < 5; i++) {
			indexTable.setValueAt((results.get(i) >= 5 ? Result.PASS : Result.FAIL), i, 3);
		}
		indexTable.setValueAt((results.get(5) >= 0.5f ? Result.PASS : Result.FAIL), 5, 3);
		indexTable.setValueAt((results.get(6) >= 5f ? Result.PASS : Result.FAIL), 6, 3);
		indexTable.setValueAt((results.get(7) >= 5f ? Result.PASS : Result.FAIL), 7, 3);


	}

	private JTable createIndicesTable(String name, JScrollPane scroll) {
		//scroll.setLayout(new BorderLayout());
		JTable table_parameters = new JTable();
		table_indices_list.putIfAbsent(name, table_parameters);
		table_parameters.setBackground(Color.LIGHT_GRAY);
		table_parameters.setModel(new DefaultTableModel(
			new Object[][] {
				{"Dimension", 						"", 	0.0f, Result.NULL},
				{"Compresibility", 					"", 	0.0f, Result.NULL},
				{"Flowability", 					"", 	0.0f, Result.NULL},
				{"Lubricity/Stability", 			"", 	0.0f, Result.NULL},
				{"Lubricity/Dosage", 				"", 	0.0f, Result.NULL},
				{"Parameter Index", 				"IP", 	0.0f, Result.NULL},
				{"Index of Profile Parameter", 		"IPP", 	0.0f, Result.NULL},
				{"Index of Good Compresibility", 	"IGC", 	0.0f, Result.NULL},
			},
			new String[] {
				"Index", "Key", "Value", "Pass/Fail"
			}
		) {
			private static final long serialVersionUID = -2586545168665749190L;
			Class<?>[] columnTypes = new Class[] {
				Object.class, Object.class, Float.class, Object.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_parameters.getColumnModel().getColumn(0).setPreferredWidth(111);
		table_parameters.getColumnModel().getColumn(1).setPreferredWidth(40);
		//scroll.add(table_parameters, BorderLayout.CENTER);
		//scroll.add(table_parameters.getTableHeader(), BorderLayout.NORTH);
		scroll.setViewportView(table_parameters);
		Dimension d = table_parameters.getPreferredSize();
		scroll.setPreferredSize(new Dimension(d.width, table_parameters.getRowHeight() * (table_parameters.getRowCount() + 2)));

		return table_parameters;
	}

	private JTable createParameterTable(String name, JScrollPane table_panel) {
		//table_panel.setLayout(new BorderLayout());
		JTable table_parameters = new JTable();
		table_parameters_list.putIfAbsent(name, table_parameters);
		table_parameters.setBackground(Color.LIGHT_GRAY);
		table_parameters.setModel(new DefaultTableModel(
			new Object[][] {
				{"Bulk Density", "Da", 0.0f, 0.0f, 0.0f},
				{"Tapped Density", "Dc", 0.0f, 0.0f, 0.0f},
				{"Inter Particle Porosity", "Ie", 0.0f, 0.0f, 0.0f},
				{"Carr's Index", "Ic", 0.0f, 0.0f, 0.0f},
				{"Cohesion Index", "Icd", 0.0f, 0.0f, 0.0f},
				{"Hausner Ratio", "HR", 0.0f, 0.0f, 0.0f},
				{"Angle of Response", "AR", 0.0f, 0.0f, 0.0f},
				{"Powder Flow", "t\"", 0.0f, 0.0f, 0.0f},
				{"Loss on Drying", "%HR", 0.0f, 0.0f, 0.0f},
				{"Hygroscopicity", "%H", 0.0f, 0.0f, 0.0f},
				{"Particles <45 um", "Pf", 0.0f, 0.0f, 0.0f},
				{"Homogeneity", "HI", 0.0f, 0.0f, 0.0f},
			},
			new String[] {
				"Parameter", "Key", "Actual Value", "SeDeM Value", "Chart Value"
			}
		) {
			private static final long serialVersionUID = 7304897116716776263L;
			Class<?>[] columnTypes = new Class[] {
				Object.class, Object.class, Float.class, Float.class, Float.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_parameters.getColumnModel().getColumn(0).setPreferredWidth(111);
		table_parameters.getColumnModel().getColumn(1).setPreferredWidth(40);

		//table_panel.add(table_parameters, BorderLayout.CENTER);
		//table_panel.add(table_parameters.getTableHeader(), BorderLayout.NORTH);
		table_panel.setViewportView(table_parameters);
		Dimension d = table_parameters.getPreferredSize();
		table_panel.setPreferredSize(new Dimension(d.width, table_parameters.getRowHeight() * (table_parameters.getRowCount() + 2)));
		return table_parameters;
	}

	public void setHomogenityData() {
		System.out.println(tabs.getSelectedComponent().getName());
		data.get(tabs.getSelectedComponent().getName()).homogeneityIndex = homogenity.getHomogenityIndex();
		data.get(tabs.getSelectedComponent().getName()).homogenietyData = homogenity.getData();
		data.get(tabs.getSelectedComponent().getName()).particlesSmallerThan45um = homogenity.getPercentSmallerThan(45.0f);
		doCalcs();
	}

	public void setDensityData() {
		float bulk = density.getBulkDensity();
		float tapped = density.getTappedDensity();
		data.get(tabs.getSelectedComponent().getName()).densityData = density.getData();
		data.get(tabs.getSelectedComponent().getName()).bulkDensity = bulk;
		data.get(tabs.getSelectedComponent().getName()).tappedDensity = tapped;
		doCalcs();
	}


	public void setFlowData() {
		data.get(tabs.getSelectedComponent().getName()).flowData = flow.getData();
		data.get(tabs.getSelectedComponent().getName()).angleOfResponse = flow.getAngle_of_response();
		data.get(tabs.getSelectedComponent().getName()).powderFlow = flow.getFlowability();
		doCalcs();
	}

	public void setCohesionIndexData() {
		data.get(tabs.getSelectedComponent().getName()).cohesionIndexData = cohIndex.getData();
		data.get(tabs.getSelectedComponent().getName()).cohesionIndex = cohIndex.getCohesionIndex();
		doCalcs();
	}

	public void setLODData() {
		data.get(tabs.getSelectedComponent().getName()).lossOnDryingData = lod.getData();
		data.get(tabs.getSelectedComponent().getName()).lossOnDrying = lod.getPercent_lost();
		doCalcs();
	}


	public void setHygroData() {
		data.get(tabs.getSelectedComponent().getName()).hygroscopicityData = hygro.getData();
		data.get(tabs.getSelectedComponent().getName()).hygroscopicity = hygro.getPercent_gain();
		doCalcs();
	}

	private void doCalcs() {
		float processed = 0;
		SeDeMData d = data.get(tabs.getSelectedComponent().getName());
		JTable table_parameters = table_parameters_list.get(tabs.getSelectedComponent().getName());

		if (d.bulkDensity != 0 && d.tappedDensity != 0) {
			data.get(tabs.getSelectedComponent().getName()).hausnerRatio = d.tappedDensity / d.bulkDensity;
			data.get(tabs.getSelectedComponent().getName()).carrIndex = ((d.tappedDensity - d.bulkDensity) / d.tappedDensity) * 100;
			data.get(tabs.getSelectedComponent().getName()).interParticlePorosity = (d.tappedDensity - d.bulkDensity) / (d.tappedDensity * d.bulkDensity);
		}

		table_parameters.setValueAt(d.bulkDensity, SeDeMParameters.Bulk_Density.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.bulkDensity * 10;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Bulk_Density.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Bulk_Density.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.tappedDensity, SeDeMParameters.Tapped_Density.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.tappedDensity * 10;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Tapped_Density.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Tapped_Density.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.homogeneityIndex, SeDeMParameters.Homogeneity_Index.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.homogeneityIndex * 500;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Homogeneity_Index.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Homogeneity_Index.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		processed = 0;

		table_parameters.setValueAt(d.hausnerRatio, SeDeMParameters.Hausner_Ratio.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (10 * d.hausnerRatio) / 3;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Hausner_Ratio.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Hausner_Ratio.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.carrIndex, SeDeMParameters.Carrs_Index.ordinal(), 2);
		processed = d.carrIndex / 5;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Carrs_Index.ordinal(), 3);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Carrs_Index.ordinal(), 4);

		table_parameters.setValueAt(d.angleOfResponse, SeDeMParameters.Angle_of_Repose.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - ( d.angleOfResponse / 5 );
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Angle_of_Repose.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Angle_of_Repose.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.cohesionIndex, SeDeMParameters.Cohesion_Index.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.cohesionIndex / 20;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Cohesion_Index.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Cohesion_Index.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.hygroscopicity, SeDeMParameters.Hygroscopicity.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (d.hygroscopicity / 2);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Hygroscopicity.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Hygroscopicity.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.interParticlePorosity, SeDeMParameters.Inter_particle_Porosity.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = (d.interParticlePorosity * 10) / 1.2f;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Inter_particle_Porosity.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Inter_particle_Porosity.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.lossOnDrying, SeDeMParameters.Loss_on_Drying.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - d.lossOnDrying;
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Loss_on_Drying.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Loss_on_Drying.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.powderFlow, SeDeMParameters.Powder_Flow.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (d.powderFlow / 2);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Powder_Flow.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Powder_Flow.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);

		table_parameters.setValueAt(d.particlesSmallerThan45um, SeDeMParameters.Particles_smaller_than_45µm.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (d.particlesSmallerThan45um / 5);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Particles_smaller_than_45µm.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.max(0, Math.min(10, processed)), SeDeMParameters.Particles_smaller_than_45µm.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
	}

	public static Map<String, SeDeMData> getProjectData() {
		return instance.data;
	}

	public JTabbedPane getTabs() {
		return tabs;
	}







}
