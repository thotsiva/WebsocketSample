package com.java;

public class ReceiveMessage {
    public String UserId;
    public String conversationId;
    public String channel;
    public Object message; // Object type to accommodate different types
    public String agent;

    @Override
    public String toString() {
        return "ReceiveMessage{" +
                "UserId='" + UserId + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", channel='" + channel + '\'' +
                ", message=" + message +
                ", agent='" + agent + '\'' +
                '}';
    }
}
