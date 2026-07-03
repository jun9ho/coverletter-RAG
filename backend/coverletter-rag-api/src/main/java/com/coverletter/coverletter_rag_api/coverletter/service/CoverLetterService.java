package com.coverletter.coverletter_rag_api.coverletter.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coverletter.coverletter_rag_api.coverletter.domain.dto.CoverLetterGenerateRequest;
import com.coverletter.coverletter_rag_api.coverletter.domain.dto.CoverLetterGenerateResponse;
import com.coverletter.coverletter_rag_api.coverletter.domain.dto.UsedExperienceResponse;
import com.coverletter.coverletter_rag_api.coverletter.domain.entity.CoverLetterEntity;
import com.coverletter.coverletter_rag_api.coverletter.repository.CoverLetterRepository;
import com.coverletter.coverletter_rag_api.experience.service.ExperienceService;
import com.coverletter.coverletter_rag_api.openai.OpenAiChatService;
import com.coverletter.coverletter_rag_api.qdrant.domain.dto.QdrantExperienceSearchResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoverLetterService {

    private final ExperienceService experienceService;
    private final OpenAiChatService openAiChatService;
    private final CoverLetterRepository coverLetterRepository;

    @Transactional
    public CoverLetterGenerateResponse generate(CoverLetterGenerateRequest request) {
        int maxLength = request.maxLength() == null ? 700 : request.maxLength();

        String searchQuery = buildSearchQuery(request);

        List<QdrantExperienceSearchResult> searchedExperiences = experienceService.search(searchQuery);

        List<QdrantExperienceSearchResult> selectedExperiences = searchedExperiences.stream()
                .limit(3)
                .toList();

        if (selectedExperiences.isEmpty()) {
            throw new IllegalStateException("자소서 생성에 사용할 경험을 찾지 못했습니다.");
        }

        String systemPrompt = buildSystemPrompt();
        String userPrompt = buildUserPrompt(request, selectedExperiences, maxLength);

        String answer = openAiChatService.generate(systemPrompt, userPrompt);

        String usedExperiencesText = buildUsedExperiencesText(selectedExperiences);

        CoverLetterEntity saved = coverLetterRepository.save(
                CoverLetterEntity.builder()
                        .company(request.company())
                        .position(request.position())
                        .question(request.question())
                        .answer(answer)
                        .usedExperiences(usedExperiencesText)
                        .maxLength(maxLength)
                        .createdAt(LocalDateTime.now())
                        .build());

        List<UsedExperienceResponse> usedExperiences = selectedExperiences.stream()
                .map(exp -> new UsedExperienceResponse(
                        exp.experienceId(),
                        exp.title(),
                        exp.category(),
                        exp.score()))
                .toList();

        return CoverLetterGenerateResponse.of(saved, usedExperiences);
    }

    private String buildSearchQuery(CoverLetterGenerateRequest request) {
        return """
                회사: %s
                직무: %s
                자소서 문항: %s
                관련 역량과 경험을 찾기 위한 검색어
                """.formatted(
                request.company(),
                request.position(),
                request.question());
    }

    private String buildSystemPrompt() {
        return """
                너는 취업 자기소개서 작성 도우미다.

                반드시 지켜야 할 규칙:
                1. 제공된 경험 정보만 근거로 답변한다.
                2. 없는 사실, 수치, 성과를 지어내지 않는다.
                3. 문장은 자연스럽고 지원서 문체로 작성한다.
                4. 구조는 문제 상황 → 나의 행동 → 결과/배운 점 흐름으로 작성한다.
                5. 회사와 직무에 맞게 표현하되 과장하지 않는다.
                6. 기술 경험이 있으면 직무 역량과 연결해서 설명한다.
                """;
    }

    private String buildUserPrompt(
            CoverLetterGenerateRequest request,
            List<QdrantExperienceSearchResult> experiences,
            int maxLength) {
        return """
                [회사]
                %s

                [지원 직무]
                %s

                [자기소개서 문항]
                %s

                [글자 수 제한]
                %d자 이내

                [검색된 사용자 경험]
                %s

                위 경험들을 바탕으로 자기소개서 답변 초안을 작성해줘.
                답변만 출력해.
                """.formatted(
                request.company(),
                request.position(),
                request.question(),
                maxLength,
                buildExperienceContext(experiences));
    }

    private String buildExperienceContext(List<QdrantExperienceSearchResult> experiences) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < experiences.size(); i++) {
            QdrantExperienceSearchResult exp = experiences.get(i);

            sb.append("경험 ").append(i + 1).append("\n");
            sb.append("- experienceId: ").append(exp.experienceId()).append("\n");
            sb.append("- 제목: ").append(exp.title()).append("\n");
            sb.append("- 카테고리: ").append(exp.category()).append("\n");
            sb.append("- 기간: ").append(exp.period()).append("\n");
            sb.append("- 태그: ").append(exp.tags()).append("\n");
            sb.append("- 검색 점수: ").append(exp.score()).append("\n");
            sb.append("- 내용: ").append(exp.content()).append("\n\n");
        }

        return sb.toString();
    }

    private String buildUsedExperiencesText(List<QdrantExperienceSearchResult> experiences) {
        StringBuilder sb = new StringBuilder();

        for (QdrantExperienceSearchResult exp : experiences) {
            sb.append("experienceId=")
                    .append(exp.experienceId())
                    .append(", title=")
                    .append(exp.title())
                    .append(", score=")
                    .append(exp.score())
                    .append("\n");
        }

        return sb.toString();
    }
}