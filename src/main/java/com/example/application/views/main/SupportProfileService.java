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
public class SupportProfileService {

    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL_NAME = "gpt-3.5-turbo-0301";

    public SupportProfileResponse createSupportProfile(GPTRequest partnerRequest) throws IOException {
        String userPrompt = partnerRequest.getUserPrompt();
        String gptPrompt = String.format(
                "Based on the user input: '%s'. Please generate a JSON object for a Support Profile with the following fields: \n"
                        +
                        "- Name of the support profile \n" +
                        "- Name, address, phone number, and type (person or organization) of the owner \n" +
                        "- List of impacts, including the impact name, severity level (High, Moderate, or Low), and threshold phrase \n"
                        +
                        "- List of consumers, including the consumer name, address, phone number, and type (person or organization) \n"
                        +
                        "- Threshold mapping, which maps threshold keywords to their corresponding measurement units. \n"
                        +
                        "Format the response as follows: \n" +
                        "{\n" +
                        "  \"name\": \"\",\n" +
                        "  \"owner\": {\n" +
                        "    \"Contact\": {\n" +
                        "      \"name\": \"\",\n" +
                        "      \"address\": \"\",\n" +
                        "      \"phone\": \"\",\n" +
                        "      \"type\": \"person/organization\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"Impacts\": [\n" +
                        "    {\n" +
                        "      \"name\": \"\",\n" +
                        "      \"severity\": \"High/Moderate/Low\",\n" +
                        "      \"thresholdPhrase\": \"\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"consumers\": [\n" +
                        "    {\n" +
                        "      \"Contact\": {\n" +
                        "        \"name\": \"\",\n" +
                        "        \"address\": \"\",\n" +
                        "        \"phone\": \"\",\n" +
                        "        \"type\": \"person/organization\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"thresholdMapping\": {\n" +
                        "    \"\": \"\"\n" +
                        "  }\n" +
                        "}",
                userPrompt);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(GPT_API_URL);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + partnerRequest.getGPTKey());
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            ObjectMapper objectMapper = new ObjectMapper();
            Message userMessage = new Message("system", gptPrompt);
            List<Message> messages = Arrays.asList(userMessage);
            String requestBodyJson = objectMapper
                    .writeValueAsString(new GptRequest(MODEL_NAME, messages, 1024, 1, null, 1.0));
            StringEntity requestBody = new StringEntity(requestBodyJson);
            httpPost.setEntity(requestBody);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                JsonNode jsonNode = objectMapper.readTree(response.getEntity().getContent());
                JsonNode jsonResponseNode = jsonNode.get("choices").get(0).get("message").get("content");
                if (jsonResponseNode != null) {
                    String jsonResponseText = jsonResponseNode.asText().trim();
                    SupportProfileResponse readValue = null;
                    try {

                        readValue = objectMapper.readValue(jsonResponseText,
                                SupportProfileResponse.class);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    return readValue;
                } else {
                    throw new IOException("Unexpected GPT-3 API response format.");
                }
            }
        }
    }
}
