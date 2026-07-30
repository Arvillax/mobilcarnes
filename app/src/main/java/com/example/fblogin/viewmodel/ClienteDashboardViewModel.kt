package com.example.fblogin.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.fblogin.ui.admin.charts.BarData
import com.example.fblogin.ui.admin.charts.LineData
import com.example.fblogin.ui.admin.charts.PieData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val ChartVino = Color(0xFF610000)
private val ChartCarmesi = Color(0xFF9C0720)
private val ChartCrimson = Color(0xFFDC143C)
private val ChartCoral = Color(0xFFF1666D)
private val ChartRosa = Color(0xFFFF9EA2)
private val ChartGreen = Color(0xFF2E7D32)

class ClienteDashboardViewModel : ViewModel() {

    // KPIs cliente
    private val _totalCompras = MutableStateFlow("12")
    val totalCompras: StateFlow<String> = _totalCompras.asStateFlow()

    private val _totalGastado = MutableStateFlow("$1,450")
    val totalGastado: StateFlow<String> = _totalGastado.asStateFlow()

    private val _productosComprados = MutableStateFlow("45 kg")
    val productosComprados: StateFlow<String> = _productosComprados.asStateFlow()

    private val _ultimaCompra = MutableStateFlow("25/07")
    val ultimaCompra: StateFlow<String> = _ultimaCompra.asStateFlow()

    // Graficos
    private val _historialGastos = MutableStateFlow(listOf(
        LineData("Mar", 350f),
        LineData("Abr", 280f),
        LineData("May", 420f),
        LineData("Jun", 390f),
        LineData("Jul", 510f)
    ))
    val historialGastos: StateFlow<List<LineData>> = _historialGastos.asStateFlow()

    private val _categoriasFavoritas = MutableStateFlow(listOf(
        PieData("Res", 45f, ChartVino),
        PieData("Cerdo", 25f, ChartCarmesi),
        PieData("Pollo", 20f, ChartCrimson),
        PieData("Embutidos", 10f, ChartCoral)
    ))
    val categoriasFavoritas: StateFlow<List<PieData>> = _categoriasFavoritas.asStateFlow()

    private val _productosFavoritos = MutableStateFlow(listOf(
        BarData("Costillas", 15f, ChartVino),
        BarData("Pechuga", 12f, ChartCarmesi),
        BarData("Punta Anca", 8f, ChartCrimson),
        BarData("Lomo", 6f, ChartCoral),
        BarData("Chorizo", 4f, ChartRosa)
    ))
    val productosFavoritos: StateFlow<List<BarData>> = _productosFavoritos.asStateFlow()

    private val _estadoMisPedidos = MutableStateFlow(listOf(
        PieData("Preparando", 1f, ChartVino),
        PieData("En camino", 1f, ChartCrimson),
        PieData("Entregados", 10f, ChartGreen)
    ))
    val estadoMisPedidos: StateFlow<List<PieData>> = _estadoMisPedidos.asStateFlow()
}
