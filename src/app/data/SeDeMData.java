package app.data;

import app.data.lists.SeDeMIndices;
import app.data.sedemParameterData.CohesionIndexData;
import app.data.sedemParameterData.DensityData;
import app.data.sedemParameterData.HomogenityData;
import app.data.sedemParameterData.HygroscopicityData;
import app.data.sedemParameterData.LossOnDryingData;
import app.data.sedemParameterData.PowderFlowData;

public class SeDeMData {
	
	public HomogenityData homogenietyData;
	public DensityData densityData;
	public PowderFlowData flowData;
	public CohesionIndexData cohesionIndexData;
	public LossOnDryingData lossOnDryingData;
	public HygroscopicityData hygroscopicityData;
	
	public float homogeneityIndex;
	public float bulkDensity;
	public float tappedDensity;
	public float cohesionIndex;
	public float carrIndex;
	public float hausnerRatio;
	public float interParticlePorosity;
	public float powderFlow;
	public float angleOfResponse;
	public float lossOnDrying;
	public float particlesSmallerThan45um;
	public float hygroscopicity;
	
	public float dimension_index;
	public float compressibility_index;
	public float flowability_index;
	public float lubricity_stability_index;
	public float lubricity_dosage_index;
	
	public float IP;
	public float IPP;
	public float IGC;
	
	public float getIndexValue(SeDeMIndices index) {
		switch (index) {
		case Dimention:
			return dimension_index;
		case Compressibility:
			return compressibility_index;
		case Flowability:
			return flowability_index;
		case Lubricity_Dosage:
			return lubricity_dosage_index;
		case Lubricity_Stability:
			return lubricity_stability_index;
		default:
			return 10;
		}
	}
}
