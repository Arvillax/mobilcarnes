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
import java.util.Calendar
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
private val ChartGray = Color(0xFF9E9E9E)

private val palette = listOf(
    Color(0xFF610000), Color(0xFF9C0720), Color(0xFFDC143C),
    Color(0xFFF1666D), Color(0xFFFF9EA2), Color(0xFF9E9E9E),
    Color(0xFF2E7D32), Color(0xFFF9A825), Color(0xFFD32F2F)
)

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

    // === Datos para graficos (Firestore) ===

    private val _inventarioData = MutableStateFlow<List<BarData>>(emptyList())
    val inventarioData: StateFlow<List<BarData>> = _inventarioData.asStateFlow()

    private val _ventasSemanales = MutableStateFlow<List<LineData>>(emptyList())
    val ventasSemanales: StateFlow<List<LineData>> = _ventasSemanales.asStateFlow()

    private val _productosMasVendidos = MutableStateFlow<List<BarData>>(emptyList())
    val productosMasVendidos: StateFlow<List<BarData>> = _productosMasVendidos.asStateFlow()

    private val _ventasPorProducto = MutableStateFlow<List<PieData>>(emptyList())
    val ventasPorProducto: StateFlow<List<PieData>> = _ventasPorProducto.asStateFlow()

    // cache de ventas raw para reutilizar en graficos 2, 3, 4
    private var ventasRaw: List<Map<String, Any>> = emptyList()

    init {
        recargar()
    }

    fun recargar() {
        cargarVentasHoy()
        cargarStockCritico()
        cargarInventario()
        cargarVentasParaGraficos()
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

    // ==========================================
    // GRAFICO 1: Inventario actual (barras horizontales)
    // ==========================================
    private fun cargarInventario() {
        db.collection("productos").get()
            .addOnSuccessListener { result ->
                _inventarioData.value = result.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre") ?: return@mapNotNull null
                    val stock = (doc.getLong("stock") ?: 0).toInt()
                    val color = when {
                        stock > 30 -> ChartGreen
                        stock in 15..30 -> ChartYellow
                        else -> ChartRed
                    }
                    BarData(nombre, stock.toFloat(), color)
                }.sortedByDescending { it.value }
            }
            .addOnFailureListener {
                _inventarioData.value = emptyList()
            }
    }

    // ==========================================
    // GRAFICOS 2, 3, 4: Una sola query de ventas
    // ==========================================
    private fun cargarVentasParaGraficos() {
        db.collection("ventas").get()
            .addOnSuccessListener { result ->
                ventasRaw = result.mapNotNull { it.data }
                procesarVentasSemanales()
                procesarTopProductos()
                procesarVentasPorProducto()
            }
            .addOnFailureListener {
                _ventasSemanales.value = emptyList()
                _productosMasVendidos.value = emptyList()
                _ventasPorProducto.value = emptyList()
            }
    }

    // GRAFICO 2: Ventas últimos 7 días (línea/área)
    private fun procesarVentasSemanales() {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val diasSemana = listOf("Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab")

        // generar fechas de los últimos 7 días
        val fechasUtiles = mutableMapOf<String, String>() // "dd/MM" -> "nombreDia"
        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val fechaStr = sdf.format(cal.time)
            val diaSemana = diasSemana[cal.get(Calendar.DAY_OF_WEEK) - 1]
            val clave = fechaStr.substring(0, 5) // "dd/MM"
            fechasUtiles[fechaStr] = diaSemana
        }

        // agrupar ventas por día
        val porDia = mutableMapOf<String, Double>()
        ventasRaw.forEach { venta ->
            val fecha = venta["fecha"] as? String ?: return@forEach
            val fechaBase = fecha.take(10) // "dd/MM/yyyy"
            if (fechasUtiles.containsKey(fechaBase)) {
                val total = venta["total"] as? Double ?: 0.0
                val dia = fechasUtiles[fechaBase] ?: return@forEach
                porDia[dia] = (porDia[dia] ?: 0.0) + total
            }
        }

        // mantener orden cronológico
        val ordenDias = listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")
        _ventasSemanales.value = ordenDias.map { dia ->
            LineData(dia, (porDia[dia] ?: 0.0).toFloat())
        }
    }

    // GRAFICO 3: Top productos vendidos (barras verticales) — por frecuencia
    private fun procesarTopProductos() {
        val conteo = mutableMapOf<String, Int>()
        ventasRaw.forEach { venta ->
            @Suppress("UNCHECKED_CAST")
            val items = venta["items"] as? List<Map<String, Any>> ?: emptyList()
            items.forEach { item ->
                val nombre = item["nombre"] as? String ?: return@forEach
                conteo[nombre] = (conteo[nombre] ?: 0) + 1
            }
        }
        _productosMasVendidos.value = conteo.entries
            .sortedByDescending { it.value }
            .take(5)
            .mapIndexed { idx, (nombre, count) ->
                BarData(nombre, count.toFloat(), palette[idx % palette.size])
            }
    }

    // GRAFICO 4: Ventas por producto (dona) — cantidad total vendida
    private fun procesarVentasPorProducto() {
        val porProducto = mutableMapOf<String, Double>()
        ventasRaw.forEach { venta ->
            @Suppress("UNCHECKED_CAST")
            val items = venta["items"] as? List<Map<String, Any>> ?: emptyList()
            items.forEach { item ->
                val nombre = item["nombre"] as? String ?: return@forEach
                val cantidad = (item["cantidad"] as? Number)?.toDouble() ?: 0.0
                porProducto[nombre] = (porProducto[nombre] ?: 0.0) + cantidad
            }
        }
        _ventasPorProducto.value = porProducto.entries
            .sortedByDescending { it.value }
            .mapIndexed { idx, (nombre, cant) ->
                PieData(nombre, cant.toFloat(), palette[idx % palette.size])
            }
    }
}
