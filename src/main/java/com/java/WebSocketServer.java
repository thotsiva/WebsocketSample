package com.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;

import io.vertx.core.http.ServerWebSocket;

// if whole request is gZipped

//public class WebSocketServer {
//
//    private final Gson gson = new Gson();
//
//    public void handleWebSocket(ServerWebSocket webSocket) {
//        webSocket.handler(buffer -> {
//            String incomingMessage = buffer.toString();
//            ReceiveMessage receiveMessage = parseMessage(incomingMessage);
//            if (receiveMessage != null) {
//                callBotAdapter(receiveMessage);
//            }
//        });
//    }
//
//    private ReceiveMessage parseMessage(String message) {
//        // Attempt to decode and decompress if gzipped
//        String jsonMessage = decompressIfGzipped(message);
//        
//        // If decompression fails, treat the message as plain text
//        if (jsonMessage == null) {
//            jsonMessage = message; // fallback to the original message
//        }
//
//        // Now parse the JSON message
//        try {
//            return gson.fromJson(jsonMessage, ReceiveMessage.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null; // Return null if parsing fails
//        }
//    }
//
//    private String decompressIfGzipped(String message) {
//        // Attempt to decode the message from Base64
//        byte[] compressed;
//        try {
//            compressed = Base64.getDecoder().decode(message);
//        } catch (IllegalArgumentException e) {
//            // If decoding fails, this is not Base64, so return null
//            return null;
//        }
//
//        // If decoding was successful, attempt to decompress
//        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
//            StringBuilder outStr = new StringBuilder();
//            byte[] data = new byte[1024];
//            int len;
//            while ((len = gzipInputStream.read(data)) != -1) {
//                outStr.append(new String(data, 0, len));
//            }
//            return outStr.toString();
//        } catch (IOException e) {
//            // Return null if not gzipped or if any exception occurs
//            return null;
//        }
//    }
//
//    private void callBotAdapter(ReceiveMessage receiveMessage) {
//        // Handle the parsed message object
//        System.out.println("Received message: " + receiveMessage);
//    }
//}


public class WebSocketServer {

    private final Gson gson = new Gson();

    public void handleWebSocket(ServerWebSocket webSocket) {
        webSocket.handler(buffer -> {
            String incomingMessage = buffer.toString();
            ReceiveMessage receiveMessage = parseMessage(incomingMessage);
            if (receiveMessage != null) {
                callBotAdapter(receiveMessage);
            }
        });
    }

    private ReceiveMessage parseMessage(String message) {
        ReceiveMessage receiveMessage;

        // First, parse the incoming JSON message
        try {
            receiveMessage = gson.fromJson(message, ReceiveMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }

        // Check if the message field is a gzipped Base64 string
        if (receiveMessage.message instanceof String) {
            String compressedMessage = (String) receiveMessage.message;
            String decompressedMessage = decompressIfGzipped(compressedMessage);

            // If decompression is successful, set the message field
            if (decompressedMessage != null) {
                receiveMessage.message = decompressedMessage; // Replace with decompressed message
            } else {
                System.out.println("Message field is not a valid gzipped string or Base64 encoded.");
            }
        }

        return receiveMessage;
    }

    private String decompressIfGzipped(String base64Message) {
        // Attempt to decode the message from Base64
        byte[] compressed;
        try {
            compressed = Base64.getDecoder().decode(base64Message);
        } catch (IllegalArgumentException e) {
            // If decoding fails, this is not Base64; return null
            return null;
        }

        // Attempt to decompress the gzipped data
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
            StringBuilder outStr = new StringBuilder();
            byte[] data = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(data)) != -1) {
                outStr.append(new String(data, 0, len));
            }
            return outStr.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if any exception occurs during decompression
        }
    }

    private void callBotAdapter(ReceiveMessage receiveMessage) {
        // Handle the parsed message object
        System.out.println("Received message: " + receiveMessage);
    }
}