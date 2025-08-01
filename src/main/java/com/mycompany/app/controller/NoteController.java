package com.mycompany.app.controller;

import com.mycompany.app.entity.Note;
import com.mycompany.app.entity.User;
import com.mycompany.app.security.CustomUserDetails;
import com.mycompany.app.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestParam(value = "search", required = false) String search,
                           Model model) {
        
        User user = userDetails.getUser();
        List<Note> notes;
        
        if (search != null && !search.trim().isEmpty()) {
            notes = noteService.searchNotes(user, search.trim());
            model.addAttribute("search", search);
        } else {
            notes = noteService.getAllNotesByUser(user);
        }
        
        model.addAttribute("notes", notes);
        model.addAttribute("user", user);
        model.addAttribute("noteCount", notes.size());
        
        return "notes/dashboard";
    }
    
    @GetMapping("/notes/new")
    public String newNotePage(Model model) {
        model.addAttribute("note", new Note());
        return "notes/form";
    }
    
    @PostMapping("/notes")
    public String createNote(@Valid @ModelAttribute("note") Note note,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "notes/form";
        }
        
        User user = userDetails.getUser();
        note.setUser(user);
        
        try {
            noteService.saveNote(note);
            redirectAttributes.addFlashAttribute("message", "Note created successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating note: " + e.getMessage());
            return "redirect:/notes/new";
        }
    }
    
    @GetMapping("/notes/{id}")
    public String viewNote(@PathVariable Long id,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        
        User user = userDetails.getUser();
        
        try {
            Note note = noteService.getNoteByIdAndUser(id, user);
            model.addAttribute("note", note);
            return "notes/view";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dashboard";
        }
    }
    
    @GetMapping("/notes/{id}/edit")
    public String editNotePage(@PathVariable Long id,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        User user = userDetails.getUser();
        
        try {
            Note note = noteService.getNoteByIdAndUser(id, user);
            model.addAttribute("note", note);
            return "notes/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dashboard";
        }
    }
    
    @PostMapping("/notes/{id}")
    public String updateNote(@PathVariable Long id,
                            @Valid @ModelAttribute("note") Note note,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "notes/form";
        }
        
        User user = userDetails.getUser();
        
        try {
            Note existingNote = noteService.getNoteByIdAndUser(id, user);
            existingNote.setTitle(note.getTitle());
            existingNote.setContent(note.getContent());
            
            noteService.saveNote(existingNote);
            redirectAttributes.addFlashAttribute("message", "Note updated successfully!");
            return "redirect:/notes/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dashboard";
        }
    }
    
    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable Long id,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        
        User user = userDetails.getUser();
        
        try {
            noteService.deleteNoteByIdAndUser(id, user);
            redirectAttributes.addFlashAttribute("message", "Note deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/dashboard";
    }
}
