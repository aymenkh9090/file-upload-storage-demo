package com.aymen.doc.repository;

import com.aymen.doc.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    boolean existsByStoredName(String storedName);
}
