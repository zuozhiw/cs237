package edu.ics.uci.core;

import java.util.Objects;

public class MockTippersResponse {

    private MockTippersPayload payload;
    private Double confidence;
    private Integer id;
    private Integer virtual_sensor_id;
    private String timestamp;

    @Override
    public String toString() {
        return "MockTippersResponse{" +
                "payload=" + payload +
                ", confidence=" + confidence +
                ", id='" + id + '\'' +
                ", virtual_sensor_id='" + virtual_sensor_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockTippersResponse that = (MockTippersResponse) o;
        return Objects.equals(payload, that.payload) &&
                Objects.equals(confidence, that.confidence) &&
                Objects.equals(id, that.id) &&
                Objects.equals(virtual_sensor_id, that.virtual_sensor_id) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, confidence, id, virtual_sensor_id, timestamp);
    }

    public MockTippersPayload getPayload() {
        return payload;
    }

    public void setPayload(MockTippersPayload payload) {
        this.payload = payload;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVirtual_sensor_id() {
        return virtual_sensor_id;
    }

    public void setVirtual_sensor_id(Integer virtual_sensor_id) {
        this.virtual_sensor_id = virtual_sensor_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public MockTippersResponse() {
    }

    public MockTippersResponse(MockTippersPayload payload, Double confidence, Integer id, Integer virtual_sensor_id, String timestamp) {
        this.payload = payload;
        this.confidence = confidence;
        this.id = id;
        this.virtual_sensor_id = virtual_sensor_id;
        this.timestamp = timestamp;
    }
}