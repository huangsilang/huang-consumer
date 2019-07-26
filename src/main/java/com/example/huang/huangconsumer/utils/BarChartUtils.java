package com.example.huang.huangconsumer.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;

public class BarChartUtils {

    public static void main( String[ ] args )throws Exception
    {
        final String fiat = "手机";
        final String audi = "AUDI";
        final String ford = "FORD";
        final String speed = "Speed";
        final String millage = "Millage";
        final String userrating = "User Rating";
        final String safety = "safety";

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

        dataset.addValue( 1.0 , fiat , speed );
        dataset.addValue( 3.0 , fiat , userrating );
        dataset.addValue( 5.0 , fiat , millage );
        dataset.addValue( 5.0 , fiat , safety );

        dataset.addValue( 5.0 , audi , speed );
        dataset.addValue( 6.0 , audi , userrating );
        dataset.addValue( 10.0 , audi , millage );
        dataset.addValue( 4.0 , audi , safety );

        dataset.addValue( 4.0 , ford , speed );
        dataset.addValue( 2.0 , ford , userrating );
        dataset.addValue( 3.0 , ford , millage );
        dataset.addValue( 6.0 , ford , safety );

        JFreeChart barChart = ChartFactory.createBarChart(
                "你好",
                "Category", "Score",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);
        barChart.setTitle(new TextTitle("你好", new Font("宋体", Font.BOLD, 25)));
        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */
        File BarChart = new File( "E:\\BarChart.jpeg" );
        ChartUtilities.saveChartAsJPEG( BarChart , barChart , width , height );
    }

}
