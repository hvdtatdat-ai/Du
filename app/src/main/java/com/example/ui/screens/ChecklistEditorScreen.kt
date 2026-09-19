package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChecklistEntity
import com.example.data.ChecklistItem
import com.example.data.ChecklistSerializer
import com.example.ui.theme.NotesAccentTan
import com.example.ui.theme.NotesAccentTanDark
import com.example.ui.theme.NotesBackground
import com.example.ui.theme.NotesTextMuted
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary
import java.util.UUID

@Composable
fun ChecklistEditorScreen(
    initialChecklist: ChecklistEntity?,
    onSave: (id: Long, title: String, items: List<ChecklistItem>) -> Unit,
    onDelete: ((ChecklistEntity) -> Unit)?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(initialChecklist?.title ?: "") }
    val items = remember {
        mutableStateListOf<ChecklistItem>().apply {
            if (initialChecklist != null) {
                addAll(ChecklistSerializer.deserialize(initialChecklist.itemsJson))
            }
            if (isEmpty()) {
                add(ChecklistItem(UUID.randomUUID().toString(), "", false))
            }
        }
    }

    var newItemText by remember { mutableStateOf("") }

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
                    .testTag("checklist_editor_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NotesTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (initialChecklist != null && onDelete != null) {
                    IconButton(
                        onClick = { onDelete(initialChecklist) },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("checklist_editor_delete_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete list",
                            tint = NotesTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = {
                        val validItems = items.filter { it.text.isNotBlank() }
                        if (title.isNotBlank() || validItems.isNotEmpty()) {
                            onSave(
                                initialChecklist?.id ?: 0L,
                                title.ifBlank { "Checklist" },
                                validItems
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
                    modifier = Modifier.testTag("checklist_editor_save_button")
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Title
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (title.isEmpty()) {
                        Text(
                            text = "List title",
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
                            .testTag("checklist_editor_title_input")
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = {
                            items[index] = item.copy(isChecked = !item.isChecked)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (item.isChecked) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "Toggle",
                            tint = if (item.isChecked) NotesAccentTan else NotesTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = item.text,
                        onValueChange = { newText ->
                            items[index] = item.copy(text = newText)
                        },
                        textStyle = TextStyle(
                            color = if (item.isChecked) NotesTextMuted else NotesTextPrimary,
                            fontSize = 16.sp,
                            textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        cursorBrush = SolidColor(NotesAccentTan),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            items.add(ChecklistItem(UUID.randomUUID().toString(), "", false))
                        }),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { items.removeAt(index) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove item",
                            tint = NotesTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))

                // Add item button
                TextButton(
                    onClick = {
                        items.add(ChecklistItem(UUID.randomUUID().toString(), "", false))
                    },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = NotesAccentTan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add item",
                        color = NotesAccentTan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
