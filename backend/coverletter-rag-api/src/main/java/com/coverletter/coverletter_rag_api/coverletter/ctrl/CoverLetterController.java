package com.coverletter.coverletter_rag_api.coverletter.ctrl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coverletter.coverletter_rag_api.coverletter.domain.dto.CoverLetterGenerateRequest;
import com.coverletter.coverletter_rag_api.coverletter.domain.dto.CoverLetterGenerateResponse;
import com.coverletter.coverletter_rag_api.coverletter.service.CoverLetterService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cover-letters")
@RequiredArgsConstructor
public class CoverLetterController {

    private final CoverLetterService coverLetterService;

    @Operation(summary = "자기소개서 초안 생성")
    @PostMapping("/generate")
    public CoverLetterGenerateResponse generate(
            @Valid @RequestBody CoverLetterGenerateRequest request
    ) {
        return coverLetterService.generate(request);
    }
}