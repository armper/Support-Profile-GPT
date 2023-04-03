package com.example.application.views.main;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportProfileResponse {
    private String name;
    private Owner owner;
    @JsonProperty("Impacts")
    private List<Impact> impacts;
    private List<Consumer> consumers;
    private Map<String, String> thresholdMapping;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Impact> getImpacts() {
        return impacts;
    }

    public void setImpacts(List<Impact> impacts) {
        this.impacts = impacts;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Consumer> consumers) {
        this.consumers = consumers;
    }

    public Map<String, String> getThresholdMapping() {
        return thresholdMapping;
    }

    public void setThresholdMapping(Map<String, String> thresholdMapping) {
        this.thresholdMapping = thresholdMapping;
    }

    public static class Owner {
        @JsonProperty("Contact")
        private Contact contact;

        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        @Override
        public String toString() {
            return "Owner{" +
                    "contact=" + contact +
                    '}';
        }
    }

    public static class Impact {
        private String name;
        private String severity;
        private String thresholdPhrase;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getThresholdPhrase() {
            return thresholdPhrase;
        }

        public void setThresholdPhrase(String thresholdPhrase) {
            this.thresholdPhrase = thresholdPhrase;
        }

        @Override
        public String toString() {
            return "Impact{" +
                    "name='" + name + '\'' +
                    ", severity='" + severity + '\'' +
                    ", thresholdPhrase='" + thresholdPhrase + '\'' +
                    '}';
        }
    }

    public static class Consumer {
        @JsonProperty("Contact")
        private Contact contact;

        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        @Override
        public String toString() {
            return "Consumer{" +
                    "contact=" + contact +
                    '}';
        }

    }

    public static class Contact {
        private String name;
        private String address;
        private String phone;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", phone='" + phone + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SupportProfileResponse{" +
                "name='" + name + '\'' +
                ", owner=" + owner +
                ", impacts=" + impacts +
                ", consumers=" + consumers +
                ", thresholdMapping=" + thresholdMapping +
                '}';
    }
}
