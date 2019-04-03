package domain;

import java.io.Serializable;

public class RawTrace implements Serializable {

    private Integer firstSpanId;

    public RawTrace() {

    }

    public RawTrace(Integer firstSpanId) {
        this.firstSpanId = firstSpanId;
    }

    public Integer getFirstSpanId() {
        return firstSpanId;
    }

    public void setFirstSpanId(Integer firstSpanId) {
        this.firstSpanId = firstSpanId;
    }
}
