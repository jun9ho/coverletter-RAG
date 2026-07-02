package com.coverletter.coverletter_rag_api.experience.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coverletter.coverletter_rag_api.experience.domain.entity.ExperienceEntity;

public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {
}