package com.csv.chart.drawn2xls;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import java.util.Date;

public class drawn2xls {
    public static void msgExportExcel(String code, SXSSFWorkbook wb, List<Object[]> title,
                                      List<List<Object[]>> resultList, List<Object[]> chartShow) {

        SXSSFSheet sheet =null;
        File file =null;
        int size = resultList.size();//sheet数
        try {
            for(int i =0;i<size;i++){
                sheet = (SXSSFSheet) wb.createSheet("Page " + i);
                Row hrow = sheet.createRow(0);
                Object[] obj = title.get(i);
                //写入标题
                for(int k=0;k<obj.length;k++){
                    Cell cell = hrow.createCell(k);
                    cell.setAsActiveCell();
                    cell.setCellValue(obj[k] == null? "" : obj[k].toString());
                    //设置列宽度
                    sheet.setColumnWidth(k, 3500);
                }

                //写入数据
                List<Object[]> list = resultList.get(i);
                for(int j=0;j<list.size();j++){
                    Object[] o = list.get(j);
                    Row row = sheet.createRow(j+1);
                    for (int k = 0; k < o.length; k++) {
                        Cell cell = row.createCell(k);
                        cell.setCellValue(o[k] == null ? "" : o[k].toString());
                    }
                    if (i % 100 == 0) {
                        sheet.flushRows(100);
                    }
                }
                //创建图表

             //   file =  ExportChartUtil.createChartLine(list,chartShow.get(i)[0].toString(),chartShow.get(i)[1].toString(),chartShow.get(i)[2].toString(),chartShow.get(i)[3].toString());


                imageOut(wb,sheet,file);
            }
        } catch (IOException e) {
            e.printStackTrace();
           // logger.error(e.getMessage());
        }
    }


    public static File createChartLine(List<Object[]> list,String title,String xTitle,String yTitle,String num){

//	public static void main(String args[]){
        //构造数据集合
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i=0;list!=null && i<list.size();i++){
            Object [] obj = list.get(i);
            dataset.addValue(Integer.parseInt(obj[0].toString()),"数据",obj[1].toString()+"\n"+obj[2].toString());
        }

        //核心对象
        JFreeChart chart = ChartFactory.createLineChart(title, 	//图形的主标题
                xTitle, 				//X轴外标签的名称
                yTitle, 						//Y轴外标签的名称
                dataset,
                PlotOrientation.VERTICAL,	//图形的显示方式（水平和垂直）
                true,						//是否显示子标题
                true,						//是否在图形上显示数值的提示
                true);						//是否生成URL地址
        //解决主标题的乱码
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 18));
        //解决子标题的乱码
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        chart.getLegend().setPosition(RectangleEdge.RIGHT);//右侧显示子菜单
        //获取图表区域对象
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
//		categoryPlot.setBackgroundPaint(null);
        //获取X轴对象
        CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
        //获取Y轴对象
        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        //解决X轴上的乱码
        categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 15));
        //解决X轴外的乱码
        categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));
        //解决Y轴上的乱码
        numberAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 15));
        //解决Y轴外的乱码
        numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));

        //改变Y轴的刻度，默认值从1计算
        numberAxis.setAutoTickUnitSelection(false);
        NumberTickUnit unit = new NumberTickUnit(Integer.parseInt(num));
        numberAxis.setTickUnit(unit);

        //获取绘图区域对象
        LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();
        lineAndShapeRenderer.setBaseShapesVisible(true);//设置转折点

        //让数值显示到页面上
        lineAndShapeRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        lineAndShapeRenderer.setBaseItemLabelsVisible(true);
        lineAndShapeRenderer.setBaseItemLabelFont(new Font("宋体", Font.BOLD, 15));

        //显示图形
//		ChartFrame chartFrame = new ChartFrame("xyz", chart);
//		chartFrame.setVisible(true);
//		chartFrame.pack();

        String filename = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+".png";//用时间作为文件名防止重名的问题发生
        File file = new File(filename);//保存文件到web容器中
        try {
            ChartUtilities.saveChartAsPNG(file,chart,600,500);
        } catch (IOException e) {
            e.printStackTrace();
          //  logger.error(e.getMessage());
        }

        return file;
    }



    private static void imageOut(SXSSFWorkbook wb,SXSSFSheet sheet,File file) {

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        BufferedImage bufferImg;
        try {
            bufferImg = ImageIO.read(file);
            ImageIO.write(bufferImg, "png", byteArrayOut);
            Drawing dp = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0,0,512,255,7,4,10,20);//设置图表在excel中位置
         //   anchor.setAnchorType(2);//
            anchor.setAnchorType(ClientAnchor.AnchorType.byId(2));
            dp.createPicture(anchor,wb.addPicture(byteArrayOut.toByteArray(),wb.PICTURE_TYPE_PNG)).resize(0.8);

        } catch (IOException e) {
            e.printStackTrace();
          //  logger.error(e.getMessage());
        }

    }
}
