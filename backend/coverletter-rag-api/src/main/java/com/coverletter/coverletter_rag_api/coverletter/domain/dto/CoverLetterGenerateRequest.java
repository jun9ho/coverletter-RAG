package com.coverletter.coverletter_rag_api.coverletter.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CoverLetterGenerateRequest(
        @NotBlank String company,
        @NotBlank String position,
        @NotBlank String question,
        Integer maxLength
) {
}