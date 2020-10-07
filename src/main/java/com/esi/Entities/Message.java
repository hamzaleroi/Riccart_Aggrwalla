package com.esi.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {

    @JsonProperty("type")
    public MessageType type;

    @JsonProperty("timestamp")
    public int timestamp;

    @JsonProperty("sender_index")
    public int sender_index;

    @JsonProperty("receiver_index")
    public int receiver_index;



    @JsonCreator
    public Message(@JsonProperty("type") MessageType type, @JsonProperty("timestamp") int timestamp, @JsonProperty("sender_index") int sender_index, @JsonProperty("receiver_index") int receiver_index) {
        this.type = type;
        this.timestamp = timestamp;
        this.sender_index = sender_index;
        this.receiver_index = receiver_index;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", timestamp=" + timestamp +
                ", sender_index=" + sender_index +
                ", receiver_index=" + receiver_index +
                '}';
    }
}
