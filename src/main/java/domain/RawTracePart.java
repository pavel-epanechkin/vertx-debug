package domain;

import java.io.Serializable;

public class RawTracePart implements Serializable {

    private Integer traceId;

    private Integer orderNumber;

    private Integer spanId;

    public RawTracePart() {

    }

    public RawTracePart(Integer traceId, Integer orderNumber, Integer spanId) {
        this.traceId = traceId;
        this.orderNumber = orderNumber;
        this.spanId = spanId;
    }

    public Integer getTraceId() {
        return traceId;
    }

    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
    }
}
