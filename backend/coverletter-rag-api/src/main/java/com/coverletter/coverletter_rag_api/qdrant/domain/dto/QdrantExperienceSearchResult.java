package com.coverletter.coverletter_rag_api.qdrant.domain.dto;

import java.util.List;

public record QdrantExperienceSearchResult(
        Long experienceId,
        String title,
        String category,
        String period,
        String content,
        List<String> tags,
        Double score
) {
}