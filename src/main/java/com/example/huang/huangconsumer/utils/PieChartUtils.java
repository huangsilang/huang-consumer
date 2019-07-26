package com.example.huang.huangconsumer.utils;

import com.example.huang.huangconsumer.constant.Constant;
import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PieChartUtils {

    private static Logger logger = LoggerFactory.getLogger(PieChartUtils.class);
    private static ChartTheme currentTheme = new StandardChartTheme("JFree");

    /**
     * 按姓名统计
     * @param list
     * @return
     * @throws IOException
     */
    public static String createImgByName(List<ConsumerPojo> list) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        Map<String, List<ConsumerPojo>> map = list.stream().collect(Collectors.groupingBy(ConsumerPojo::getName));
        for (String s:map.keySet()) {
            Optional<BigDecimal> opt = map.get(s).stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
            BigDecimal bigDecima = opt.isPresent() ? opt.get() : new BigDecimal(0);
            dataset.setValue(s+"-"+bigDecima, bigDecima);
        }
        String s = list.stream().map(ConsumerPojo::getName).distinct().collect(Collectors.joining("、"));
        String name =s+"的消费情况";
        long l = System.currentTimeMillis();
        String url = Constant.IMG_PATH+l+Constant.IMG_BY_NAME_PATH;
        return jFreeChart(name,dataset, Constant.FILE_PHTH+l+Constant.IMG_BY_NAME_PATH,url);
    }
    /**
     * 按产品统计所有的
     * @param list
     */
    public static String createImgByProduct(List<ConsumerPojo> list) {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        Map<String, List<ConsumerPojo>> map = list.stream().collect(Collectors.groupingBy(ConsumerPojo::getProduct));
        for (String s:map.keySet()) {//按照姓名再按照产品分组
            //Map<String, List<ConsumerPojo>> collect = map.get(s).stream().collect(Collectors.groupingBy(ConsumerPojo::getProduct));
            Optional<BigDecimal> opt = map.get(s).stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
            BigDecimal bigDecima = opt.isPresent() ? opt.get() : new BigDecimal(0);
            dataset.setValue(s+"-"+bigDecima, bigDecima);
        }
        String name = "产品花费详情";
        long l = System.currentTimeMillis();
        String url = Constant.IMG_PATH+l+Constant.IMG_BY_PRODUCT_PATH;
        return jFreeChart(name,dataset,Constant.FILE_PHTH+l+Constant.IMG_BY_PRODUCT_PATH,url);
        //list.stream().forEach(l->dataset.setValue(l.getProduct()+"-"+l.getHowMuch(), l.getHowMuch() ));
        //String s = list.stream().map(ConsumerPojo::getName).distinct().collect(Collectors.joining("、"));
    }

    /**
     *
     * @param name        图片里面的主题
     * @param dataset     图片的数据
     * @param filePath   图片文件的名字
     * @param url        客户端访问图片的地址
     * @return
     */
    public static String jFreeChart(String name,DefaultPieDataset dataset,String filePath,String url){

        try {
            JFreeChart chart = ChartFactory.createPieChart(
                    name, // chart title
                    dataset, // data
                    true, // include legend
                    true,
                    false);
            Font font = new Font("隶书", Font.BOLD, 22);
            Font font1 = new Font("隶书", Font.BOLD, 16);
            chart.setTitle(new TextTitle(name, font));
            PiePlot plot = (PiePlot)chart.getPlot();
            plot.setLabelFont(font1);
            chart.getLegend().setItemFont(font1);
            System.out.println(chart.getLegend().getContentXOffset());
            int width = 640; /* Width of the image */
            int height = 480; /* Height of the image */
            File pieChart = new File( filePath );
            ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
        } catch (IOException e) {
            logger.error("生成饼图异常",e);
        }
        return url;
    }

    public static void deleteFile(){
        File f = new File(Constant.FILE_PHTH);
        String[] list = f.list();
        for (String s:list) {
            File f1 = new File(Constant.FILE_PHTH+s);
            f1.delete();
        }
    }

}
