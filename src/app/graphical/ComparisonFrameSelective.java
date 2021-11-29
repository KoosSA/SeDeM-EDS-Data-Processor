package app.graphical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;

import app.sedem.SeDeMParameters;
import app.utils.SeDeMData;

public class ComparisonFrameSelective extends JFrame {

	private static final long serialVersionUID = 2909389028655044044L;
	private MainFrame main;
	private JTabbedPane tabs;
	private Map<String, List<Float>> grd = new HashMap<String, List<Float>>();
	private JPanel panel = new JPanel();
	private JPanel panel_selection;
	private Map<String, JCheckBox> checks = new HashMap<>();
	private Map<String, JButton> color_btns = new HashMap<>();
	private int width = 800, height = 600;
	private Random rand;
	private List<Color> colors = new ArrayList<>(); 
	
	public ComparisonFrameSelective(String name, MainFrame main) {
		getContentPane().setLayout(new BorderLayout(0, 0));
		setTitle(name);
		this.main = main;
		rand = new Random(1234567890);
		tabs = main.getTabs();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, Color.GRAY, null, Color.BLACK));
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
//		JPanel p2 = new JPanel();
//		p2.setBackground(Color.LIGHT_GRAY);
//		p2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, Color.BLACK));
//		
//		getContentPane().add(p2, BorderLayout.WEST);
//		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		
		JPanel p3 = new JPanel();
		p3.setBackground(Color.LIGHT_GRAY);
		JButton btn_update = new JButton("Draw");
		btn_update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		p3.add(btn_update);
		getContentPane().add(p3, BorderLayout.SOUTH);
		
		panel_selection = new JPanel();
		panel_selection.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.BLACK));
		getContentPane().add(panel_selection, BorderLayout.WEST);
		panel_selection.setLayout(new BoxLayout(panel_selection, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < tabs.getComponentCount(); i++) {
			JPanel p = new JPanel();
			String target = tabs.getComponent(i).getName();
			//int index = i;
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			JCheckBox cb = new JCheckBox(tabs.getComponent(i).getName(), false);
			p.setAlignmentX(LEFT_ALIGNMENT);
			cb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					update();
				}
			});;
			rand.setSeed(1234567890 * (i + 12345));
			JButton cc = new JButton();
			Color c = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
			cc.setBackground(c);
			cc.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color col = JColorChooser.showDialog(null, "Color picker", c);
					cc.setBackground(col);
					update();
				}
			});
			color_btns.putIfAbsent(target, cc);
			p.add(cb);
			p.add(cc);
			panel_selection.add(p);
			checks.putIfAbsent(target, cb);
		}
		
		//update();
		setVisible(true);
		setSize(width, height);
		setLocationRelativeTo(null);
		
		
	}
	
	private void update() {
		width = getWidth();
		height = getHeight();
		colors.clear();
		Map<String, SeDeMData> data = main.getProjectData();
		grd.clear();
		for (int i = 0; i < tabs.getComponentCount(); i++) {
			String name = tabs.getComponent(i).getName();
			if (checks.get(name).isSelected()) {
				//System.out.println(name);;
				grd.put(name, doCalcs(data.get(name)));
				colors.add(color_btns.get(name).getBackground());
			}
		}
		
		getContentPane().remove(panel);
		panel = SeDeMPolygon.createChartPanel(grd, "Comparison chart", colors);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setSize(width, height);
		
	}
	
	private List<Float> doCalcs(SeDeMData d) {
		float processed = 0;
		List<Float> values = new ArrayList<Float>(12);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		values.add(0f);
		
		
		processed = d.bulkDensity * 10;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Bulk_Density.ordinal(), processed);
		processed = 0;
		
		processed = d.tappedDensity * 10;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Tapped_Density.ordinal(), processed);
		processed = 0;
		
		processed = d.homogeneityIndex * 500;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Homogeneity_Index.ordinal(), processed);
		processed = 0;
		
		processed = 10 - (10 * d.hausnerRatio) / 3;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Hausner_Ratio.ordinal(), processed);
		processed = 0;
		
		processed = d.carrIndex / 5;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Carrs_Index.ordinal(), processed);
		processed = 0;
		
		processed = 10 - ( d.angleOfResponse / 5 );
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Angle_of_Repose.ordinal(), processed);
		processed = 0;
		
		processed = d.cohesionIndex / 20;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Cohesion_Index.ordinal(), processed);
		processed = 0;
		
		processed = 10 - (d.hygroscopicity / 2);
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Hygroscopicity.ordinal(), processed);
		processed = 0;
		
		processed = (d.interParticlePorosity * 10) / 1.2f;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Inter_particle_Porosity.ordinal(), processed);
		processed = 0;
		
		processed = 10 - d.lossOnDrying;
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Loss_on_Drying.ordinal(), processed);
		processed = 0;
		
		processed = 10 - (d.powderFlow / 2);
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Powder_Flow.ordinal(), processed);
		processed = 0;
		
		processed = 10 - (d.particlesSmallerThan45um / 5);
		processed = Math.min(10, processed);
		values.set(SeDeMParameters.Particles_smaller_than_45µm.ordinal(), processed);
		processed = 0;
		
		return values;
	}
    

}
