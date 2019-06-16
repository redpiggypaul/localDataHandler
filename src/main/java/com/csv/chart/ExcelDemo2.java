package com.csv.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

public class ExcelDemo2 {
    public static void main(String[] args) throws Exception {

        int sampleListSize = 0;
        HashMap<String, ArrayList<sampleDataEntry>> dataSample = new HashMap<String, ArrayList<sampleDataEntry>>();

        ArrayList<Color> colorArrayList = new ArrayList<Color>();
        colorArrayList.add(new Color(210, 105, 30));
        colorArrayList.add(new Color(0, 191, 255));
        colorArrayList.add(new Color(100, 15, 130));
        colorArrayList.add(new Color(20, 225, 125));

        // excel2003工作表
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Sheet 1");
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

        ArrayList<sampleDataEntry> tempList1 = new ArrayList<sampleDataEntry>();
        tempList1.add(new sampleDataEntry("time1", "Ser1", 1));
        tempList1.add(new sampleDataEntry("time2", "Ser1", 2));

        ArrayList<sampleDataEntry> tempList2 = new ArrayList<sampleDataEntry>();
        tempList2.add(new sampleDataEntry("time1", "Ser2", 11));
        tempList2.add(new sampleDataEntry("time2", "Ser2", 12));


        dataSample.put("Ser1", tempList1);
        dataSample.put("Ser2", tempList2);
        // 设置具体数据
        List<String> timeList = new ArrayList<String>();
        timeList.add("10:00");
        timeList.add("11:00");
        timeList.add("12:00");
        List<Integer> appList = new ArrayList<Integer>();
        appList.add(120);
        appList.add(200);
        appList.add(150);
        List<Integer> oraList = new ArrayList<Integer>();
        oraList.add(230);
        oraList.add(200);
        oraList.add(235);


        // 设置图片中的字体和颜色以及字号
        Font titleFont = new Font("黑体", Font.BOLD, 12);
        Font xfont = new Font("黑体", Font.BOLD, 10);
        Font labelFont = new Font("黑体", Font.BOLD, 10);

        // 设置数据区域
        DefaultCategoryDataset dataset4data = new DefaultCategoryDataset();

        Iterator iterator = dataSample.keySet().iterator();
        while (iterator.hasNext()) {
            Object localKey = iterator.next();
            System.out.println("map.get(key) is :" + dataSample.get(localKey));
            if(sampleListSize<dataSample.get(localKey).size()) {
                sampleListSize = dataSample.get(localKey).size();
            }
            for (int i = 0; i < dataSample.get(localKey).size(); i++) {
                dataset4data.addValue(dataSample.get(localKey).get(i).getDuration(),
                        localKey.toString(),
                        dataSample.get(localKey).get(i).getTimeTag());
            }

        }


        // 设置数据区域
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < timeList.size(); i++) {
            String time = timeList.get(i);
            dataset.addValue(appList.get(i), "苹果", time);
            dataset.addValue(oraList.get(i), "橘子", time);
        }
        JFreeChart chart = ChartFactory.createLineChart("性能指标／响应时间", "测试", "响应时间", dataset, PlotOrientation.VERTICAL, true,
                true, true);
        // 设置图例字体
        chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 10));
        // 设置标题字体
        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));
        // 图形的绘制结构对象
        CategoryPlot plot = chart.getCategoryPlot();
        // 获取显示线条的对象
        LineAndShapeRenderer lasp = (LineAndShapeRenderer) plot.getRenderer();
        // 设置拐点是否可见/是否显示拐点
        lasp.setBaseShapesVisible(true);
        // 设置拐点不同用不同的形状
        lasp.setDrawOutlines(true);
        // 设置线条是否被显示填充颜色
        lasp.setUseFillPaint(false);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        // 设置折线大小以及折线的颜色
        //     renderer.setSeriesStroke(0, new BasicStroke(1.0F));
        //   renderer.setSeriesPaint(0, new Color(210, 105, 30));
        // renderer.setSeriesStroke(1, new BasicStroke(1.0F));
        // renderer.setSeriesPaint(1, new Color(0, 191, 255));

        for (int i = 0; i < dataSample.size(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(1.0F));
            renderer.setSeriesPaint(i, colorArrayList.get(i));
        }


        // 设置折点的大小
        lasp.setSeriesOutlineStroke(0, new BasicStroke(0.025F));
        lasp.setSeriesOutlineStroke(1, new BasicStroke(0.05F));
        // 设置网格线
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setRangeGridlinesVisible(true);
        // x轴
        CategoryAxis domainAxis = plot.getDomainAxis();
        // 设置x轴不显示，即让x轴和数据区重合
        domainAxis.setAxisLineVisible(false);
        // x轴标题
        domainAxis.setLabelFont(xfont);
        // x轴数据倾斜
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.95D));
        // X轴坐标上数值字体
        domainAxis.setTickLabelFont(labelFont);
        // 设置Y轴间隔
        NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
        numAxis.setTickUnit(new NumberTickUnit(50));
        // y轴
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(xfont);
        // 设置y轴不显示，即和数据区重合
        rangeAxis.setAxisLineVisible(false);
        // y轴坐标上数值字体
        rangeAxis.setTickLabelFont(labelFont);
        rangeAxis.setFixedDimension(0);
        CategoryPlot cp = chart.getCategoryPlot();
        // 背景色设置
        cp.setBackgroundPaint(ChartColor.WHITE);
        cp.setRangeGridlinePaint(ChartColor.GRAY);

        int widthStep = 10;
        int highStep = 15;
        int sizeTimes = 1;
        if((dataSample.size()>4)||(sampleListSize>15)){
            sizeTimes=Math.max(dataSample.size()/3,sampleListSize/10);

        }

        // 创建图例，设置图例的位置，这里的设置实际不起作用，怎么设都在下边
        LegendTitle legendTitle = new LegendTitle(chart.getPlot());
        legendTitle.setPosition(RectangleEdge.BOTTOM);
        try {
            ChartUtilities.writeChartAsPNG(byteArrayOut, chart, (300+sizeTimes*widthStep*10), (100+sizeTimes*highStep*8));
            String fileSavePath = "exTest.png";
            BufferedImage bufferImg = ImageIO.read(new File(fileSavePath));
            ImageIO.write(bufferImg, "png", byteArrayOut);
        } catch (IOException e) {
        }
        // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 八个参数，前四个表示图片离起始单元格和结束单元格边缘的位置，



        // 后四个表示起始和结束单元格的位置，如下表示从第2列到第12列，从第1行到第15行,需要注意excel起始位置是0
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 2, (short) 1, (short) (2+sizeTimes*widthStep), (short) (5+sizeTimes*highStep));
        //  anchor.setAnchorType(3);
        anchor.setAnchorType(ClientAnchor.AnchorType.byId(3));
        // 插入图片
        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
        // excel2003后缀
        FileOutputStream fileOut = new FileOutputStream("/Users/zhuhaoran26/Documents/csvloader/exTest.xls");
        wb.write(fileOut);
        fileOut.close();
    }
}

