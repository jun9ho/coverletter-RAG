package com.coverletter.coverletter_rag_api.experience.domain.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record ExperienceCreateRequest(
        @NotBlank
        String title,

        String category,

        String period,

        @NotBlank
        String content,

        List<String> tags
) {
}