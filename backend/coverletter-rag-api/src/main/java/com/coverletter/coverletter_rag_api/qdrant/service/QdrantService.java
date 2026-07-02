package com.coverletter.coverletter_rag_api.qdrant.service;

import com.coverletter.coverletter_rag_api.experience.domain.entity.ExperienceEntity;
import com.coverletter.coverletter_rag_api.qdrant.domain.dto.QdrantExperienceSearchResult;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QdrantService {

    private final RestClient qdrantRestClient;

    @Value("${qdrant.collection-name}")
    private String collectionName;

    public QdrantService(@Qualifier("qdrantRestClient") RestClient qdrantRestClient) {
        this.qdrantRestClient = qdrantRestClient;
    }

    public void upsertExperience(ExperienceEntity experience, List<Double> vector) {
        List<String> tagList = parseTags(experience.getTags());

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("experienceId", experience.getId());
        payload.put("title", experience.getTitle());
        payload.put("category", experience.getCategory());
        payload.put("period", experience.getPeriod());
        payload.put("content", experience.getContent());
        payload.put("tags", tagList);

        Map<String, Object> point = new LinkedHashMap<>();
        point.put("id", experience.getId());
        point.put("vector", vector);
        point.put("payload", payload);

        Map<String, Object> requestBody = Map.of(
                "points", List.of(point)
        );

        qdrantRestClient.put()
                .uri("/collections/{collectionName}/points?wait=true", collectionName)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }

    public List<QdrantExperienceSearchResult> searchExperiences(List<Double> queryVector, int limit) {
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("query", queryVector);
        requestBody.put("limit", limit);
        requestBody.put("with_payload", true);

        QdrantQueryResponse response = qdrantRestClient.post()
                .uri("/collections/{collectionName}/points/query", collectionName)
                .body(requestBody)
                .retrieve()
                .body(QdrantQueryResponse.class);

        if (response == null || response.result() == null || response.result().points() == null) {
            return List.of();
        }

        return response.result().points()
                .stream()
                .map(this::toSearchResult)
                .toList();
    }

    private QdrantExperienceSearchResult toSearchResult(QdrantPoint point) {
        Map<String, Object> payload = point.payload();

        return new QdrantExperienceSearchResult(
                toLong(payload.get("experienceId")),
                toStringValue(payload.get("title")),
                toStringValue(payload.get("category")),
                toStringValue(payload.get("period")),
                toStringValue(payload.get("content")),
                toStringList(payload.get("tags")),
                point.score()
        );
    }

    private List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }

        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isBlank())
                .toList();
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.longValue();
        }

        return Long.parseLong(String.valueOf(value));
    }

    private String toStringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(Object value) {
        if (value == null) {
            return List.of();
        }

        if (value instanceof List<?> list) {
            return list.stream()
                    .map(String::valueOf)
                    .toList();
        }

        return List.of(String.valueOf(value));
    }

    private record QdrantQueryResponse(
            QdrantResult result
    ) {
    }

    private record QdrantResult(
            List<QdrantPoint> points
    ) {
    }

    private record QdrantPoint(
            Object id,
            Double score,
            Map<String, Object> payload
    ) {
    }
}