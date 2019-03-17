package domain;

import java.io.Serializable;

public class RawTrace implements Serializable {

    private Integer traceId;

    public RawTrace() {

    }

    public RawTrace(Integer traceId) {
        this.traceId = traceId;
    }

    public Integer getTraceId() {
        return traceId;
    }

    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }
}
