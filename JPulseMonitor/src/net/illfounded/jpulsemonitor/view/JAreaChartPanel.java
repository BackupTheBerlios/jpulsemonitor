/*
 * This file is part of the JPulsemonitor.
 *
 * JPulsemonitor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.illfounded.jpulsemonitor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.Spacer;
import org.jfree.ui.TextAnchor;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * Shows a nice LineChart.
 */
public class JAreaChartPanel extends JPanel {
	// Eclipse generated serialVersionUID
	private static final long serialVersionUID = 5442890972825127841L;

	private String[] _axisLabels;
	private String[] _legendLabels;
	private Logger _log;
	private final JFreeChart _chart;
	
    /**
	 * Creates a new AreaChartPanel object, initialized with some default data.
	 */
	public JAreaChartPanel(String title, String xName, String yName) {
		super();
		_log = Logger.getLogger("net.illfounded.jpulsemonitor");
		
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(true);

        // create the chart...
        _chart = createChart(dataset, title, xName, yName);
        final ChartPanel chartPanel = new ChartPanel(_chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(550, 350));
        chartPanel.setMouseZoomable(true, false);
        
        add(chartPanel);
	}
	
    /**
     * Creates a chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return A chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String title, String xName, String yName) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title,
            xName, 
            yName,
            dataset,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.white);
        
        final StandardLegend sl = (StandardLegend) chart.getLegend();
        sl.setDisplaySeriesShapes(true);

        final XYPlot plot = chart.getXYPlot();
        //plot.setOutlinePaint(null);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(false);

        // add a labelled marker for the original closing time...
        // final Day hour = new Day(4, 6, 2004);
        // double millis = hour.getFirstMillisecond();
        // final Marker originalEnd = new ValueMarker(millis);
        // originalEnd.setPaint(Color.orange);
        // originalEnd.setLabel("Start cyclus");
        // originalEnd.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        // originalEnd.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        // plot.addDomainMarker(originalEnd);
        
        final XYItemRenderer renderer = plot.getRenderer();
        if (renderer instanceof StandardXYItemRenderer) {
            final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
            rr.setPlotShapes(true);
            rr.setShapesFilled(true);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            renderer.setSeriesStroke(1, new BasicStroke(2.0f));
           }
        
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yy"));
        
        return chart;
    }
    
    public void addMarker(String text, double value) {
        // add a labelled marker for the bid start price...
        final Marker min = new ValueMarker(value);
        min.setPaint(Color.green);
        min.setLabel(text);
        min.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
        min.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        
        _chart.getXYPlot().addRangeMarker(min);
    }
    
    public void addTimeSeries(String name, ChartValue[] values) {
        TimeSeries timeS = new TimeSeries(name, Day.class);;
        ChartValue value;
       
        for (int i = 0; i < values.length; i++) {
            value = values[i];
            try {
                timeS.add(new Day(value.getKey()), value.getValue());
            } catch (Exception exe) {
                // Most probably two values for the same date... omit the second value
                _log.log(Level.INFO, exe.getMessage());
            }
        }
        
        TimeSeriesCollection dataset = (TimeSeriesCollection)_chart.getXYPlot().getDataset();
        dataset.addSeries(timeS);
    }

}
