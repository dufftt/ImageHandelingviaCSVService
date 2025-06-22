/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.csv_handeling.Kafka;

import java.security.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author soura
 */
public class KafkaMessage {


    private String trackId;
    private String action;
    private String timestamp;

    public KafkaMessage() {
    }

    public String getTrackId() {
        return trackId;
    }
    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp() {
        this.timestamp = String.valueOf(LocalDateTime.now());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KafkaMessage{");
        sb.append("trackId=").append(trackId);
        sb.append(", action=").append(action);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
    public KafkaMessage(String trackId, String action) {
        this.trackId = trackId;
        this.action = action;
        this.timestamp = String.valueOf(LocalDateTime.now());
    }


}
