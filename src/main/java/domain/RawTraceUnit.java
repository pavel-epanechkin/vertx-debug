package domain;

import java.io.Serializable;

public class RawTraceUnit implements Serializable {

    private Integer spanId;

    private Integer orderNumber;

    public RawTraceUnit() {

    }

    public RawTraceUnit(Integer spanId, Integer orderNumber) {
        this.spanId = spanId;
        this.orderNumber = orderNumber;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
