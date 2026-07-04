package com.coverletter.coverletter_rag_api.document.ctrl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coverletter.coverletter_rag_api.document.domain.dto.ExperienceExtractRequest;
import com.coverletter.coverletter_rag_api.document.domain.dto.ExperienceExtractResponse;
import com.coverletter.coverletter_rag_api.document.service.ExperienceExtractionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final ExperienceExtractionService experienceExtractionService;

    @Operation(summary = "긴 텍스트에서 경험 후보 추출")
    @PostMapping("/extract-experiences")
    public ExperienceExtractResponse extractExperiences(
            @Valid @RequestBody ExperienceExtractRequest request) {
        return experienceExtractionService.extract(request.content());
    }
}