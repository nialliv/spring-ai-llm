package ru.artemev.springaillm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artemev.springaillm.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
