package com.java;

import java.io.IOException;

import com.google.gson.Gson;

public class Example {
    public static void main(String[] args) throws IOException {
        ReceiveMessage message = new ReceiveMessage();
        message.UserId = "user123";
        message.conversationId = "conv456";
        message.channel = "websocket";
        message.message = "This is a plain text message.";
        message.agent = "agent007";

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(message);

        // Compress and encode the JSON payload
        String gzippedPayload = GzipUtil.gzipToBase64(jsonPayload);

        // Send this gzippedPayload via WebSocket
        System.out.println("Gzipped Payload: " + gzippedPayload);
    }
}
