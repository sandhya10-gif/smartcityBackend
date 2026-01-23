package com.example.SmartCity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "colleges")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BASIC INFO T
    private String name;
    private String city;

    // Government / Private / Autonomous / Deemed
    private String type;

    // Stored as CSV: CSE,IT,ECE
    private String courses;

    // ENGINEERING / ARTS / MEDICAL
    @Enumerated(EnumType.STRING)
    private CollegeCategory category;

    // HOSTEL INFO
    @Column(name = "hostel_available")
    private Boolean hostelAvailable;

    @Column(name = "hostel_fee")
    private Double hostelFee;

    // LOCATION (for nearest colleges)
    private Double latitude;
    private Double longitude;

    // LINKS
    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "google_map_url")
    private String googleMapUrl;

    @Column(name = "logo_url")
    private String logoUrl;

    // getter
    public String getLogoUrl() {
        return logoUrl;
    }

    // setter
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }


    /* ---------- GETTERS ---------- */

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getType() { return type; }
    public String getCourses() { return courses; }
    public Boolean getHostelAvailable() { return hostelAvailable; }
    public Double getHostelFee() { return hostelFee; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getWebsiteUrl() { return websiteUrl; }
    public String getGoogleMapUrl() { return googleMapUrl; }
    public CollegeCategory getCategory() {
        return category;
    }



    /* ---------- SETTERS ---------- */

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setType(String type) { this.type = type; }
    public void setCourses(String courses) { this.courses = courses; }
    public void setHostelAvailable(Boolean hostelAvailable) { this.hostelAvailable = hostelAvailable; }
    public void setHostelFee(Double hostelFee) { this.hostelFee = hostelFee; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
    public void setGoogleMapUrl(String googleMapUrl) { this.googleMapUrl = googleMapUrl; }
    public void setCategory(CollegeCategory category) {
        this.category = category;
    }
}
