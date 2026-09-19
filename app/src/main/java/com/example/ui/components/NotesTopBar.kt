package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.theme.NotesAccentTan
import com.example.ui.theme.NotesSurfaceContainer
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary

@Composable
fun NotesTopBar(
    currentTab: MainTab,
    selectedCategory: String,
    availableCategories: List<String>,
    showCategoryMenu: Boolean,
    onCategoryClick: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onDismissCategoryMenu: () -> Unit,
    onSortClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayTitle = if (currentTab == MainTab.NOTES) {
        if (selectedCategory == "All Notes") "Notes" else selectedCategory
    } else {
        "Lists"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title with dropdown chevron
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = NotesAccentTan),
                        onClick = onCategoryClick
                    )
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .testTag("top_bar_title_dropdown")
            ) {
                Text(
                    text = displayTitle,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = NotesTextPrimary
                )

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select category",
                    tint = NotesTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Category Dropdown
            DropdownMenu(
                expanded = showCategoryMenu,
                onDismissRequest = onDismissCategoryMenu,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                availableCategories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category,
                                    fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                                    color = if (category == selectedCategory) NotesAccentTan else NotesTextPrimary
                                )
                                if (category == selectedCategory) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = NotesAccentTan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        onClick = { onCategorySelected(category) }
                    )
                }
            }
        }

        // Action Icons on Right
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onSortClick,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("sort_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Sort notes",
                    tint = NotesTextPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = NotesTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
