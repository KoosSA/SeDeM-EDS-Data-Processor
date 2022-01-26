package app.graphical.comparisonGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import app.data.other.GraphData;
import app.graphical.MainFrame;
import app.graphical.SeDeMPolygon;

public class ComparisonFrameSelective extends JFrame {

	private static final long serialVersionUID = 2909389028655044044L;
	private JTabbedPane tabs;
	private Map<String, GraphData> graph_data = new HashMap<String, GraphData>();
	private JPanel panel_graph;
	private int width = 850, height = 600;
	
	public ComparisonFrameSelective(String name, MainFrame main) {
		getContentPane().setLayout(new BorderLayout(0, 0));
		setTitle(name);
		tabs = main.getTabs();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panel_graph = new JPanel();
		panel_graph.setLayout(new BorderLayout());
		add(panel_graph, BorderLayout.CENTER);
		
		add(createSelectionPanel(), BorderLayout.WEST);
		
		setVisible(true);
		setSize(width, height);
		setLocationRelativeTo(null);
		
	}
	
	private JScrollPane createSelectionPanel() {
		JScrollPane selection_scroll = new JScrollPane();
		JPanel panel_selection = new JPanel();
		panel_selection.setLayout(new BoxLayout(panel_selection, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < tabs.getComponentCount(); i++) {
			Selector selector = new Selector(tabs.getComponent(i).getName());
			panel_selection.add(selector);
			selector.setOnCheckboxValueChanged(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateGraphData(selector.getName(), selector.isSelected(), selector.getColor());
					updateGraph();
					//System.out.println(graph_data.keySet());
				}
			});
			selector.addColorChangeListenor(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					updateGraphData(selector.getName(), selector.isSelected(), selector.getColor());
					updateGraph();
				}
			});
		}
		
		selection_scroll.setViewportView(panel_selection);
		return selection_scroll;
	}
	

	private void updateGraph() {
		remove(panel_graph);
		
		panel_graph = SeDeMPolygon.createChartPanel("Comparison", graph_data);
		
		add(panel_graph, BorderLayout.CENTER);
		
		panel_graph.revalidate();
	}
	
	private void updateGraphData(String name, boolean isSelected, Color color) {
		graph_data.remove(name);
		if (isSelected) {
			graph_data.putIfAbsent(name, new GraphData(name, color));
		}
	}
	

    

}
