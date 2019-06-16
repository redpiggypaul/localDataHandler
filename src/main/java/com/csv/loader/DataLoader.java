/**
 *
 */
package com.csv.loader;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.csv.entity.*;
import com.csv.util.DateHandle;
import com.csv.util.StringUtil;
import com.csv.util.readProperity;
import com.csvreader.CsvReader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.model.CalculationChain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author paul.zhu
 * @date 2016年7月14日 上午11:15:44
 */
public class DataLoader {
    public final static String INSERT_PREFIX = "INSERT INTO test(month,soldItem) VALUES(";
    public final static SimpleDateFormat inSdf = new SimpleDateFormat("MMM yyyy", Locale.US);
    public final static SimpleDateFormat outSdf = new SimpleDateFormat("yyyyMM");

    public static void main(String args[]) throws IOException {
        // generateInsertSql();

        String singleBufferWriterLine = null;
        BufferedWriter bufferWriter = null;
        BufferedWriter realWriter = null;
        String tempPath = readProperity.getProValue("templateFilePath");  // //Users//appledev131//Documents//REXAUTO4//
        androidInstanceEntry mainAndroidEntry = new androidInstanceEntry();
        iosInstanceEntry mainIOSEntry = new iosInstanceEntry();
        try {
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            String testNGtemplateName = tempPath + "testNGtemplate.xml";
            File targetFile = new File(tempPath + time + ".xml");
            File runnerFile = new File("//Users//appledev131//Documents//REXAUTO4//src//REXenvConfig//run_" + time + ".xml");         //todo change for windows
            //  List<autoTCEntity> tcLinelist = processTCselection("/Users/appledev131/Documents/REXAUTO4/TC4run.csv");
            List<autoTCEntity> tcLinelist = readExcel4auto("//Users//appledev131//Documents//csvloader//src//main//java//com//csv//EnvConfigDate.xls", 0);  //todo change for windows
            accountInfo theAccountGroup = readExcel4autoAccount("//Users//appledev131//Documents//csvloader//src//main//java//com//csv//EnvConfigDate.xls", 0);  //todo change for windows
            envConfigEntry theENVConfigInfo = readExcel4autoENVConfig("//Users//appledev131//Documents//csvloader//src//main//java//com//csv//EnvConfigDate.xls", 0);  //todo change for windows
            if (theENVConfigInfo.getTargetMobileOS().equalsIgnoreCase("android")) {
                mainAndroidEntry = readExcel4andEntry("//Users//appledev131//Documents//csvloader//src//main//java//com//csv//EnvConfigDate.xls", 0);  //todo change for windows
            } else {
                mainIOSEntry = readExcel4iosEntry("//Users//appledev131//Documents//csvloader//src//main//java//com//csv//EnvConfigDate.xls", 0);  //todo change for windows
            }
            if (tcLinelist.size() == 0) {
                throw new FileNotFoundException("The TC selection file is missing!");
            }

            // 读取文件
            String fileName = testNGtemplateName;
            FileReader tempFileRead = new FileReader(fileName);
            BufferedReader br = new BufferedReader(tempFileRead);
            bufferWriter = new BufferedWriter(new FileWriter(targetFile));
            realWriter = new BufferedWriter(new FileWriter(runnerFile));
            int ind = 1;
            int totalNo = getTotalLines(fileName);
            System.out.println("There are " + totalNo + " lines in the text!");
            if (totalNo == 13) {
                while ((singleBufferWriterLine = br.readLine()) != null) {
                    if (ind == 6) {
                        readLineVarFile(fileName, ind);
                        bufferWriter.write(singleBufferWriterLine.replaceFirst("date", time));//输出字符串
                        bufferWriter.newLine();//换行
                        bufferWriter.flush();

                        realWriter.write(singleBufferWriterLine.replaceFirst("date", time));//输出字符串
                        realWriter.newLine();//换行
                        realWriter.flush();

                    } else if (ind == 9) {
                        String rowContent = getLineVarFile(fileName, ind);
                        String rowEndContent = getLineVarFile(fileName, 10);
                        for (autoTCEntity theSingle : tcLinelist) {
                            readLineVarFile(fileName, ind);

                            if (theSingle.getSelectStatus() == true) {
                                bufferWriter.write(rowContent.replaceFirst("AppTestAccount", theSingle.getTcName()));//输出字符串
                                bufferWriter.newLine();//换行
                                bufferWriter.flush();
                                bufferWriter.write(rowEndContent);//输出字符串
                                bufferWriter.newLine();//换行
                                bufferWriter.flush();

                                realWriter.write(rowContent.replaceFirst("AppTestAccount", theSingle.getTcName()));//输出字符串
                                realWriter.newLine();//换行
                                realWriter.flush();
                                realWriter.write(rowEndContent);//输出字符串
                                realWriter.newLine();//换行
                                realWriter.flush();
                            }
                        }
                        //ind = 10;

                    } else {
                        System.out.println(singleBufferWriterLine);
                        bufferWriter.write(singleBufferWriterLine);//输出字符串
                        bufferWriter.newLine();//换行
                        bufferWriter.flush();

                        realWriter.write(singleBufferWriterLine);//输出字符串
                        realWriter.newLine();//换行
                        realWriter.flush();
                    }

                    if (ind == 9) {
                        singleBufferWriterLine = br.readLine();
                    }
                    ind++;
                }

            /*    // 获取文件的内容的总行数

                // 指定读取的行号
                int lineNumber = 6;

                //读取指定行的内容
                readLineVarFile("//Users//appledev131//Documents//REXAUTO4//testNGtemplate.xml", lineNumber);

                lineNumber = 9;
                readLineVarFile("//Users//appledev131//Documents//REXAUTO4//testNGtemplate.xml", lineNumber);
                */
            } else {
                throw new FileNotFoundException("The testNG sample xml file is wrong!");
            }

            Properties prop = new Properties();// 属性集合对象
            FileInputStream fis = new FileInputStream(("\\Users\\appledev131\\Documents\\csvloader\\src\\main\\java\\com\\csv\\config_template.properties").replaceAll("\\\\", File.separator));// 属性文件输入流
            prop.load(fis);// 将属性文件流装载到Properties对象中
            fis.close();// 关闭流
            TimeZone tz = TimeZone.getTimeZone("America/Chicago");
            Calendar cl = Calendar.getInstance(tz, Locale.US);
            Calendar cl4month = Calendar.getInstance(tz, Locale.US);
            String defaultDay = String.valueOf(cl.get(Calendar.DAY_OF_MONTH));
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            SimpleDateFormat sdf4month = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            String yesterdayString = null;
            String monkeydayString = null;
            String tomorrowString = null;
            String lastMonthName = null;
            String last2MonthName = null;
            int lastDayInt = DateHandle.getLastDayOfMonth(new Date(), "America/Chicago");
            if (theENVConfigInfo.getTargetMobileOS().toLowerCase().startsWith("ios")) {
                cl4month.add(Calendar.MONTH, -1);
                //       cl.(Calendar.MONTH, -1);
                lastMonthName = new String(sdf4month.format(cl4month.getTime()));
                cl4month.add(Calendar.MONTH, -1);
                last2MonthName = new String(sdf4month.format(cl4month.getTime()));
                if (Integer.parseInt(defaultDay) > 2 && Integer.parseInt(defaultDay) != lastDayInt) {
                    int yesterdayInt = Integer.parseInt(defaultDay) - 2;
                    yesterdayString = new String(String.valueOf(yesterdayInt));
                    monkeydayString = new String(String.valueOf(Integer.parseInt(defaultDay) + 1));
                } else {
                    cl.add(Calendar.MONTH, -1);
                    int lastDay = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
                    yesterdayString = String.valueOf(lastDay - 1);
                    monkeydayString = String.valueOf(lastDay - 2);
                }
            } else {
                if (Integer.parseInt(defaultDay) > 3) {
                    int yesterdayInt = Integer.parseInt(defaultDay) - 2;
                    yesterdayString = new String(String.valueOf(yesterdayInt));
                    monkeydayString = new String(String.valueOf(yesterdayInt - 1));
                } else {
                    cl.set(Calendar.DAY_OF_MONTH, 1);
                    cl.add(Calendar.DATE, -2);
                    yesterdayString = sdf.format(cl.getTime());
                    cl.add(Calendar.DATE, -1);
                    monkeydayString = sdf.format(cl.getTime());
                }
            }
            if (Integer.parseInt(defaultDay) != lastDayInt) {
                tomorrowString = new String(String.valueOf(Integer.parseInt(defaultDay) + 1));
            } else {
                tomorrowString = new String("1");
            }
            prop.setProperty("tomorrow", tomorrowString);
            prop.setProperty("normal_startdate", monkeydayString);
            prop.setProperty("normal_enddate", yesterdayString);
            prop.setProperty("abnormal_startdate", yesterdayString);
            prop.setProperty("abnormal_enddate", monkeydayString);

            prop.setProperty("resumeAccount", theAccountGroup.getUserMail("resume"));
            prop.setProperty("resumeAccountPSW", theAccountGroup.getPSW("resume"));
            prop.setProperty("TEAccount", theAccountGroup.getUserMail("T&E"));
            prop.setProperty("TEAccountPSW", theAccountGroup.getPSW("T&E"));
            prop.setProperty("SetAccount", theAccountGroup.getUserMail("AccountSetting"));
            prop.setProperty("SetAccountPSW", theAccountGroup.getPSW("AccountSetting"));

            prop.setProperty("autoRunningOSType", theENVConfigInfo.getAutoRunningOSType());
            prop.setProperty("targetMobileOS", theENVConfigInfo.getTargetMobileOS());
            prop.setProperty("deviceORsimulator", theENVConfigInfo.getdeviceORsimulator());
            prop.setProperty("pageLoadTimeout", theENVConfigInfo.getpageLoadTimeout());
            prop.setProperty("singleElementSearchTimeDuration", theENVConfigInfo.getsingleElementSearchTimeDuration());

            prop.setProperty("singleElementSearchTimeDuration", theENVConfigInfo.getsingleElementSearchTimeDuration());
            prop.setProperty("singleElementSearchTimeDuration", theENVConfigInfo.getsingleElementSearchTimeDuration());
            prop.setProperty("singleElementSearchTimeDuration", theENVConfigInfo.getsingleElementSearchTimeDuration());
            prop.setProperty("singleElementSearchTimeDuration", theENVConfigInfo.getsingleElementSearchTimeDuration());

            if (theENVConfigInfo.getTargetMobileOS().equalsIgnoreCase("android")) {
                prop.setProperty("screenWide", theENVConfigInfo.getscreenWide());
                prop.setProperty("screenLength", theENVConfigInfo.getscreenLength());
                prop.setProperty("upOFscreen", theENVConfigInfo.getupOFscreen());
                prop.setProperty("downOFscreen", theENVConfigInfo.getdownOFscreen());

                prop.setProperty("androidOSVersion", mainAndroidEntry.getosVersion());
                prop.setProperty("androidPlatformVersion", mainAndroidEntry.getplatform());


                prop.setProperty("IOSscreenWide", "");
                prop.setProperty("IOSscreenLength", "");
                prop.setProperty("IOSupOFscreen", "");
                prop.setProperty("IOSdownOFscreen", "");

                prop.setProperty("IOSdeviceType", "");
                prop.setProperty("IOSdeviceName", "");
                prop.setProperty("IOSplatformVersion", "");
                prop.setProperty("IOSplatformName", "");
                prop.setProperty("IOSorientationMode", "");
                prop.setProperty("IOSudid", "");
            } else {
                prop.setProperty("normal_startmonth", last2MonthName);
                prop.setProperty("normal_endmonth", lastMonthName);
                prop.setProperty("abnormal_startmonth", lastMonthName);
                prop.setProperty("abnormal_endmonth", last2MonthName);

                prop.setProperty("screenWide", "");
                prop.setProperty("screenLength", "");
                prop.setProperty("upOFscreen", "");
                prop.setProperty("downOFscreen", "");

                prop.setProperty("androidOSVersion", "");
                prop.setProperty("androidPlatformVersion", "");

                prop.setProperty("IOSscreenWide", theENVConfigInfo.getIOSscreenWide());
                prop.setProperty("IOSscreenLength", theENVConfigInfo.getIOSscreenLength());
                prop.setProperty("IOSupOFscreen", theENVConfigInfo.getIOSupOFscreen());
                prop.setProperty("IOSdownOFscreen", theENVConfigInfo.getIOSdownOFscreen());

                prop.setProperty("IOSdeviceType", mainIOSEntry.getDeviceType());
                prop.setProperty("IOSdeviceName", mainIOSEntry.getDeviceName());
                prop.setProperty("IOSplatformVersion", mainIOSEntry.getplatformversion());
                prop.setProperty("IOSplatformName", mainIOSEntry.getplatformname());
                prop.setProperty("IOSbundleID", mainIOSEntry.getBundleId());

                if (theENVConfigInfo.getTargetMobileOS().contains("ipad")) {
                    prop.setProperty("IOSorientationMode", mainIOSEntry.getorientation());
                } else {
                    prop.setProperty("IOSorientationMode", "");
                }
                if (theENVConfigInfo.getdeviceORsimulator().equalsIgnoreCase("device")) {
                    prop.setProperty("IOSudid", mainIOSEntry.getUdid());
                } else {
                    prop.setProperty("IOSudid", "");
                }
            }
            for (autoTCEntity theSingle : tcLinelist) {
                if (theSingle.getSelectStatus() == true) {
                    prop.setProperty(theSingle.getTcName() + "_account", theSingle.getMail());
                    prop.setProperty(theSingle.getTcName() + "_accountPSW", theSingle.getPSW());
                }
            }

            /*
                        Properties prop = new Properties();// 属性集合对象
            FileInputStream fis = new FileInputStream(("\\Users\\appledev131\\Documents\\csvloader\\src\\main\\java\\com\\csv\\config_template.properties").replaceAll("\\\\", File.separator));// 属性文件输入流
            prop.load(fis);// 将属性文件流装载到Properties对象中
            fis.close();// 关闭流
             */

            FileOutputStream fos = new FileOutputStream(("\\Users\\appledev131\\Documents\\REXAUTO4\\src\\REXenvConfig\\config.properties").replaceAll("\\\\", File.separator));
            // 将Properties集合保存到流中
            prop.store(fos, "Copyright (c) Paul ZHU's Studio");
            fos.close();// 关闭流

        } catch (Exception e) {
            e.printStackTrace();
        }
        //	processRecords("//Users//appledev131//Documents//REXAUTO4//TC4run.csv");
        //	processRecords("F://private//ts.csv");
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
                result = line.toString();
            }
            line = reader.readLine();

        }
        reader.close();
        return result;
    }

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


    public static List<autoTCEntity> readExcel4auto(String filePath, int rowIndex) {
        List<autoTCEntity> list = new ArrayList<autoTCEntity>();
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
                if (sheet.getSheetName().equalsIgnoreCase("TC choose")) {
                    int index4TCName = 0;
                    int index4Status = 0;
                    int index4mail = 0;
                    int index4psw = 0;

                    HSSFRow tophssfRow = sheet.getRow(0);
                    int toplastCellNum = tophssfRow.getLastCellNum();
                    for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                        HSSFCell cell = tophssfRow.getCell(cellIndex);
                        String topLineContent = cell.getStringCellValue();
                        if (topLineContent.equalsIgnoreCase("TC Name")) {
                            index4TCName = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("Selected")) {
                            index4Status = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("UserMail")) {
                            index4mail = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("Password")) {
                            index4psw = cellIndex;
                        }
                    }

                    for (int j = rowIndex; j <= lastRowNum; j++) {
                        autoTCEntity singleTcEntry = new autoTCEntity();
                        //   rowList = new ArrayList<String>();
                        HSSFRow hssfRow = sheet.getRow(j);
                        int lastCellNum = hssfRow.getLastCellNum();
                        if (hssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = hssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == index4TCName) {
                                    singleTcEntry.setTcName(cell.getStringCellValue());
                                }
                                if (cellIndex == index4Status) {
                                /*
                                    if (temp.equalsIgnoreCase("yes") || temp.toLowerCase().contains("yes")) {
                                        TCList.setSelectStatus(true);
                                    } else if (temp.equalsIgnoreCase("no") || temp.toLowerCase().contains("no"))
                                        TCList.setSelectStatus(false);
                                    else {
                                        TCList.setSelectStatus(false);
                                    }
                                    */
                                    String temp = cell.getStringCellValue();
                                    if (temp.equalsIgnoreCase("yes") || temp.toLowerCase().contains("yes")) {
                                        singleTcEntry.setSelectStatus(true);
                                    } else if (temp.equalsIgnoreCase("no") || temp.toLowerCase().contains("no"))
                                        singleTcEntry.setSelectStatus(false);
                                    else {
                                        singleTcEntry.setSelectStatus(false);
                                    }
                                }
                                if (cellIndex == index4mail) {
                                    singleTcEntry.setMail(cell.getStringCellValue());
                                }
                                if (cellIndex == index4psw) {
                                    singleTcEntry.setPSW(cell.getStringCellValue());
                                }

                            }
                        }
                        list.add(singleTcEntry);
                    }
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

    public static accountInfo readExcel4autoAccount(String filePath, int rowIndex) {
        accountInfo result = new accountInfo();
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
                if (sheet.getSheetName().equalsIgnoreCase("Account List")) {
                    int index4ResumeAccount = 0;
                    int index4ResumeAccountPSW = 0;
                    int index4TEAccount = 0;
                    int index4TEAccountPSW = 0;
                    int index4AccSetAccount = 0;
                    int index4AccSetAccountPSW = 0;

                    HSSFRow tophssfRow = sheet.getRow(0);
                    int toplastCellNum = tophssfRow.getLastCellNum();
                    for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                        HSSFCell cell = tophssfRow.getCell(cellIndex);
                        String topLineContent = cell.getStringCellValue();
                        if (topLineContent.equalsIgnoreCase("resumeAccount")) {
                            index4ResumeAccount = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("resumeAccountPSW")) {
                            index4ResumeAccountPSW = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("TEAccount")) {
                            index4TEAccount = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("TEAccountPSW")) {
                            index4TEAccountPSW = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("SetAccount")) {
                            index4AccSetAccount = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("SetAccountPSW")) {
                            index4AccSetAccountPSW = cellIndex;
                        }
                    }

                    accountInfo singleAccountEntry = new accountInfo();
                    //   rowList = new ArrayList<String>();
                    HSSFRow hssfRow = sheet.getRow(1);
                    int lastCellNum = hssfRow.getLastCellNum();
                    if (hssfRow != null) {
                        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                            HSSFCell cell = hssfRow.getCell(cellIndex);
                            //  rowList.add(getCellValue(cell));
                            if (cellIndex == index4ResumeAccount) {
                                singleAccountEntry.setUserMail(cell.getStringCellValue());
                            }
                            if (cellIndex == index4ResumeAccountPSW) {
                                singleAccountEntry.setPSW(cell.getStringCellValue());
                            }
                            if (cellIndex == index4TEAccount) {
                                singleAccountEntry.setUserTEMail(cell.getStringCellValue());
                            }
                            if (cellIndex == index4TEAccountPSW) {
                                singleAccountEntry.setPSWTE(cell.getStringCellValue());
                            }
                            if (cellIndex == index4AccSetAccount) {
                                singleAccountEntry.setUserSetMail(cell.getStringCellValue());
                            }
                            if (cellIndex == index4AccSetAccountPSW) {
                                singleAccountEntry.setPSWSet(cell.getStringCellValue());
                            }
                        }
                    }
                    result = singleAccountEntry;
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
        return result;
    }


    public static envConfigEntry readExcel4autoENVConfig(String filePath, int rowIndexd) {
        envConfigEntry result = new envConfigEntry();
        InputStream inputStream = null;
        int rowIndex = 0;
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
                if (sheet.getSheetName().equalsIgnoreCase("ENV Config")) {
                    envConfigEntry theENVConfigEntry = new envConfigEntry();
                    int index4autoRunningOSType = 0;
                    int index4targetMobileOS = 0;
                    int index4deviceORsimulator = 0;
                    int index4pageLoadTimeout = 0;
                    int index4singleElementSearchTimeDuration = 0;

                    HSSFRow tophssfRow = sheet.getRow(0);
                    int toplastCellNum = 5;
                    for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                        HSSFCell cell = tophssfRow.getCell(cellIndex);
                        String topLineContent = cell.getStringCellValue();
                        if (topLineContent.equalsIgnoreCase("autoRunningOSType")) {
                            index4autoRunningOSType = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("targetMobileOS")) {
                            index4targetMobileOS = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("deviceORsimulator")) {
                            index4deviceORsimulator = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("pageLoadTimeout")) {
                            index4pageLoadTimeout = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("singleElementSearchTimeDuration")) {
                            index4singleElementSearchTimeDuration = cellIndex;
                        }
                    }

                    HSSFRow entryhssfRow = sheet.getRow(1);
                    int lastCellNum = entryhssfRow.getLastCellNum();
                    if (entryhssfRow != null) {
                        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                            HSSFCell cell = entryhssfRow.getCell(cellIndex);
                            //  rowList.add(getCellValue(cell));
                            if (cellIndex == index4autoRunningOSType) {
                                theENVConfigEntry.setAutoRunningOSType(cell.getStringCellValue());
                            }
                            if (cellIndex == index4targetMobileOS) {
                                theENVConfigEntry.setTargetMobileOS(cell.getStringCellValue());
                            }
                            if (cellIndex == index4deviceORsimulator) {
                                theENVConfigEntry.setdeviceORsimulator(cell.getStringCellValue());
                            }
                            if (cellIndex == index4pageLoadTimeout) {
                                theENVConfigEntry.setpageLoadTimeout(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                            }
                            if (cellIndex == index4singleElementSearchTimeDuration) {
                                theENVConfigEntry.setsingleElementSearchTimeDuration(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                            }
                        }
                    }
                    if (theENVConfigEntry.getTargetMobileOS().equalsIgnoreCase("android")) {
                        androidInstanceEntry andEntry = new androidInstanceEntry();
                        int AND_screenwide = 0;
                        int AND_screenLength = 0;
                        int AND_upOFscreen = 0;
                        int AND_downOFscreen = 0;

                        tophssfRow = sheet.getRow(4);
                        toplastCellNum = 5;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("screenWide")) {
                                AND_screenwide = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("screenLength")) {
                                AND_screenLength = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("upOFscreen")) {
                                AND_upOFscreen = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("downOFscreen")) {
                                AND_downOFscreen = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(5);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == AND_screenwide) {
                                    theENVConfigEntry.setscreenWide(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == AND_screenLength) {
                                    theENVConfigEntry.setscreenLength(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == AND_upOFscreen) {
                                    theENVConfigEntry.setupOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == AND_downOFscreen) {
                                    theENVConfigEntry.setdownOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                            }
                        }

                        int devicename = 0;
                        int osversion = 0;
                        int app = 0;
                        int pversion = 0;

                        tophssfRow = sheet.getRow(6);
                        toplastCellNum = 4;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("device name")) {
                                devicename = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("os version")) {
                                osversion = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("app")) {
                                app = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("platform version")) {
                                pversion = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(7);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == devicename) {
                                    andEntry.setDeviceName(cell.getStringCellValue());
                                }
                                if (cellIndex == osversion) {
                                    try {
                                        andEntry.setosVersion(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("1").format(cell.getNumericCellValue()))));
                                    } catch (Exception e) {
                                        andEntry.setosVersion(cell.getStringCellValue());
                                    }
                                }
                                if (cellIndex == app) {
                                    andEntry.setapp(cell.getStringCellValue());
                                }
                                if (cellIndex == pversion) {
                                    andEntry.setplatform(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                            }
                        }

                    } else {
                        iosInstanceEntry iosEntry = new iosInstanceEntry();
                        int IOS_screenwide = 0;
                        int IOS_screenLength = 0;
                        int IOS_upOFscreen = 0;
                        int IOS_downOFscreen = 0;

                        tophssfRow = sheet.getRow(8);
                        toplastCellNum = 5;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("IOSscreenWide")) {
                                IOS_screenwide = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("IOSscreenLength")) {
                                IOS_screenLength = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("IOSupOFscreen")) {
                                IOS_upOFscreen = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("IOSdownOFscreen")) {
                                IOS_downOFscreen = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(9);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == IOS_screenwide) {
                                    theENVConfigEntry.setIOSscreenWide(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == IOS_screenLength) {
                                    theENVConfigEntry.setIOSscreenLength(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == IOS_upOFscreen) {
                                    theENVConfigEntry.setIOSupOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == IOS_downOFscreen) {
                                    theENVConfigEntry.setIOSdownOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                            }
                        }

                        int deviceType = 0;
                        int deviceName = 0;
                        int app = 0;
                        int pversion = 0;
                        int pname = 0;
                        int bund = 0;
                        int udid = 0;
                        int ormode = 0;

                        tophssfRow = sheet.getRow(8);
                        toplastCellNum = 1;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("iPhone/iPAD")) {
                                deviceType = cellIndex;
                            }
                        }
                        entryhssfRow = sheet.getRow(9);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                if (cellIndex == deviceType) {
                                    iosEntry.setDeviceType(cell.getStringCellValue());
                                }
                            }
                        }
                        tophssfRow = sheet.getRow(10);
                        toplastCellNum = 4;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("device name")) {
                                deviceName = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("app")) {
                                app = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("platform version")) {
                                pversion = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("platform name")) {
                                pname = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(11);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == deviceName) {
                                    iosEntry.setDeviceName(cell.getStringCellValue());
                                }
                                if (cellIndex == app) {
                                }
                                if (cellIndex == pversion) {
                                    iosEntry.setplatformversion(cell.getStringCellValue());
                                }
                                if (cellIndex == pname) {
                                    iosEntry.setplatformname(cell.getStringCellValue());
                                }
                            }
                        }

                        if (theENVConfigEntry.getdeviceORsimulator().equalsIgnoreCase("device")) {
                            tophssfRow = sheet.getRow(10);
                            toplastCellNum = 7;
                            for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                                HSSFCell cell = tophssfRow.getCell(cellIndex);
                                String topLineContent = cell.getStringCellValue();
                                if (topLineContent.equalsIgnoreCase("bundleID")) {
                                    bund = cellIndex;
                                } else if (topLineContent.equalsIgnoreCase("udid")) {
                                    udid = cellIndex;
                                } else if (topLineContent.equalsIgnoreCase("orientation")) {
                                    ormode = cellIndex;
                                }
                            }
                            entryhssfRow = sheet.getRow(11);
                            lastCellNum = entryhssfRow.getLastCellNum();
                            if (entryhssfRow != null) {
                                for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                    HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                    //  rowList.add(getCellValue(cell));
                                    if (cellIndex == bund) {
                                        iosEntry.setBundleId(cell.getStringCellValue());
                                    }
                                    if (cellIndex == udid) {
                                        iosEntry.setUdid(cell.getStringCellValue());
                                    }
                                }
                            }

                            if (theENVConfigEntry.getTargetMobileOS().contains("ipad")) {
                                entryhssfRow = sheet.getRow(11);
                                lastCellNum = entryhssfRow.getLastCellNum();
                                if (entryhssfRow != null) {
                                    for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                        HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                        if (cellIndex == ormode) {
                                            iosEntry.setorientation(cell.getStringCellValue());
                                        }

                                    }
                                }
                            } else {
                            }

                        } else {
                        }
                    }
                    result = theENVConfigEntry;
                    break;

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
        return result;
    }


    public static androidInstanceEntry readExcel4andEntry(String filePath, int rowIndex) {
        androidInstanceEntry result = new androidInstanceEntry();
        androidInstanceEntry andEntry = new androidInstanceEntry();
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
                if (sheet.getSheetName().equalsIgnoreCase("ENV Config")) {
                    envConfigEntry theENVConfigEntry = new envConfigEntry();
                    int index4autoRunningOSType = 0;
                    int index4targetMobileOS = 0;
                    int index4deviceORsimulator = 0;
                    int index4pageLoadTimeout = 0;
                    int index4singleElementSearchTimeDuration = 0;

                    HSSFRow tophssfRow = sheet.getRow(0);
                    int toplastCellNum = 5;
                    for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                        HSSFCell cell = tophssfRow.getCell(cellIndex);
                        String topLineContent = cell.getStringCellValue();
                        if (topLineContent.equalsIgnoreCase("autoRunningOSType")) {
                            index4autoRunningOSType = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("targetMobileOS")) {
                            index4targetMobileOS = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("deviceORsimulator")) {
                            index4deviceORsimulator = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("pageLoadTimeout")) {
                            index4pageLoadTimeout = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("singleElementSearchTimeDuration")) {
                            index4singleElementSearchTimeDuration = cellIndex;
                        }
                    }

                    HSSFRow entryhssfRow = sheet.getRow(1);
                    int lastCellNum = entryhssfRow.getLastCellNum();
                    if (entryhssfRow != null) {
                        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                            HSSFCell cell = entryhssfRow.getCell(cellIndex);
                            //  rowList.add(getCellValue(cell));
                            if (cellIndex == index4autoRunningOSType) {
                                theENVConfigEntry.setAutoRunningOSType(cell.getStringCellValue());
                            }
                            if (cellIndex == index4targetMobileOS) {
                                theENVConfigEntry.setTargetMobileOS(cell.getStringCellValue());
                            }
                            if (cellIndex == index4deviceORsimulator) {
                                theENVConfigEntry.setdeviceORsimulator(cell.getStringCellValue());
                            }
                            if (cellIndex == index4pageLoadTimeout) {
                                theENVConfigEntry.setpageLoadTimeout(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                            }
                            if (cellIndex == index4singleElementSearchTimeDuration) {
                                theENVConfigEntry.setsingleElementSearchTimeDuration(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                            }
                        }
                    }
                    if (theENVConfigEntry.getTargetMobileOS().equalsIgnoreCase("android")) {

                        int AND_screenwide = 0;
                        int AND_screenLength = 0;
                        int AND_upOFscreen = 0;
                        int AND_downOFscreen = 0;

                        tophssfRow = sheet.getRow(4);
                        toplastCellNum = 5;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("screenWide")) {
                                AND_screenwide = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("screenLength")) {
                                AND_screenLength = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("upOFscreen")) {
                                AND_upOFscreen = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("downOFscreen")) {
                                AND_downOFscreen = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(5);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == AND_screenwide) {
                                    theENVConfigEntry.setscreenWide(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == AND_screenLength) {
                                    theENVConfigEntry.setscreenLength(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == AND_upOFscreen) {
                                    theENVConfigEntry.setupOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == AND_downOFscreen) {
                                    theENVConfigEntry.setdownOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                            }
                        }

                        int devicename = 0;
                        int osversion = 0;
                        int app = 0;
                        int pversion = 0;

                        tophssfRow = sheet.getRow(6);
                        toplastCellNum = 4;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("device name")) {
                                devicename = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("os version")) {
                                osversion = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("app")) {
                                app = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("platform version")) {
                                pversion = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(7);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == devicename) {
                                    andEntry.setDeviceName(cell.getStringCellValue());
                                }
                                if (cellIndex == osversion) {
                                    //
                                    try {
                                        System.out.println(cell.getStringCellValue());
                                        andEntry.setosVersion(cell.getStringCellValue());
                                    } catch (Exception e) {
                                        System.out.println(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                        andEntry.setosVersion(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                    }
                                }
                                if (cellIndex == app) {
                                    andEntry.setapp(cell.getStringCellValue());
                                }
                                if (cellIndex == pversion) {
                                    andEntry.setplatform(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                            }
                        }


                    }
                    result = new androidInstanceEntry(andEntry);
                    break;

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
        return result;
    }


    public static iosInstanceEntry readExcel4iosEntry(String filePath, int rowIndex) {
        iosInstanceEntry result = new iosInstanceEntry();
        iosInstanceEntry iosEntry = new iosInstanceEntry();
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
                if (sheet.getSheetName().equalsIgnoreCase("ENV Config")) {
                    envConfigEntry theENVConfigEntry = new envConfigEntry();
                    int index4autoRunningOSType = 0;
                    int index4targetMobileOS = 0;
                    int index4deviceORsimulator = 0;
                    int index4pageLoadTimeout = 0;
                    int index4singleElementSearchTimeDuration = 0;

                    HSSFRow tophssfRow = sheet.getRow(0);
                    int toplastCellNum = 5;
                    for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                        HSSFCell cell = tophssfRow.getCell(cellIndex);
                        String topLineContent = cell.getStringCellValue();
                        if (topLineContent.equalsIgnoreCase("autoRunningOSType")) {
                            index4autoRunningOSType = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("targetMobileOS")) {
                            index4targetMobileOS = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("deviceORsimulator")) {
                            index4deviceORsimulator = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("pageLoadTimeout")) {
                            index4pageLoadTimeout = cellIndex;
                        } else if (topLineContent.equalsIgnoreCase("singleElementSearchTimeDuration")) {
                            index4singleElementSearchTimeDuration = cellIndex;
                        }
                    }

                    HSSFRow entryhssfRow = sheet.getRow(1);
                    int lastCellNum = entryhssfRow.getLastCellNum();
                    if (entryhssfRow != null) {
                        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                            HSSFCell cell = entryhssfRow.getCell(cellIndex);
                            //  rowList.add(getCellValue(cell));
                            if (cellIndex == index4autoRunningOSType) {
                                theENVConfigEntry.setAutoRunningOSType(cell.getStringCellValue());
                            }
                            if (cellIndex == index4targetMobileOS) {
                                theENVConfigEntry.setTargetMobileOS(cell.getStringCellValue());
                            }
                            if (cellIndex == index4deviceORsimulator) {
                                theENVConfigEntry.setdeviceORsimulator(cell.getStringCellValue());
                            }
                            if (cellIndex == index4pageLoadTimeout) {
                                theENVConfigEntry.setpageLoadTimeout(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                            }
                            if (cellIndex == index4singleElementSearchTimeDuration) {
                                theENVConfigEntry.setsingleElementSearchTimeDuration(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                            }
                        }
                    }
                    if (theENVConfigEntry.getTargetMobileOS().equalsIgnoreCase("android")) {

                    } else {

                        int IOS_screenwide = 0;
                        int IOS_screenLength = 0;
                        int IOS_upOFscreen = 0;
                        int IOS_downOFscreen = 0;

                        tophssfRow = sheet.getRow(8);
                        toplastCellNum = 5;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("IOSscreenWide")) {
                                IOS_screenwide = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("IOSscreenLength")) {
                                IOS_screenLength = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("IOSupOFscreen")) {
                                IOS_upOFscreen = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("IOSdownOFscreen")) {
                                IOS_downOFscreen = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(9);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == IOS_screenwide) {
                                    theENVConfigEntry.setIOSscreenWide(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == IOS_screenLength) {
                                    theENVConfigEntry.setIOSscreenLength(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == IOS_upOFscreen) {
                                    theENVConfigEntry.setIOSupOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                                if (cellIndex == IOS_downOFscreen) {
                                    theENVConfigEntry.setIOSdownOFscreen(String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()))));
                                }
                            }
                        }

                        int deviceType = 0;
                        int deviceName = 0;
                        int app = 0;
                        int pversion = 0;
                        int pname = 0;
                        int bund = 0;
                        int udid = 0;
                        int ormode = 0;

                        tophssfRow = sheet.getRow(8);
                        toplastCellNum = 1;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("iPhone/iPAD")) {
                                deviceType = cellIndex;
                            }
                        }
                        entryhssfRow = sheet.getRow(9);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                if (cellIndex == deviceType) {
                                    iosEntry.setDeviceType(cell.getStringCellValue());
                                }
                            }
                        }
                        tophssfRow = sheet.getRow(10);
                        toplastCellNum = 4;
                        for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                            HSSFCell cell = tophssfRow.getCell(cellIndex);
                            String topLineContent = cell.getStringCellValue();
                            if (topLineContent.equalsIgnoreCase("device name")) {
                                deviceName = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("app")) {
                                app = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("platform version")) {
                                pversion = cellIndex;
                            } else if (topLineContent.equalsIgnoreCase("platform name")) {
                                pname = cellIndex;
                            }
                        }

                        entryhssfRow = sheet.getRow(11);
                        lastCellNum = entryhssfRow.getLastCellNum();
                        if (entryhssfRow != null) {
                            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                //  rowList.add(getCellValue(cell));
                                if (cellIndex == deviceName) {
                                    iosEntry.setDeviceName(cell.getStringCellValue());
                                }
                                if (cellIndex == app) {
                                }
                                if (cellIndex == pversion) {
                                    iosEntry.setplatformversion(cell.getStringCellValue());
                                }
                                if (cellIndex == pname) {
                                    iosEntry.setplatformname(cell.getStringCellValue());
                                }
                            }
                        }

                        if (theENVConfigEntry.getdeviceORsimulator().equalsIgnoreCase("device")) {
                            tophssfRow = sheet.getRow(10);
                            toplastCellNum = 7;
                            for (int cellIndex = 0; cellIndex < toplastCellNum; cellIndex++) {
                                HSSFCell cell = tophssfRow.getCell(cellIndex);
                                String topLineContent = cell.getStringCellValue();
                                if (topLineContent.equalsIgnoreCase("bundleID")) {
                                    bund = cellIndex;
                                } else if (topLineContent.equalsIgnoreCase("udid")) {
                                    udid = cellIndex;
                                } else if (topLineContent.equalsIgnoreCase("orientation")) {
                                    ormode = cellIndex;
                                }
                            }
                            entryhssfRow = sheet.getRow(11);
                            lastCellNum = entryhssfRow.getLastCellNum();
                            if (entryhssfRow != null) {
                                for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                    HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                    //  rowList.add(getCellValue(cell));
                                    if (cellIndex == bund) {
                                        iosEntry.setBundleId(cell.getStringCellValue());
                                    }
                                    if (cellIndex == udid) {
                                        iosEntry.setUdid(cell.getStringCellValue());
                                    }
                                }
                            }

                            if (theENVConfigEntry.getTargetMobileOS().contains("ipad")) {
                                entryhssfRow = sheet.getRow(11);
                                lastCellNum = entryhssfRow.getLastCellNum();
                                if (entryhssfRow != null) {
                                    for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                                        HSSFCell cell = entryhssfRow.getCell(cellIndex);
                                        if (cellIndex == ormode) {
                                            iosEntry.setorientation(cell.getStringCellValue());
                                        }

                                    }
                                }
                            } else {
                            }

                        } else {
                        }

                    }
                    result = new iosInstanceEntry(iosEntry);
                    break;

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
        return result;
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
