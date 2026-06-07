package com.divyansh.website;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByPublishedTrueOrderByCreatedAtDesc();
    List<BlogPost> findByCategoryAndPublishedTrue(String category);
}
