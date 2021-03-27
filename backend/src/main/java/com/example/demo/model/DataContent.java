package com.example.demo.model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataContent {
    String dataid;
    String content;
    byte[] contentByte;

    public DataContent(String dataid, String content) {
        this.dataid = dataid;
        this.content = content;
        this.contentByte = content.getBytes(StandardCharsets.UTF_8);
    }

    public DataContent(String dataid, byte[] contentByte) {
        this.dataid = dataid;
        this.contentByte = contentByte;
        this.content = Arrays.toString(contentByte);
    }
}
