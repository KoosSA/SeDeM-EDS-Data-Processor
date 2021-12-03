package app.graphical;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class Selector extends JPanel {
	
	private static final long serialVersionUID = -8997522860143684443L;
	
	private JCheckBox checkbox = new JCheckBox();
	private Button color_button = new Button();
	private Color color = Color.BLACK;
	private Selector instance;
	private Random random = new Random();
	
	public Selector(String name) {
		instance = this;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setName(name);
		checkbox.setText(name);
		checkbox.setSelected(false);
		add(checkbox);
		color_button.setMaximumSize(new Dimension(300, 20));
		color_button.setMinimumSize(new Dimension(200, 5));
		add(color_button);
		//random.setSeed(12358456 * ((name.length() + random.nextInt(1000)) * 125487));
		color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
		color_button.setBackground(color);
		color_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((color = JColorChooser.showDialog(instance, "Color picker", color)) == null) {
					color = Color.BLACK;
				}
				color_button.setBackground(color);
			}
		});
	}
	
	public void setOnCheckboxValueChanged(ActionListener actionListener) {
		checkbox.addActionListener(actionListener);
	}
	
	public boolean isSelected() {
		return checkbox.isSelected();
	}
	
	public Color getColor() {
		return color;
	}

	public void addColorChangeListenor(PropertyChangeListener propertyChangeListener) {
		color_button.addPropertyChangeListener("background", propertyChangeListener);
	}
	
	

}
