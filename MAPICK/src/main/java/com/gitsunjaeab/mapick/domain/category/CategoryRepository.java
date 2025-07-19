package com.gitsunjaeab.mapick.domain.category;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = """
    SELECT c.*
    FROM categories c
    JOIN (
        SELECT r.category_id
        FROM bookmarks b
        JOIN roadmaps r ON b.roadmap_id = r.id
        GROUP BY r.category_id
        ORDER BY COUNT(*) DESC
        LIMIT 5
    ) AS top_categories ON c.id = top_categories.category_id
""", nativeQuery = true)
    List<Category> findTop5Categories();
}
