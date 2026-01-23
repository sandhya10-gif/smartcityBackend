package com.example.SmartCity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private String type;        // Government / Private
    private String specialties; // Cardiology, Neurology, etc.

    @Column(name = "emergency_available")
    private Boolean emergencyAvailable;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "google_map_url")
    private String googleMapUrl;

    private Double latitude;
    private Double longitude;

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }


    // --- GETTERS ---

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getType() {
        return type;
    }

    public String getSpecialties() {
        return specialties;
    }

    public Boolean getEmergencyAvailable() {
        return emergencyAvailable;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getGoogleMapUrl() {
        return googleMapUrl;
    }

    // --- SETTERS ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }

    public void setEmergencyAvailable(Boolean emergencyAvailable) {
        this.emergencyAvailable = emergencyAvailable;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setGoogleMapUrl(String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }
}