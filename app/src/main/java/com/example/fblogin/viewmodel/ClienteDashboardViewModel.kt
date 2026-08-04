package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.data.Venta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClienteDashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // === KPIs ===

    private val _totalCompras = MutableStateFlow("0")
    val totalCompras: StateFlow<String> = _totalCompras.asStateFlow()

    private val _totalGastado = MutableStateFlow("$0")
    val totalGastado: StateFlow<String> = _totalGastado.asStateFlow()

    private val _productosComprados = MutableStateFlow("0 kg")
    val productosComprados: StateFlow<String> = _productosComprados.asStateFlow()

    private val _ultimaCompra = MutableStateFlow("N/A")
    val ultimaCompra: StateFlow<String> = _ultimaCompra.asStateFlow()

    // === Historial ===

    private val _ventas = MutableStateFlow<List<Venta>>(emptyList())
    val ventas: StateFlow<List<Venta>> = _ventas.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init {
        recargar()
    }

    fun recargar() {
        _cargando.value = true
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (email == null) {
            _cargando.value = false
            return
        }
        db.collection("ventas")
            .whereEqualTo("clienteEmail", email)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    Venta(
                        id = doc.id,
                        fecha = doc.getString("fecha") ?: "",
                        total = doc.getDouble("total") ?: 0.0,
                        clienteEmail = doc.getString("clienteEmail") ?: "",
                        items = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                    )
                }.sortedByDescending { it.fecha }

                _ventas.value = lista

                // KPIs reales
                _totalCompras.value = "${lista.size}"
                _totalGastado.value = "$${String.format("%,.0f", lista.sumOf { it.total })}"

                val totalKg = lista.sumOf { venta ->
                    venta.items.sumOf { item ->
                        (item["cantidad"] as? Number)?.toDouble() ?: 0.0
                    }
                }
                _productosComprados.value = "${String.format("%.0f", totalKg)} kg"

                _ultimaCompra.value = lista.firstOrNull()?.fecha ?: "N/A"

                _cargando.value = false
            }
            .addOnFailureListener {
                _cargando.value = false
            }
    }
}
