package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChecklistEntity
import com.example.data.ChecklistSerializer
import com.example.data.NoteEntity
import com.example.ui.theme.NotesAccentTan
import com.example.ui.theme.NotesSurfaceContainer
import com.example.ui.theme.NotesTextMuted
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary
import com.example.ui.theme.TagIdeas
import com.example.ui.theme.TagPersonal
import com.example.ui.theme.TagWork
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onPinClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(note.updatedAt))

    val tagColor = when (note.category) {
        "Personal" -> TagPersonal
        "Work" -> TagWork
        "Ideas" -> TagIdeas
        else -> NotesAccentTan.copy(alpha = 0.3f)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NotesSurfaceContainer),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("note_card_${note.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category badge
                if (note.category.isNotBlank() && note.category != "All Notes") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(tagColor.copy(alpha = 0.35f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = note.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = NotesTextPrimary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPinClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = if (note.isPinned) "Unpin note" else "Pin note",
                            tint = if (note.isPinned) NotesAccentTan else NotesTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete note",
                            tint = NotesTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (note.title.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NotesTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (note.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    color = NotesTextSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = formattedDate,
                fontSize = 11.sp,
                color = NotesTextMuted
            )
        }
    }
}

@Composable
fun ChecklistCard(
    checklist: ChecklistEntity,
    onClick: () -> Unit,
    onToggleItem: (String) -> Unit,
    onPinClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = ChecklistSerializer.deserialize(checklist.itemsJson)
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(checklist.updatedAt))

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NotesSurfaceContainer),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("checklist_card_${checklist.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = checklist.title.ifBlank { "Untitled List" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NotesTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPinClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (checklist.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = if (checklist.isPinned) "Unpin" else "Pin",
                            tint = if (checklist.isPinned) NotesAccentTan else NotesTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            tint = NotesTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show preview of up to 4 items
            items.take(4).forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleItem(item.id) }
                        .padding(vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = if (item.isChecked) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (item.isChecked) NotesAccentTan else NotesTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.text,
                        fontSize = 14.sp,
                        color = if (item.isChecked) NotesTextMuted else NotesTextSecondary,
                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (items.size > 4) {
                Text(
                    text = "+ ${items.size - 4} more items",
                    fontSize = 12.sp,
                    color = NotesAccentTan,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = formattedDate,
                fontSize = 11.sp,
                color = NotesTextMuted
            )
        }
    }
}
