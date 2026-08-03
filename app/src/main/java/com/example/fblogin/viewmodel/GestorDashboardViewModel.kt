package com.example.fblogin.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.fblogin.ui.admin.charts.BarData
import com.example.fblogin.ui.admin.charts.LineData
import com.example.fblogin.ui.admin.charts.PieData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// colores graficos institucionales
private val ChartVino = Color(0xFF610000)
private val ChartCarmesi = Color(0xFF9C0720)
private val ChartCrimson = Color(0xFFDC143C)
private val ChartCoral = Color(0xFFF1666D)
private val ChartRosa = Color(0xFFFF9EA2)
private val ChartGreen = Color(0xFF2E7D32)
private val ChartYellow = Color(0xFFF9A825)
private val ChartRed = Color(0xFFD32F2F)

class GestorDashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // === KPIs operativos (conectados a Firestore) ===

    private val _totalStock = MutableStateFlow("0 kg")
    val totalStock: StateFlow<String> = _totalStock.asStateFlow()

    private val _ventasHoy = MutableStateFlow("$0")
    val ventasHoy: StateFlow<String> = _ventasHoy.asStateFlow()

    private val _stockCritico = MutableStateFlow("0")
    val stockCritico: StateFlow<String> = _stockCritico.asStateFlow()

    private val _pedidosHoy = MutableStateFlow("0")
    val pedidosHoy: StateFlow<String> = _pedidosHoy.asStateFlow()

    // === Datos para graficos (mock por ahora) ===

    private val _inventarioData = MutableStateFlow(listOf(
        BarData("Costillas", 50f, ChartGreen),
        BarData("Pollo", 100f, ChartGreen),
        BarData("Lomo Cerdo", 30f, ChartYellow),
        BarData("Cordero", 15f, ChartRed),
        BarData("Punta Anca", 8f, ChartRed)
    ).sortedBy { it.value })
    val inventarioData: StateFlow<List<BarData>> = _inventarioData.asStateFlow()

    private val _ventasSemanales = MutableStateFlow(listOf(
        LineData("Lun", 1200f),
        LineData("Mar", 1500f),
        LineData("Mie", 1100f),
        LineData("Jue", 1800f),
        LineData("Vie", 2200f),
        LineData("Sab", 2800f),
        LineData("Dom", 1900f)
    ))
    val ventasSemanales: StateFlow<List<LineData>> = _ventasSemanales.asStateFlow()

    private val _productosMasVendidos = MutableStateFlow(listOf(
        BarData("Costillas", 85f, ChartVino),
        BarData("Pollo", 72f, ChartCarmesi),
        BarData("Lomo", 64f, ChartCrimson),
        BarData("Punta Anca", 58f, ChartCoral),
        BarData("Chorizo", 45f, ChartRosa)
    ))
    val productosMasVendidos: StateFlow<List<BarData>> = _productosMasVendidos.asStateFlow()

    private val _estadoPedidos = MutableStateFlow(listOf(
        PieData("Pendientes", 8f, ChartVino),
        PieData("En proceso", 5f, ChartCrimson),
        PieData("Entregados", 22f, ChartGreen),
        PieData("Cancelados", 2f, ChartRed)
    ))
    val estadoPedidos: StateFlow<List<PieData>> = _estadoPedidos.asStateFlow()

    init {
        recargar()
    }

    fun recargar() {
        cargarVentasHoy()
        cargarStockCritico()
    }

    // ==========================================
    // KPI: Ventas del día (suma total)
    // ==========================================
    private fun cargarVentasHoy() {
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        db.collection("ventas").get()
            .addOnSuccessListener { result ->
                var suma = 0.0
                var count = 0
                result.documents.forEach { doc ->
                    val fecha = doc.getString("fecha") ?: ""
                    if (fecha.contains(hoy)) {
                        suma += doc.getDouble("total") ?: 0.0
                        count++
                    }
                }
                _ventasHoy.value = "$%,.0f".format(suma)
                _pedidosHoy.value = "$count"
            }
            .addOnFailureListener {
                _ventasHoy.value = "$0"
                _pedidosHoy.value = "0"
            }
    }

    // ==========================================
    // KPI: Stock total + Stock bajo (< 15)
    // ==========================================
    private fun cargarStockCritico() {
        db.collection("productos").get()
            .addOnSuccessListener { result ->
                var totalKg = 0
                var bajos = 0
                result.documents.forEach { doc ->
                    val stock = (doc.getLong("stock") ?: 0).toInt()
                    totalKg += stock
                    if (stock < 15) bajos++
                }
                _totalStock.value = "$totalKg kg"
                _stockCritico.value = "$bajos"
            }
            .addOnFailureListener {
                _totalStock.value = "0 kg"
                _stockCritico.value = "0"
            }
    }
}
