package br.com.grupo_38.flight_on_time.dto;

public class PredictResponse {
    private boolean onTime;
    private double confidence;
    private String message;

    public PredictResponse() {
    }

    public PredictResponse(boolean onTime, double confidence, String message) {
        this.onTime = onTime;
        this.confidence = confidence;
        this.message = message;
    }

    public boolean isOnTime() {
        return onTime;
    }

    public void setOnTime(boolean onTime) {
        this.onTime = onTime;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
