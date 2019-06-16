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
public class globalActionKeyEntity {
    @Id
    @GeneratedValue
    private String GroupName;
    private String OperationName;

//	OperationName="tempName" Step="tempStep" PageName="tempPage" OperationType="opeType" elementName="eleName" elementType="eleType" elementParameter="elePara"


    public globalActionKeyEntity() {

    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String name) {
        this.GroupName = name;
    }


    public String getOpeName() {
        return OperationName;
    }

    public void setOpeName(String name) {
        this.OperationName = name;
    }




}
