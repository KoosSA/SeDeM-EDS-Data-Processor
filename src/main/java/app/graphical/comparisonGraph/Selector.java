package app.graphical.comparisonGraph;

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

// TODO: Auto-generated Javadoc
/**
 * The Class Selector.
 */
public class Selector extends JPanel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8997522860143684443L;
	
	/** The checkbox. */
	private JCheckBox checkbox = new JCheckBox();
	
	/** The color button. */
	private Button color_button = new Button();
	
	/** The color. */
	private Color color = Color.BLACK;
	
	/** The instance. */
	private Selector instance;
	
	/** The random. */
	private Random random = new Random();
	
	/**
	 * Instantiates a new selector.
	 *
	 * @param name the name
	 */
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
	
	/**
	 * Sets the on checkbox value changed.
	 *
	 * @param actionListener the new on checkbox value changed
	 */
	public void setOnCheckboxValueChanged(ActionListener actionListener) {
		checkbox.addActionListener(actionListener);
	}
	
	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return checkbox.isSelected();
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Adds the color change listenor.
	 *
	 * @param propertyChangeListener the property change listener
	 */
	public void addColorChangeListenor(PropertyChangeListener propertyChangeListener) {
		color_button.addPropertyChangeListener("background", propertyChangeListener);
	}
	
	

}
