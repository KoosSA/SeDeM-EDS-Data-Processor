package koossa.sedem.app.sedemData;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class SeDeMIndex {
	
	public SeDeMIndex(String indexName, String key, double value, String result) {
		this.index  = new SimpleStringProperty(indexName);
		this.key = new SimpleStringProperty(key);
		this.value = new SimpleDoubleProperty(value);
		this.result = new SimpleStringProperty(result);
	}
	
	private SimpleStringProperty index;
	private SimpleStringProperty key;
	private SimpleDoubleProperty value;
	private SimpleStringProperty result;
	
	public double getValue() {
		return value.get();
	}
	
	public String getIndex() {
		return index.get();
	}
	
	public String getKey() {
		return key.get();
	}
	
	public String getResult() {
		return result.get();
	}

	public void setIndex(String index) {
		this.index.set(index);
	}

	public void setKey(String key) {
		this.key.set(key);
	}

	public void setValue(double value) {
		this.value.set(value);
	}

	public void setResult(String result) {
		this.result.set(result);
	}
	
	

}
