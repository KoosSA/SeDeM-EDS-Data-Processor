package app.data.sedemParameterData;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class LossOnDryingData.
 */
public class LossOnDryingData {
	
	/** The mass before. */
	public List<Float> mass_before;
	
	/** The mass after. */
	public List<Float> mass_after;
	
	/** The data size. */
	public int dataSize;
	
	/** The mass container. */
	public List<Float> mass_container;
	
	/**
	 * Clear.
	 */
	public void clear() {
		try {
			mass_after.clear();
			mass_before.clear();
			mass_container.clear();
			dataSize = 0;
		} catch (Exception e) {
			
		}
	}

}
