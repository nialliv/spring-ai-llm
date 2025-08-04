package ru.artemev.springaillm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.artemev.springaillm.model.Chat;
import ru.artemev.springaillm.repo.ChatRepository;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public List<Chat> getAllChats() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow();
    }

    public Chat createNewChat(String title) {
        Chat chat = Chat.builder().title(title).build();
        return chatRepository.save(chat);
    }

    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }
}
