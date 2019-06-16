package jsonANDxml.util;

/**
 * @author pzhu023
 */
public class operationItem implements Comparable<operationItem> {
    private String operationName;
    private String pageName;
    private String operationType;

    private String elementName;
    private String elementPara;

    private int step;

    public operationItem(String oName, String pName, String oType, String eleName, String elePara, int s) {
        this.operationName = oName;
        this.pageName = pName;
        this.operationType = oType;

        this.elementName = eleName;
        this.elementPara = elePara;

        this.step = s;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getOpeName() {
        return operationName;
    }

    public String getPageName() {
        return pageName;
    }

    public String getOpeType() {
        return operationType;
    }

    public String getElementName() {
        return elementName;
    }

    public String getElementPara() {
        return elementPara;
    }

    public int getStep() {
        return step;
    }

    public String toString() {
        return "[opeName = " + this.operationName + ", step = " + this.step + ", para = " + this.elementPara + "]";
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    public int compareTo(operationItem o) {
        return 0;
    }
}
