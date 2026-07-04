package com.coverletter.coverletter_rag_api.document.service;

import org.springframework.stereotype.Service;

import com.coverletter.coverletter_rag_api.document.domain.dto.ExperienceExtractResponse;
import com.coverletter.coverletter_rag_api.openai.OpenAiChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceExtractionService {

    private final OpenAiChatService openAiChatService;
    private final ObjectMapper objectMapper;

    public ExperienceExtractResponse extract(String content) {
        String systemPrompt = """
                너는 사용자의 긴 이력서, 자기소개서, 포트폴리오 텍스트에서
                자기소개서에 활용 가능한 경험을 추출하는 도우미다.

                반드시 아래 JSON 형식만 반환한다.
                마크다운 코드블록을 사용하지 마라.
                설명 문장을 붙이지 마라.
                없는 사실은 절대 만들지 마라.
                기간을 알 수 없으면 period는 null로 둔다.
                category는 아래 중 하나로 분류한다.
                - project
                - internship
                - research
                - education
                - activity
                - award
                - other

                content는 자기소개서에 바로 활용할 수 있도록
                상황, 역할, 행동, 결과가 드러나게 3~6문장으로 정리한다.

                tags는 기술, 역량, 키워드 중심으로 3~7개 추출한다.

                반환 형식:
                {
                  "extractedExperiences": [
                    {
                      "title": "경험 제목",
                      "category": "project",
                      "period": "2024.03~2024.06",
                      "content": "경험 내용",
                      "tags": ["Spring Boot", "협업", "문제 해결"]
                    }
                  ]
                }
                """;

        String userPrompt = """
                아래 텍스트에서 자기소개서에 활용 가능한 경험들을 추출해줘.

                텍스트:
                %s
                """.formatted(content);

        String result = openAiChatService.generate(systemPrompt, userPrompt);

        try {
            String cleanedJson = cleanJson(result);
            return objectMapper.readValue(cleanedJson, ExperienceExtractResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("경험 추출 결과를 JSON으로 변환하지 못했습니다. result=" + result, e);
        }
    }

    private String cleanJson(String result) {
        if (result == null) {
            throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
        }

        return result
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}