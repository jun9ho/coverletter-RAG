package com.coverletter.coverletter_rag_api.coverletter.domain.dto;


public record UsedExperienceResponse(
        Long experienceId,
        String title,
        String category,
        Double score
) {
}