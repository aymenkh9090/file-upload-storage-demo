package com.aymen.doc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "original_name",nullable = false,length = 255)
    private String originalName;

    @Column(name = "stored_name",nullable = false,unique = true,length = 255)
    private String storedName;

    @Column(name = "content_type",nullable = false,length = 128)
    private String contentType;

    @Column(name = "size",nullable = false)
    private long size;

    @Column(name = "storage_path",nullable = false,length = 500)
    private String storagePath;

    @Column(name = "created_at",nullable = false,updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }




}
