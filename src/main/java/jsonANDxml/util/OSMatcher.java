package jsonANDxml.util;

/**
 * Created by appledev131 on 4/14/16.
 */
public class OSMatcher {
    public int matchOSi2i(int theCode) {
        int result = 0;
        if (1 == theCode) {
            result = 1;
        } else if (2 == theCode) {
            result = 2;
        } else {
            result = 0;
        }
        return result;
    }

    public String matchOSi2s(int theCode) {
        String result = null;
        if (1 == theCode) {
            result = "AND";
        } else if (2 == theCode) {
            result = "IOS";
        } else {
            result = "ERROR";
        }
        return result;
    }

    public String matchOSs2s(String theCode) {
        String result = null;
        if (theCode.equalsIgnoreCase("and") || theCode.toLowerCase().contains("and")) {
            result = "AND";
        } else if (theCode.equalsIgnoreCase("ios") || theCode.toLowerCase().contains("ios")) {
            result = "IOS";
        } else {
            result = "ERROR";
        }
        return result;
    }

    public int matchOSs2i(String theCode) {
        int result = 0;
        if (theCode.equalsIgnoreCase("and") || theCode.toLowerCase().contains("and")) {
            result = 1;
        } else if (theCode.equalsIgnoreCase("ios") || theCode.toLowerCase().contains("ios")) {
            result = 2;
        } else {
            result = 0;
        }
        return result;
    }

    public static int matchOS(Object theCode) {
        int result = 0;
        if (theCode instanceof String) {
            if (((String) theCode).equalsIgnoreCase("and") || ((String) theCode).toLowerCase().contains("and")) {
                result = 1;
            } else if (((String) theCode).equalsIgnoreCase("ios") || ((String) theCode).toLowerCase().contains("ios")) {
                result = 2;
            } else {
                result = 0;
            }
        }
        else if(theCode instanceof Integer)
        {
            if (1 == (Integer)theCode) {
                result = 1;
            } else if (2 == (Integer)theCode) {
                result = 2;
            } else {
                result = 0;
            }
        }
        else
        {
            result = 0;
        }
        return result;
    }
}
