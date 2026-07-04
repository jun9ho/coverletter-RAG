package com.coverletter.coverletter_rag_api.document.domain.dto;

import java.util.List;

public record ExperienceExtractResponse(
        List<ExtractedExperienceDto> extractedExperiences) {
}