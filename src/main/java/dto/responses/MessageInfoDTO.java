package dto.responses;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public class MessageInfoDTO implements Serializable {

    private Integer ID;

    private String MESSAGEID;

    private String LABEL;

    private Boolean RECEIVED;

    private String SENTTIME;

    private String RECEIVEDTIME;

    private String PREVMESSAGEID;

    private String TARGETADDRESS;

    private String REPLYADDRESS;

    private List<Map<String, Object>> headers;

    private String body;

    public String getMessageId() {
      return MESSAGEID;
    }

    public void setMessageId(String messageId) {
      this.MESSAGEID = messageId;
    }

    public String getLabel() {
      return LABEL;
    }

    public void setLabel(String label) {
      this.LABEL = label;
    }

    public String getSentTime() {
      return SENTTIME;
    }

    public void setSentTime(String sentTime) {
      this.SENTTIME = sentTime;
    }

    public String getPrevMessageId() {
      return PREVMESSAGEID;
    }

    public void setPrevMessageId(String prevMessageId) {
      this.PREVMESSAGEID = prevMessageId;
    }

    public String getTargetAddress() {
      return TARGETADDRESS;
    }

    public void setTargetAddress(String targetAddress) {
      this.TARGETADDRESS = targetAddress;
    }

    public String getReplyAddress() {
      return REPLYADDRESS;
    }

    public void setReplyAddress(String replyAddress) {
      this.REPLYADDRESS = replyAddress;
    }

    public List<Map<String, Object>> getHeaders() {
      return headers;
    }

    public void setHeaders(List<Map<String, Object>> headers) {
      this.headers = headers;
    }

    public String getBody() {
      return body;
    }

    public void setBody(String body) {
      this.body = body;
    }

    public Integer getId() {
      return ID;
    }

    public void setId(Integer id) {
      this.ID = id;
    }

    public Boolean getReceived() {
        return RECEIVED;
    }

    public void setReceived(Boolean received) {
        this.RECEIVED = received;
    }

    public String getReceivedTime() {
        return RECEIVEDTIME;
    }

    public void setReceivedTime(String receivedTime) {
        this.RECEIVEDTIME = receivedTime;
    }
}
