package dto.responses;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class TraceInfoDTO implements Serializable {

    private Integer traceId;

    private List<String> traceLabels;

    private String startTime;

    private String endTime;

    public Integer getTraceId() {
        return traceId;
    }

    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }

    public List<String> getTraceLabels() {
        return traceLabels;
    }

    public void setTraceLabels(List<String> traceLabels) {
        this.traceLabels = traceLabels;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
