package app.data.sedemParameterData;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class HygroscopicityData.
 */
public class HygroscopicityData {
	
	/** The mass before. */
	public List<Float> mass_before;
	
	/** The mass after. */
	public List<Float> mass_after;
	
	/** The mass container. */
	public List<Float> mass_container;
	
	/** The data size. */
	public int dataSize;
	
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
