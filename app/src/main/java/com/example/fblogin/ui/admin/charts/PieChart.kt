package com.example.fblogin.ui.admin.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// grafico donut (torta con hueco)
@Composable
fun DonutChart(
    data: List<PieData>,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "donut_anim"
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    val total = data.sumOf { it.value.toDouble() }.toFloat()

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // grafico donut
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 40f
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)

            var startAngle = -90f

            data.forEach { slice ->
                val sweepAngle = (slice.value / total) * 360f * animatedProgress

                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )

                startAngle += sweepAngle
            }

            // centro vacio
            drawCircle(
                color = Color.White,
                radius = radius - strokeWidth / 2,
                center = center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // leyenda debajo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            data.forEach { slice ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        drawCircle(color = slice.color)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = slice.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${String.format("%.1f", (slice.value / total) * 100)}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
