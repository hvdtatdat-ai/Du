package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainTab
import com.example.ui.NotesViewModel
import com.example.ui.components.BottomCapsuleNav
import com.example.ui.components.ChecklistCard
import com.example.ui.components.EmptyListsState
import com.example.ui.components.EmptyNotesState
import com.example.ui.components.NoteCard
import com.example.ui.components.NotesTopBar
import com.example.ui.components.SearchBarComponent
import com.example.ui.components.SettingsDialog
import com.example.ui.components.SortDialog
import com.example.ui.theme.NotesBackground

@Composable
fun MainNotesScreen(
    viewModel: NotesViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val checklists by viewModel.checklists.collectAsState()

    val editingNote by viewModel.editingNote.collectAsState()
    val isCreatingNote by viewModel.isCreatingNote.collectAsState()
    val editingChecklist by viewModel.editingChecklist.collectAsState()
    val isCreatingChecklist by viewModel.isCreatingChecklist.collectAsState()

    val showCategoryMenu by viewModel.showCategoryMenu.collectAsState()
    val showSortDialog by viewModel.showSortDialog.collectAsState()
    val showSettingsDialog by viewModel.showSettingsDialog.collectAsState()

    // Handle back button when editor is open
    if (editingNote != null || isCreatingNote) {
        BackHandler { viewModel.closeNoteEditor() }
        NoteEditorScreen(
            initialNote = editingNote,
            onSave = { id, title, content, category ->
                viewModel.saveNote(id, title, content, category)
            },
            onDelete = if (editingNote != null) { { viewModel.deleteNote(it) } } else null,
            onBack = { viewModel.closeNoteEditor() }
        )
        return
    }

    if (editingChecklist != null || isCreatingChecklist) {
        BackHandler { viewModel.closeChecklistEditor() }
        ChecklistEditorScreen(
            initialChecklist = editingChecklist,
            onSave = { id, title, items ->
                viewModel.saveChecklist(id, title, items)
            },
            onDelete = if (editingChecklist != null) { { viewModel.deleteChecklist(it) } } else null,
            onBack = { viewModel.closeChecklistEditor() }
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NotesBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            NotesTopBar(
                currentTab = currentTab,
                selectedCategory = selectedCategory,
                availableCategories = viewModel.availableCategories,
                showCategoryMenu = showCategoryMenu,
                onCategoryClick = { viewModel.toggleCategoryMenu() },
                onCategorySelected = { viewModel.setSelectedCategory(it) },
                onDismissCategoryMenu = { viewModel.dismissCategoryMenu() },
                onSortClick = { viewModel.openSortDialog() },
                onSettingsClick = { viewModel.openSettingsDialog() }
            )

            // Search Bar
            SearchBarComponent(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                placeholderText = if (currentTab == MainTab.NOTES) "Search notes..." else "Search lists..."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_content"
                ) { tab ->
                    when (tab) {
                        MainTab.NOTES -> {
                            if (notes.isEmpty()) {
                                EmptyNotesState(isSearch = searchQuery.isNotBlank())
                            } else {
                                LazyColumn(
                                    contentPadding = PaddingValues(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 8.dp,
                                        bottom = 120.dp
                                    ),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(notes, key = { it.id }) { note ->
                                        NoteCard(
                                            note = note,
                                            onClick = { viewModel.startEditingNote(note) },
                                            onPinClick = { viewModel.togglePinNote(note) },
                                            onDeleteClick = { viewModel.deleteNote(note) },
                                            modifier = Modifier.padding(vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        MainTab.LISTS -> {
                            if (checklists.isEmpty()) {
                                EmptyListsState(isSearch = searchQuery.isNotBlank())
                            } else {
                                LazyColumn(
                                    contentPadding = PaddingValues(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 8.dp,
                                        bottom = 120.dp
                                    ),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(checklists, key = { it.id }) { list ->
                                        ChecklistCard(
                                            checklist = list,
                                            onClick = { viewModel.startEditingChecklist(list) },
                                            onToggleItem = { itemId ->
                                                viewModel.toggleChecklistItem(list, itemId)
                                            },
                                            onPinClick = { viewModel.togglePinChecklist(list) },
                                            onDeleteClick = { viewModel.deleteChecklist(list) },
                                            modifier = Modifier.padding(vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Bottom Capsule Navigation
        BottomCapsuleNav(
            currentTab = currentTab,
            onTabSelected = { viewModel.setTab(it) },
            onFabClick = { viewModel.onFabClicked() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Dialogs
        if (showSortDialog) {
            SortDialog(
                selectedOption = sortOption,
                onOptionSelected = { viewModel.setSortOption(it) },
                onDismiss = { viewModel.dismissSortDialog() }
            )
        }

        if (showSettingsDialog) {
            SettingsDialog(
                onDismiss = { viewModel.dismissSettingsDialog() },
                onClearData = { viewModel.clearAllData() }
            )
        }
    }
}
