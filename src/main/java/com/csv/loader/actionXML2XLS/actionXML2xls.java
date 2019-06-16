/**
 *
 */
package com.csv.loader.actionXML2XLS;

import com.csv.entity.*;
import com.csv.util.StringUtil;
import com.csv.util.XMLException;
import com.csv.util.readProperity;
import com.csvreader.CsvReader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import org.apache.poi.hssf.usermodel.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author paul.zhu
 * @date 2016年7月14日 上午11:15:44
 */
public class actionXML2xls {
    public final static String INSERT_PREFIX = "INSERT INTO test(month,soldItem) VALUES(";
    public final static SimpleDateFormat inSdf = new SimpleDateFormat("MMM yyyy", Locale.US);
    public final static SimpleDateFormat outSdf = new SimpleDateFormat("yyyyMM");

    public static void main(String args[]) throws IOException {
        // generateInsertSql();
        /**
         * excel文件一个sheet最大的行数
         */
        final int MAX_ROW = 65535;
        /**
         * excel文件一个sheet最大的列数
         */
        final int MAX_COL = 255;
        String singleBufferWriterLine = null;
        BufferedWriter bufferWriter = null;
        BufferedWriter realWriter = null;
        String actionPath = readProperity.getProValue("actionXML_ActionFolder");  //  //Users//appledev131//Documents//REXAUTO4//OperationXML//Action//

        try {
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            String targetFile = new String("/Users/appledev131/Documents/csvloader/src/main/java/com/csv/action" + time);     //todo change for windows
            File runnerFile = new File("//Users//appledev131//Documents//REXAUTO4//src//REXenvConfig//run_" + time + ".xml");     //todo change for windows
            String[] columnNames = {"OperationName", "Step", "PageName", "OperationType", "elementName", "elementType", "elementParameter"};
            LinkedHashMap<String, String> list4test = new LinkedHashMap<String, String>();
            ArrayList<LinkedHashMap<String, String>> list4operationRecordLine = new ArrayList<LinkedHashMap<String, String>>();
            ArrayList<String> sheetName = new ArrayList<String>();
            sheetName.clear();
            list4test.clear();
            list4operationRecordLine.clear();

            ArrayList<String> actionXMLFileList = new ArrayList<String>();
            actionXMLFileList.clear();
            actionXMLFileList.add("Account");
            actionXMLFileList.add("editProfile");
            actionXMLFileList.add("login");
            actionXMLFileList.add("ONProject");
            actionXMLFileList.add("Preference");
            actionXMLFileList.add("Profile_Award");
            actionXMLFileList.add("Profile_CertifiANDlicense");
            actionXMLFileList.add("Profile_Education");
            actionXMLFileList.add("Profile_Language");
            actionXMLFileList.add("Profile_MilitaryService");
            actionXMLFileList.add("Profile_Patent");
            actionXMLFileList.add("Profile_ProAffiliation");
            actionXMLFileList.add("Profile_Publication");
            actionXMLFileList.add("Profile_Reference");
            actionXMLFileList.add("Profile_Skill");
            actionXMLFileList.add("Profile_WorkExp");
            actionXMLFileList.add("Role_Filte");
            actionXMLFileList.add("switchInNaviBar");

            for (String theActionFileName : actionXMLFileList) {
                HashMap<String, operationItem> theOpeMap = readActionXML(actionPath + "Action_", theActionFileName, 0);


                if (theOpeMap.size() != 0) {
                    sheetName.add(theActionFileName);
                    for (Iterator it = theOpeMap.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry opeEntry = (Map.Entry) it.next();
                        operationItem temp = (operationItem) opeEntry.getValue();

                        list4test.put(temp.getOpeName() + "_" +
                                        temp.getStep(),
                                temp.getOpeName() + ":REXope:" +
                                        temp.getStep() + ":REXope:"
                                        + temp.getPageName() + ":REXope:"
                                        + temp.getOpeType() + ":REXope:"
                                        + temp.getElementName() + ":REXope:"
                                        + temp.getElementType() + ":REXope:"
                                        + temp.getElementPara());

                    }
                }
                list4operationRecordLine.add(new LinkedHashMap<String, String>(list4test));
                list4test.clear();
            }
            buildExcelNew(targetFile, columnNames, list4operationRecordLine, sheetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */


    public static HashMap<String, operationItem> readActionXML
    (String absPath, String fileNameXML, Object osType) throws Exception {
        HashMap<String, operationItem> opeMap = new HashMap<String, operationItem>();
        opeMap.clear();
        int lineN = 1;
        try {
            File file = new File(absPath + fileNameXML + ".xml");
            if (!file.exists()) {
                throw new IOException("Can't find elements mapping file: " + fileNameXML);
            }
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element root = document.getRootElement();
            if (file.toString().toLowerCase().contains(root.attributeValue("name").toLowerCase())) {
                if (root.getName().equalsIgnoreCase("page")) {
                    for (Iterator<?> it = root.elementIterator(); it.hasNext(); ) {
                        lineN++;
                        Element elementObj = (Element) it.next();
                        String oName = elementObj.attributeValue("OperationName");
                        String pName = elementObj.attributeValue("PageName");
                        String oType = elementObj.attributeValue("OperationType");
                        String eleName = elementObj.attributeValue("elementName");
                        String eleType = elementObj.attributeValue("elementType");
                        String elePara = elementObj.attributeValue("elementParameter");
                        Integer s = Integer.parseInt(elementObj.attributeValue("Step"));
                        operationItem eLocator;
                        eLocator = new operationItem(oName, pName, oType, eleName, eleType, elePara, s);
                        opeMap.put(oName + ":" + s.toString(), eLocator);
                    }
                } else {
                    throw new XMLException("The Root Line is not PAGE!!!");
                }
            }
        } catch (XMLException e) {
            throw new XMLException("Utility.XmlUtils.readActionXMLDocumentAll : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opeMap;
    }

    private static List<autoTCEntity> processTCselection(String csvFilePath) {
        List<autoTCEntity> list = new ArrayList<autoTCEntity>();
        try {
            CsvReader reader = new CsvReader(csvFilePath);
            reader.readHeaders();

            while (reader.readRecord()) {
                try {
                    autoTCEntity TCList = new autoTCEntity();
                    String singleTCName = reader.get("TC Name");
                    TCList.setTcName(singleTCName);
                    if (!StringUtil.isEmpty(reader.get("Selected"))) {
                        String temp = reader.get("Selected");
                        if (temp.equalsIgnoreCase("yes") || temp.toLowerCase().contains("yes")) {
                            TCList.setSelectStatus(true);
                        } else if (temp.equalsIgnoreCase("no") || temp.toLowerCase().contains("no"))
                            TCList.setSelectStatus(false);
                        else {
                            TCList.setSelectStatus(false);
                        }
                    } else {
                        TCList.setSelectStatus(false);
                    }
                    list.add(TCList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Record size: " + list.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }


    // 文件内容的总行数。
    static int getTotalLines(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName)));
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return lines;
    }


    /**
     * excel文件的读取
     *
     * @param filePath
     * @param rowIndex 从第几行读取数据
     * @return
     */
    public static List<List<String>> readExcel(String filePath, int rowIndex) {
        List<List<String>> list = new ArrayList<List<String>>();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            int sheetSize = hssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetSize; i++) {
                // 1.获取sheet表
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }
                // 2.解析sheet表,如果没有指定从第几行开始读取数据,则默认从第二行起，对excel内容进行解析
                if (rowIndex == 0) {
                    rowIndex = 1;
                }
                int lastRowNum = sheet.getLastRowNum();
                List<String> rowList = null;
                for (int j = rowIndex; j <= lastRowNum; j++) {
                    rowList = new ArrayList<String>();
                    HSSFRow hssfRow = sheet.getRow(j);
                    int lastCellNum = hssfRow.getLastCellNum();
                    if (hssfRow != null) {
                        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                            HSSFCell cell = hssfRow.getCell(cellIndex);
                            rowList.add(getCellValue(cell));
                        }
                    }
                    list.add(rowList);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 导出 excel文件
     *
     * @param filePathAndName 创建excel存放的目的路径
     * @param columnNames     excel列名数组
     * @param list4all        excel列数据集合
     */
    public static void buildExcel(String filePathAndName, String[] columnNames, ArrayList<ArrayList<String>> list4all, ArrayList<String> sheetName) {
        HSSFWorkbook hssfWorkbook = buildExcel(columnNames, list4all, sheetName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePathAndName + ".xls");
            hssfWorkbook.write(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void buildExcelNew(String filePathAndName, String[] columnNames, ArrayList<LinkedHashMap<String, String>> list4all, ArrayList<String> sheetName) {
        HSSFWorkbook hssfWorkbook = buildExcelNew(columnNames, list4all, sheetName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePathAndName + ".xls");
            hssfWorkbook.write(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void buildExcelSingle(String filePathAndName, String[] columnNames, ArrayList<String> list, String sheetName) {
        HSSFWorkbook hssfWorkbook = buildExcelSingle(columnNames, list, sheetName);


        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePathAndName + ".xls");
            hssfWorkbook.write(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出 excel文件,当数据量大于65535时,需要进行分sheet,列数不能大于255
     *
     * @param columnNames excel列名数组
     * @param list4all    excel列数据集合
     * @return
     */
    public static HSSFWorkbook buildExcel(String[] columnNames, ArrayList<ArrayList<String>> list4all, ArrayList<String> sheetName) {
        //  int size = CollectionUtils.isEmpty(list) ? 0 : list.size();
        // 计算一个excel文件sheet工作空间的个数
        //  int sheetCount = (size + MAX_ROW) / MAX_ROW;
        //logger.info("need sheet size is {}", sheetCount);
        // 1.创建excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        int sheetNum = sheetName.size();
        //   StringBuffer sheetName = new StringBuffer("action");
        for (int i = 0; i < sheetNum; i++) {
            // 2.创建一个excel中sheet工作空间
            HSSFSheet sheet = hssfWorkbook.createSheet(sheetName.get(i));
            sheet.setDefaultColumnWidth(16);
            // 3.添加excel表头行,第一行
            HSSFRow row = sheet.createRow(0);
            // 4.添加单元格,并设置表头值,表头居中
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFCell cell;
            // 4.1设置表头信息
            if (columnNames != null && columnNames.length != 0 && columnNames.length < 255) {
                for (int j = 0; j < columnNames.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(columnNames[j]);
                    cell.setCellStyle(cellStyle);
                }
            } else {
                //        logger.error(String.format("excel header is not blank or header length larger than 255"));
                //   throw new Exception("excel header is not blank or header length larger than 255");
            }
            // 5.添加单元格,填充excel数据,第一行为表头,从第二行开始
            // int start = i * MAX_ROW;
            // int index = (i + 1) * MAX_ROW > size ? size : (i + 1) * MAX_ROW;
            ArrayList<String> list = list4all.get(i);
            int rowIndex = 0;//每一个sheet从下标为1开始
            for (int j = 0; j < list.size(); j++) {
                // 5.1 创建行
                row = sheet.createRow(++rowIndex);
                String rowList = list.get(j);
                //int rowSize = rowList.size();
                String[] tempList = rowList.split(":REXope:");
                int rowSize = tempList.length;
                if (tempList != null && !tempList.equals("")) {
                    for (int k = 0; k < rowSize; k++) {
                        // 5.2 创建列
                        cell = row.createCell(k);
                        cell.setCellValue(tempList[k]);
                    }
                }
            }
            list.clear();
        }
        return hssfWorkbook;
    }


    public static HSSFWorkbook buildExcelNew(String[] columnNames, ArrayList<LinkedHashMap<String, String>> list4all, ArrayList<String> sheetName) {
        //  int size = CollectionUtils.isEmpty(list) ? 0 : list.size();
        // 计算一个excel文件sheet工作空间的个数
        //  int sheetCount = (size + MAX_ROW) / MAX_ROW;
        //logger.info("need sheet size is {}", sheetCount);
        // 1.创建excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        int sheetNum = sheetName.size();
        //   StringBuffer sheetName = new StringBuffer("action");
        for (int i = 0; i < sheetNum; i++) {
            // 2.创建一个excel中sheet工作空间
            HSSFSheet sheet = hssfWorkbook.createSheet(sheetName.get(i));
            sheet.setDefaultColumnWidth(16);
            // 3.添加excel表头行,第一行
            HSSFRow row = sheet.createRow(0);
            // 4.添加单元格,并设置表头值,表头居中
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFCell cell;
            // 4.1设置表头信息
            if (columnNames != null && columnNames.length != 0 && columnNames.length < 255) {
                for (int j = 0; j < columnNames.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(columnNames[j]);
                    cell.setCellStyle(cellStyle);
                }
            } else {
                //        logger.error(String.format("excel header is not blank or header length larger than 255"));
                //   throw new Exception("excel header is not blank or header length larger than 255");
            }
            // 5.添加单元格,填充excel数据,第一行为表头,从第二行开始
            // int start = i * MAX_ROW;
            // int index = (i + 1) * MAX_ROW > size ? size : (i + 1) * MAX_ROW;
            LinkedHashMap<String, String> maplist = list4all.get(i);

            Object[] key = maplist.keySet().toArray();
            Arrays.sort(key);
            int rowIndex = 0;//每一个sheet从下标为1开始
            for (int index = 0; index < key.length; index++) {
                //    System.out.println(maplist.get(key[i]));

                //     for (int j = 0; j < maplist.size(); j++) {
                // 5.1 创建行
                row = sheet.createRow(++rowIndex);
                String rowList = maplist.get(key[index]);
                //int rowSize = rowList.size();
                String[] tempList = rowList.split(":REXope:");
                int rowSize = tempList.length;
                if (tempList != null && !tempList.equals("")) {
                    for (int k = 0; k < rowSize; k++) {
                        // 5.2 创建列
                        cell = row.createCell(k);
                        cell.setCellValue(tempList[k]);
                    }
                }
                //   }
            }
            maplist.clear();
        }
        return hssfWorkbook;
    }

    public static HSSFWorkbook buildExcelSingle(String[] columnNames, ArrayList<String> list, String sheetName) {
        //  int size = CollectionUtils.isEmpty(list) ? 0 : list.size();
        // 计算一个excel文件sheet工作空间的个数
        //  int sheetCount = (size + MAX_ROW) / MAX_ROW;
        //logger.info("need sheet size is {}", sheetCount);
        // 1.创建excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        int sheetNum = 1;
        //   StringBuffer sheetName = new StringBuffer("action");
        for (int i = 0; i < sheetNum; i++) {
            // 2.创建一个excel中sheet工作空间
            HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);

            sheet.setDefaultColumnWidth(16);
            // 3.添加excel表头行,第一行
            HSSFRow row = sheet.createRow(0);
            // 4.添加单元格,并设置表头值,表头居中
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFCell cell;
            // 4.1设置表头信息
            if (columnNames != null && columnNames.length != 0 && columnNames.length < 255) {
                for (int j = 0; j < columnNames.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(columnNames[j]);
                    cell.setCellStyle(cellStyle);
                }
            } else {
                //        logger.error(String.format("excel header is not blank or header length larger than 255"));
                //   throw new Exception("excel header is not blank or header length larger than 255");
            }
            // 5.添加单元格,填充excel数据,第一行为表头,从第二行开始
            // int start = i * MAX_ROW;
            // int index = (i + 1) * MAX_ROW > size ? size : (i + 1) * MAX_ROW;
            //  ArrayList<String> list = list4all.get(i);
            int rowIndex = 0;//每一个sheet从下标为1开始
            for (int j = 0; j < list.size(); j++) {
                // 5.1 创建行
                row = sheet.createRow(++rowIndex);
                String rowList = list.get(j);
                //int rowSize = rowList.size();
                String[] tempList = rowList.split(":REXope:");
                int rowSize = tempList.length;
                if (tempList != null && !tempList.equals("")) {
                    for (int k = 0; k < rowSize; k++) {
                        // 5.2 创建列
                        cell = row.createCell(k);
                        cell.setCellValue(tempList[k]);
                    }
                }
            }
            list.clear();
        }
        return hssfWorkbook;
    }

    private static String getCellValue(HSSFCell hssfCell) {
        // 返回布尔类型的值
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        }// 返回数值类型的值
        else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } // 返回字符串类型的值
        else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
}
