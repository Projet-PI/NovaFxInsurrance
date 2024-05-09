package tn.esprit.services;

public class ChatResponse {
    private final String regulation;
    private final String elapsedTime;

    public ChatResponse(String regulation, String elapsedTime) {
        this.regulation = regulation;
        this.elapsedTime = elapsedTime;
    }

    public String getRegulation() {
        return regulation;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }
}
