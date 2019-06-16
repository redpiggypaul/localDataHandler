/**
 *
 */
package com.csv.loader.PageXML2Xls;

import com.csv.entity.ElementMapping;
import com.csv.entity.TestEntity;
import com.csv.entity.autoTCEntity;
import com.csv.loader.FileList;
import com.csv.util.StringUtil;
import com.csv.util.XMLException;
import com.csv.util.readProperity;
import com.csvreader.CsvReader;
import org.apache.poi.hssf.usermodel.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author paul.zhu
 * @date 2016年7月14日 上午11:15:44
 */
public class IOSRootPageXML2xls extends  IOSBase{
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
        String pagePath = readProperity.getProValue("pageXML_IOSPath"); // page path  /Users/appledev131/Documents/REXAUTO4/PageXML/IOS/
        String tempPath = readProperity.getProValue("templateFilePath");  // //Users//appledev131//Documents//REXAUTO4//;
        String targetXLSPath = readProperity.getProValue("pageXLSPath");   // pageXLSPath = //Users//appledev131//Documents//REXAUTO4//PageXML//XLSgroup//
        try {

            FileList theL = new FileList(pagePath ,  tempPath + "checkFileName");
            ArrayList<String> pageXML = theL.getList2Arrary();

            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            String targetFile = new String(targetXLSPath + "pageSample_IOS_Root");
            String[] columnNames = {"name", "parentElementName", "waitTime", "textContent", "defaultValue", "type", "value", "NextPage", "triggerWindow", "showMode"};
            ArrayList<String> list4test = new ArrayList<String>();
            ArrayList<ArrayList<String>> list4operationRecordLine = new ArrayList<ArrayList<String>>();
            ArrayList<String> sheetName = new ArrayList<String>();
            sheetName.clear();
            list4test.clear();
            list4operationRecordLine.clear();

            ArrayList<String> pageXMLFileList = new ArrayList<String>();
            pageXMLFileList.clear();
            pageXMLFileList = pageXML;

            for (String theActionFileName : pageXMLFileList) {
                HashMap<String, ElementMapping> theElementMap = readPageXML(pagePath, theActionFileName);
                if (theElementMap.size() != 0) {
                    sheetName.add(theActionFileName.replaceFirst(".xml",""));
                    for (Iterator it = theElementMap.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry opeEntry = (Map.Entry) it.next();
                        ElementMapping temp = (ElementMapping) opeEntry.getValue();
                        list4test.add(
                                temp.getElementName() + ":REXele:" +
                                        temp.getParentElement() + ":REXele:"
                                        + temp.getWaitTime() + ":REXele:"
                                        + temp.getTextContent() + ":REXele:"
                                        + temp.getDefaultValue() + ":REXele:"
                                        + temp.getType() + ":REXele:"
                                        + temp.getLocatorStr() + ":REXele:"
                                        + temp.getNextPage() + ":REXele:"
                                        + temp.getTWindow() + ":REXele:"
                                        + temp.getMode()
                        );
                    }
                }
                list4operationRecordLine.add(new ArrayList<String>(list4test));
                list4test.clear();
            }
            buildExcel(targetFile, columnNames, list4operationRecordLine, sheetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private static void generateInsertSql() {
        try {
            CsvReader reader = new CsvReader("F://private//stat");
            reader.readHeaders();
            if (reader.readRecord()) {
                try {
                    StringBuilder builder = new StringBuilder(INSERT_PREFIX);
                    String month = reader.get("Month").replace("\"", "");
                    Date monthDate = inSdf.parse(month);
                    builder.append("'").append(outSdf.format(monthDate)).append("',");
                    builder.append("").append(new BigDecimal(reader.get("Sold Item")));
                    builder.append(")");
                    System.out.println(builder.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processRecords(String csvFilePath) {
        List<TestEntity> list = new ArrayList<TestEntity>();
        try {
            CsvReader reader = new CsvReader(csvFilePath);
            reader.readHeaders();

            while (reader.readRecord()) {
                try {
                    TestEntity record = new TestEntity();
                    String month = reader.get("Month").replace("\"", "");
                    Date monthDate = inSdf.parse(month);
                    record.setMonth(outSdf.format(monthDate));
                    if (!StringUtil.isEmpty(reader.get("Sold Item"))) {
                        record.setSoldItem(Integer.valueOf(reader.get("Sold Item").replace(",", "")));
                    } else {
                        record.setSoldItem(0);
                    }
                    list.add(record);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Record size: " + list.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EntityManager eManager = null;
        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("testDb");
            eManager = factory.createEntityManager();
            eManager.getTransaction().begin();
            for (TestEntity item : list) {
                eManager.persist(item);
            }
            eManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eManager != null) {
                eManager.close();
            }
        }
    }

    public static HashMap<String, ElementMapping> readPageXML
            (String absPath, String fileNameXML) throws Exception {
        HashMap<String, ElementMapping> elementsMap = new HashMap<String, ElementMapping>();
        elementsMap.clear();
        int lineN = 1;
        File file = new File(absPath + fileNameXML);
        if (!file.exists()) {
            throw new IOException("Can't find elements mapping file: " + fileNameXML);
        } else {
            System.out.println("Start to handle the file : " + file.getName());
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        try {
            if (file.toString().toLowerCase().contains(root.attributeValue("name").toLowerCase())) {
                if (root.getName().equalsIgnoreCase("page")) {
                    for (Iterator<?> it = root.elementIterator(); it.hasNext(); ) {
                        lineN++;
                        try {
                            Element elementObj = (Element) it.next();
                            String name = elementObj.attributeValue("name");
                            String waitTime = elementObj.attributeValue("waitTime");
                            String type = elementObj.attributeValue("type");
                            String value = elementObj.attributeValue("value");
                            String nextPage = elementObj.attributeValue("NextPage");
                            String triggerWindow = elementObj.attributeValue("triggerWindow");
                            String showMode = elementObj.attributeValue("showMode");
                            String dValue = elementObj.attributeValue("defaultValue"); // for textfiled default e.g value content
                            String text = elementObj.attributeValue("textContent");    // for text content of btn/line/title
                            String parEleName = elementObj.attributeValue("parentElementName");

                            ElementMapping eLocator;
                            if ((showMode.equalsIgnoreCase("ready") || showMode.toLowerCase().contains("ready"))) {
                                eLocator = new ElementMapping(name, value, waitTime, type, nextPage, showMode, dValue, text, triggerWindow, parEleName);
                                elementsMap.put(name, eLocator);
                            }
                        } catch (Exception e) {
                            throw new XMLException("The content in Element Line " + lineN + "is incorrect : " + e.getCause());
                        }
                    }
                } else {
                    throw new XMLException("The Root Line is not PAGE!!!");
                }
            } else {
                throw new XMLException("The NAME att is missing in the pageName ");
            }
        } catch (XMLException e) {
            throw new XMLException("Utility.XmlUtils.readXML : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elementsMap;
    }

    private static List<autoTCEntity> processTCselection(String csvFilePath) {
        List<autoTCEntity> list = new ArrayList<autoTCEntity>();
        try {
            CsvReader reader = new CsvReader(csvFilePath);
            reader.readHeaders();

            //    InputStream inputStream = null;
            //    inputStream = new FileInputStream("/Users/appledev131/Documents/REXAUTO4/Wb1.xlsx");
            //    HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            //    HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            //    System.out.println("~~~~~~~~~~~~~~"+sheet.getSheetName());

            while (reader.readRecord()) {
                try {
                    autoTCEntity TCList = new autoTCEntity();
                    String singleTCName = reader.get("TC Name");
                    //Date monthDate = inSdf.parse(month);
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
                        //	TCList.setSelectStatus(Integer.valueOf(reader.get("Sold Item").replace(",", "")));
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

/*
        EntityManager eManager = null;
        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("testDb");
            eManager = factory.createEntityManager();
            eManager.getTransaction().begin();
            for (autoTCEntity item : list) {
                eManager.persist(item);
            }
            eManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eManager != null) {
                eManager.close();
            }
        }
*/
        return list;
    }


    static void readLineVarFile(String fileName, int lineNumber) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName)));
        String line = reader.readLine();
        if (lineNumber < 0 || lineNumber > getTotalLines(fileName)) {
            System.out.println("不在文件的行数范围之内。");
        }
        int num = 0;
        while (line != null) {
            if (lineNumber == ++num) {
                //      System.out.println("line     " + lineNumber + ":     " + line);
            }
            line = reader.readLine();
        }
        reader.close();
    }

    static String getLineVarFile(String fileName, int lineNumber) throws IOException {
        String result = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName)));
        String line = reader.readLine();
        if (lineNumber < 0 || lineNumber > getTotalLines(fileName)) {
            System.out.println("不在文件的行数范围之内。");
        }
        int num = 0;
        while (line != null) {

            if (lineNumber == ++num) {
                //         System.out.println("line     " + lineNumber + ":     " + line);
                result = line.toString();
            }
            line = reader.readLine();

        }
        reader.close();
        return result;
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
            //    throw new BizException(String.format("import excel file[%s] fail,the file is not exist.", filePath));
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
                String[] tempList = rowList.split(":REXele:");
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
                String[] tempList = rowList.split(":REXele:");
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
