package koossa.sedem.app.utils;

import app.data.lists.SeDeMParameters;
import javafx.collections.ObservableList;
import koossa.sedem.app.sedemData.SeDeMParameter;

public class FxCalculator {

	public static double calculateDimensionIndex(ObservableList<SeDeMParameter> processedValues) {
		return (processedValues.get(SeDeMParameters.Bulk_Density.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Tapped_Density.ordinal()).getSedemValue()) / 2;
	}

	public static double calculateCompresibilityIndex(ObservableList<SeDeMParameter> processedValues) {
		return (processedValues.get(SeDeMParameters.Inter_particle_Porosity.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Carrs_Index.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Cohesion_Index.ordinal()).getSedemValue()) / 3;
	}

	public static double calculateFlowabilityIndex(ObservableList<SeDeMParameter> processedValues) {
		return (processedValues.get(SeDeMParameters.Hausner_Ratio.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Powder_Flow.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Angle_of_Repose.ordinal()).getSedemValue()) / 3;

	}

	public static double calculateLubricityStabilityIndex(ObservableList<SeDeMParameter> processedValues) {
		return (processedValues.get(SeDeMParameters.Loss_on_Drying.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Hygroscopicity.ordinal()).getSedemValue()) / 2;
	}

	public static double calculateLubricityDosageIndex(ObservableList<SeDeMParameter> processedValues) {
		return (processedValues.get(SeDeMParameters.Particles_smaller_than_45um.ordinal()).getSedemValue()
				+ processedValues.get(SeDeMParameters.Homogeneity_Index.ordinal()).getSedemValue()) / 2;
	}

	public static double calculateIP(ObservableList<SeDeMParameter> processedValues) {
		double sum = 0;
		for (int i = 0; i < SeDeMParameters.values().length; i++) {
			if (parameterPass(processedValues, SeDeMParameters.values()[i])) {
				sum++;
			}
		}
		return sum / SeDeMParameters.values().length;
	}

	public static double calculateIPP(ObservableList<SeDeMParameter> processedValues) {
		double sum = 0;
		for (int i = 0; i < SeDeMParameters.values().length; i++) {
			sum += processedValues.get(SeDeMParameters.values()[i].ordinal()).getSedemValue();
		}
		return sum / SeDeMParameters.values().length;
	}

	public static double calculateIGC(ObservableList<SeDeMParameter> processedValues) {
		return calculateIPP(processedValues) * 0.952f;
	}

	private static boolean parameterPass(ObservableList<SeDeMParameter> processedValues, SeDeMParameters parameter) {
		if (processedValues.get(parameter.ordinal()).getSedemValue() >= 5) {
			return true;
		}
		return false;
	}

}
