package com.example.application.views.main.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Impact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String severity;
    private String thresholdPhrase;

    @ManyToOne
    @JoinColumn(name = "support_profile_id")
    private SupportProfile supportProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public SupportProfile getSupportProfile() {
        return supportProfile;
    }

    public void setSupportProfile(SupportProfile supportProfile) {
        this.supportProfile = supportProfile;
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