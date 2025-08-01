package com.mycompany.app.repository;

import com.mycompany.app.entity.Note;
import com.mycompany.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    /**
     * Find all notes by user, ordered by updated date descending
     * @param user the user whose notes to retrieve
     * @return list of notes belonging to the user
     */
    List<Note> findByUserOrderByUpdatedAtDesc(User user);
    
    /**
     * Find all notes by user ID, ordered by updated date descending
     * @param userId the user ID whose notes to retrieve
     * @return list of notes belonging to the user
     */
    List<Note> findByUserIdOrderByUpdatedAtDesc(Long userId);
    
    /**
     * Find a note by ID and user (for security - ensures user can only access their own notes)
     * @param id the note ID
     * @param user the user who should own the note
     * @return Optional containing the note if found and belongs to user
     */
    Optional<Note> findByIdAndUser(Long id, User user);
    
    /**
     * Find notes by user and title containing search term (case insensitive)
     * @param user the user whose notes to search
     * @param title the search term for title
     * @return list of matching notes
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY n.updatedAt DESC")
    List<Note> findByUserAndTitleContainingIgnoreCase(@Param("user") User user, @Param("title") String title);
    
    /**
     * Find notes by user and content containing search term (case insensitive)
     * @param user the user whose notes to search
     * @param content the search term for content
     * @return list of matching notes
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND LOWER(n.content) LIKE LOWER(CONCAT('%', :content, '%')) ORDER BY n.updatedAt DESC")
    List<Note> findByUserAndContentContainingIgnoreCase(@Param("user") User user, @Param("content") String content);
    
    /**
     * Search notes by user and search term in both title and content
     * @param user the user whose notes to search
     * @param searchTerm the search term
     * @return list of matching notes
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY n.updatedAt DESC")
    List<Note> searchByUserAndTerm(@Param("user") User user, @Param("searchTerm") String searchTerm);
    
    /**
     * Count notes by user
     * @param user the user whose notes to count
     * @return number of notes belonging to the user
     */
    long countByUser(User user);
}
