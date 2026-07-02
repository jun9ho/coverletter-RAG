package com.coverletter.coverletter_rag_api.experience.domain.dto;

import java.util.Arrays;
import java.util.List;

import com.coverletter.coverletter_rag_api.experience.domain.entity.ExperienceEntity;

public record ExperienceResponse(
        Long id,
        String title,
        String category,
        String period,
        String content,
        List<String> tags
) {
    public static ExperienceResponse from(ExperienceEntity entity) {
        List<String> tagList = entity.getTags() == null || entity.getTags().isBlank()
                ? List.of()
                : Arrays.stream(entity.getTags().split(","))
                    .map(String::trim)
                    .toList();

        return new ExperienceResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getCategory(),
                entity.getPeriod(),
                entity.getContent(),
                tagList
        );
    }
}