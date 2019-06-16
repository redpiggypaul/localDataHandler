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
@Table(name = "REXautoAccountInfo_mobile")
public class accountInfo {
    @Id
    @GeneratedValue
    private Long id;
    private String userMail;
    private String password;
    private String userTEMail;
    private String passwordTE;
    private String userSetMail;
    private String passwordSet;

    public accountInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserMail(String type) {
        if (type.equalsIgnoreCase("resume") || type.equalsIgnoreCase("normal")) {
            return userMail;
        } else if (type.equalsIgnoreCase("T&E")) {
            return userTEMail;
        } else if (type.equalsIgnoreCase("AccountSetting")) {
            return userSetMail;
        } else {
            return userMail;
        }
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getPSW() {
        return password;
    }

    public String getPSW(String type) {
        if (type.equalsIgnoreCase("resume") || type.equalsIgnoreCase("normal")) {
            return password;
        } else if (type.equalsIgnoreCase("T&E")) {
            return passwordTE;
        } else if (type.equalsIgnoreCase("AccountSetting")) {
            return passwordSet;
        } else {
            return password;
        }
    }

    public void setPSW(String password) {
        this.password = password;
    }


    public String getUserTEMail() {
        return userTEMail;
    }

    public void setUserTEMail(String userMail) {
        this.userTEMail = userMail;
    }

    public String getPSWTE() {
        return passwordTE;
    }

    public void setPSWTE(String password) {
        this.passwordTE = password;
    }


    public String getUserSetMail() {
        return userSetMail;
    }

    public void setUserSetMail(String userMail) {
        this.userSetMail = userMail;
    }

    public String getPSWSet() {
        return passwordSet;
    }

    public void setPSWSet(String password) {
        this.passwordSet = password;
    }


}
