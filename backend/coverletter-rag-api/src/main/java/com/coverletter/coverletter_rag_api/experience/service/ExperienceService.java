package com.coverletter.coverletter_rag_api.experience.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coverletter.coverletter_rag_api.experience.domain.dto.ExperienceCreateRequest;
import com.coverletter.coverletter_rag_api.experience.domain.dto.ExperienceResponse;
import com.coverletter.coverletter_rag_api.experience.domain.entity.ExperienceEntity;
import com.coverletter.coverletter_rag_api.experience.repository.ExperienceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

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
}