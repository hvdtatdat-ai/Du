package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChecklistEntity
import com.example.data.ChecklistItem
import com.example.data.ChecklistSerializer
import com.example.data.NoteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    NOTES,
    LISTS
}

enum class SortOption(val displayName: String) {
    DATE_MODIFIED_DESC("Date modified (newest)"),
    DATE_MODIFIED_ASC("Date modified (oldest)"),
    TITLE_ASC("Title (A-Z)"),
    TITLE_DESC("Title (Z-A)")
}

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val notesDao = database.notesDao()
    private val checklistsDao = database.checklistsDao()

    private val _currentTab = MutableStateFlow(MainTab.NOTES)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All Notes")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DATE_MODIFIED_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    val availableCategories = listOf("All Notes", "Personal", "Work", "Ideas")

    // Filtered & Sorted Notes
    val notes: StateFlow<List<NoteEntity>> = combine(
        notesDao.getAllNotes(),
        _searchQuery,
        _selectedCategory,
        _sortOption
    ) { allNotes, query, category, sort ->
        allNotes
            .filter { note ->
                val matchesCategory = (category == "All Notes" || note.category == category)
                val matchesQuery = query.isBlank() ||
                        note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
                matchesCategory && matchesQuery
            }
            .sortedWith { a, b ->
                // Pinned notes always come first
                if (a.isPinned != b.isPinned) {
                    if (a.isPinned) -1 else 1
                } else {
                    when (sort) {
                        SortOption.DATE_MODIFIED_DESC -> b.updatedAt.compareTo(a.updatedAt)
                        SortOption.DATE_MODIFIED_ASC -> a.updatedAt.compareTo(b.updatedAt)
                        SortOption.TITLE_ASC -> a.title.compareTo(b.title, ignoreCase = true)
                        SortOption.TITLE_DESC -> b.title.compareTo(a.title, ignoreCase = true)
                    }
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered & Sorted Checklists
    val checklists: StateFlow<List<ChecklistEntity>> = combine(
        checklistsDao.getAllChecklists(),
        _searchQuery,
        _sortOption
    ) { allLists, query, sort ->
        allLists
            .filter { list ->
                query.isBlank() ||
                        list.title.contains(query, ignoreCase = true) ||
                        list.itemsJson.contains(query, ignoreCase = true)
            }
            .sortedWith { a, b ->
                if (a.isPinned != b.isPinned) {
                    if (a.isPinned) -1 else 1
                } else {
                    when (sort) {
                        SortOption.DATE_MODIFIED_DESC -> b.updatedAt.compareTo(a.updatedAt)
                        SortOption.DATE_MODIFIED_ASC -> a.updatedAt.compareTo(b.updatedAt)
                        SortOption.TITLE_ASC -> a.title.compareTo(b.title, ignoreCase = true)
                        SortOption.TITLE_DESC -> b.title.compareTo(a.title, ignoreCase = true)
                    }
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialog & Screen states
    private val _editingNote = MutableStateFlow<NoteEntity?>(null)
    val editingNote: StateFlow<NoteEntity?> = _editingNote.asStateFlow()

    private val _isCreatingNote = MutableStateFlow(false)
    val isCreatingNote: StateFlow<Boolean> = _isCreatingNote.asStateFlow()

    private val _editingChecklist = MutableStateFlow<ChecklistEntity?>(null)
    val editingChecklist: StateFlow<ChecklistEntity?> = _editingChecklist.asStateFlow()

    private val _isCreatingChecklist = MutableStateFlow(false)
    val isCreatingChecklist: StateFlow<Boolean> = _isCreatingChecklist.asStateFlow()

    private val _showCategoryMenu = MutableStateFlow(false)
    val showCategoryMenu: StateFlow<Boolean> = _showCategoryMenu.asStateFlow()

    private val _showSortDialog = MutableStateFlow(false)
    val showSortDialog: StateFlow<Boolean> = _showSortDialog.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        _showCategoryMenu.value = false
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _showSortDialog.value = false
    }

    fun toggleCategoryMenu() {
        _showCategoryMenu.value = !_showCategoryMenu.value
    }

    fun dismissCategoryMenu() {
        _showCategoryMenu.value = false
    }

    fun openSortDialog() {
        _showSortDialog.value = true
    }

    fun dismissSortDialog() {
        _showSortDialog.value = false
    }

    fun openSettingsDialog() {
        _showSettingsDialog.value = true
    }

    fun dismissSettingsDialog() {
        _showSettingsDialog.value = false
    }

    // Actions on FAB '+' click
    fun onFabClicked() {
        if (_currentTab.value == MainTab.NOTES) {
            _isCreatingNote.value = true
        } else {
            _isCreatingChecklist.value = true
        }
    }

    // Note operations
    fun startEditingNote(note: NoteEntity) {
        _editingNote.value = note
    }

    fun closeNoteEditor() {
        _isCreatingNote.value = false
        _editingNote.value = null
    }

    fun saveNote(id: Long = 0, title: String, content: String, category: String) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (id == 0L) {
                notesDao.insertNote(
                    NoteEntity(
                        title = title.trim(),
                        content = content.trim(),
                        category = category,
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                val existing = notesDao.getNoteById(id)
                if (existing != null) {
                    notesDao.updateNote(
                        existing.copy(
                            title = title.trim(),
                            content = content.trim(),
                            category = category,
                            updatedAt = now
                        )
                    )
                }
            }
            closeNoteEditor()
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notesDao.deleteNote(note)
            if (_editingNote.value?.id == note.id) {
                closeNoteEditor()
            }
        }
    }

    fun togglePinNote(note: NoteEntity) {
        viewModelScope.launch {
            notesDao.updateNote(note.copy(isPinned = !note.isPinned, updatedAt = System.currentTimeMillis()))
        }
    }

    // Checklist operations
    fun startEditingChecklist(checklist: ChecklistEntity) {
        _editingChecklist.value = checklist
    }

    fun closeChecklistEditor() {
        _isCreatingChecklist.value = false
        _editingChecklist.value = null
    }

    fun saveChecklist(id: Long = 0, title: String, items: List<ChecklistItem>) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val itemsJson = ChecklistSerializer.serialize(items)
            if (id == 0L) {
                checklistsDao.insertChecklist(
                    ChecklistEntity(
                        title = title.trim(),
                        itemsJson = itemsJson,
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                val existing = checklistsDao.getChecklistById(id)
                if (existing != null) {
                    checklistsDao.updateChecklist(
                        existing.copy(
                            title = title.trim(),
                            itemsJson = itemsJson,
                            updatedAt = now
                        )
                    )
                }
            }
            closeChecklistEditor()
        }
    }

    fun deleteChecklist(checklist: ChecklistEntity) {
        viewModelScope.launch {
            checklistsDao.deleteChecklist(checklist)
            if (_editingChecklist.value?.id == checklist.id) {
                closeChecklistEditor()
            }
        }
    }

    fun togglePinChecklist(checklist: ChecklistEntity) {
        viewModelScope.launch {
            checklistsDao.updateChecklist(
                checklist.copy(isPinned = !checklist.isPinned, updatedAt = System.currentTimeMillis())
            )
        }
    }

    fun toggleChecklistItem(checklist: ChecklistEntity, itemId: String) {
        viewModelScope.launch {
            val items = ChecklistSerializer.deserialize(checklist.itemsJson).toMutableList()
            val index = items.indexOfFirst { it.id == itemId }
            if (index != -1) {
                items[index] = items[index].copy(isChecked = !items[index].isChecked)
                checklistsDao.updateChecklist(
                    checklist.copy(
                        itemsJson = ChecklistSerializer.serialize(items),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            notesDao.deleteAllNotes()
            checklistsDao.deleteAllChecklists()
        }
    }
}
