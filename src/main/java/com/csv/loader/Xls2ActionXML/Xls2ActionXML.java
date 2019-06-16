/**
 *
 */
package com.csv.loader.Xls2ActionXML;

import com.csv.entity.*;
import com.csv.util.StringUtil;
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

/**
 * @author paul.zhu
 * @date 2016年7月14日 上午11:15:44
 */
public class Xls2ActionXML {
    public final static String INSERT_PREFIX = "INSERT INTO test(month,soldItem) VALUES(";
    public final static SimpleDateFormat inSdf = new SimpleDateFormat("MMM yyyy", Locale.US);
    public final static SimpleDateFormat outSdf = new SimpleDateFormat("yyyyMM");

    public static void main(String args[]) throws IOException {
        // generateInsertSql();

        String singleBufferWriterLine = null;
        BufferedWriter bufferWriter = null;
        String autoToolPath = readProperity.getProValue("templateFilePath"); // templateFilePath = //Users//appledev131//Documents//REXAUTO4//
        String userDocuPath = readProperity.getProValue("userDocumentFolder");  // //Users//appledev131//Documents//
        try {
            String targetFileName = "testAction";
            String actionXMLtemplateName = autoToolPath + "ActionXMLTemplate.xml";

            List<actionEntity> actionLinelist = readExcel4Action2XML(userDocuPath + "csvloader//src//main//java//com//csv//action.xls", 0, actionXMLtemplateName);    //todo change for windows


        } catch (Exception e) {
            e.printStackTrace();
        }
        //	processRecords("//Users//appledev131//Documents//REXAUTO4//TC4run.csv");
        //	processRecords("F://private//ts.csv");
    }

    /**
     *
     */


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


    public static List<actionEntity> readExcel4Action2XML(String filePath, int rowIndex, String actionXMLtemplateName) throws Exception {
        List<actionEntity> list = new ArrayList<actionEntity>();
        InputStream inputStream = null;
        String autoToolPath = readProperity.getProValue("templateFilePath"); // templateFilePath = //Users//appledev131//Documents//REXAUTO4//
        try {
            inputStream = new FileInputStream(filePath);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            int sheetSize = hssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetSize; i++) {
                // 1.获取sheet表
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                System.out.println("~~~~~~~~~" + sheet.getSheetName());
                if (sheet == null) {
                    continue;
                }
                if (sheet.getSheetName().equalsIgnoreCase("actionSample")) {
                    System.out.println("~~~~~~~~~ Ignore the Action option Sample sheet ~~~~~~~~~");
                } else {

                    // 2.解析sheet表,如果没有指定从第几行开始读取数据,则默认从第二行起，对excel内容进行解析
                    if (rowIndex == 0) {
                        rowIndex = 1;
                    }
                    int lastRowNum = sheet.getLastRowNum();
                    List<String> rowList = null;
                    if (!sheet.getSheetName().equalsIgnoreCase("")) {
                        int index4OpeName = 0;
                        int index4Step = 0;
                        int index4PageName = 0;
                        int index4OpeType = 0;
                        int index4EleName = 0;
                        int index4EleType = 0;
                        int index4Para = 0;


                        HSSFRow tophssfRow = sheet.getRow(0);
                        int toplastCellNum = tophssfRow.getLastCellNum();
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("OperationName")) {
                                index4OpeName = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("Step")) {
                                index4Step = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("PageName")) {
                                index4PageName = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("OperationType")) {
                                index4OpeType = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("elementName")) {
                                index4EleName = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("elementType")) {
                                index4EleType = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("elementParameter")) {
                                index4Para = cellIndex;
                            }
                        }

                        for (int j = rowIndex; j <= lastRowNum; j++) {
                            actionEntity singleTcEntry = new actionEntity();
                            //   rowList = new ArrayList<String>();
                            HSSFRow hssfRow = sheet.getRow(j);
                            int lastCellNum = hssfRow.getLastCellNum();
                            if (hssfRow != null) {
                                for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                    HSSFCell cell = hssfRow.getCell(cellIndex);
                                    //  rowList.add(getCellValue(cell));
                                    if (cellIndex == index4OpeName) {
                                        singleTcEntry.setOpeName(cell.getStringCellValue());
                                    }
                                    if (cellIndex == index4Step) {
                                        singleTcEntry.setStep(cell.getStringCellValue());
                                    }
                                    if (cellIndex == index4PageName) {
                                        singleTcEntry.setPageName(cell.getStringCellValue());
                                    }

                                    if (cellIndex == index4OpeType) {
                                        singleTcEntry.setOperationType(cell.getStringCellValue());
                                    }
                                    if (cellIndex == index4EleName) {
                                        singleTcEntry.setElementName(cell.getStringCellValue());
                                    }
                                    if (cellIndex == index4EleType) {
                                        singleTcEntry.setElementType(cell.getStringCellValue());
                                    }
                                    if (cellIndex == index4Para) {
                                        singleTcEntry.setElementParameter(cell.getStringCellValue());
                                    }
                                }
                            }
                            list.add(singleTcEntry);
                        }
                    }

                    // after get the sheet content then

                    File targetFile = new File(autoToolPath + "OperationXML/createdByXLS/Action_" + sheet.getSheetName() + ".xml");
                    if (list.size() == 0) {
                        throw new FileNotFoundException("The Action XLS file is missing!");
                    }

                    // 读取文件
                    String fileName = actionXMLtemplateName;

                    FileReader tempFileRead = new FileReader(fileName);
                    BufferedReader br = new BufferedReader(tempFileRead);
                    BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(targetFile));
                    int ind = 1;
                    int totalNo = getTotalLines(fileName);
                    String singleBufferWriterLine = null;
                    System.out.println("There are " + totalNo + " lines in the text!");
                    if (totalNo == 3) {
                        while ((singleBufferWriterLine = br.readLine()) != null) {
                            if (ind == 1) {
                                readLineVarFile(fileName, ind);
                                bufferWriter.write(singleBufferWriterLine.replaceFirst("ActionTemplate", sheet.getSheetName()));//输出字符串
                                bufferWriter.newLine();//换行
                                bufferWriter.flush();

                            } else if (ind == 2) {
                                String rowContent = getLineVarFile(fileName, ind);
                                String backup = getLineVarFile(fileName, ind);
                                for (actionEntity theSingle : list) {
                                    readLineVarFile(fileName, ind);
                                    if (theSingle.getOpeName() != null) {

                                        rowContent = new String(rowContent.replaceFirst("tempName", theSingle.getOpeName()));
                                        rowContent = new String(rowContent.replaceFirst("tempStep", theSingle.getStep()));
                                        rowContent = new String(rowContent.replaceFirst("tempPage", theSingle.getPageName()));
                                        rowContent = new String(rowContent.replaceFirst("opeType", theSingle.getOperationType()));
                                        rowContent = new String(rowContent.replaceFirst("eleName", theSingle.getElementName()));
                                        rowContent = new String(rowContent.replaceFirst("eleType", theSingle.getElementType()));

                                        rowContent = new String(rowContent.replaceFirst("localPara", ""));
                                        bufferWriter.write(rowContent);//输出字符串
                                        bufferWriter.newLine();//换行
                                        bufferWriter.flush();
                                    }
                                    rowContent = new String(backup);
                                }
                            } else {
                                System.out.println(singleBufferWriterLine);
                                bufferWriter.write(singleBufferWriterLine);//输出字符串
                                bufferWriter.newLine();//换行
                                bufferWriter.flush();
                            }
                            ind++;
                        }


                    }
                    list.clear();
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
