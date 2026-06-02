package com.example.instaproject.repository;

import com.example.instaproject.models.Message;
import com.example.instaproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(User user1, User user2);

    int countBySenderAndReceiverAndIsReadFalse(User sender, User receiver);
}
