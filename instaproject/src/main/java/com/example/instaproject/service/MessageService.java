package com.example.instaproject.service;

import com.example.instaproject.models.Message;
import com.example.instaproject.models.User;
import com.example.instaproject.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message sendMessage(User sender, User receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        return messageRepository.save(message);
    }

    @Transactional
    public List<Message> getConversation(User user1, User user2) {
        List<Message> messages = messageRepository.findConversation(user1, user2);
        messages.stream()
            .filter(m -> m.getReceiver().getId().equals(user1.getId()) && !m.isRead())
            .forEach(m -> {
                m.setRead(true);
                messageRepository.save(m);
            });
        return messages;
    }

    public int getUnreadCount(User sender, User receiver) {
        return messageRepository.countBySenderAndReceiverAndIsReadFalse(sender, receiver);
    }
}
