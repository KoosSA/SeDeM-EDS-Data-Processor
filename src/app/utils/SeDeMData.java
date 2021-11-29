package app.utils;

import app.sedem.cohesionIndex.CohesionIndexData;
import app.sedem.density.DensityData;
import app.sedem.homogeneity.HomogenityData;
import app.sedem.hygroscopicity.HygroData;
import app.sedem.lossondrying.LODData;
import app.sedem.powderflow.PowderFlowData;

public class SeDeMData {
	
	public HomogenityData homogenietyData;
	public DensityData densityData;
	public PowderFlowData flowData;
	public CohesionIndexData cohesionIndexData;
	public LODData lossOnDryingData;
	public HygroData hygroscopicityData;
	
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
	

}
