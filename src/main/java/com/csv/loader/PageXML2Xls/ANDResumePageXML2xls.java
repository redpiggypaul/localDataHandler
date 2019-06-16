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
public class ANDResumePageXML2xls extends  ANDBase{
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
        String pagePath = readProperity.getProValue("pageXML_AndPath"); // page path  /Users/appledev131/Documents/REXAUTO4/PageXML/And/
        String tempPath = readProperity.getProValue("templateFilePath");  // //Users//appledev131//Documents//REXAUTO4//
        String targetXLSPath = readProperity.getProValue("pageXLSPath");   // pageXLSPath = //Users//appledev131//Documents//REXAUTO4//PageXML//XLSgroup//
        try {

            FileList theL = new FileList(pagePath + "Resume/", tempPath + "checkFileName");
            ArrayList<String> pageXML = theL.getList2Arrary();

            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            String targetFile = new String(targetXLSPath + "pageSample_And_Resume");
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
                HashMap<String, ElementMapping> theElementMap = readPageXML(pagePath + "Resume//", theActionFileName);
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

}
