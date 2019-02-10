package domain;

import java.io.Serializable;


public class MessageInfo implements Serializable {

    private Integer ID;

    private String MESSAGEID;

    private String LABEL;

    private Boolean RECEIVED;

    private long SENTTIME;

    private long RECEIVEDTIME;

    private String PREVMESSAGEID;

    private String TARGETADDRESS;

    private String REPLYADDRESS;

    private String headers;

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

    public long getSentTime() {
      return SENTTIME;
    }

    public void setSentTime(long sentTime) {
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

    public String getHeaders() {
      return headers;
    }

    public void setHeaders(String headers) {
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

    public long getReceivedTime() {
        return RECEIVEDTIME;
    }

    public void setReceivedTime(long receivedTime) {
        this.RECEIVEDTIME = receivedTime;
    }
}
