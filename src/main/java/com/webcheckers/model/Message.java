package com.webcheckers.model;

import lombok.Data;

@Data
public class Message {
    private String text;
    private MessageType type;

    public Message(String text, MessageType type) {
        this.text = text;
        this.type = type;
    }
}
