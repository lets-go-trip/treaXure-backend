package com.trip.xplore.place.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Add other fields related to Place

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
} 