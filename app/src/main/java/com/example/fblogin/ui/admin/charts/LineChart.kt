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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// grafico de lineas con area rellena
@Composable
fun AreaLineChart(
    data: List<LineData>,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "line_anim"
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    val maxValue = data.maxOf { it.value }

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tendencia mensual",
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
            val chartAreaHeight = canvasHeight - 40f
            val stepX = canvasWidth / (data.size - 1)

            // calcular puntos
            val points = data.mapIndexed { index, lineData ->
                val x = index * stepX
                val y = chartAreaHeight - (lineData.value / maxValue) * chartAreaHeight * animatedProgress
                Offset(x, y)
            }

            // dibujar area rellena
            val areaPath = Path().apply {
                moveTo(points.first().x, chartAreaHeight)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, chartAreaHeight)
                close()
            }

            drawPath(
                path = areaPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9C0720).copy(alpha = 0.4f),
                        Color(0xFF9C0720).copy(alpha = 0.05f)
                    )
                )
            )

            // dibujar linea
            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }

            drawPath(
                path = linePath,
                color = Color(0xFF9C0720),
                style = Stroke(width = 4f)
            )

            // dibujar puntos
            points.forEach { point ->
                drawCircle(
                    color = Color(0xFF9C0720),
                    radius = 8f,
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 4f,
                    center = point
                )
            }

            // labels eje X
            data.forEachIndexed { index, lineData ->
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 26f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText(
                        lineData.label,
                        index * stepX,
                        canvasHeight - 4f,
                        paint
                    )
                }
            }

            // valores arriba de cada punto
            points.forEachIndexed { index, point ->
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    drawText(
                        "$${String.format("%.0f", data[index].value)}",
                        point.x,
                        point.y - 14f,
                        paint
                    )
                }
            }
        }
    }
}
