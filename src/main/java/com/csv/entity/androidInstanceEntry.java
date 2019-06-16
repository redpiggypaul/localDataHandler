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
public class androidInstanceEntry {
    @Id
    @GeneratedValue
    private String deviceName;
    private String osVersion; //
    private String app;   //
    private String platform;


    public androidInstanceEntry() {

    }

    public androidInstanceEntry(androidInstanceEntry sample) {
        this.deviceName = new String(sample.getDeviceName());
        this.osVersion = new String(sample.getosVersion());
        this.platform = new String(sample.getplatform());

    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String dName) {
        this.deviceName = dName;
    }

    public String getosVersion() {
        return osVersion;
    }

    public void setosVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getapp() {
        return app;
    }

    public void setapp(String app) {
        this.app = app;
    }

    public String getplatform() {
        return platform;
    }

    public void setplatform(String platform) {
        this.platform = platform;
    }


}
