package koossa.sedem.app.graphics;

import java.util.ArrayList;
import java.util.List;

import app.data.lists.SeDeMParameters;
import app.graphical.SeDeMPolygon;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import koossa.sedem.app.sedemData.ExcipientSeDeMData;
import koossa.sedem.app.sedemData.SeDeMIndex;
import koossa.sedem.app.sedemData.SeDeMParameter;
import koossa.sedem.app.utils.FxCalculator;

public class SedemParameterViewController {

	@FXML
	TableView<SeDeMParameter> parameterTable;
	static ObservableList<SeDeMParameter> parameterData = FXCollections.observableArrayList();
	@FXML
	TableView<SeDeMIndex> indexTable;
	static ObservableList<SeDeMIndex> indexData = FXCollections.observableArrayList();
	@FXML
	SwingNode graphPanel;

	static ObservableMap<SeDeMParameters, Double> excipientData = FXCollections.observableHashMap();
	static String currentName = "";

	public void initialize() {
		parameterTable.setItems(parameterData);
		parameterTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("parameter"));
		parameterTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("key"));
		parameterTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("actualValue"));
		parameterTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("sedemValue"));
		parameterTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("chartValue"));
		parameterData.add(SeDeMParameters.Bulk_Density.ordinal(), new SeDeMParameter("Bulk Density", "Da", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Tapped_Density.ordinal(), new SeDeMParameter("Tapped Density", "Dc", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Inter_particle_Porosity.ordinal(), new SeDeMParameter("Inter Particle Porosity", "Ie", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Carrs_Index.ordinal(), new SeDeMParameter("Carr's Index", "Ic", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Cohesion_Index.ordinal(), new SeDeMParameter("Cohesion Index", "Icd", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Hausner_Ratio.ordinal(), new SeDeMParameter("Hausner Ratio", "HR", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Angle_of_Repose.ordinal(), new SeDeMParameter("Angle of Repose", "AR", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Powder_Flow.ordinal(), new SeDeMParameter("Powder Flow", "t\"", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Loss_on_Drying.ordinal(), new SeDeMParameter("Loss on Drying", "%HR", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Hygroscopicity.ordinal(), new SeDeMParameter("Hygroscopicity", "%H", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Particles_smaller_than_45um.ordinal(), new SeDeMParameter("Particles < 45um", "Pf", Double.NaN, Double.NaN, Double.NaN));
		parameterData.add(SeDeMParameters.Homogeneity_Index.ordinal(), new SeDeMParameter("Homogeneiety", "HI", Double.NaN, Double.NaN, Double.NaN));
		
		indexTable.setItems(indexData);
		indexTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("index"));
		indexTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("key"));
		indexTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("value"));
		indexTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("result"));
		indexData.add(new SeDeMIndex("Dimension", "", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Compresibility", "", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Flowability", "", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Lubricity/Stability", "", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Lubricity/Dosage", "", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Parameter Index", "IP", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Index of Profile Parameter", "IPP", Double.NaN, ""));
		indexData.add(new SeDeMIndex("Index of Good Compresibility", "IGC", Double.NaN, ""));

		excipientData.addListener(new MapChangeListener<SeDeMParameters, Double>() {
			@Override
			public void onChanged(Change<? extends SeDeMParameters, ? extends Double> c) {
				double processed = excipientData.getOrDefault(SeDeMParameters.Bulk_Density, 0.0) * 10;
				parameterData.get(SeDeMParameters.Bulk_Density.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Bulk_Density, 0.0), processed,
						Math.clamp(processed, 0, 10));
				processed = excipientData.getOrDefault(SeDeMParameters.Tapped_Density, 0.0) * 10;
				parameterData.get(SeDeMParameters.Tapped_Density.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Tapped_Density, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = (excipientData.getOrDefault(SeDeMParameters.Inter_particle_Porosity, 0.0) * 10) / 1.2f;
				parameterData.get(SeDeMParameters.Inter_particle_Porosity.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Inter_particle_Porosity, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = 10 - (10 * excipientData.getOrDefault(SeDeMParameters.Hausner_Ratio, 0.0)) / 3;
				parameterData.get(SeDeMParameters.Hausner_Ratio.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Hausner_Ratio, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = excipientData.getOrDefault(SeDeMParameters.Carrs_Index, 0.0) / 5;
				parameterData.get(SeDeMParameters.Carrs_Index.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Carrs_Index, 0.0), processed,
						Math.clamp(processed, 0, 10));
				processed = 10 - (excipientData.getOrDefault(SeDeMParameters.Angle_of_Repose, 0.0) / 5);
				parameterData.get(SeDeMParameters.Angle_of_Repose.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Angle_of_Repose, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = excipientData.getOrDefault(SeDeMParameters.Cohesion_Index, 0.0) / 20;
				parameterData.get(SeDeMParameters.Cohesion_Index.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Cohesion_Index, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = 10 - (excipientData.getOrDefault(SeDeMParameters.Hygroscopicity, 0.0) / 2);
				parameterData.get(SeDeMParameters.Hygroscopicity.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Hygroscopicity, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = 10 - (excipientData.getOrDefault(SeDeMParameters.Powder_Flow, 0.0) / 2);
				parameterData.get(SeDeMParameters.Powder_Flow.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Powder_Flow, 0.0), processed,
						Math.clamp(processed, 0, 10));
				processed = 10 - excipientData.getOrDefault(SeDeMParameters.Loss_on_Drying, 0.0);
				parameterData.get(SeDeMParameters.Loss_on_Drying.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Loss_on_Drying, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = excipientData.getOrDefault(SeDeMParameters.Homogeneity_Index, 0.0) * 500;
				parameterData.get(SeDeMParameters.Homogeneity_Index.ordinal()).setValues(excipientData.getOrDefault(SeDeMParameters.Homogeneity_Index, 0.0),
						processed, Math.clamp(processed, 0, 10));
				processed = 10 - (excipientData.getOrDefault(SeDeMParameters.Particles_smaller_than_45um, 0.0) / 5);
				parameterData.get(SeDeMParameters.Particles_smaller_than_45um.ordinal()).setValues(
						excipientData.getOrDefault(SeDeMParameters.Particles_smaller_than_45um, 0.0), processed,
						Math.clamp(processed, 0, 10));
			
				indexData.get(0).setValue(FxCalculator.calculateDimensionIndex(parameterData));
				indexData.get(1).setValue(FxCalculator.calculateCompresibilityIndex(parameterData));
				indexData.get(2).setValue(FxCalculator.calculateFlowabilityIndex(parameterData));
				indexData.get(3).setValue(FxCalculator.calculateLubricityStabilityIndex(parameterData));
				indexData.get(4).setValue(FxCalculator.calculateLubricityDosageIndex(parameterData));
				indexData.get(5).setValue(FxCalculator.calculateIP(parameterData));
				indexData.get(6).setValue(FxCalculator.calculateIPP(parameterData));
				indexData.get(7).setValue(FxCalculator.calculateIGC(parameterData));
				
				for (int i = 0; i < 5; i++) {
					indexData.get(i).setResult((indexData.get(i).getValue() >= 5 ? "PASS" : "FAIL"));
				}
				indexData.get(5).setResult((indexData.get(5).getValue() >= 0.5 ? "PASS" : "FAIL"));
				indexData.get(6).setResult((indexData.get(6).getValue() >= 5 ? "PASS" : "FAIL"));
				indexData.get(7).setResult((indexData.get(7).getValue() >= 5 ? "PASS" : "FAIL"));
				
				List<Float> lst = new ArrayList<Float>();
				parameterData.forEach(param -> {
					lst.add((float) param.getChartValue());
				});
				graphPanel.setContent(SeDeMPolygon.createChartPanel(lst, currentName));
			}
		});

	}
	
	public static void updateData(ExcipientSeDeMData data) {
		excipientData.clear();
		currentName = data.excipientName;
		excipientData.putAll(data.parameters);
		
	}

}
