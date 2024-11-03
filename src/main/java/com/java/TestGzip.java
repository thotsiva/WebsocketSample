package com.java;

import java.io.IOException;
import java.util.Base64;

public class TestGzip {
//	public static void main(String[] args) {
//        try {
//            String jsonMessage = "{\"productId\": \"123\", \"name\": \"Product Name\"}";
//            byte[] gzippedData = GzipUtil.gzip(jsonMessage);
//            String base64Gzipped = Base64.getEncoder().encodeToString(gzippedData);
//            System.out.println("Base64 Gzipped Data: " + base64Gzipped);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
	
	public static void main(String[] args) {
        try {
            // Create a ReceiveMessage JSON string
            String jsonMessage = "This is a plain text message.";
            // Gzip and encode it
            byte[] gzippedData = GzipUtil.gzip(jsonMessage);
            String base64Gzipped = Base64.getEncoder().encodeToString(gzippedData);
            System.out.println("Base64 Gzipped Data: " + base64Gzipped);

            // Alternatively, send the JSON directly without gzipping
            System.out.println("Plain JSON Data: " + jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

// H4sIAAAAAAAA/6tWKijKTylNLvFMUbJSUDI0MlbSUVDKS8xNBXEDIHIKfiB+LQBRRhxsLAAAAA==

//Base64 Gzipped Data: H4sIAAAAAAAA/x3JTQ5AQAyG4avQtYV/iRNwALEeNIjRJtPBQtxdWX1fnveGTtC1E9Rw6EnSDCIYmU50YvzK9KcP8qL80mKI0KpdOAiPG3rVHUXMjKoNWstR0LOzU6hFlbz6v3FcwfMCO8p36nIAAAA=
//Plain JSON Data: {"UserId":"user123","conversationId":"conv456","channel":"websocket","message":"Hello, World!","agent":"agent007"}