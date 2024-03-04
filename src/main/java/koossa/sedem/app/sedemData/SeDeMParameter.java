package koossa.sedem.app.sedemData;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class SeDeMParameter {
	
	public SeDeMParameter(String parameterName, String key, double actual, double sedem, double chart) {
		this.parameter  = new SimpleStringProperty(parameterName);
		this.key = new SimpleStringProperty(key);
		this.actualValue = new SimpleDoubleProperty(actual);
		this.chartValue = new SimpleDoubleProperty(chart);
		this.sedemValue = new SimpleDoubleProperty(sedem);
	}
	
	private SimpleStringProperty parameter;
	private SimpleStringProperty key;
	private SimpleDoubleProperty actualValue;
	private SimpleDoubleProperty sedemValue;
	private SimpleDoubleProperty chartValue;
	
	public double getActualValue() {
		return actualValue.get();
	}
	
	public double getChartValue() {
		return chartValue.get();
	}
	
	public String getParameter() {
		return parameter.get();
	}
	
	public String getKey() {
		return key.get();
	}
	
	public double getSedemValue() {
		return sedemValue.get();
	}

	public void setParameter(String parameter) {
		this.parameter.set(parameter);
	}

	public void setKey(String key) {
		this.key.set(key);
	}

	public void setActualValue(double actualValue) {
		this.actualValue.set(actualValue);
	}

	public void setSedemValue(double sedemValue) {
		this.sedemValue.set(sedemValue);
	}

	public void setChartValue(double chartValue) {
		this.chartValue.set(chartValue);
	}

	public void setValues(double actualValue, double sedemValue, double chartValue) {
		setActualValue(actualValue);
		setSedemValue(sedemValue);
		setChartValue(chartValue);
	}
	
	

}
