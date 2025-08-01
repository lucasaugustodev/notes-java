package com.mycompany.app.service;

import com.mycompany.app.entity.Note;
import com.mycompany.app.entity.User;
import com.mycompany.app.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    /**
     * Get all notes for a user, ordered by updated date descending
     * @param user the user whose notes to retrieve
     * @return list of notes belonging to the user
     */
    public List<Note> getAllNotesByUser(User user) {
        return noteRepository.findByUserOrderByUpdatedAtDesc(user);
    }
    
    /**
     * Get a note by ID and user (ensures user can only access their own notes)
     * @param id the note ID
     * @param user the user who should own the note
     * @return the note if found and belongs to user
     * @throws RuntimeException if note not found or doesn't belong to user
     */
    public Note getNoteByIdAndUser(Long id, User user) {
        return noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found or access denied"));
    }
    
    /**
     * Save a note (create or update)
     * @param note the note to save
     * @return the saved note
     */
    public Note saveNote(Note note) {
        if (note.getTitle() == null || note.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Note title cannot be empty");
        }
        return noteRepository.save(note);
    }
    
    /**
     * Delete a note by ID and user (ensures user can only delete their own notes)
     * @param id the note ID
     * @param user the user who should own the note
     * @throws RuntimeException if note not found or doesn't belong to user
     */
    public void deleteNoteByIdAndUser(Long id, User user) {
        Note note = getNoteByIdAndUser(id, user);
        noteRepository.delete(note);
    }
    
    /**
     * Search notes by user and search term in both title and content
     * @param user the user whose notes to search
     * @param searchTerm the search term
     * @return list of matching notes
     */
    public List<Note> searchNotes(User user, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllNotesByUser(user);
        }
        return noteRepository.searchByUserAndTerm(user, searchTerm.trim());
    }
    
    /**
     * Search notes by title
     * @param user the user whose notes to search
     * @param title the search term for title
     * @return list of matching notes
     */
    public List<Note> searchNotesByTitle(User user, String title) {
        return noteRepository.findByUserAndTitleContainingIgnoreCase(user, title);
    }
    
    /**
     * Search notes by content
     * @param user the user whose notes to search
     * @param content the search term for content
     * @return list of matching notes
     */
    public List<Note> searchNotesByContent(User user, String content) {
        return noteRepository.findByUserAndContentContainingIgnoreCase(user, content);
    }
    
    /**
     * Count notes by user
     * @param user the user whose notes to count
     * @return number of notes belonging to the user
     */
    public long countNotesByUser(User user) {
        return noteRepository.countByUser(user);
    }
    
    /**
     * Create a new note for a user
     * @param title the note title
     * @param content the note content
     * @param user the user who owns the note
     * @return the created note
     */
    public Note createNote(String title, String content, User user) {
        Note note = new Note(title, content, user);
        return saveNote(note);
    }
    
    /**
     * Update an existing note
     * @param id the note ID
     * @param title the new title
     * @param content the new content
     * @param user the user who owns the note
     * @return the updated note
     * @throws RuntimeException if note not found or doesn't belong to user
     */
    public Note updateNote(Long id, String title, String content, User user) {
        Note note = getNoteByIdAndUser(id, user);
        note.setTitle(title);
        note.setContent(content);
        return saveNote(note);
    }
}
