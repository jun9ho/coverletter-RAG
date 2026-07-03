package com.coverletter.coverletter_rag_api.coverletter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coverletter.coverletter_rag_api.coverletter.domain.entity.CoverLetterEntity;

public interface CoverLetterRepository extends JpaRepository<CoverLetterEntity, Long> {
}