package com.coverletter.coverletter_rag_api.coverletter.domain.dto;

import java.util.List;

import com.coverletter.coverletter_rag_api.coverletter.domain.entity.CoverLetterEntity;

public record CoverLetterGenerateResponse(
        Long id,
        String company,
        String position,
        String question,
        String answer,
        List<UsedExperienceResponse> usedExperiences) {
    public static CoverLetterGenerateResponse of(
            CoverLetterEntity entity,
            List<UsedExperienceResponse> usedExperiences) {
        return new CoverLetterGenerateResponse(
                entity.getId(),
                entity.getCompany(),
                entity.getPosition(),
                entity.getQuestion(),
                entity.getAnswer(),
                usedExperiences);
    }
}