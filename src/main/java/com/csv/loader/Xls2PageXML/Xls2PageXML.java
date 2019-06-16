/**
 *
 */
package com.csv.loader.Xls2PageXML;

import com.csv.entity.ElementMapping;
import com.csv.entity.TestEntity;
import com.csv.entity.actionEntity;
import com.csv.entity.autoTCEntity;
import com.csv.loader.FileList;
import com.csv.loader.FolderList;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author paul.zhu
 * @date 2016年7月14日 上午11:15:44
 */
public class Xls2PageXML {
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
            String pageXMLtemplateName = autoToolPath + "PageXMLTemplate.xml";
            String osType = "iPAD";  // And IOS iPAD

            ArrayList<String> And_folderName = new ArrayList<String>();
            FolderList theL4And = new FolderList(autoToolPath + "PageXML/And/", userDocuPath + "checkANDFolderName");
            And_folderName = theL4And.getFolderList2Arrary();
            And_folderName.add("Root");

            ArrayList<String> IOS_folderName = new ArrayList<String>();
            FolderList theL4IOS = new FolderList(autoToolPath + "PageXML/IOS/", userDocuPath + "checkANDFolderName");
            IOS_folderName = theL4IOS.getFolderList2Arrary();
            IOS_folderName.add("Root");

            ArrayList<String> IPAD_folderName = new ArrayList<String>();
            FolderList theL4IPAD = new FolderList(autoToolPath + "PageXML/iPAD/", userDocuPath + "checkANDFolderName");
            IPAD_folderName = theL4IPAD.getFolderList2Arrary();
            IPAD_folderName.add("Root");

            if (osType.equalsIgnoreCase("And")) {
                for (String temp : And_folderName) {
                    List<ElementMapping> actionLinelist = readExcel4Page2XML(autoToolPath + "PageXML/XLSgroup/pageSample_", osType, temp, 0, pageXMLtemplateName);
                }
            } else if (osType.equalsIgnoreCase("IOS")) {
                for (String temp : IOS_folderName) {
                    List<ElementMapping> actionLinelist = readExcel4Page2XML(autoToolPath + "PageXML/XLSgroup/pageSample_", osType, temp, 0, pageXMLtemplateName);
                }
            } else if (osType.equalsIgnoreCase("iPAD")) {
                for (String temp : IPAD_folderName) {
                    List<ElementMapping> actionLinelist = readExcel4Page2XML(autoToolPath + "PageXML/XLSgroup/pageSample_", osType, temp, 0, pageXMLtemplateName);
                }
            } else {
                throw new Exception("Wrong os type : " + osType);
            }

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


