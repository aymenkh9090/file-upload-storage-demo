package com.aymen.doc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DocumentUpdateRequest(
        @NotBlank(message = "le nom est Obligatoire")
        @Size(max = 255,message = "le nom ne doit depasser 255 caractére")
        String displayName
) {
}
