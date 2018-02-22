package com.example.Test.model;

import java.util.Date;

public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;

    public int getId() {
        return id;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public String getConversationId() {
        if (fromId < toId)
            return String.format("%d_%d", fromId, toId);
        else
            return String.format("%d_%d", toId, fromId);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
