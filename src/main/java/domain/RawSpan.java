package domain;

import java.io.Serializable;

public class RawSpan implements Serializable {

    private Integer prevSpanId;

    private Integer childSpansCount;

    private String firstMessageLabel;

    private String lastMessageLabel;

    private Long startTime;

    private Long endTime;

    public RawSpan() {

    }

    public RawSpan(Integer prevSpanId, Integer childsCount) {
        this.prevSpanId = prevSpanId;
        this.childSpansCount = childsCount;
    }

    public Integer getPrevSpanId() {
        return prevSpanId;
    }

    public void setPrevSpanId(Integer prevSpanId) {
        this.prevSpanId = prevSpanId;
    }

    public Integer getChildSpansCount() {
        return childSpansCount;
    }

    public void setChildSpansCount(Integer childSpansCount) {
        this.childSpansCount = childSpansCount;
    }

    public String getFirstMessageLabel() {
        return firstMessageLabel;
    }

    public void setFirstMessageLabel(String firstMessageLabel) {
        this.firstMessageLabel = firstMessageLabel;
    }

    public String getLastMessageLabel() {
        return lastMessageLabel;
    }

    public void setLastMessageLabel(String lastMessageLabel) {
        this.lastMessageLabel = lastMessageLabel;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
