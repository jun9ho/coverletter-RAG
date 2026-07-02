package com.coverletter.coverletter_rag_api.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenAiEmbeddingService {

    private final RestClient openAiRestClient;

    @Value("${openai.embedding-model}")
    private String embeddingModel;

    public OpenAiEmbeddingService(@Qualifier("openAiRestClient") RestClient openAiRestClient) {
        this.openAiRestClient = openAiRestClient;
    }

    public List<Double> embed(String input) {
        OpenAiEmbeddingRequest request = new OpenAiEmbeddingRequest(
                embeddingModel,
                input
        );

        OpenAiEmbeddingResponse response = openAiRestClient.post()
                .uri("/embeddings")
                .body(request)
                .retrieve()
                .body(OpenAiEmbeddingResponse.class);

        if (response == null || response.data() == null || response.data().isEmpty()) {
            throw new IllegalStateException("OpenAI embedding 응답이 비어 있습니다.");
        }

        return response.data().get(0).embedding();
    }

    private record OpenAiEmbeddingRequest(
            String model,
            String input
    ) {
    }

    private record OpenAiEmbeddingResponse(
            List<EmbeddingData> data
    ) {
    }

    private record EmbeddingData(
            List<Double> embedding
    ) {
    }
}