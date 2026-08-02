package com.example.fblogin.ui.admin.charts

import androidx.compose.ui.graphics.Color

// modelos de datos para graficos
data class BarData(val label: String, val value: Float, val color: Color)
data class PieData(val label: String, val value: Float, val color: Color)
data class LineData(val label: String, val value: Float)

// ==========================================
// MOCK DATA — COMENTADO: datos ahora vienen de ReportesViewModel
// ==========================================

/*
// colores graficos
private val ChartVino = Color(0xFF610000)
private val ChartCarmesi = Color(0xFF9C0720)
private val ChartCrimson = Color(0xFFDC143C)
private val ChartCoral = Color(0xFFF1666D)
private val ChartRosa = Color(0xFFFF9EA2)
private val ChartGray = Color(0xFF9E9E9E)
private val ChartGreen = Color(0xFF2E7D32)
private val ChartYellow = Color(0xFFF9A825)
private val ChartRed = Color(0xFFD32F2F)

// ==========================================
// 1. VENTAS POR MES (barras verticales)
// ==========================================
val ventasPorMes = listOf(
    BarData("Ene", 8500f, ChartVino),
    BarData("Feb", 7200f, ChartCarmesi),
    BarData("Mar", 9800f, ChartCrimson),
    BarData("Abr", 6400f, ChartCoral),
    BarData("May", 11200f, ChartRosa),
    BarData("Jun", 10500f, ChartCarmesi)
)

// ==========================================
// 2. VENTAS POR PRODUCTO (donut)
// ==========================================
val ventasPorProducto = listOf(
    PieData("Costillas de Res", 35f, ChartVino),
    PieData("Pechuga de Pollo", 25f, ChartCrimson),
    PieData("Lomo de Cerdo", 18f, ChartCoral),
    PieData("Chuletas de Cordero", 12f, ChartRosa),
    PieData("Albóndigas Mixtas", 6f, ChartGray),
    PieData("Punta de Anca", 4f, ChartCarmesi)
)

// ==========================================
// 3. TENDENCIA MENSUAL (lineas)
// ==========================================
val tendenciaMensual = listOf(
    LineData("Ene", 7200f),
    LineData("Feb", 8100f),
    LineData("Mar", 7800f),
    LineData("Abr", 9500f),
    LineData("May", 10200f),
    LineData("Jun", 11800f)
)

// ==========================================
// 4. TOP PRODUCTOS (barras horizontales)
// ==========================================
val topProductos = listOf(
    BarData("Costillas de Res", 45f, ChartVino),
    BarData("Pechuga de Pollo", 38f, ChartCrimson),
    BarData("Lomo de Cerdo", 27f, ChartCoral),
    BarData("Punta de Anca", 19f, ChartCarmesi),
    BarData("Chuletas de Cordero", 14f, ChartRosa)
)

// ==========================================
// 5. INVENTARIO ACTUAL (barras horizontales con alertas)
// ==========================================
val inventarioActual = listOf(
    BarData("Costillas de Res", 50f, ChartGreen),
    BarData("Pechuga de Pollo", 100f, ChartGreen),
    BarData("Lomo de Cerdo", 30f, ChartYellow),
    BarData("Chuletas de Cordero", 15f, ChartRed),
    BarData("Albóndigas Mixtas", 80f, ChartGreen),
    BarData("Punta de Anca", 8f, ChartRed)
)

// ==========================================
// 6. DISTRIBUCION USUARIOS (donut)
// ==========================================
val distribucionUsuarios = listOf(
    PieData("Administradores", 3f, ChartVino),
    PieData("Gestores", 5f, ChartCarmesi),
    PieData("Clientes", 17f, ChartCrimson)
)
*/
