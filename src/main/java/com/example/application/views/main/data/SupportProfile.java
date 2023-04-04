package com.example.application.views.main.data;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "support_profiles")
public class SupportProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Contact owner;

    @JsonProperty("Impacts")
    @OneToMany(mappedBy = "supportProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Impact> impacts;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "support_profile_consumer", joinColumns = @JoinColumn(name = "support_profile_id"), inverseJoinColumns = @JoinColumn(name = "consumer_id"))
    private List<Contact> consumers;

    @ElementCollection
    @CollectionTable(name = "threshold_mapping", joinColumns = @JoinColumn(name = "support_profile_id"))
    private Map<String, String> thresholdMapping;

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

    public Contact getOwner() {
        return owner;
    }

    public void setOwner(Contact owner) {
        this.owner = owner;
    }

    public List<Impact> getImpacts() {
        return impacts;
    }

    public void setImpacts(List<Impact> impacts) {
        this.impacts = impacts;
    }

    public List<Contact> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Contact> consumers) {
        this.consumers = consumers;
    }

    public Map<String, String> getThresholdMapping() {
        return thresholdMapping;
    }

    public void setThresholdMapping(Map<String, String> thresholdMapping) {
        this.thresholdMapping = thresholdMapping;
    }

    @Override
    public String toString() {
        return "SupportProfileResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
