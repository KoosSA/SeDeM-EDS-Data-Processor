package app.sedem.lossondrying;

import java.util.List;

public class LODData {
	
	public List<Float> mass_before;
	public List<Float> mass_after;
	public int dataSize;
	
	public void clear() {
		try {
			mass_after.clear();
			mass_before.clear();
			dataSize = 0;
		} catch (Exception e) {
			
		}
	}

}
