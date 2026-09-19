package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.theme.NotesAccentTan
import com.example.ui.theme.NotesAccentTanDark
import com.example.ui.theme.NotesActivePill
import com.example.ui.theme.NotesSurfaceContainer
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary

@Composable
fun BottomCapsuleNav(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = NotesSurfaceContainer,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Notes
                NavTabItem(
                    title = "Notes",
                    selected = currentTab == MainTab.NOTES,
                    icon = { tint ->
                        Icon(
                            imageVector = Icons.Default.Article,
                            contentDescription = "Notes",
                            tint = tint,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    testTag = "tab_notes",
                    onClick = { onTabSelected(MainTab.NOTES) }
                )

                // Center FAB Button
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(NotesAccentTan)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = NotesAccentTanDark),
                            role = Role.Button,
                            onClick = onFabClick
                        )
                        .testTag("fab_add_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new",
                        tint = NotesAccentTanDark,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Tab 2: Lists
                NavTabItem(
                    title = "Lists",
                    selected = currentTab == MainTab.LISTS,
                    icon = { tint ->
                        Icon(
                            imageVector = Icons.Outlined.FactCheck,
                            contentDescription = "Lists",
                            tint = tint,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    testTag = "tab_lists",
                    onClick = { onTabSelected(MainTab.LISTS) }
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    title: String,
    selected: Boolean,
    icon: @Composable (Color) -> Unit,
    testTag: String,
    onClick: () -> Unit
) {
    val pillBgColor = if (selected) NotesActivePill else Color.Transparent
    val iconTint = if (selected) NotesTextPrimary else NotesTextSecondary
    val textColor = if (selected) NotesAccentTan else NotesTextSecondary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        // Active indicator pill
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(pillBgColor),
            contentAlignment = Alignment.Center
        ) {
            icon(iconTint)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor
        )
    }
}
