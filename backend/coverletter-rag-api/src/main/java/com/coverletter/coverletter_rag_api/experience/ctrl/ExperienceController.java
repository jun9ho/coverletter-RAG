package com.coverletter.coverletter_rag_api.experience.ctrl;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coverletter.coverletter_rag_api.experience.domain.dto.ExperienceCreateRequest;
import com.coverletter.coverletter_rag_api.experience.domain.dto.ExperienceResponse;
import com.coverletter.coverletter_rag_api.experience.service.ExperienceService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @Operation(summary = "경험 등록")
    @PostMapping
    public ExperienceResponse create(@Valid @RequestBody ExperienceCreateRequest request) {
        return experienceService.create(request);
    }

    @Operation(summary = "경험 목록 조회")
    @GetMapping
    public List<ExperienceResponse> findAll() {
        return experienceService.findAll();
    }

    @Operation(summary = "경험 단건 조회")
    @GetMapping("/{id}")
    public ExperienceResponse findById(@PathVariable Long id) {
        return experienceService.findById(id);
    }
}