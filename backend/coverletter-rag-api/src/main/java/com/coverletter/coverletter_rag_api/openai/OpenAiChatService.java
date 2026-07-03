package com.coverletter.coverletter_rag_api.openai;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenAiChatService {

    private final RestClient openAiRestClient;

    @Value("${openai.chat-model}")
    private String chatModel;

    public OpenAiChatService(@Qualifier("openAiRestClient") RestClient openAiRestClient) {
        this.openAiRestClient = openAiRestClient;
    }

    public String generate(String systemPrompt, String userPrompt) {
        ChatRequest request = new ChatRequest(
                chatModel,
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userPrompt)
                ),
                0.4
        );

        ChatResponse response = openAiRestClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(ChatResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new IllegalStateException("OpenAI Chat 응답이 비어 있습니다.");
        }

        Message message = response.choices().get(0).message();

        if (message == null || message.content() == null || message.content().isBlank()) {
            throw new IllegalStateException("OpenAI Chat 응답 content가 비어 있습니다.");
        }

        return message.content();
    }

    private record ChatRequest(
            String model,
            List<Message> messages,
            Double temperature
    ) {
    }

    private record Message(
            String role,
            String content
    ) {
    }

    private record ChatResponse(
            List<Choice> choices
    ) {
    }

    private record Choice(
            Message message
    ) {
    }
}