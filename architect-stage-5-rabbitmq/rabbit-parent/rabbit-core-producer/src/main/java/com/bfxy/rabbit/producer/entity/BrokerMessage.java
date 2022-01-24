package com.bfxy.rabbit.producer.entity;

import java.io.Serializable;
import java.util.Date;

import com.bfxy.rabbit.api.Message;


/**
 * 	$BrokerMessage 消息记录表实体映射
 * @author Alienware
 *
 */
public class BrokerMessage implements Serializable {
	
	private static final long serialVersionUID = 7447792462810110841L;

	private String messageId;

    private Message message;

    private Integer tryCount = 0;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getNextRetry() {
        return nextRetry;
    }

    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}