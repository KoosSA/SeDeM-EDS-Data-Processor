package app.graphical;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import app.sedem.SeDeMParameters;

public class SeDeMPolygon {
	
    private static CategoryDataset createDataset(List<Float> data, String name) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //for (int i = 1; i <= 1; i++) {
        for (int j = 0; j < SeDeMParameters.values().length; j++) {
    		Comparable<?> colKey = SeDeMParameters.values()[j];
    		dataset.addValue(5, "Passing line", colKey);
    	}
            //String rowKey = "Observation " + name;
            for (int j = 0; j < SeDeMParameters.values().length; j++) {
                Comparable<?> colKey = SeDeMParameters.values()[j];
                dataset.addValue(data.get(j), name, colKey);
            }
        //}
            
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset, String title, List<Color> colors) {
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        plot.setMaxValue(10);
        plot.setBaseSeriesOutlineStroke(new BasicStroke(2));
        plot.setWebFilled(true);
        plot.setSeriesPaint(0, Color.BLACK);
        plot.setSeriesOutlineStroke(0, new BasicStroke(1));
        if (colors != null) {
        	for (int i = 0; i < colors.size(); i++) {
        		plot.setSeriesPaint(i + 1, colors.get(i));
			}
        }
        
        JFreeChart chart = new JFreeChart("SeDeM Graph: " + title, plot);
        
        return chart;
    }

    public static JPanel createChartPanel(List<Float> data, String title) {
        JFreeChart jfreechart = createChart(createDataset(data, title), title, null);
        return new ChartPanel(jfreechart);
    }
    
    
    
    
    
    public static JPanel createChartPanel(Map<String, List<Float>> data, String chartName, List<Color> colors) {
        JFreeChart jfreechart = createChart(createDataset(data), chartName, colors);
        return new ChartPanel(jfreechart);
    }
    
    private static CategoryDataset createDataset(Map<String, List<Float>> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int j = 0; j < SeDeMParameters.values().length; j++) {
    		Comparable<?> colKey = SeDeMParameters.values()[j];
    		dataset.addValue(5, "Passing line", colKey);
    	}
        data.keySet().forEach(name -> {
        	for (int j = 0; j < SeDeMParameters.values().length; j++) {
        		Comparable<?> colKey = SeDeMParameters.values()[j];
        		dataset.addValue(data.get(name).get(j), name, colKey);
        	}
        });
        
        return dataset;
    }
	
}
