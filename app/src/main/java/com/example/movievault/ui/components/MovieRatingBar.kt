package com.example.movievault.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movievault.ui.theme.StarGold

/**
 * Componente reutilizable para mostrar la puntuación con 5 estrellas doradas.
 */
@Composable
fun MovieRatingBar(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < 4) StarGold else StarGold.copy(alpha = 0.3f), // Simulación de estrellas
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = "$rating / 10",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = StarGold,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}
