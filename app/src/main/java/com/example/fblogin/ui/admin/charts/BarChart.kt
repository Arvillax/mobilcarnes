package com.example.fblogin.ui.admin.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// grafico de barras verticales
@Composable
fun VerticalBarChart(
    data: List<BarData>,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "bar_anim"
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    val maxValue = data.maxOf { it.value }

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ventas por mes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barAreaHeight = canvasHeight - 40f
            val barWidth = (canvasWidth / data.size) * 0.5f
            val gap = (canvasWidth / data.size) * 0.5f

            // dibujar barras
            data.forEachIndexed { index, bar ->
                val barHeight = (bar.value / maxValue) * barAreaHeight * animatedProgress
                val x = index * (barWidth + gap) + gap / 2
                val y = barAreaHeight - barHeight

                // barra
                drawRect(
                    color = bar.color,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )

                // valor arriba
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    drawText(
                        "$${String.format("%.0f", bar.value)}",
                        x + barWidth / 2,
                        y - 8f,
                        paint
                    )
                }

                // label abajo
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 26f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText(
                        bar.label,
                        x + barWidth / 2,
                        canvasHeight - 4f,
                        paint
                    )
                }
            }
        }
    }
}

// grafico de barras horizontales
@Composable
fun HorizontalBarChart(
    data: List<BarData>,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "hbar_anim"
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    val maxValue = data.maxOf { it.value }

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        data.forEach { bar ->
            val progress = (bar.value / maxValue) * animatedProgress

            Text(
                text = bar.label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
            ) {
                // fondo
                drawRect(
                    color = Color(0xFFE0E0E0),
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height)
                )

                // barra
                drawRect(
                    color = bar.color,
                    topLeft = Offset.Zero,
                    size = Size(size.width * progress, size.height)
                )
            }

            Text(
                text = "${String.format("%.0f", bar.value)} kg",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