    public static List<ElementMapping> readExcel4Page2XML(String filePath, String osType, String xlsName, int rowIndex, String actionXMLtemplateName) throws Exception {
        List<ElementMapping> list = new ArrayList<ElementMapping>();
        InputStream inputStream = null;
        try {


            inputStream = new FileInputStream(filePath + osType + "_" + xlsName + ".xls");
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            int sheetSize = hssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetSize; i++) {
                // 1.获取sheet表
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                System.out.println("~~~~~~~~~" + sheet.getSheetName());
                if (sheet == null) {
                    continue;
                }
                // 2.解析sheet表,如果没有指定从第几行开始读取数据,则默认从第二行起，对excel内容进行解析
                if (rowIndex == 0) {
                    rowIndex = 1;
                }
                int lastRowNum = sheet.getLastRowNum();
                List<String> rowList = null;
                if (!sheet.getSheetName().equalsIgnoreCase("")) {
                    int index4theEleName = 0;
                    int index4theParEleName = 0;
                    int index4theWTime = 0;
                    int index4theTextContent = 0;
                    int index4theDValue = 0;
                    int index4theType = 0;
                    int index4theLocator = 0;
                    int index4theNPage = 0;
                    int index4theTWin = 0;
                    int index4theMode = 0;

                    HSSFRow tophssfRow = sheet.getRow(0);
                    int toplastCellNum = tophssfRow.getLastCellNum();
                    for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                        HSSFCell cell = tophssfRow.getCell(cellIndex);
                        String topLineContent = cell.getStringCellValue();
                        if (topLineContent.equalsIgnoreCase("name")) {
                            index4theEleName = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("parentElementName")) {
                            index4theParEleName = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("waitTime")) {
                            index4theWTime = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("textContent")) {
                            index4theTextContent = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("defaultValue")) {
                            index4theDValue = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("type")) {
                            index4theType = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("value")) {
                            index4theLocator = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("NextPage")) {
                            index4theNPage = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("triggerWindow")) {
                            index4theTWin = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("showMode")) {
                            index4theMode = cellIndex;
                        }
                    }

                    for (int j = rowIndex; j <= lastRowNum; j++) {
                        ElementMapping singleTcEntry = new ElementMapping();
                        //   rowList = new ArrayList<String>();
                        HSSFRow hssfRow = sheet.getRow(j);
                        int lastCellNum = hssfRow.getLastCellNum();
                        if (hssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = hssfRow.getCell(cellIndex);
                                if (cellIndex == index4theEleName) {
                                    singleTcEntry.setElementName(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theParEleName) {
                                    singleTcEntry.setParElementName(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theWTime) {
                                    singleTcEntry.setWaitTime((cell.getStringCellValue()));
                                }
                                if (cellIndex == index4theTextContent) {
                                    singleTcEntry.setTextCon(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theDValue) {
                                    singleTcEntry.setDValue(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theType) {
                                    singleTcEntry.setType(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theLocator) {
                                    singleTcEntry.setLocatorStr(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theNPage) {
                                    singleTcEntry.setNPage(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theTWin) {
                                    singleTcEntry.setTWindow(cell.getStringCellValue());
                                }
                                if (cellIndex == index4theMode) {
                                    singleTcEntry.setMode(cell.getStringCellValue());
                                }
                            }
                        }
                        list.add(singleTcEntry);
                    }
                }

                java.io.File preparefolder = new File("/Users/appledev131/Documents/REXAUTO4/PageXML/createdByXls/" + osType);
                java.io.File preparefolderSub = new File("/Users/appledev131/Documents/REXAUTO4/PageXML/createdByXls/" + osType + "/" + xlsName);

                if (!preparefolder.getParentFile().exists()) {              //如果目标文件所在的目录不存在，则创建父目录
                    System.out.println("目标文件所在目录不存在，准备创建它");
                    if (!preparefolder.getParentFile().mkdirs()) {
                        System.out.println("创建目标文件所在目录失败");
                    }
                } else {
                    preparefolder.mkdir();
                }
                if (!xlsName.equalsIgnoreCase("Root")) {
                    if (!preparefolderSub.getParentFile().exists()) {              //如果目标文件所在的目录不存在，则创建父目录
                        System.out.println("目标文件所在目录不存在，准备创建它");
                        if (!preparefolderSub.getParentFile().mkdirs()) {
                            System.out.println("创建目标文件所在目录失败");
                        }
                    } else {
                        preparefolderSub.mkdir();
                    }
                } else {
                }

                File targetFile = null;
                if (!xlsName.equalsIgnoreCase("Root")) {
                    targetFile = new File("/Users/appledev131/Documents/REXAUTO4/PageXML/createdByXls/" + osType + "/" + xlsName + "/" + sheet.getSheetName() + ".xml");      //todo change for windows
                } else {
                    targetFile = new File("/Users/appledev131/Documents/REXAUTO4/PageXML/createdByXls/" + osType + "/" + sheet.getSheetName() + ".xml");      //todo change for windows
                }

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
                            bufferWriter.write(singleBufferWriterLine.replaceFirst("PageTemplate", sheet.getSheetName()));//输出字符串
                            bufferWriter.newLine();//换行
                            bufferWriter.flush();

                        } else if (ind == 2) {
                            String rowContent = getLineVarFile(fileName, ind);
                            String backup = getLineVarFile(fileName, ind);
                            for (ElementMapping theSingle : list) {
                                readLineVarFile(fileName, ind);
                                if (theSingle.getElementName() != null) {

                                    rowContent = new String(rowContent.replaceFirst("theEleName", theSingle.getElementName()));
                                    rowContent = new String(rowContent.replaceFirst("theParEleName", theSingle.getParentElement()));
                                    rowContent = new String(rowContent.replaceFirst("theWTime", "0"));
                                    rowContent = new String(rowContent.replaceFirst("theTextContent", theSingle.getTextContent()));
                                    rowContent = new String(rowContent.replaceFirst("theDValue", theSingle.getDefaultValue()));
                                    rowContent = new String(rowContent.replaceFirst("theType", theSingle.getType()));
                                    rowContent = new String(rowContent.replaceFirst("theLocator", theSingle.getLocatorStr()));
                                    rowContent = new String(rowContent.replaceFirst("theNPage", theSingle.getNextPage()));
                                    rowContent = new String(rowContent.replaceFirst("theTWin", theSingle.getTWindow()));

                                    rowContent = new String(rowContent.replaceFirst("theMode", theSingle.getMode()));
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


    public static List<actionEntity> readExcel4Action(String filePath, int rowIndex) {
        List<actionEntity> list = new ArrayList<actionEntity>();
        InputStream inputStream = null;
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
            }
        } catch (
                FileNotFoundException e
                )

        {
            e.printStackTrace();
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        } finally

        {
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
