// Notes App JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Auto-resize textarea
    const textareas = document.querySelectorAll('textarea');
    textareas.forEach(function(textarea) {
        autoResize(textarea);
        textarea.addEventListener('input', function() {
            autoResize(this);
        });
    });

    // Search form enhancement
    const searchForm = document.querySelector('form[action*="dashboard"]');
    if (searchForm) {
        const searchInput = searchForm.querySelector('input[name="search"]');
        if (searchInput) {
            // Clear search button
            if (searchInput.value) {
                addClearButton(searchInput);
            }
            
            searchInput.addEventListener('input', function() {
                if (this.value) {
                    addClearButton(this);
                } else {
                    removeClearButton(this);
                }
            });
        }
    }

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('button[onclick*="confirm"]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this note?')) {
                e.preventDefault();
                return false;
            }
        });
    });

    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach(function(card, index) {
        card.style.animationDelay = (index * 0.1) + 's';
        card.classList.add('fade-in');
    });

    // Character counter for note content
    const contentTextarea = document.querySelector('#content');
    if (contentTextarea) {
        addCharacterCounter(contentTextarea);
    }

    // Auto-save functionality (optional)
    const noteForm = document.querySelector('form[action*="/notes"]');
    if (noteForm && noteForm.querySelector('#title') && noteForm.querySelector('#content')) {
        enableAutoSave(noteForm);
    }
});

// Auto-resize textarea function
function autoResize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}

// Add clear button to search input
function addClearButton(input) {
    if (input.parentNode.querySelector('.clear-search')) return;
    
    const clearButton = document.createElement('button');
    clearButton.type = 'button';
    clearButton.className = 'btn btn-outline-secondary clear-search';
    clearButton.innerHTML = '<i class="fas fa-times"></i>';
    clearButton.style.position = 'absolute';
    clearButton.style.right = '45px';
    clearButton.style.top = '50%';
    clearButton.style.transform = 'translateY(-50%)';
    clearButton.style.zIndex = '10';
    clearButton.style.padding = '0.25rem 0.5rem';
    clearButton.style.fontSize = '0.75rem';
    
    input.parentNode.style.position = 'relative';
    input.parentNode.appendChild(clearButton);
    
    clearButton.addEventListener('click', function() {
        input.value = '';
        input.focus();
        removeClearButton(input);
    });
}

// Remove clear button from search input
function removeClearButton(input) {
    const clearButton = input.parentNode.querySelector('.clear-search');
    if (clearButton) {
        clearButton.remove();
    }
}

// Add character counter to textarea
function addCharacterCounter(textarea) {
    const counter = document.createElement('div');
    counter.className = 'text-muted small mt-1';
    counter.style.textAlign = 'right';
    
    const updateCounter = function() {
        const length = textarea.value.length;
        counter.textContent = length + ' characters';
        
        if (length > 5000) {
            counter.className = 'text-warning small mt-1';
        } else if (length > 10000) {
            counter.className = 'text-danger small mt-1';
        } else {
            counter.className = 'text-muted small mt-1';
        }
    };
    
    textarea.parentNode.appendChild(counter);
    updateCounter();
    
    textarea.addEventListener('input', updateCounter);
}

// Auto-save functionality
function enableAutoSave(form) {
    const titleInput = form.querySelector('#title');
    const contentTextarea = form.querySelector('#content');
    let autoSaveTimeout;
    
    const autoSave = function() {
        const title = titleInput.value.trim();
        const content = contentTextarea.value.trim();
        
        if (title || content) {
            // Save to localStorage
            const noteData = {
                title: title,
                content: content,
                timestamp: new Date().toISOString()
            };
            
            localStorage.setItem('notesapp_autosave', JSON.stringify(noteData));
            
            // Show auto-save indicator
            showAutoSaveIndicator();
        }
    };
    
    const handleInput = function() {
        clearTimeout(autoSaveTimeout);
        autoSaveTimeout = setTimeout(autoSave, 2000); // Auto-save after 2 seconds of inactivity
    };
    
    titleInput.addEventListener('input', handleInput);
    contentTextarea.addEventListener('input', handleInput);
    
    // Load auto-saved data on page load
    loadAutoSavedData(titleInput, contentTextarea);
    
    // Clear auto-save data on form submit
    form.addEventListener('submit', function() {
        localStorage.removeItem('notesapp_autosave');
    });
}

// Show auto-save indicator
function showAutoSaveIndicator() {
    let indicator = document.querySelector('.autosave-indicator');
    if (!indicator) {
        indicator = document.createElement('div');
        indicator.className = 'autosave-indicator alert alert-info py-1 px-2 small position-fixed';
        indicator.style.top = '20px';
        indicator.style.right = '20px';
        indicator.style.zIndex = '9999';
        indicator.innerHTML = '<i class="fas fa-save me-1"></i>Auto-saved';
        document.body.appendChild(indicator);
    }
    
    indicator.style.display = 'block';
    
    setTimeout(function() {
        indicator.style.display = 'none';
    }, 2000);
}

// Load auto-saved data
function loadAutoSavedData(titleInput, contentTextarea) {
    const savedData = localStorage.getItem('notesapp_autosave');
    if (savedData && !titleInput.value && !contentTextarea.value) {
        try {
            const data = JSON.parse(savedData);
            const savedTime = new Date(data.timestamp);
            const now = new Date();
            const hoursDiff = (now - savedTime) / (1000 * 60 * 60);
            
            // Only restore if saved within last 24 hours
            if (hoursDiff < 24) {
                if (confirm('Found auto-saved content from ' + savedTime.toLocaleString() + '. Would you like to restore it?')) {
                    titleInput.value = data.title || '';
                    contentTextarea.value = data.content || '';
                    autoResize(contentTextarea);
                }
            } else {
                // Remove old auto-save data
                localStorage.removeItem('notesapp_autosave');
            }
        } catch (e) {
            console.error('Error loading auto-saved data:', e);
            localStorage.removeItem('notesapp_autosave');
        }
    }
}

// Utility function to show loading state
function showLoading(button) {
    const originalText = button.innerHTML;
    button.innerHTML = '<span class="loading me-1"></span>Loading...';
    button.disabled = true;
    
    return function() {
        button.innerHTML = originalText;
        button.disabled = false;
    };
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl/Cmd + S to save (prevent default browser save)
    if ((e.ctrlKey || e.metaKey) && e.key === 's') {
        e.preventDefault();
        const saveButton = document.querySelector('button[type="submit"]');
        if (saveButton) {
            saveButton.click();
        }
    }
    
    // Ctrl/Cmd + N for new note
    if ((e.ctrlKey || e.metaKey) && e.key === 'n') {
        e.preventDefault();
        const newNoteLink = document.querySelector('a[href*="/notes/new"]');
        if (newNoteLink) {
            window.location.href = newNoteLink.href;
        }
    }
});
