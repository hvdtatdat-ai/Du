package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NotesEmptyIconColor
import com.example.ui.theme.NotesTextPrimary
import com.example.ui.theme.NotesTextSecondary

/**
 * Custom Canvas icon matching the exact document with folded corner and "+" symbol
 * from the user's reference screenshot.
 */
@Composable
fun EmptyNotesDocumentIcon(
    modifier: Modifier = Modifier,
    color: Color = NotesEmptyIconColor
) {
    Canvas(modifier = modifier.size(width = 76.dp, height = 94.dp)) {
        val w = size.width
        val h = size.height
        val foldSize = w * 0.32f
        val strokeW = w * 0.085f

        // Document outline path with folded top-right corner
        val bodyPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w - foldSize, 0f)
            lineTo(w, foldSize)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }

        // Draw body outline
        drawPath(
            path = bodyPath,
            color = color,
            style = Stroke(width = strokeW)
        )

        // Draw the fold flap
        val foldPath = Path().apply {
            moveTo(w - foldSize, 0f)
            lineTo(w - foldSize, foldSize)
            lineTo(w, foldSize)
        }
        drawPath(
            path = foldPath,
            color = color,
            style = Stroke(width = strokeW)
        )

        // Draw bold "+" in center
        val centerX = w / 2f
        val centerY = h / 2f + foldSize * 0.15f
        val plusArm = w * 0.18f
        val plusThickness = strokeW * 1.05f

        // Horizontal bar of plus
        drawRoundRect(
            color = color,
            topLeft = Offset(centerX - plusArm, centerY - plusThickness / 2f),
            size = Size(plusArm * 2f, plusThickness),
            cornerRadius = CornerRadius(2f, 2f)
        )

        // Vertical bar of plus
        drawRoundRect(
            color = color,
            topLeft = Offset(centerX - plusThickness / 2f, centerY - plusArm),
            size = Size(plusThickness, plusArm * 2f),
            cornerRadius = CornerRadius(2f, 2f)
        )
    }
}

@Composable
fun EmptyNotesState(
    isSearch: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            EmptyNotesDocumentIcon()

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = if (isSearch) "No matching notes" else "No notes yet",
                color = NotesTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isSearch) "Try searching for something else" else "Tap \"+\" to create your first note",
                color = NotesTextSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyListsState(
    isSearch: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FactCheck,
                contentDescription = null,
                tint = NotesEmptyIconColor,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = if (isSearch) "No matching lists" else "No lists yet",
                color = NotesTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isSearch) "Try searching for something else" else "Tap \"+\" to create your first list",
                color = NotesTextSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}
