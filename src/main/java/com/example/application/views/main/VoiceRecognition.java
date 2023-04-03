package com.example.application.views.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

@Tag("voice-recognition")
@JsModule("./voice-recognition.js")
public class VoiceRecognition extends Component {

    public VoiceRecognition() {
        // Used for Vaadin to create the element
    }

    public void setContinuous(boolean continuous) {
        getElement().setProperty("continuous", continuous);
    }

    public boolean isContinuous() {
        return getElement().getProperty("continuous", true);
    }

    @Synchronize("myresult")
    public String getSpeech() {
        return getElement().getProperty("speech", "none java");
    }

    public void start() {
        getElement().callJsFunction("start");
    }

    public void stop() {
        getElement().callJsFunction("stop");
    }

    public void abort() {
        getElement().callJsFunction("abort");
    }

    public Registration addStartListener(ComponentEventListener<StartEvent> listener) {
        return addListener(StartEvent.class, listener);
    }

    public Registration addErrorListener(ComponentEventListener<ErrorEvent> listener) {
        return addListener(ErrorEvent.class, listener);
    }

    public Registration addEndListener(ComponentEventListener<EndEvent> listener) {
        return addListener(EndEvent.class, listener);
    }

    public Registration addResultListener(ComponentEventListener<ResultEvent> listener) {
        return getElement().addEventListener("speechResult", componentEvent -> {
            String speech = componentEvent.getEventData().getString("event.detail.speech");
            listener.onComponentEvent(new ResultEvent(this, speech));
        }).addEventData("event.detail.speech");
    }

    public static class StartEvent extends ComponentEvent<VoiceRecognition> {
        public StartEvent(VoiceRecognition source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class ErrorEvent extends ComponentEvent<VoiceRecognition> {
        public ErrorEvent(VoiceRecognition source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class EndEvent extends ComponentEvent<VoiceRecognition> {
        public EndEvent(VoiceRecognition source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class ResultEvent extends ComponentEvent<VoiceRecognition> {
        private String speech;

        public ResultEvent(VoiceRecognition source, String speech) {
            super(source, true);
            this.speech = speech;
        }

        public String getSpeech() {
            return speech;
        }
    }

}
