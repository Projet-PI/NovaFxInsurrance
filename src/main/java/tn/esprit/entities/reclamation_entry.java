package tn.esprit.entities;


public class reclamation_entry {
    public Integer id;
    public String prompt;
    public String day_time;
    public String response;
    public String status;
    public String responseType;

    public reclamation_entry() {
    }

    public reclamation_entry(Integer id, String prompt, String day_time, String response, String status, String responseType) {
        this.id = id;
        this.prompt = prompt;
        this.day_time = day_time;
        this.response = response;
        this.status = status;
        this.responseType = responseType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getDay_time() {
        return day_time;
    }

    public void setDay_time(String day_time) {
        this.day_time = day_time;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    @Override
    public String toString() {
        return "reclamation_entry{" +
                "id=" + id +
                ", prompt='" + prompt + '\'' +
                ", day_time='" + day_time + '\'' +
                ", response='" + response + '\'' +
                ", status='" + status + '\'' +
                ", responseType='" + responseType + '\'' +
                '}';
    }
}