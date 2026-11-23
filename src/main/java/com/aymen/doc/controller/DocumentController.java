package com.aymen.doc.controller;

import com.aymen.doc.dto.DocumentResponse;
import com.aymen.doc.entity.Document;
import com.aymen.doc.service.DocumentService;
import com.aymen.doc.service.LocalStorageService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final LocalStorageService localStorageService;


    public DocumentController(DocumentService documentService, LocalStorageService localStorageService) {
        this.documentService = documentService;
        this.localStorageService = localStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse upload(@RequestPart("file") MultipartFile file) throws Exception {
        return documentService.upload(file);
    }

    @GetMapping("/{id}")
    public DocumentResponse get(@PathVariable UUID id){
        Document d = documentService.getEntity(id);
        return new DocumentResponse(
                d.getId(),d.getOriginalName(),d.getContentType(),d.getSize(),d.getCreatedAt(),
                "/api/documents/"+id+"/download"
        );
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable UUID id) throws Exception {
        Document d = documentService.getEntity(id);
        LocalStorageService.RessourceData data = localStorageService.load(d.getStoredName());
        MediaType type = MediaType.parseMediaType(d.getContentType());
        return ResponseEntity.ok()
                .contentType(type)
                .contentLength(data.bytes().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(d.getOriginalName()).build().toString())
                .body(data.bytes());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) throws Exception {
        documentService.delete(id);
    }





}
