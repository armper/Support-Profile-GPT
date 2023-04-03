package com.example.application.views.main;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.shared.Registration;

public class VoiceRecognitionView extends Composite<Div> {
    private VoiceRecognition voiceRecognition;

    public VoiceRecognitionView() {
        voiceRecognition = new VoiceRecognition();
        voiceRecognition.setContinuous(false);

        Button startButton = new Button("Start", event -> voiceRecognition.start());
        Button stopButton = new Button("Stop", event -> voiceRecognition.stop());
        Button abortButton = new Button("Abort", event -> voiceRecognition.abort());

        voiceRecognition.addResultListener(event -> fireEvent(new TextReadyEvent(this, event.getSpeech())));

        getContent().add(voiceRecognition, startButton, stopButton, abortButton);
    }

    public Registration addTextReadyListener(ComponentEventListener<TextReadyEvent> listener) {
        return addListener(TextReadyEvent.class, listener);
    }

    public static class TextReadyEvent extends ComponentEvent<VoiceRecognitionView> {
        private final String recognizedText;

        public TextReadyEvent(VoiceRecognitionView source, String recognizedText) {
            super(source, false);
            this.recognizedText = recognizedText;
        }

        public String getRecognizedText() {
            return recognizedText;
        }
    }
}
