package app.data.sedemParameterData;

import java.util.List;

public class LossOnDryingData {
	
	public List<Float> mass_before;
	public List<Float> mass_after;
	public int dataSize;
	public List<Float> mass_container;
	
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
