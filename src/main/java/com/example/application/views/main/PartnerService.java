package com.example.application.views.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PartnerService {

    private static final String API_KEY = "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";
    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL_NAME = "gpt-3.5-turbo-0301";

    public PartnerResponse createPartner(GPTRequest partnerRequest) throws IOException {
        String userPrompt = partnerRequest.getUserPrompt();
        String gptPrompt = String.format("Based on the user input: '%s'. Please generate a JSON object for a partner with the following fields: first name, last name, address, phone number, and email address. Format the response as follows: {\"firstName\": \"\", \"lastName\": \"\", \"address\": \"\", \"phoneNumber\": \"\", \"email\": \"\"}", userPrompt);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(GPT_API_URL);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            ObjectMapper objectMapper = new ObjectMapper();
            Message userMessage = new Message("system", gptPrompt);
            List<Message> messages = Arrays.asList(userMessage);
            String requestBodyJson = objectMapper.writeValueAsString(new GptRequest(MODEL_NAME, messages, 150, 1, null, 1.0));
            StringEntity requestBody = new StringEntity(requestBodyJson);
            httpPost.setEntity(requestBody);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                JsonNode jsonNode = objectMapper.readTree(response.getEntity().getContent());
                JsonNode jsonResponseNode = jsonNode.get("choices").get(0).get("message").get("content");
                if (jsonResponseNode != null) {
                    String jsonResponseText = jsonResponseNode.asText().trim();
                    return objectMapper.readValue(jsonResponseText, PartnerResponse.class);
                } else {
                    throw new IOException("Unexpected GPT-3 API response format.");
                }
            }
        }
    }
}
