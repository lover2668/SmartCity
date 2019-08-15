package com.tourcool.core.entity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月13日18:01
 * @Email: 971613168@qq.com
 */
public class MessageBean {

    /**
     * createTime : 1554879358000
     * message : 订单：19041014555747创建！
     * messageId : 221
     * orderId : 62
     * readStatus : 1
     */

    private long createTime;
    private String message;
    private String messageId;
    private String orderId;
    private int readStatus;

    private int countUnreadMsg;

    public int getCountUnreadMsg() {
        return countUnreadMsg;
    }

    public void setCountUnreadMsg(int countUnreadMsg) {
        this.countUnreadMsg = countUnreadMsg;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
