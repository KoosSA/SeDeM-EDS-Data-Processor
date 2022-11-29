package app.graphical.correctiveExcipient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.data.SeDeMData;
import app.data.lists.SeDeMIndices;
import app.graphical.MainFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class CorrectiveExcipientFrame.
 */
public class CorrectiveExcipientFrame extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5764330672920163598L;
	
	/** The calculation display. */
	private JTextArea calculationDisplay;
	
	/** The root. */
	private JPanel root;

	/**
	 * Instantiates a new corrective excipient frame.
	 *
	 * @param name the name
	 * @param main the main
	 */
	public CorrectiveExcipientFrame(String name, MainFrame main) {
		setTitle(name);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		construct();

		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Construct.
	 */
	private void construct() {
		root = new JPanel();
		root.setLayout(new BorderLayout());
		root.setLayout(new BorderLayout(0, 0));
		root.add(createCalculationDisplay(), BorderLayout.CENTER);
		root.add(createBottomBar(), BorderLayout.SOUTH);
		root.add(createSelector(), BorderLayout.NORTH);
		getContentPane().add(root);
	}

	/**
	 * Creates the bottom bar.
	 *
	 * @return the j panel
	 */
	private JPanel createBottomBar() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JButton savebtn = new JButton("Save Calculation");
		panel.add(savebtn);
		savebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser ch = new JFileChooser();
				if (ch.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
					try {
						File f = new File(ch.getSelectedFile() + ".png");
						ImageIO.write(createImage(root), "png", f);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JButton closebtn = new JButton("Close");
		panel.add(closebtn);
		closebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		return panel;
	}

	/**
	 * Creates the selector.
	 *
	 * @return the j panel
	 */
	private JPanel createSelector() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		JComboBox<String> toCorrect = new JComboBox<>();
		JComboBox<String> correctWith = new JComboBox<String>();
		MainFrame.getProjectData().keySet().forEach(key -> {
			toCorrect.addItem(key);
			correctWith.addItem(key);
		});
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("Correct ");
		panel.add(lblNewLabel);
		panel.add(toCorrect);

		JLabel lblNewLabel_1 = new JLabel("with");
		panel.add(lblNewLabel_1);
		panel.add(correctWith);

		JButton btnNewButton = new JButton("Recalculate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCalculation(correctWith.getSelectedItem().toString(), toCorrect.getSelectedItem().toString());
			}
		});
		toCorrect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				doCalculation(correctWith.getSelectedItem().toString(), toCorrect.getSelectedItem().toString());
			}
		});
		correctWith.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				doCalculation(correctWith.getSelectedItem().toString(), toCorrect.getSelectedItem().toString());
			}
		});
		btnNewButton.setBackground(new Color(51, 204, 51));
		panel.add(btnNewButton);
		return panel;
	}

	/**
	 * Creates the calculation display.
	 *
	 * @return the j panel
	 */
	private JPanel createCalculationDisplay() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(new BorderLayout(0, 0));
		JScrollPane scroll = new JScrollPane();
		JTextArea textArea = new JTextArea();
		textArea.setEnabled(true);
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		panel.add(scroll);
		scroll.setViewportView(textArea);
		calculationDisplay = textArea;
		return panel;
	}

	/**
	 * Do calculation.
	 *
	 * @param with the with
	 * @param to the to
	 */
	private void doCalculation(String with, String to) {
		StringBuilder log = new StringBuilder();
		SeDeMData withData = MainFrame.getProjectData().get(with);
		SeDeMData toData = MainFrame.getProjectData().get(to);
		float cp = 0;
		
		for (int i = 0; i < SeDeMIndices.values().length; i++) {
			SeDeMIndices index = SeDeMIndices.values()[i];
			float t = toData.getIndexValue(index);
			float w = withData.getIndexValue(index);
			if (t < 5) {
				log.append(index + " must be corrected. \n");
				log.append("%CP = 100 - { [ (RE - R) / (RE - RP) ] * 100 } \n");
				float tempCP = 100 - (((w - 5) / (w - t)) * 100);
				log.append("%CP = 100 - { [ (" + w + " - " + 5 + ") / (" + w + " - " + t + ") ] * 100 } \n");
				log.append("%CP = " + tempCP + " \n \n \n");
				if (tempCP > cp) {
					cp = tempCP;
				}
			} else {
				log.append(index + " is already acceptable. \n \n");
			}
		}
		
		log.append("Thus the final amount of " + with + " to correct " + to + " should be " + cp + "% \n");
		calculationDisplay.setText("");
		calculationDisplay.setText(log.toString());
		
	}
	
	/**
	 * Creates the image.
	 *
	 * @param panel the panel
	 * @return the buffered image
	 */
	private BufferedImage createImage(JPanel panel) {
	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    g.dispose();
	    return bi;
	}

}
