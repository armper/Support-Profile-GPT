package com.example.application.views.main.web;

public class GPTRequest {

    private String userPrompt;

    private String gptKey;

    public String getGPTKey() {
        return gptKey;
    }

    public void setGPTKey(String gptKey) {
        this.gptKey = gptKey;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

}
