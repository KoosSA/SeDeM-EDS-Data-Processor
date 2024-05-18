package app.data;

import app.data.lists.SeDeMIndices;
import app.data.sedemParameterData.CohesionIndexData;
import app.data.sedemParameterData.DensityData;
import app.data.sedemParameterData.HomogenityData;
import app.data.sedemParameterData.HygroscopicityData;
import app.data.sedemParameterData.LossOnDryingData;
import app.data.sedemParameterData.PowderFlowData;

// TODO: Auto-generated Javadoc
/**
 * The Class SeDeMData.
 */
public class SeDeMData {
	
	/** The homogeniety data. */
	public HomogenityData homogenietyData;
	
	/** The density data. */
	public DensityData densityData;
	
	/** The flow data. */
	public PowderFlowData flowData;
	
	/** The cohesion index data. */
	public CohesionIndexData cohesionIndexData;
	
	/** The loss on drying data. */
	public LossOnDryingData lossOnDryingData;
	
	/** The hygroscopicity data. */
	public HygroscopicityData hygroscopicityData;
	
	/** The homogeneity index. */
	public float homogeneityIndex;
	
	/** The bulk density. */
	public float bulkDensity;
	
	/** The tapped density. */
	public float tappedDensity;
	
	/** The cohesion index. */
	public float cohesionIndex;
	
	/** The carr index. */
	public float carrIndex;
	
	/** The hausner ratio. */
	public float hausnerRatio;
	
	/** The inter particle porosity. */
	public float interParticlePorosity;
	
	/** The powder flow. */
	public float powderFlow;
	
	/** The angle of response. */
	public float angleOfResponse;
	
	/** The loss on drying. */
	public float lossOnDrying;
	
	/** The particles smaller than 45 um. */
	public float particlesSmallerThan45um;
	
	/** The hygroscopicity. */
	public float hygroscopicity;
	
	/** The dimension index. */
	public float dimension_index;
	
	/** The compressibility index. */
	public float compressibility_index;
	
	/** The flowability index. */
	public float flowability_index;
	
	/** The lubricity stability index. */
	public float lubricity_stability_index;
	
	/** The lubricity dosage index. */
	public float lubricity_dosage_index;
	
	/** The ip. */
	public float IP;
	
	/** The ipp. */
	public float IPP;
	
	/** The igc. */
	public float IGC;
	
	/**
	 * Gets the index value.
	 *
	 * @param index the index
	 * @return the index value
	 */
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
