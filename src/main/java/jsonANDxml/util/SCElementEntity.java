package jsonANDxml.util;


import jsonANDxml.LocalException.XMLException;

import java.util.ArrayList;

/**
 * @author paul.zhu
 */
public class SCElementEntity {
    private StringBuilder pageName;
    private StringBuilder elementName;
    private StringBuilder locatorType;
    private StringBuilder locatorStr;
    private StringBuilder rowDefaultValue;
    private ArrayList<StringBuilder> defaultValue;
    private StringBuilder rowTextContent;
    private ArrayList<StringBuilder> textContent;
    private StringBuilder triggerWin;
    private StringBuilder showMode;
    private StringBuilder nextPage;
    protected String falg4MultiLanguage = new String("^^");

    private StringBuilder checkInputWithException(StringBuilder input) throws Exception {
        StringBuilder result = new StringBuilder("");
        try {
            if (input == null || input.toString().equals("")) {
                throw new XMLException("XML for Page Element miss some mandatory ITEM as pageName/elementName/locatorType/locatorStr ");
            } else {
                result.append(input);
            }
        } catch (XMLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            return result;
        }
    }

    private StringBuilder checkInputWOException(StringBuilder input) throws Exception {
        StringBuilder result = new StringBuilder("");
        try {
            result.append(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            return result;
        }
    }

    private ArrayList<StringBuilder> checkInput2ListWOException(StringBuilder input) throws Exception {
        ArrayList<StringBuilder> result = new ArrayList<StringBuilder>();
        try {
            String[] temp = input.toString().split(this.falg4MultiLanguage);
            for (int i = 0; i < temp.length; i++) {
                result.add(new StringBuilder(temp[i]));
            }
            //result.append(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            return result;
        }
    }

    public SCElementEntity(StringBuilder pName, StringBuilder eName, StringBuilder locType, StringBuilder locStr, StringBuilder dValue, StringBuilder tContent, StringBuilder tWin, StringBuilder sMODE, StringBuilder nPage) throws Exception {
        this.pageName = checkInputWithException(pName);
        this.elementName = checkInputWithException(eName);
        this.locatorType = checkInputWithException(locType);
        this.locatorStr = checkInputWithException(locStr);
        this.rowDefaultValue = checkInputWOException(dValue);
        this.defaultValue = checkInput2ListWOException(dValue);
        this.rowTextContent = checkInputWOException(tContent);
        this.textContent = checkInput2ListWOException(tContent);
        this.triggerWin = checkInputWOException(tWin);
        this.showMode = checkInputWOException(sMODE);
        this.nextPage = checkInputWOException(nPage);

    }

    public void setPageName(StringBuilder pageName) throws Exception {
        this.pageName = checkInputWithException(pageName);
    }

    public StringBuilder getPageName() {
        return this.pageName;
    }

    public StringBuilder getElementName() {
        return elementName;
    }

    public void setElementName(StringBuilder elementName) throws Exception {
        this.elementName = checkInputWithException(elementName);
    }

    public StringBuilder getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(StringBuilder type) throws Exception {
        this.locatorType = checkInputWithException(type);
    }

    //   public String getLocatorType() {
    //     return locatorType.toString();
    //}

    public void setLocatorType(String type) throws Exception {
        this.locatorType = checkInputWithException(new StringBuilder(type));
    }

    public StringBuilder getLocatorStr() {
        return locatorStr;
    }

    public void setLocatorStr(StringBuilder locatorStr) throws Exception {
        this.locatorStr = checkInputWithException(locatorStr);
    }


    public void setDefaultValue(StringBuilder defaultValue) throws Exception {
        this.defaultValue = checkInput2ListWOException(defaultValue);
    }

    public StringBuilder getDefaultValue() {
        return defaultValue.get(0);
    }

    public StringBuilder getDefaultValue(int index) {
        return defaultValue.get(index);
    }

    public ArrayList<StringBuilder> getDefaultValueList() {
        return defaultValue;
    }

    public StringBuilder getDefaultValueASLongStr() {
        return this.rowDefaultValue;
    }

    public void setTextContent(StringBuilder textContent) throws Exception {
        this.textContent = checkInput2ListWOException(textContent);
    }

    public StringBuilder getTextContent() {
        return textContent.get(0);
    }

    public StringBuilder getTextContent(int index) {
        return textContent.get(index);
    }

    public ArrayList<StringBuilder> getTextContentList() {
        return textContent;
    }

    public StringBuilder getTextContentASLongStr() {
        return this.rowTextContent;
    }


    public void setTriggerWin(StringBuilder triggerWin) throws Exception {
        this.triggerWin = checkInputWOException(triggerWin);
    }

    public StringBuilder getTriggerWin() {
        return triggerWin;
    }

    public void setShowMode(StringBuilder showMode) {
        this.showMode = showMode;
    }

    public StringBuilder getShowMode() {
        return showMode;
    }

    public void setNextPage(StringBuilder nextPage) {
        this.nextPage = nextPage;
    }

    public StringBuilder getNextPage() {
        return nextPage;
    }
}

