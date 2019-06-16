package com.csv.entity;

/**
 * @author lyou009
 */
public class ElementMapping {
    private String waitTime ;
    private String locatorStr;
    private String elementName;
    private String type;
    private String nextPage;
    private String mode;
    private String defaultValue;
    private String textContent;
    private String tWindow;
    private String parentEle;


    public ElementMapping(String name, String locatorStr, String waitTime, String type, String nPage, String mode, String dValue, String tContent, String triggerWindow, String parentElement) {
        this.elementName = name;
        this.locatorStr = locatorStr;
        this.type = type;
        this.waitTime = waitTime;
        this.nextPage = nPage;
        this.mode = mode;
        this.defaultValue = dValue;
        this.textContent = tContent;
        this.tWindow = triggerWindow;
        this.parentEle = parentElement;
    }


    public ElementMapping() {

    }


    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getParElementName() {
        return parentEle;
    }

    public void setParElementName(String elementName) {
        this.parentEle = elementName;
    }

    public String getTextCon() {
        return textContent;
    }

    public void setTextCon(String cont) {
        this.textContent = cont;
    }


    public String getDValue() {
        return defaultValue;
    }

    public void setDValue(String cont) {
        this.defaultValue = cont;
    }


    public String getLocatorStr() {
        return locatorStr;
    }

    public void setLocatorStr(String locatorStr) {
        this.locatorStr = locatorStr;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getNPage() {
        return nextPage;
    }

    public void setNPage(String pagename) {
        this.nextPage = pagename;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String  extmode) {
        this.mode = extmode;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getTWindow() {
        return tWindow;
    }
    public void setTWindow(String winName) {
         this.tWindow = winName;
    }
    public String getParentElement() {
        return parentEle;
    }
}
