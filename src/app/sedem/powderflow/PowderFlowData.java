package app.sedem.powderflow;

import java.util.List;

public class PowderFlowData {
	
	public List<Float> time;
	public List<Float> mass;
	public List<Float> cone_height;
	public List<Float> cone_radius;
	public int dataSize;
	
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
