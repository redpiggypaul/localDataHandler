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
public class actionKeyEntity {
    @Id
    @GeneratedValue
    private String OperationName;
    private String Step;

//	OperationName="tempName" Step="tempStep" PageName="tempPage" OperationType="opeType" elementName="eleName" elementType="eleType" elementParameter="elePara"


    public actionKeyEntity() {

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

}
