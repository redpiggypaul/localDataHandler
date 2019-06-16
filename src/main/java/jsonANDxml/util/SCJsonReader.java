package jsonANDxml.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jsonANDxml.LocalException.XMLException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by appledev131 on 8/29/16.
 */
public class SCJsonReader {

    public static void main(String[] args) {
        try {
            HashMap<String, StringBuilder> result1 = readJSON4defaultValue("\\\\PageXML\\\\bAccount.json");
            HashMap<String, SCElementEntity> result2 = readJSON4FFPandaPageElement("\\\\PageXML\\\\bAccount.json");
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createJSON_PageElement(TreeMap<String, SCElementEntity> extEleMap, StringBuilder fileName, StringBuilder pathName) throws IOException {
        if (fileName.toString().endsWith(".json")) {
        } else if (fileName.toString().contains(".")) {
            fileName = new StringBuilder(fileName.substring(0, fileName.lastIndexOf(".")) + ".json");
        } else {
            fileName = new StringBuilder(fileName + ".json");
        }
        JsonObject object = new JsonObject();  // 创建一个json对象
        object.addProperty("pagename", fileName.toString());       // 为json对象添加属性

        JsonArray languages = new JsonArray(); // 创建json数组

        // for (Iterator it = extEleMap.entrySet().iterator(); it.hasNext(); ) {
        for (Iterator it = extEleMap.keySet().iterator(); it.hasNext(); ) {

            JsonObject language = new JsonObject();
            // Map.Entry eleEntry = (Map.Entry) it.next();
            //  ElementMapping theEle = (ElementMapping) eleEntry.getValue();
            SCElementEntity theEle = extEleMap.get(it.next());
            language.addProperty("pageName", theEle.getPageName().toString());
            language.addProperty("elementName", theEle.getElementName().toString());
            language.addProperty("locatorType", theEle.getLocatorType().toString());
            language.addProperty("locatorStr", theEle.getLocatorStr().toString());
            language.addProperty("defaultValue", theEle.getDefaultValueASLongStr().toString());
            language.addProperty("textContent", theEle.getTextContentASLongStr().toString());
            language.addProperty("triggerWindow", theEle.getTriggerWin().toString());
            language.addProperty("showMode", theEle.getShowMode().toString());
            language.addProperty("nextPage", theEle.getNextPage().toString());
            languages.add(language);
            object.add("elements", languages);   // 将数组添加到json对象
        }

        String jsonStr = object.toString();   // 将json对象转化成json字符串

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(pathName + "/" + fileName)));
        pw.print(jsonStr);
        pw.flush();
        pw.close();
    }

    public static HashMap<String, StringBuilder> readJSON4defaultValue(String fileName) throws Exception {
        // long startTime = System.currentTimeMillis();
        HashMap<String, StringBuilder> elementsMap = new HashMap<String, StringBuilder>();
        elementsMap.clear();
        int lineN = 1;
        // 创建json解析器
        JsonParser parser = new JsonParser();
        // 使用解析器解析json数据，返回值是JsonElement，强制转化为其子类JsonObject类型
        StringBuilder theFileName = new StringBuilder(fileName);
        JsonObject object = (JsonObject) parser.parse(new FileReader(System.getProperty("user.dir") + theFileName));

        try {
            if ((theFileName.toString().toLowerCase().endsWith(object.get("pagename").getAsString().toLowerCase()))) {
                JsonArray languages2 = object.getAsJsonArray("elements");
                for (JsonElement jsonElement : languages2) {
                    lineN++;
                    try {
                        JsonObject elementLine = jsonElement.getAsJsonObject();
                        StringBuilder name = new StringBuilder(elementLine.get("elementName").getAsString());
                        StringBuilder dValue = new StringBuilder(elementLine.get("defaultValue").getAsString());
                        elementsMap.put(name.toString(), dValue);
                    } catch (Exception e) {
                        throw new XMLException("The content in Element Line " + lineN + "is incorrect : " + e.getCause());
                    }
                }
            } else {
                throw new XMLException("The NAME att is missing in the pageName 4 Android");
            }
        } catch (XMLException e) {
            throw new XMLException(SCJsonReader.class.getName() + Thread.currentThread().getStackTrace()[1].getMethodName() + " : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return elementsMap;
        }
    }

    public static HashMap<String, SCElementEntity> readJSON4FFPandaPageElement(String fileName) throws Exception {
        HashMap<String, SCElementEntity> elementsMap = new HashMap<String, SCElementEntity>();
        elementsMap.clear();
        int lineN = 1;
        // 创建json解析器
        JsonParser parser = new JsonParser();
        // 使用解析器解析json数据，返回值是JsonElement，强制转化为其子类JsonObject类型
        StringBuilder theFileName = new StringBuilder(fileName);
        JsonObject object = (JsonObject) parser.parse(new FileReader(System.getProperty("user.dir") + theFileName));
        try {
            if ((theFileName.toString().toLowerCase().endsWith(object.get("pagename").getAsString().toLowerCase()))) {
                JsonArray languages2 = object.getAsJsonArray("elements");
                for (JsonElement jsonElement : languages2) {
                    lineN++;
                    try {
                        JsonObject elementLine = jsonElement.getAsJsonObject();
                        //     Element elementObj = (Element) it.next();
                        StringBuilder pageName = new StringBuilder(elementLine.get("pageName").getAsString());
                        StringBuilder eleName = new StringBuilder(elementLine.get("elementName").getAsString());
                        StringBuilder locType = new StringBuilder(elementLine.get("locatorType").getAsString());
                        StringBuilder locStr = new StringBuilder(elementLine.get("locatorStr").getAsString());
                        StringBuilder dValue = new StringBuilder(elementLine.get("defaultValue").getAsString());
                        StringBuilder text = new StringBuilder(elementLine.get("textContent").getAsString());
                        StringBuilder tWin = new StringBuilder(elementLine.get("triggerWindow").getAsString());
                        StringBuilder sMode = new StringBuilder(elementLine.get("showMode").getAsString());
                        StringBuilder nPage = new StringBuilder(elementLine.get("nextPage").getAsString());
                        SCElementEntity eLocator;
                        eLocator = new SCElementEntity(pageName, eleName, locType, locStr, dValue, text, tWin, sMode, nPage);
                        elementsMap.put(eleName.toString(), eLocator);
                    } catch (Exception e) {
                        throw new XMLException("The content in Element Line " + lineN + "is incorrect : " + e.getCause());
                    }
                }
            } else {
                throw new XMLException("The NAME att is missing in the pageName ");
            }
        } catch (XMLException e) {
            throw new XMLException(SCJsonReader.class.getName() + Thread.currentThread().getStackTrace()[1].getMethodName() + " : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return elementsMap;
        }
    }

}
