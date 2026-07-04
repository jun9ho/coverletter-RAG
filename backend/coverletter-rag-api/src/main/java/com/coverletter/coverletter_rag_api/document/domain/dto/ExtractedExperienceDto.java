package com.coverletter.coverletter_rag_api.document.domain.dto;

import java.util.List;

public record ExtractedExperienceDto(
        String title,
        String category,
        String period,
        String content,
        List<String> tags) {
}