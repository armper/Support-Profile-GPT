package com.example.application.views.main.frontend;

import org.vaadin.addons.pandalyte.VoiceRecognition;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.shared.Registration;

public class VoiceRecognitionView extends Composite<Div> {
    private VoiceRecognition voiceRecognition;

    public VoiceRecognitionView() {
        voiceRecognition = new VoiceRecognition();
        voiceRecognition.setContinuous(false);

        voiceRecognition.addResultListener(event -> fireEvent(new TextReadyEvent(this, event.getSpeech())));

        getContent().add(new Label("Use the Start button to record your voice"), voiceRecognition);
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
