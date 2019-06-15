package edu.ics.uci.core;

public class ChatMessage {

    private String chatMessage;
    private String sender;
    private String receiver;

    public ChatMessage() {

    }

    public ChatMessage(String chatMessage, String sender, String receiver) {
        this.chatMessage = chatMessage;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
