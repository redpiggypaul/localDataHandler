package com.csv.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.Properties;


public class SingletonLogger {
    private volatile static SingletonLogger uniqueInstance;
    public static Logger logger = Logger.getLogger(SingletonLogger.class);
    //  DOMConfigurator.configure("log4j.xml");
    // private static org.apache.log4j.Logger logger;
    private static String filePath = "src/log4j.properties";
    private static boolean flag = false;
    private static String classname;

    //Initialize Log4j logs
    public SingletonLogger(String c) {
        super();
        // this.classname=c;
        this.classname = c; // 解决时间戳和类名之间的过渡问题
        logger = org.apache.log4j.Logger.getLogger(classname);
    }

    private void getProperty() {
        PropertyConfigurator.configure(new File(filePath).getAbsolutePath());
        flag = true;
    }

    private void getFlag() {
        if (flag == false) {
            getProperty();
        }

    }

    // This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
    public void startTestCase(String sTestCaseName) {
        getFlag();
        int stan_length = 60;
        String stan_cont = "------------------------------------------------------------";
        String key_content = sTestCaseName + " Start";
        int content_length = key_content.length();
        StringBuffer buffer = new StringBuffer(stan_cont);

        buffer.replace((stan_length - content_length) / 2, (stan_length - content_length) / 2 + content_length, key_content);

        logger.info(stan_cont);
        logger.info(buffer.toString());
        logger.info(stan_cont);
    }

    //This is to print log for the ending of the test case
    public void endTestCase(String sTestCaseName) {
        getFlag();
        int stan_length = 60;
        String stan_cont = "------------------------------------------------------------";
        String key_content = sTestCaseName + " End";
        int content_length = key_content.length();
        StringBuffer buffer = new StringBuffer(stan_cont);

        buffer.replace((stan_length - content_length) / 2, (stan_length - content_length) / 2 + content_length, key_content);

        logger.info(stan_cont);
        logger.info(buffer.toString());
        logger.info(stan_cont);
    }

    // Need to create these methods, so that they can be called
    public void info(StringBuilder message) {
        getFlag();
        logger.info(message);
    }

    public void warn(String message) {

        getFlag();
        logger.warn(message);
    }

    public void error(StringBuilder message) {

        getFlag();
        logger.error(message);
    }

    public void fatal(String message) {

        getFlag();
        logger.fatal(message);
    }

    public void debug(StringBuilder message) {

        getFlag();
        if (logger.isDebugEnabled() == true) {
            System.out.println("@@@@@@ Logger Debug mode is enabled @@@@@@");
            logger.debug(message);
        } else {
            System.out.println("Logger Debug mode is disable!");
        }
    }

    private SingletonLogger() {
        super();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SingletonLogger getInstance() {
        if (uniqueInstance == null) {
            synchronized (SingletonLogger.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SingletonLogger();
                }
            }
        }
        return uniqueInstance;
    }


    public static void main(String[] args) {
        SingletonLogger x = SingletonLogger.getInstance();
        x.info(new StringBuilder("111"));
        x.error(new StringBuilder("abc"));
        x.debug(new StringBuilder("cba"));
        // x.startTestCase("aasad");
    }

}
