package app.sedem.parameters.hygroscopicity;

import java.util.List;

public class HygroData {
	
	public List<Float> mass_before;
	public List<Float> mass_after;
	public List<Float> mass_container;
	public int dataSize;
	
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
