package com.example.fblogin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fblogin.ui.theme.Crimson
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Rosa

// card estadistica reutilizable
@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Rosa.copy(alpha = 0.2f),
    contentColor: Color = Crimson
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

// titulo de seccion para graficos
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Vino,
        modifier = modifier.padding(vertical = 8.dp)
    )
}
