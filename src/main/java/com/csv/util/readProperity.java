package com.csv.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
/**
 * Created by appledev131 on 7/18/16.
 */
public final class readProperity {


    //  /Users/appledev131/Documents/csvloader

    public static String getProValue(String key)
    {
        String result = null;
        String propsPath = (System.getProperty("user.dir") + "\\src\\envConfig\\config.properties").replaceAll("\\\\", File.separator);  //todo change for windows
        Properties props = new Properties();

        try {
            InputStream in = Files.newInputStream(Paths.get(propsPath));
            props.load(in);
            result = props.getProperty(key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String x = (System.getProperty("user.dir") + "\\src\\envConfig\\config.properties").replaceAll("\\\\", File.separator);     //todo change for windows
        System.out.println("x : " + x);
        Properties props = new Properties();
        x = ("\\Users\\appledev131\\Documents\\csvloader\\src\\envConfig\\config.properties").replaceAll("\\\\", File.separator);  //todo change for windows

        try {
            InputStream in = Files.newInputStream(Paths.get(x));
            props.load(in);
            System.out.println(props.getProperty("pageLoadTimeout"));
            System.out.println(props.getProperty("upOFscreen"));
            System.out.println(props.getProperty("downOFscreen"));
            System.out.println(props.getProperty("upallNumber"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println(getProValue("operation.platform"));
    }
}
