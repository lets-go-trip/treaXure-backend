package com.trip.xplore.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Add other fields related to Like (e.g., relationships to User, Photo, etc.)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
} 