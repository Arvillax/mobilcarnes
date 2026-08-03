package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.data.Venta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GestorVentasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _ventas = MutableStateFlow<List<Venta>>(emptyList())
    val ventas: StateFlow<List<Venta>> = _ventas.asStateFlow()

    private val _totalVentas = MutableStateFlow(0.0)
    val totalVentas: StateFlow<Double> = _totalVentas.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init {
        recargar()
    }

    fun recargar() {
        _cargando.value = true
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        db.collection("ventas").get()
            .addOnSuccessListener { result ->
                val ventasHoy = result.mapNotNull { doc ->
                    val fecha = doc.getString("fecha") ?: ""
                    if (fecha == hoy) {
                        Venta(
                            id = doc.id,
                            fecha = fecha,
                            total = doc.getDouble("total") ?: 0.0,
                            clienteEmail = doc.getString("clienteEmail") ?: "",
                            items = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                        )
                    } else null
                }.sortedByDescending { it.fecha }
                _ventas.value = ventasHoy
                _totalVentas.value = ventasHoy.sumOf { it.total }
                _cargando.value = false
            }
            .addOnFailureListener {
                _ventas.value = emptyList()
                _totalVentas.value = 0.0
                _cargando.value = false
            }
    }
}