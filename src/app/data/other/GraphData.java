package app.data.other;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import app.data.SeDeMData;
import app.data.lists.SeDeMParameters;
import app.graphical.MainFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphData.
 */
public class GraphData {
	
	/** The data. */
	private List<Float> data;
	
	/** The color. */
	private Color color;
	
	/**
	 * Instantiates a new graph data.
	 *
	 * @param name the name
	 * @param color the color
	 */
	public GraphData(String name, Color color) {
		SeDeMData sedemData = MainFrame.getProjectData().get(name);
		data = generate(sedemData);
		this.color = color;
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
	 * Gets the data.
	 *
	 * @return the data
	 */
	public List<Float> getData() {
		return data;
	}
	
	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Generate.
	 *
	 * @param d the d
	 * @return the list
	 */
	private List<Float> generate(SeDeMData d) {
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
		values.set(SeDeMParameters.Particles_smaller_than_45um.ordinal(), processed);
		processed = 0;
		
		return values;
	}
	
	

}
