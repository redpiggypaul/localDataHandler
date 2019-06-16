/**
 *
 */
package com.csv.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author paul zhu
 * @date 2016年7月14日 下午2:17:15
 */
@Entity
@Table(name = "REXautoTC_mobile")
public class actionEntity {
    @Id
    @GeneratedValue
    private String OperationName;
    private String Step;
    private String PageName;
    private String OperationType;
    private String elementName;
    private String elementType;
    private String elementParameter;

//	OperationName="tempName" Step="tempStep" PageName="tempPage" OperationType="opeType" elementName="eleName" elementType="eleType" elementParameter="elePara"


    public actionEntity() {

    }

    public String getOpeName() {
        return OperationName;
    }

    public void setOpeName(String name) {
        this.OperationName = name;
    }


    public String getStep() {
        return Step;
    }

    public void setStep(String id) {
        this.Step = id;
    }

    public String getPageName() {
        return PageName;
    }

    public void setPageName(String pname) {
        this.PageName = pname;
    }

    public String getOperationType() {
        return OperationType;
    }

    public void setOperationType(String pname) {
        this.OperationType = pname;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String name) {
        this.elementName = name;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String type) {
        this.elementType = type;
    }
    public String getElementParameter() {
        return elementParameter;
    }

    public void setElementParameter(String para) {
        this.elementParameter = para;
    }
}
