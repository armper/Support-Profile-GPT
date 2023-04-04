package com.example.application.views.main.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.example.application.views.main.data.SupportProfile;
import com.example.application.views.main.data.SupportProfileRepository;
import com.example.application.views.main.web.GPTRequest;
import com.example.application.views.main.web.GptRequest;
import com.example.application.views.main.web.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SupportProfileService {

    private static final Logger log = LoggerFactory.getLogger(SupportProfileService.class);

    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL_NAME = "gpt-3.5-turbo-0301";

    private final SupportProfileRepository supportProfileRepository;

    public SupportProfileService(SupportProfileRepository supportProfileRepository) {
        this.supportProfileRepository = supportProfileRepository;
    }

    public SupportProfile createSupportProfile(GPTRequest partnerRequest) throws IOException {
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
                        "   \"name\":\"Chat GPT Support Profile\",\n" +
                        "   \"owner\":{\n" +
                        "      \"name\":\"Bob Smith\",\n" +
                        "      \"address\":\"123 Humbolt Street Claremont CA 94401\",\n" +
                        "      \"phone\":\"443-406-6636\",\n" +
                        "      \"type\":\"person\"\n" +
                        "   },\n" +
                        "   \"Impacts\":[\n" +
                        "      {\n" +
                        "         \"name\":\"balloon fiesta\",\n" +
                        "         \"severity\":\"High\",\n" +
                        "         \"thresholdPhrase\":\"winds above 50mph\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "         \"name\":\"airline landing\",\n" +
                        "         \"severity\":\"Medium\",\n" +
                        "         \"thresholdPhrase\":\"tornado within 50 miles of\"\n" +
                        "      }\n" +
                        "   ],\n" +
                        "   \"consumers\":[\n" +
                        "      {\n" +
                        "         \"name\":\"John Doe\",\n" +
                        "         \"address\":\"456 Main Street San Francisco CA 94102\",\n" +
                        "         \"phone\":\"415-555-1234\",\n" +
                        "         \"type\":\"person\"\n" +
                        "      }\n" +
                        "   ],\n" +
                        "   \"thresholdMapping\":{\n" +
                        "      \"winds above 50mph\":\"mph\",\n" +
                        "      \"tornado within 50 miles of\":\"miles\"\n" +
                        "   }\n" +
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
                InputStream content = response.getEntity().getContent();
                JsonNode jsonNode = objectMapper.readTree(content);
                String jsonResponse = objectMapper.writeValueAsString(jsonNode);
                log.info("GPT-3 API response: {}", jsonResponse);
                JsonNode jsonResponseNode = jsonNode.get("choices").get(0).get("message").get("content");
                if (jsonResponseNode != null) {
                    String jsonResponseText = jsonResponseNode.asText().trim();
                    SupportProfile readValue = null;
                    try {

                        readValue = objectMapper.readValue(jsonResponseText,
                                SupportProfile.class);
                    } catch (Exception e) {
                        log.error("Error!", e);
                    }
                    return readValue;
                } else {
                    throw new IOException("Unexpected GPT-3 API response format.");
                }
            }
        }
    }

    public void saveSupportProfile(SupportProfile supportProfileResponse) {
        supportProfileRepository.save(supportProfileResponse);
    }

    public Set<SupportProfile> findAll() {
        return supportProfileRepository.findAll();
    }

    public SupportProfile findById(Long id) {
        return supportProfileRepository.findById(id).orElse(null);
    }
}
