package com.divyansh.website;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime now);
    List<Story> findByAuthorAndExpiresAtAfter(String author, LocalDateTime now);
    void deleteByExpiresAtBefore(LocalDateTime now);
}