package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NoteEntity
import com.example.ui.theme.NotesAccentTan
import com.example.ui.theme.NotesAccentTanDark
import com.example.ui.theme.NotesActivePill
import com.example.ui.theme.NotesBackground
import com.example.ui.theme.NotesSurfaceContainer
import com.example.ui.theme.NotesTextMuted
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteEditorScreen(
    initialNote: NoteEntity?,
    onSave: (id: Long, title: String, content: String, category: String) -> Unit,
    onDelete: ((NoteEntity) -> Unit)?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(initialNote?.title ?: "") }
    var content by remember { mutableStateOf(initialNote?.content ?: "") }
    var selectedCategory by remember { mutableStateOf(initialNote?.category ?: "Personal") }

    val categories = listOf("Personal", "Work", "Ideas")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NotesBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Top Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("note_editor_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NotesTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (initialNote != null && onDelete != null) {
                    IconButton(
                        onClick = { onDelete(initialNote) },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("note_editor_delete_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete note",
                            tint = NotesTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = {
                        if (title.isNotBlank() || content.isNotBlank()) {
                            onSave(
                                initialNote?.id ?: 0L,
                                title.ifBlank { "Untitled" },
                                content,
                                selectedCategory
                            )
                        } else {
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NotesAccentTan,
                        contentColor = NotesAccentTanDark
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("note_editor_save_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Save", fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Category Chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NotesAccentTan else NotesSurfaceContainer)
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NotesAccentTanDark else NotesTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title Input
            Box(modifier = Modifier.fillMaxWidth()) {
                if (title.isEmpty()) {
                    Text(
                        text = "Title",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = NotesTextMuted
                    )
                }
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        color = NotesTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(NotesAccentTan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_editor_title_input")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content Input
            Box(modifier = Modifier.fillMaxSize().weight(1f, fill = false)) {
                if (content.isEmpty()) {
                    Text(
                        text = "Note something down...",
                        fontSize = 17.sp,
                        color = NotesTextMuted,
                        lineHeight = 24.sp
                    )
                }
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = TextStyle(
                        color = NotesTextPrimary,
                        fontSize = 17.sp,
                        lineHeight = 24.sp
                    ),
                    cursorBrush = SolidColor(NotesAccentTan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .testTag("note_editor_content_input")
                )
            }
        }
    }
}
