package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SortOption
import com.example.ui.theme.NotesAccentTan
import com.example.ui.theme.NotesSurfaceContainer
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary

@Composable
fun SortDialog(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NotesSurfaceContainer,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Sort by",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NotesTextPrimary
            )
        },
        text = {
            Column {
                SortOption.values().forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = NotesAccentTan,
                                unselectedColor = NotesTextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option.displayName,
                            color = if (option == selectedOption) NotesAccentTan else NotesTextPrimary,
                            fontWeight = if (option == selectedOption) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = NotesAccentTan)
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onClearData: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NotesSurfaceContainer,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NotesTextPrimary
            )
        },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = NotesAccentTan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Notes & Lists v1.0",
                            fontWeight = FontWeight.SemiBold,
                            color = NotesTextPrimary,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Material Design 3 Dark Theme",
                            color = NotesTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onClearData()
                            onDismiss()
                        })
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteForever,
                        contentDescription = null,
                        tint = NotesAccentTan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Reset / Clear All Notes & Lists",
                        color = NotesTextPrimary,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = NotesAccentTan)
            ) {
                Text("Done")
            }
        }
    )
}
