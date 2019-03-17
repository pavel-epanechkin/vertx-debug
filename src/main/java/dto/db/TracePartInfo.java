package dto.db;

import java.sql.Timestamp;

public class TracePartInfo {

    private Integer trace_id;

    private Integer trace_part_id;

    private Integer span_id;

    private String first_message_label;

    private String last_message_label;

    private Timestamp start_time;

    private Timestamp end_time;

    public Integer getTraceId() {
        return trace_id;
    }

    public void setTraceId(Integer traceId) {
        this.trace_id = traceId;
    }

    public Integer getTracePartId() {
        return trace_part_id;
    }

    public void setTracePartId(Integer tracePartId) {
        this.trace_part_id = tracePartId;
    }

    public Integer getSpanId() {
        return span_id;
    }

    public void setSpanId(Integer spanId) {
        this.span_id = spanId;
    }

    public String getFirstMessageLabel() {
        return first_message_label;
    }

    public void setFirstMessageLabel(String firstMessageLabel) {
        this.first_message_label = firstMessageLabel;
    }

    public String getLastMessageLabel() {
        return last_message_label;
    }

    public void setLastMessageLabel(String lastMessageLabel) {
        this.last_message_label = lastMessageLabel;
    }

    public Timestamp getStartTime() {
        return start_time;
    }

    public void setStartTime(Timestamp startTime) {
        this.start_time = startTime;
    }

    public Timestamp getEndTime() {
        return end_time;
    }

    public void setEndTime(Timestamp endTime) {
        this.end_time = endTime;
    }
}
