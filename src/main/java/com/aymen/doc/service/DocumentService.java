package com.aymen.doc.service;

import com.aymen.doc.dto.DocumentResponse;
import com.aymen.doc.entity.Document;
import com.aymen.doc.repository.DocumentRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentService {

   private final DocumentRepository repository;
   private final LocalStorageService storage;


    public DocumentService(DocumentRepository repository, LocalStorageService storage) {
        this.repository = repository;
        this.storage = storage;
    }

    @Transactional
    public DocumentResponse upload(MultipartFile file) throws IOException {

        validate(file);
        LocalStorageService.StoredFile stored = storage.store(file);
        try {
            Document document = Document.builder()
                    .originalName(safeOriginalName(file.getOriginalFilename()))
                    .storedName(stored.storedName())
                    .contentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                    .size(file.getSize())
                    .storagePath(stored.path())
                    .build();
            Document savedDocument = repository.save(document);
            return toResponse(savedDocument);
        } catch (RuntimeException ex){
            storage.delete(stored.storedName()); // en cas d'erreur apres stokage on supprime
            throw ex;
        }


    }


    @Transactional(readOnly = true)
    public Document getEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(()->new DocumentNotFoundException(id));
    }

    @Transactional
    public void delete(UUID id)  throws IOException{
        Document document = getEntity(id);
        repository.deleteById(id);
        storage.delete(document.getStoredName());
    }

    private DocumentResponse toResponse(Document d) {
        return new DocumentResponse(d.getId(),d.getOriginalName(),d.getContentType(),
                d.getSize(),d.getCreatedAt(),
                "/api/documents/"+d.getId()+"/download");
    }

    private String safeOriginalName(@Nullable String name) {
        return name == null ? "document" : name.replaceAll("[\\r\\n]","_");
    }

    private void validate(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()){
            throw  new IllegalArgumentException("file is empty");
        }
        if(file.getSize() > 20 *1024 * 1024){
            throw  new IllegalArgumentException("file size is too large");
        }
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class DocumentNotFoundException extends RuntimeException{
        public DocumentNotFoundException(UUID id) {
            super("Document with id " + id + " not found");
        }
    }






}
