package app.utils;

import java.util.List;

import app.data.lists.SeDeMParameters;

// TODO: Auto-generated Javadoc
/**
 * The Class Calculator.
 */
public class Calculator {

	/**
	 * Calculate dimension index.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateDimensionIndex(List<Float> processedValues) {
		float ans = (processedValues.get(SeDeMParameters.Bulk_Density.ordinal())
				+ processedValues.get(SeDeMParameters.Tapped_Density.ordinal())) / 2;
		return ans;
	}

	/**
	 * Calculate compresibility index.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateCompresibilityIndex(List<Float> processedValues) {
		float ans = (processedValues.get(SeDeMParameters.Inter_particle_Porosity.ordinal())
				+ processedValues.get(SeDeMParameters.Carrs_Index.ordinal())
				+ processedValues.get(SeDeMParameters.Cohesion_Index.ordinal())) / 3;
		return ans;
	}

	/**
	 * Calculate flowability index.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateFlowabilityIndex(List<Float> processedValues) {
		float ans = (processedValues.get(SeDeMParameters.Hausner_Ratio.ordinal())
				+ processedValues.get(SeDeMParameters.Powder_Flow.ordinal())
				+ processedValues.get(SeDeMParameters.Angle_of_Repose.ordinal())) / 3;
		return ans;
	}

	/**
	 * Calculate lubricity stability index.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateLubricityStabilityIndex(List<Float> processedValues) {
		float ans = (processedValues.get(SeDeMParameters.Loss_on_Drying.ordinal())
				+ processedValues.get(SeDeMParameters.Hygroscopicity.ordinal())) / 2;
		return ans;
	}

	/**
	 * Calculate lubricity dosage index.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateLubricityDosageIndex(List<Float> processedValues) {
		float ans = (processedValues.get(SeDeMParameters.Particles_smaller_than_45um.ordinal())
				+ processedValues.get(SeDeMParameters.Homogeneity_Index.ordinal())) / 2;
		return ans;
	}

	/**
	 * Calculate IP.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateIP(List<Float> processedValues) {
		float sum = 0;
		for (int i = 0; i < SeDeMParameters.values().length; i++) {
			if (parameterPass(processedValues, SeDeMParameters.values()[i])) {
				sum++;
			}
		}
		return sum / SeDeMParameters.values().length;
	}

	/**
	 * Calculate IPP.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateIPP(List<Float> processedValues) {
		float sum = 0;
		for (int i = 0; i < SeDeMParameters.values().length; i++) {
			sum += processedValues.get(SeDeMParameters.values()[i].ordinal());
		}
		return sum / SeDeMParameters.values().length;
	}

	/**
	 * Calculate IGC.
	 *
	 * @param processedValues the processed values
	 * @return the float
	 */
	public static float calculateIGC(List<Float> processedValues) {
		return calculateIPP(processedValues) * 0.952f;
	}

	/**
	 * Checks if a paramater pass SeDeM criteria.
	 *
	 * @param processedValues the processed values
	 * @param parameter       the parameter
	 * @return true, if successful
	 */
	private static boolean parameterPass(List<Float> processedValues, SeDeMParameters parameter) {
		if (processedValues.get(parameter.ordinal()) >= 5) {
			return true;
		}
		return false;
	}

	public static double calculateSeDeMValue(double actualValue, double factor, double min, double max) {
		return Math.clamp(actualValue * factor, min, max);
	}

}
