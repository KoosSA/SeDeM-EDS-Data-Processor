package app.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import app.sedem.SeDeMData;
import app.sedem.SeDeMParameters;
import app.sedem.cohesionIndex.CohesionIndexData;
import app.sedem.cohesionIndex.CohesionIndexInput;
import app.sedem.density.DensityData;
import app.sedem.density.DensityInput;
import app.sedem.graph.SeDeMPolygon;
import app.sedem.homogeneity.HomogenityData;
import app.sedem.homogeneity.HomogenityInput;
import app.sedem.hygroscopicity.HygroData;
import app.sedem.hygroscopicity.HygroInput;
import app.sedem.lossondrying.LODData;
import app.sedem.lossondrying.LODInput;
import app.sedem.powderflow.PowderFlowData;
import app.sedem.powderflow.PowderFlowInput;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2801820765057303648L;
	private JPanel contentPane;
	private Map<String, JTable> table_parameters_list = new HashMap<String, JTable>();
	private JTabbedPane tabs = new JTabbedPane();
	//private ComparisonFrame comp = new ComparisonFrame("Comparison", this);
	private MainFrame instance;
	
	private final int COL_NUM_PARAM_TABLE_GRAPH_VALUE = 4;
	private final int COL_NUM_PARAM_TABLE_PROCESSED_VALUE = 3;
	private final int COL_NUM_PARAM_TABLE_ACTUAL_VALUE = 2;
	
	private HomogenityInput homogenity = new HomogenityInput(this);
	private DensityInput density = new DensityInput(this);
	private PowderFlowInput flow = new PowderFlowInput(this);
	private CohesionIndexInput cohIndex = new CohesionIndexInput(this);
	private LODInput lod = new LODInput(this);
	private HygroInput hygro = new HygroInput(this);
	
	//private SeDeMData data = new SeDeMData();
	private Map<String, SeDeMData> data = new HashMap<>();
	//private JPanel panel_chart;

	public MainFrame() {
		instance = this;
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
				if (JOptionPane.showOptionDialog(null, "Do you really want to quit? All unsaved data will be lost.", "Confirm Quit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
					dispose();
					System.exit(0);
				}
			}
		});
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New Data Tab");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Enter name for new data:");
				if (name != null && name.length() > 0) {
					tabs.add(name, addDataTab(name));
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_Comparison = new JMenuItem("New Comparison");
		mntmNewMenuItem_Comparison.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//String name = JOptionPane.showInputDialog("Enter name for new comparison:");
				//if (name != null && name.length() > 0) {
					
				//}
				//comp.setVisible(true);
				new ComparisonFrameSelective("Data Comparison", instance);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_Comparison);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Open Existing");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data = FileUtil.openFile(ProjectData.class).data;
				data.keySet().forEach(key -> {
					//System.out.println(data.get(key).hygroscopicity);
					Component c = addDataTab(key);
					tabs.add(c);
					tabs.setSelectedComponent(c);
					doCalcs();
				});
			}

		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Save Project");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectData d = new ProjectData();
				d.data = data;
				FileUtil.saveFile(d);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_del = new JMenuItem("Delete Current Tab");
		mntmNewMenuItem_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String name = tabs.getSelectedComponent().getName();
					if (JOptionPane.showConfirmDialog(null, "Delete: " + name) == JOptionPane.OK_OPTION) {
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
					LODData d = data.get(tabs.getSelectedComponent().getName()).lossOnDryingData;
					if (d != null) {
						lod.setData(d);
					} else {
						lod.setData(new LODData());
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
					HygroData d = data.get(tabs.getSelectedComponent().getName()).hygroscopicityData;
					if (d != null) {
						hygro.setData(d);
					} else {
						hygro.setData(new HygroData());
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
		
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar.add(menuBar_1);
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
		JScrollPane scrollPane_main = new JScrollPane();
		scrollPane_main.setName(name);
		JPanel panel_main = new JPanel();
		panel_main.setBackground(Color.LIGHT_GRAY);
		scrollPane_main.setViewportView(panel_main);
		panel_main.setLayout(new BoxLayout(panel_main, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane_parameters = new JScrollPane();
		scrollPane_parameters.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane_parameters.setViewportBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
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
		scrollPane_parameters.setViewportView(table_parameters);
		panel_main.add(scrollPane_parameters);
		
		JPanel panel_chart = new JPanel();
		panel_chart.add(SeDeMPolygon.createChartPanel(TableUtils.getAllValuesInColumn(table_parameters, COL_NUM_PARAM_TABLE_GRAPH_VALUE), name));
		table_parameters.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				panel_chart.remove(0);
				panel_chart.add(SeDeMPolygon.createChartPanel(TableUtils.getAllValuesInColumn(table_parameters, COL_NUM_PARAM_TABLE_GRAPH_VALUE), name));
				
			}
		});
		panel_main.add(panel_chart);
		
		return scrollPane_main;
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
		table_parameters.setValueAt(processed, SeDeMParameters.Bulk_Density.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Bulk_Density.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.tappedDensity, SeDeMParameters.Tapped_Density.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.tappedDensity * 10;
		table_parameters.setValueAt(processed, SeDeMParameters.Tapped_Density.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Tapped_Density.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
	
		table_parameters.setValueAt(d.homogeneityIndex, SeDeMParameters.Homogeneity_Index.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.homogeneityIndex * 500;
		table_parameters.setValueAt(processed, SeDeMParameters.Homogeneity_Index.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Homogeneity_Index.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		processed = 0;
		
		table_parameters.setValueAt(d.hausnerRatio, SeDeMParameters.Hausner_Ratio.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (10 * d.hausnerRatio) / 3;
		table_parameters.setValueAt(processed, SeDeMParameters.Hausner_Ratio.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Hausner_Ratio.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
	
		table_parameters.setValueAt(d.carrIndex, SeDeMParameters.Carrs_Index.ordinal(), 2);
		processed = d.carrIndex / 5;
		table_parameters.setValueAt(processed, SeDeMParameters.Carrs_Index.ordinal(), 3);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Carrs_Index.ordinal(), 4);
	
		table_parameters.setValueAt(d.angleOfResponse, SeDeMParameters.Angle_of_Repose.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - ( d.angleOfResponse / 5 );
		table_parameters.setValueAt(processed, SeDeMParameters.Angle_of_Repose.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Angle_of_Repose.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.cohesionIndex, SeDeMParameters.Cohesion_Index.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = d.cohesionIndex / 20;
		table_parameters.setValueAt(processed, SeDeMParameters.Cohesion_Index.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Cohesion_Index.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.hygroscopicity, SeDeMParameters.Hygroscopicity.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (d.hygroscopicity / 2);
		table_parameters.setValueAt(processed, SeDeMParameters.Hygroscopicity.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Hygroscopicity.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.interParticlePorosity, SeDeMParameters.Inter_particle_Porosity.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = (d.interParticlePorosity * 10) / 1.2f;
		table_parameters.setValueAt(processed, SeDeMParameters.Inter_particle_Porosity.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Inter_particle_Porosity.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.lossOnDrying, SeDeMParameters.Loss_on_Drying.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - d.lossOnDrying;
		table_parameters.setValueAt(processed, SeDeMParameters.Loss_on_Drying.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Loss_on_Drying.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.powderFlow, SeDeMParameters.Powder_Flow.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (d.powderFlow / 2);
		table_parameters.setValueAt(processed, SeDeMParameters.Powder_Flow.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Powder_Flow.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
		
		table_parameters.setValueAt(d.particlesSmallerThan45um, SeDeMParameters.Particles_smaller_than_45µm.ordinal(), COL_NUM_PARAM_TABLE_ACTUAL_VALUE);
		processed = 10 - (d.particlesSmallerThan45um / 5);
		table_parameters.setValueAt(processed, SeDeMParameters.Particles_smaller_than_45µm.ordinal(), COL_NUM_PARAM_TABLE_PROCESSED_VALUE);
		table_parameters.setValueAt(Math.min(10, processed), SeDeMParameters.Particles_smaller_than_45µm.ordinal(), COL_NUM_PARAM_TABLE_GRAPH_VALUE);
	}

	public Map<String, SeDeMData> getProjectData() {
		return data;
	}
	
	public JTabbedPane getTabs() {
		return tabs;
	}


	

	


}
