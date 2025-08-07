package ru.artemev.springaillm.services;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.artemev.springaillm.model.Chat;
import ru.artemev.springaillm.model.ChatEntry;
import ru.artemev.springaillm.model.Role;
import ru.artemev.springaillm.repo.ChatRepository;

import java.util.List;

import static ru.artemev.springaillm.model.Role.ASSISTANT;
import static ru.artemev.springaillm.model.Role.USER;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatService myProxy;

    public List<Chat> getAllChats() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow();
    }

    public Chat createNewChat(String title) {
        Chat chat = Chat.builder().title(title).build();
        chatRepository.save(chat);
        return chat;
    }

    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }

    @Transactional
    public void addChatEntry(Long chatId, String prompt, Role role) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chat.addChatEntry(ChatEntry.builder().content(prompt).role(role).build());
    }

    @Transactional
    public void proceedInteraction(Long chatId, String prompt) {
        myProxy.addChatEntry(chatId, prompt, USER);
        String answer = chatClient.prompt().user(prompt).call().content();
        myProxy.addChatEntry(chatId, answer, ASSISTANT);
    }

    public SseEmitter proceedInteractionWithStreaming(Long chatId, String prompt) {
        myProxy.addChatEntry(chatId, prompt, USER);

        StringBuilder answer = new StringBuilder();

        SseEmitter sseEmitter = new SseEmitter(0L);

        chatClient.prompt().user(prompt).stream()
                .chatResponse()
                .subscribe(response -> processToken(response, sseEmitter, answer),
                        sseEmitter::completeWithError,
                        () -> myProxy.addChatEntry(chatId, answer.toString(), ASSISTANT));

        return sseEmitter;
    }

    @SneakyThrows
    private static void processToken(ChatResponse response, SseEmitter sseEmitter, StringBuilder answer) {
        var token = response.getResult().getOutput();
        sseEmitter.send(token);
        answer.append(token.getText());
    }
}
