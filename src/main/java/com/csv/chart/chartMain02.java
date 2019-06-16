package com.csv.chart;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;

public class chartMain02 {
    public static void main(String args[]) throws IOException,InterruptedException {

        /*
        LineXYChart xyChartMemory=new LineXYChart();
        CategoryDataset datasetMemory=xyChartMemory.createDataset(0);
        xyChartMemory.createChart(datasetMemory,0);
        LineXYChart xyChartCPU=new LineXYChart();
        CategoryDataset datasetCPU=xyChartCPU.createDataset(1);
        xyChartCPU.createChart(datasetCPU,1);
        LineXYChart xyChartFlow=new LineXYChart();
        CategoryDataset datasetFlow=xyChartFlow.createDataset(2);
        xyChartFlow.createChart(datasetFlow,2);

    */
    }

    //获取表格数据
    @SuppressWarnings("unused")
    private String[][]  readXls() throws IOException{
        String [][]array2 = new String[10][3];
        InputStream in = new FileInputStream( "D:\\java_workspace\\手机客户端性能测试表.xls");
        @SuppressWarnings("resource")
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(in);
        // 循环工作表Sheet
        for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt( numSheet);
            if(hssfSheet == null){
                continue;
            }

            // 循环行Row
            for(int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++){
                HSSFRow hssfRow = hssfSheet.getRow( rowNum);
                if(hssfRow == null){
                    continue;
                }

                // 循环列Cell
                for(int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++){
                    HSSFCell hssfCell = hssfRow.getCell( cellNum);
                    if(hssfCell == null){
                        continue;
                    }
                    array2[rowNum-1][cellNum]=getValue(hssfCell);
                    System.out.print( getValue(hssfCell)+"   " );
                }
                System.out.println();
            }
        }
        return array2;
    }

    //读取单元格
    @SuppressWarnings({ "static-access", "deprecation" })
    private String getValue(HSSFCell hssfCell){
        if(hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN){
            return String.valueOf( hssfCell.getBooleanCellValue());
        }else if(hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC){
            return String.valueOf( hssfCell.getNumericCellValue());
        }else{
            return String.valueOf( hssfCell.getStringCellValue());
        }
    }

    //收集数据
    public  CategoryDataset createDataset(int type) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series="内存曲线";   //曲线名称

        if(type==0){
            series="内存曲线";
        }else if(type==1){
            series="CPU曲线";
        }else if(type==2){
            series="流量曲线";
        }

        String [][]array3 = new String[10][3];
        try {
            array3=readXls();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<10;i++){
            // dataset.addValue(9, series, "1");  //参数分别是纵轴值、曲线名称、横轴值
            dataset.addValue(Double.valueOf(array3[i][type]), series,String.valueOf(i+1));  //参数分别是纵轴值、曲线名称、横轴值
        }

        return dataset;
    }

    // 创建图像
    public void createChart(CategoryDataset dataset,int type) {

        JFreeChart chart = ChartFactory.createLineChart("内存图", "测试时间（s）",
                "内存（K）", dataset, PlotOrientation.VERTICAL, true, true,
                true);
        if(type==0){
            chart = ChartFactory.createLineChart("内存图", "测试时间（s）",
                    "内存（K）", dataset, PlotOrientation.VERTICAL, true, true,
                    true);
        }else if(type==1){
            chart = ChartFactory.createLineChart("CPU图", "测试时间（s）",
                    "CPU（%）", dataset, PlotOrientation.VERTICAL, true, true,
                    true);
        }else if(type==2){
            chart = ChartFactory.createLineChart("流量图", "测试时间（s）",
                    "流量（K）", dataset, PlotOrientation.VERTICAL, true, true,
                    true);
        }


        CategoryPlot cp = chart.getCategoryPlot();
        cp.setBackgroundPaint(ChartColor.WHITE); // 背景色设置
        cp.setRangeGridlinePaint(ChartColor.GRAY); // 网格线色设置
        cp.setDomainGridlinePaint(ChartColor.BLACK);
        cp.setNoDataMessage("没有数据");
        // 数据轴属性部分
        NumberAxis rangeAxis = (NumberAxis) cp.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true); // 自动生成
        rangeAxis.setUpperMargin(0.20);
        rangeAxis.setLabelAngle(Math.PI / 2.0);
        rangeAxis.setAutoRange(false);

        // 数据渲染部分 主要是对折线做操作
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
                .getRenderer();

        renderer.setBaseItemLabelsVisible(true);// 设置曲线是否显示数据点
        // 设置曲线显示各数据点的值
        renderer.setSeriesPaint(0, Color.black); // 设置折线的颜色

        renderer.setBaseShapesFilled(true);

        renderer.setBaseItemLabelsVisible(true);

        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(

                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        // 解决中文乱码问题
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 15));
        chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        CategoryAxis domainAxis = plot.getDomainAxis();
		/*------设置X轴坐标上的文字-----------*/
        domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 11));
		/*------设置X轴的标题文字------------*/
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		/*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 12));
		/*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));

        // 创建窗口，并打印出来
        JPanel jPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("性能-折线图");
        frame.add(jPanel);
        frame.setBounds(100, 100, 800, 600);
        frame.setVisible(true);

    }
}
