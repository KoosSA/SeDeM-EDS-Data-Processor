package app.data.sedemParameterData;

import java.util.List;

public class CohesionIndexData {
	
	public List<Float> hardness;
	public int dataSize;
	
	public void clear() {
		try {
			hardness.clear();
			dataSize = 0;
		} catch (Exception e) {
			
		}
	}

}
