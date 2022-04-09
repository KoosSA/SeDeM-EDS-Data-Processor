package app.graphical;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import app.data.lists.SeDeMParameters;
import app.data.other.GraphData;

public class SeDeMPolygon {

    private static CategoryDataset createDataset(List<Float> data, String name) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //for (int i = 1; i <= 1; i++) {
//        for (int j = 0; j < SeDeMParameters.values().length; j++) {
//    		Comparable<?> colKey = SeDeMParameters.values()[j];
//    		dataset.addValue(5, "Passing line", colKey);
//    		dataset.addValue(10, "Max line", colKey);
//    	}
            //String rowKey = "Observation " + name;
            for (int j = 0; j < SeDeMParameters.values().length; j++) {
                Comparable<?> colKey = SeDeMParameters.values()[j];
                dataset.addValue(data.get(j), name, colKey);
            }
        //}

        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset, String title, List<Color> colors) {
        SpiderWebPlot plot = new SpiderWebPlot(dataset) {
			private static final long serialVersionUID = 9166744064889714872L;
			//put this many labels on each axis.
            private int ticks = DEFAULT_TICKS;
            private static final int DEFAULT_TICKS = 10;
            private NumberFormat format = NumberFormat.getInstance();
            //constant for creating perpendicular tick marks.
            private static final double PERPENDICULAR = 90;
            //the size of a tick mark, as a percentage of the entire line length.
            private static final double TICK_SCALE = 0.015;
            //the gap between the axis line and the numeric label itself.
            private int valueLabelGap = DEFAULT_GAP;
            private static final int DEFAULT_GAP = 10;
            //the threshold used for determining if something is "on" the axis
            private static final double THRESHOLD = 15;

            /**
             * {@inheritDoc}
             */
            @Override
            protected void drawLabel(final Graphics2D g2, final  Rectangle2D plotArea, final double value,
                                     final int cat, final double startAngle, final double extent) {
                super.drawLabel(g2, plotArea, value, cat, startAngle, extent);
                final FontRenderContext frc = g2.getFontRenderContext();
                final double[] transformed = new double[2];
                final double[] transformer = new double[2];
                final Arc2D arc1 = new Arc2D.Double(plotArea, startAngle, 0, Arc2D.OPEN);

                for(int i = 1;i <= ticks; i++){

                    final Point2D point1 = arc1.getEndPoint();

                    final double deltaX = plotArea.getCenterX();
                    final double deltaY = plotArea.getCenterY();
                    double labelX = point1.getX() - deltaX;
                    double labelY = point1.getY() - deltaY;

                    final double scale = ((double)i/(double)ticks);
                    final AffineTransform tx = AffineTransform.getScaleInstance(scale,scale);
                    //for getting the tick mark start points.
                    final AffineTransform pointTrans
                            = AffineTransform.getScaleInstance(scale + TICK_SCALE, scale + TICK_SCALE);
                    transformer[0] = labelX;
                    transformer[1] = labelY;
                    pointTrans.transform(transformer, 0, transformed, 0, 1);
                    final double pointX = transformed[0] + deltaX;
                    final double pointY = transformed[1] + deltaY;
                    tx.transform(transformer, 0, transformed, 0, 1);
                    labelX = transformed[0] + deltaX;
                    labelY = transformed[1] + deltaY;



                    double rotated = (PERPENDICULAR);

                    AffineTransform rotateTrans
                            = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);
                    transformer[0] = pointX;
                    transformer[1] = pointY;
                    rotateTrans.transform(transformer, 0, transformed, 0, 1);
                    final double x1 = transformed[0];
                    final double y1 = transformed[1];

                    rotated = (-PERPENDICULAR);
                    rotateTrans = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);

                    rotateTrans.transform(transformer, 0, transformed, 0, 1);

                    final Composite saveComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                    g2.draw(new Line2D.Double(transformed[0], transformed[1], x1, y1));

                    if(startAngle == this.getStartAngle()){
                        final String label = format.format(((double)i/(double)ticks)*this.getMaxValue());
                                             final Rectangle2D labelBounds = getLabelFont().getStringBounds(label, frc);


                        final LineMetrics lm = getLabelFont().getLineMetrics(label, frc);
                        final double ascent = lm.getAscent();

                        //move based on quadrant.
                        if(Math.abs(labelX -plotArea.getCenterX()) < THRESHOLD){
                            //on Y Axis, label to right.
                            labelX += valueLabelGap;
                            //center vertically.
                            labelY += ascent/(float)2;
                        }else if (Math.abs(labelY - plotArea.getCenterY()) < THRESHOLD){
                            //on X Axis, label underneath.
                            labelY += valueLabelGap;
                        }else if (labelX >= plotArea.getCenterX()){
                            if(labelY < plotArea.getCenterY()){
                                //quadrant 1
                                labelX += valueLabelGap;
                                labelY += valueLabelGap;
                            }else{
                                //quadrant 2
                                labelX -= valueLabelGap;
                                labelY += valueLabelGap;
                            }
                        }else{
                            if(labelY > plotArea.getCenterY()){
                                //quadrant 3
                                labelX -= valueLabelGap;
                                labelY -= valueLabelGap;
                            }else{
                                //quadrant 4
                                labelX += valueLabelGap;
                                labelY -= valueLabelGap;
                            }
                        }
                        g2.setPaint(getLabelPaint());
                        g2.setFont(getLabelFont());
                        g2.drawString(label, (float) labelX, (float) labelY);
                    }
                    g2.setComposite(saveComposite);
                }
            }

            /**
             * sets the number of tick marks on this spider chart.
             * @param ticks the new number of tickmarks.
             */
            public void setTicks(final int ticks) {
                this.ticks = ticks;
            }

            /**
             * sets the numberformat for the tick labels on this spider chart.
             * @param format the new number format object.
             */
            public void setFormat(final NumberFormat format) {
                this.format = format;
            }

        };
        plot.setMaxValue(10);
        plot.setBaseSeriesOutlineStroke(new BasicStroke(2));
        plot.setWebFilled(true);
        plot.setSeriesPaint(0, Color.MAGENTA);
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






    public static JPanel createChartPanel(String chartName, Map<String, GraphData> data) {
        JFreeChart jfreechart = createChart(chartName, data);
        return new ChartPanel(jfreechart);
    }

    private static JFreeChart createChart(String title, Map<String, GraphData> data) {
    	CategoryDataset dataset = createDatasetG(data);
        SpiderWebPlot plot = new SpiderWebPlot(dataset) {
			private static final long serialVersionUID = 9166744064889714872L;
			//put this many labels on each axis.
            private int ticks = DEFAULT_TICKS;
            private static final int DEFAULT_TICKS = 10;
            private NumberFormat format = NumberFormat.getInstance();
            //constant for creating perpendicular tick marks.
            private static final double PERPENDICULAR = 90;
            //the size of a tick mark, as a percentage of the entire line length.
            private static final double TICK_SCALE = 0.015;
            //the gap between the axis line and the numeric label itself.
            private int valueLabelGap = DEFAULT_GAP;
            private static final int DEFAULT_GAP = 10;
            //the threshold used for determining if something is "on" the axis
            private static final double THRESHOLD = 15;

            /**
             * {@inheritDoc}
             */
            @Override
            protected void drawLabel(final Graphics2D g2, final  Rectangle2D plotArea, final double value,
                                     final int cat, final double startAngle, final double extent) {
                super.drawLabel(g2, plotArea, value, cat, startAngle, extent);
                final FontRenderContext frc = g2.getFontRenderContext();
                final double[] transformed = new double[2];
                final double[] transformer = new double[2];
                final Arc2D arc1 = new Arc2D.Double(plotArea, startAngle, 0, Arc2D.OPEN);

                for(int i = 1;i <= ticks; i++){

                    final Point2D point1 = arc1.getEndPoint();

                    final double deltaX = plotArea.getCenterX();
                    final double deltaY = plotArea.getCenterY();
                    double labelX = point1.getX() - deltaX;
                    double labelY = point1.getY() - deltaY;

                    final double scale = ((double)i/(double)ticks);
                    final AffineTransform tx = AffineTransform.getScaleInstance(scale,scale);
                    //for getting the tick mark start points.
                    final AffineTransform pointTrans
                            = AffineTransform.getScaleInstance(scale + TICK_SCALE, scale + TICK_SCALE);
                    transformer[0] = labelX;
                    transformer[1] = labelY;
                    pointTrans.transform(transformer, 0, transformed, 0, 1);
                    final double pointX = transformed[0] + deltaX;
                    final double pointY = transformed[1] + deltaY;
                    tx.transform(transformer, 0, transformed, 0, 1);
                    labelX = transformed[0] + deltaX;
                    labelY = transformed[1] + deltaY;



                    double rotated = (PERPENDICULAR);

                    AffineTransform rotateTrans
                            = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);
                    transformer[0] = pointX;
                    transformer[1] = pointY;
                    rotateTrans.transform(transformer, 0, transformed, 0, 1);
                    final double x1 = transformed[0];
                    final double y1 = transformed[1];

                    rotated = (-PERPENDICULAR);
                    rotateTrans = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);

                    rotateTrans.transform(transformer, 0, transformed, 0, 1);

                    final Composite saveComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                    g2.draw(new Line2D.Double(transformed[0], transformed[1], x1, y1));

                    if(startAngle == this.getStartAngle()){
                        final String label = format.format(((double)i/(double)ticks)*this.getMaxValue());
                                             final Rectangle2D labelBounds = getLabelFont().getStringBounds(label, frc);


                        final LineMetrics lm = getLabelFont().getLineMetrics(label, frc);
                        final double ascent = lm.getAscent();

                        //move based on quadrant.
                        if(Math.abs(labelX -plotArea.getCenterX()) < THRESHOLD){
                            //on Y Axis, label to right.
                            labelX += valueLabelGap;
                            //center vertically.
                            labelY += ascent/(float)2;
                        }else if (Math.abs(labelY - plotArea.getCenterY()) < THRESHOLD){
                            //on X Axis, label underneath.
                            labelY += valueLabelGap;
                        }else if (labelX >= plotArea.getCenterX()){
                            if(labelY < plotArea.getCenterY()){
                                //quadrant 1
                                labelX += valueLabelGap;
                                labelY += valueLabelGap;
                            }else{
                                //quadrant 2
                                labelX -= valueLabelGap;
                                labelY += valueLabelGap;
                            }
                        }else{
                            if(labelY > plotArea.getCenterY()){
                                //quadrant 3
                                labelX -= valueLabelGap;
                                labelY -= valueLabelGap;
                            }else{
                                //quadrant 4
                                labelX += valueLabelGap;
                                labelY -= valueLabelGap;
                            }
                        }
                        g2.setPaint(getLabelPaint());
                        g2.setFont(getLabelFont());
                        g2.drawString(label, (float) labelX, (float) labelY);
                    }
                    g2.setComposite(saveComposite);
                }
            }

            /**
             * sets the number of tick marks on this spider chart.
             * @param ticks the new number of tickmarks.
             */
            public void setTicks(final int ticks) {
                this.ticks = ticks;
            }

            /**
             * sets the numberformat for the tick labels on this spider chart.
             * @param format the new number format object.
             */
            public void setFormat(final NumberFormat format) {
                this.format = format;
            }

        };
        plot.setMaxValue(10);
        plot.setBaseSeriesOutlineStroke(new BasicStroke(2));
        plot.setWebFilled(true);
        plot.setSeriesPaint(0, Color.BLACK);
        plot.setSeriesOutlineStroke(0, new BasicStroke(1));
        List<Color> colors = new ArrayList<>();
        data.keySet().forEach(name -> {
        	colors.add(data.get(name).getColor());
        });
        for (int i = 0; i < colors.size(); i++) {
        	plot.setSeriesPaint(i, colors.get(i));
        }
        JFreeChart chart = new JFreeChart("SeDeM Graph: " + title, plot);

        return chart;
    }

    private static CategoryDataset createDatasetG(Map<String, GraphData> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        for (int j = 0; j < SeDeMParameters.values().length; j++) {
//    		Comparable<?> colKey = SeDeMParameters.values()[j];
//    		dataset.addValue(5, "Passing line", colKey);
//    	}
        data.keySet().forEach(name -> {
        	for (int j = 0; j < SeDeMParameters.values().length; j++) {
        		Comparable<?> colKey = SeDeMParameters.values()[j];
        		dataset.addValue(data.get(name).getData().get(j), name, colKey);
        	}
        });

        return dataset;
    }

}
