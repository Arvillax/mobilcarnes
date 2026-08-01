package com.example.fblogin.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.fblogin.ui.admin.charts.BarData
import com.example.fblogin.ui.admin.charts.LineData
import com.example.fblogin.ui.admin.charts.PieData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// viewmodel para reportes admin — queries Firestore y transforma a tipos de grafico
class ReportesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // colores graficos
    private val palette = listOf(
        Color(0xFF610000), // Vino
        Color(0xFF9C0720), // Carmesi
        Color(0xFFDC143C), // Crimson
        Color(0xFFF1666D), // Coral
        Color(0xFFFF9EA2), // Rosa
        Color(0xFF9E9E9E), // Gray
        Color(0xFF2E7D32), // Green
        Color(0xFFF9A825), // Yellow
        Color(0xFFD32F2F)  // Red
    )
    private val monthNames = listOf(
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    )

    private var ventasRaw: List<Map<String, Any>> = emptyList()

    // === reportes ===

    private val _ventasPorMes = MutableStateFlow<List<BarData>>(emptyList())
    val ventasPorMes: StateFlow<List<BarData>> = _ventasPorMes

    private val _ventasPorProducto = MutableStateFlow<List<PieData>>(emptyList())
    val ventasPorProducto: StateFlow<List<PieData>> = _ventasPorProducto

    private val _tendenciaMensual = MutableStateFlow<List<LineData>>(emptyList())
    val tendenciaMensual: StateFlow<List<LineData>> = _tendenciaMensual

    private val _topProductos = MutableStateFlow<List<BarData>>(emptyList())
    val topProductos: StateFlow<List<BarData>> = _topProductos

    private val _inventarioActual = MutableStateFlow<List<BarData>>(emptyList())
    val inventarioActual: StateFlow<List<BarData>> = _inventarioActual

    private val _distribucionUsuarios = MutableStateFlow<List<PieData>>(emptyList())
    val distribucionUsuarios: StateFlow<List<PieData>> = _distribucionUsuarios

    init {
        recargar()
    }

    fun recargar() {
        cargarVentas()
        cargarProductos()
        cargarUsuarios()
    }

    // ==========================================
    // VENTAS: alimenta reportes 0, 1, 2, 3
    // ==========================================
    private fun cargarVentas() {
        db.collection("ventas").get()
            .addOnSuccessListener { result ->
                ventasRaw = result.mapNotNull { it.data }
                procesarVentasPorMes()
                procesarVentasPorProducto()
                procesarTendenciaMensual()
                procesarTopProductos()
            }
            .addOnFailureListener {
                _ventasPorMes.value = emptyList()
                _ventasPorProducto.value = emptyList()
                _tendenciaMensual.value = emptyList()
                _topProductos.value = emptyList()
            }
    }

    // reporte 0: ventas totales por mes (barras)
    private fun procesarVentasPorMes() {
        val porMes = mutableMapOf<Int, Double>()
        ventasRaw.forEach { venta ->
            val fecha = venta["fecha"] as? String ?: return@forEach
            val mes = extraerMes(fecha) ?: return@forEach
            val total = venta["total"] as? Double ?: 0.0
            porMes[mes] = (porMes[mes] ?: 0.0) + total
        }
        _ventasPorMes.value = porMes.toSortedMap().map { (mes, total) ->
            BarData(monthNames[mes - 1], total.toFloat(), palette[mes % palette.size])
        }
    }

    // reporte 1: ventas por producto — cantidad total vendida (donut)
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
        val sorted = porProducto.entries.sortedByDescending { it.value }
        _ventasPorProducto.value = sorted.mapIndexed { idx, (nombre, cant) ->
            PieData(nombre, cant.toFloat(), palette[idx % palette.size])
        }
    }

    // reporte 2: tendencia mensual (linea)
    private fun procesarTendenciaMensual() {
        val porMes = mutableMapOf<Int, Double>()
        ventasRaw.forEach { venta ->
            val fecha = venta["fecha"] as? String ?: return@forEach
            val mes = extraerMes(fecha) ?: return@forEach
            val total = venta["total"] as? Double ?: 0.0
            porMes[mes] = (porMes[mes] ?: 0.0) + total
        }
        _tendenciaMensual.value = porMes.toSortedMap().map { (mes, total) ->
            LineData(monthNames[mes - 1], total.toFloat())
        }
    }

    // reporte 3: top productos por frecuencia de venta (barras horizontales)
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
        _topProductos.value = conteo.entries.sortedByDescending { it.value }.mapIndexed { idx, (nombre, count) ->
            BarData(nombre, count.toFloat(), palette[idx % palette.size])
        }
    }

    // ==========================================
    // PRODUCTOS: reporte 4
    // ==========================================
    private fun cargarProductos() {
        db.collection("productos").get()
            .addOnSuccessListener { result ->
                _inventarioActual.value = result.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre") ?: return@mapNotNull null
                    val stock = (doc.getLong("stock") ?: 0).toInt()
                    val color = when {
                        stock > 30 -> Color(0xFF2E7D32)  // verde
                        stock in 15..30 -> Color(0xFFF9A825) // amarillo
                        else -> Color(0xFFD32F2F)  // rojo
                    }
                    BarData(nombre, stock.toFloat(), color)
                }.sortedByDescending { it.value }
            }
            .addOnFailureListener {
                _inventarioActual.value = emptyList()
            }
    }

    // ==========================================
    // USUARIOS: reporte 5
    // ==========================================
    private fun cargarUsuarios() {
        db.collection("usuarios").get()
            .addOnSuccessListener { result ->
                val porRol = mutableMapOf<String, Int>()
                result.documents.forEach { doc ->
                    val rol = doc.getString("rol") ?: "CLIENTE"
                    porRol[rol] = (porRol[rol] ?: 0) + 1
                }
                val labels = mapOf(
                    "ADMIN" to "Administradores",
                    "GESTOR" to "Gestores",
                    "CLIENTE" to "Clientes"
                )
                _distribucionUsuarios.value = porRol.entries.mapIndexed { idx, (rol, count) ->
                    PieData(labels[rol] ?: rol, count.toFloat(), palette[idx % palette.size])
                }
            }
            .addOnFailureListener {
                _distribucionUsuarios.value = emptyList()
            }
    }

    // ==========================================
    // HELPERS
    // ==========================================
    private fun extraerMes(fecha: String): Int? {
        return try {
            val parts = fecha.split("/")
            if (parts.size == 3) parts[1].toInt() else null
        } catch (e: Exception) {
            null
        }
    }
}
