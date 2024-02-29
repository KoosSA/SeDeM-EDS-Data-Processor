package app.data.sedemParameterData;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class CohesionIndexData.
 */
public class CohesionIndexData {
	
	/** The hardness. */
	public List<Float> hardness;
	
	/** The data size. */
	public int dataSize;
	
	/**
	 * Clear.
	 */
	public void clear() {
		try {
			hardness.clear();
			dataSize = 0;
		} catch (Exception e) {
			
		}
	}

}
