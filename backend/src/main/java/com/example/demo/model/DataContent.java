package com.example.demo.model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataContent {
    String dataid;
    byte[] content;
    String name;
    public DataContent(String dataid, String name, byte[] content) {
        this.dataid = dataid;
        this.name = name;
        this.content = content;
    }


    public String getDataid() {
        return dataid;
    }

    public byte[] getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

}
