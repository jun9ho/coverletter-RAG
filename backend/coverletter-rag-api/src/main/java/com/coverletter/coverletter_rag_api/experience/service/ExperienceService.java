package com.coverletter.coverletter_rag_api.experience.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coverletter.coverletter_rag_api.experience.domain.dto.ExperienceCreateRequest;
import com.coverletter.coverletter_rag_api.experience.domain.dto.ExperienceResponse;
import com.coverletter.coverletter_rag_api.experience.domain.entity.ExperienceEntity;
import com.coverletter.coverletter_rag_api.experience.repository.ExperienceRepository;
import com.coverletter.coverletter_rag_api.openai.OpenAiEmbeddingService;
import com.coverletter.coverletter_rag_api.qdrant.domain.dto.QdrantExperienceSearchResult;
import com.coverletter.coverletter_rag_api.qdrant.service.QdrantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final OpenAiEmbeddingService openAiEmbeddingService;
    private final QdrantService qdrantService;

    public ExperienceResponse create(ExperienceCreateRequest request) {
        String tags = request.tags() == null
                ? ""
                : String.join(",", request.tags());

        ExperienceEntity experience = ExperienceEntity.builder()
                .title(request.title())
                .category(request.category())
                .period(request.period())
                .content(request.content())
                .tags(tags)
                .createdAt(LocalDateTime.now())
                .build();

        ExperienceEntity saved = experienceRepository.save(experience);

        String embeddingText = buildEmbeddingText(saved);
        List<Double> vector = openAiEmbeddingService.embed(embeddingText);
        qdrantService.upsertExperience(saved, vector);

        return ExperienceResponse.from(saved);
    }

    public List<ExperienceResponse> findAll() {
        return experienceRepository.findAll()
                .stream()
                .map(ExperienceResponse::from)
                .toList();
    }

    public ExperienceResponse findById(Long id) {
        ExperienceEntity experience = experienceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("경험을 찾을 수 없습니다. id=" + id));

        return ExperienceResponse.from(experience);
    }

    public List<QdrantExperienceSearchResult> search(String query) {
        List<Double> queryVector = openAiEmbeddingService.embed(query);
        return qdrantService.searchExperiences(queryVector, 5);
    }

    private String buildEmbeddingText(ExperienceEntity experience) {
        return """
                경험명: %s
                카테고리: %s
                기간: %s
                태그: %s
                내용: %s
                """.formatted(
                experience.getTitle(),
                experience.getCategory(),
                experience.getPeriod(),
                experience.getTags(),
                experience.getContent()
        );
    }
}