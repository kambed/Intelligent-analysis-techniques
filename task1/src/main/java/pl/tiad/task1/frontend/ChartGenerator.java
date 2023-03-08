package pl.tiad.task1.frontend;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class ChartGenerator {
    public static JFreeChart generatePlot(Integer[] x, Double[] y, String name) {
        XYSeries functionSeries = new XYSeries(name);
        for (int i = 0; i < x.length; i++) {
            functionSeries.add(x[i], y[i]);
        }

        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(functionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Population value chart", "Iterations", name, seriesCollection,
                PlotOrientation.VERTICAL, false, true, false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        changeVisibility(renderer, 0, true);
        changeVisibility(renderer, 1, true);
        formatAxis(renderer, 2);
        formatAxis(renderer, 3);

        plot.setRenderer(renderer);

        plot.getRangeAxis().setAutoRange(true);                            // uncomment
        ((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false); // add

        return chart;
    }

    private static void formatAxis(XYLineAndShapeRenderer renderer, int series) {
        changeVisibility(renderer, series, true);
        renderer.setSeriesStroke(series, new BasicStroke(0.5f));
    }

    private static void changeVisibility(XYLineAndShapeRenderer renderer, int series, boolean displayLine) {
        renderer.setSeriesLinesVisible(series, displayLine);
        renderer.setSeriesShapesVisible(series, !displayLine);
    }
}