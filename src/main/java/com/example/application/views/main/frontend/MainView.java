package com.example.application.views.main.frontend;

import java.io.IOException;

import com.example.application.views.main.data.SupportProfile;
import com.example.application.views.main.service.SupportProfileService;
import com.example.application.views.main.web.GPTRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MainView.class);

    private final SupportProfileService supportProfileService;
    private final SupportProfileForm supportProfileForm = new SupportProfileForm();
    private final TextField gptKey = new TextField("GPT Key");

    public MainView(SupportProfileService supportProfileService) {
        this.supportProfileService = supportProfileService;

        gptKey.setRequired(true);

        Paragraph paragraph = new Paragraph(
                "Tell me in plain English about your new Support Profile and I will fill out the form for you.");

        Paragraph instructions = new Paragraph(
                "Tell me the name of the Support Profile, the name of the owner, the address of the owner, the phone number of the owner.");

        Paragraph impactInstructions = new Paragraph(
                "Tell me how many impacts you'd like to include. Tell me the name of each impact, the severity of the impact, and the threshold phrase for the impact.");

        Paragraph consumerInstructions = new Paragraph(
                "Tell me how many consumers. Tell me the name of each consumer, the address of the consumer, the phone number of the consumer, and the type of consumer.");

        TextArea userInputField = new TextArea("Enter your plain English Support Profile description here:");

        userInputField.setWidth("100%");
        userInputField.setRequired(true);

        Button submitButton = new Button("Submit", e -> submitSupportProfilePrompt(userInputField.getValue()));

        // Currently not used
        VoiceRecognitionView voiceRecognitionView = new VoiceRecognitionView();
        voiceRecognitionView.addTextReadyListener(e -> {
            userInputField.setValue(e.getRecognizedText());
            // submitUserPrompt(e.getRecognizedText()); // for eager submission
        });

        supportProfileForm.setVisible(false);

        add(
                voiceRecognitionView,
                gptKey, paragraph, instructions, impactInstructions, consumerInstructions, userInputField, submitButton,
                supportProfileForm);
    }

    private void submitSupportProfilePrompt(String userPrompt) {
        log.info("User prompt: {}", userPrompt);

        GPTRequest supportProfileRequest = new GPTRequest();
        supportProfileRequest.setGPTKey(gptKey.getValue());
        supportProfileRequest.setUserPrompt(userPrompt);

        try {
            SupportProfile supportProfileResponse = supportProfileService
                    .createSupportProfile(supportProfileRequest);

            if (supportProfileResponse != null) {
                supportProfileForm.setVisible(true);
                supportProfileForm.setSupportProfile(supportProfileResponse);

                supportProfileService.saveSupportProfile(supportProfileResponse);

                log.info("Support Profile data received: {}", supportProfileResponse);

                Notification.show("Support Profile data received.");
            } else {
                Notification.show("Error fetching Support Profile data.");
            }
        } catch (IOException e) {
            Notification.show("Error processing GPT API response.");
        }
    }

}
