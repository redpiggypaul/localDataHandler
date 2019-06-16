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
public class envConfigEntry {
    @Id
    @GeneratedValue
    private Long id;
    private String autoRunningOSType;
    private String targetMobileOS; // android / IOS / IOS_IPAD
    private String deviceORsimulator;   //  device / simulator
    private String pageLoadTimeout;
    private String singleElementSearchTimeDuration;

    private String screenWide;
    private String screenLength;
    private String upOFscreen;
    private String downOFscreen;

    private String IOSscreenWide;
    private String IOSscreenLength;
    private String IOSupOFscreen;
    private String IOSdownOFscreen;

    public envConfigEntry() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutoRunningOSType() {
        return autoRunningOSType;
    }

    public void setAutoRunningOSType(String type) {
        this.autoRunningOSType = type;
    }

    public String getTargetMobileOS() {
        return targetMobileOS;
    }

    public void setTargetMobileOS(String osName) {
        this.targetMobileOS = osName;
    }

    public String getdeviceORsimulator() {
        return deviceORsimulator;
    }

    public void setdeviceORsimulator(String device) {
        this.deviceORsimulator = device;
    }

    public String getpageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setpageLoadTimeout(String time) {
        this.pageLoadTimeout = time;
    }

    public String getsingleElementSearchTimeDuration() {
        return singleElementSearchTimeDuration;
    }

    public void setsingleElementSearchTimeDuration(String time) {
        this.singleElementSearchTimeDuration = time;
    }

    public String getscreenWide() {
        return screenWide;
    }

    public void setscreenWide(String wide) {
        this.screenWide = wide;
    }

    public String getIOSscreenWide() {
        return IOSscreenWide;
    }

    public void setIOSscreenWide(String wide) {
        this.IOSscreenWide = wide;
    }


    public String getscreenLength() {
        return screenLength;
    }

    public void setscreenLength(String length) {
        this.screenLength = length;
    }

    public String getIOSscreenLength() {
        return IOSscreenLength;
    }

    public void setIOSscreenLength(String length) {
        this.IOSscreenLength = length;
    }

    public String getupOFscreen() {
        return upOFscreen;
    }

    public void setupOFscreen(String up) {
        this.upOFscreen = up;
    }

    public String getIOSupOFscreen() {
        return IOSupOFscreen;
    }

    public void setIOSupOFscreen(String up) {
        this.IOSupOFscreen = up;
    }

    public String getdownOFscreen() {
        return downOFscreen;
    }

    public void setdownOFscreen(String down) {
        this.downOFscreen = down;
    }

    public String getIOSdownOFscreen() {
        return IOSdownOFscreen;
    }

    public void setIOSdownOFscreen(String down) {
        this.IOSdownOFscreen = down;
    }


}
