package com.csv.chart;

public class sampleDataEntry {
    private String timeTag;
    private String tagName;
    private Integer Duration;

    public sampleDataEntry(String timeTag,String tagName, int dvalue){
        this.timeTag=timeTag;
        this.tagName=tagName;
        this.Duration = dvalue;
    }

    public Integer getDuration() {
        return Duration;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTimeTag() {
        return timeTag;
    }
}
