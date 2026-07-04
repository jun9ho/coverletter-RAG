package com.coverletter.coverletter_rag_api.document.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExperienceExtractRequest(
        @NotBlank @Size(max = 20000) String content) {
}