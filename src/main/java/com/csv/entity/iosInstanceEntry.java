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
public class iosInstanceEntry {
    @Id
    @GeneratedValue
    private String deviceType;
    private String deviceName;
    private String platformname; //
    private String app;   //
    private String platformversion;
    private String bundleid;
    private String udid;
    private String orientation = null;


    public iosInstanceEntry() {

    }

    public iosInstanceEntry(iosInstanceEntry sample) {
        this.deviceType = new String(sample.getDeviceType());
        this.deviceName = new String(sample.getDeviceName());
        this.platformname = new String(sample.getplatformname());
        this.platformversion = new String(sample.getplatformversion());
        if (sample.getBundleId() != null) {
            this.bundleid = new String(sample.getBundleId());
        }
        if (sample.getUdid() != null) {
            this.udid = new String(sample.getUdid());
        }
        if (sample.getorientation() != null) {
            this.orientation = new String(sample.getorientation());
        }
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String dType) {
        this.deviceType = dType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String dName) {
        this.deviceName = dName;
    }

    public String getplatformname() {
        return platformname;
    }

    public void setplatformname(String osVersion) {
        this.platformname = osVersion;
    }

    public String getapp() {
        return app;
    }

    public void setapp(String app) {
        this.app = app;
    }

    public String getplatformversion() {
        return platformversion;
    }

    public void setplatformversion(String platform) {
        this.platformversion = platform;
    }

    public String getBundleId() {
        return bundleid;
    }

    public void setBundleId(String id) {
        this.bundleid = id;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String eudid) {
        this.udid = eudid;
    }

    public String getorientation() {
        return orientation;
    }

    public void setorientation(String ormode) {
        this.orientation = ormode;
    }
}
