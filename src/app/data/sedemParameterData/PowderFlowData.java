package app.data.sedemParameterData;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class PowderFlowData.
 */
public class PowderFlowData {
	
	/** The time. */
	public List<Float> time;
	
	/** The mass. */
	public List<Float> mass;
	
	/** The cone height. */
	public List<Float> cone_height;
	
	/** The cone radius. */
	public List<Float> cone_radius;
	
	/** The data size. */
	public int dataSize;
	
	/**
	 * Clear.
	 */
	public void clear() {
		try {
			time.clear();
			mass.clear();
			cone_height.clear();
			cone_radius.clear();
			dataSize = 0;
		} catch (Exception e) {
			
		}
	}

}
