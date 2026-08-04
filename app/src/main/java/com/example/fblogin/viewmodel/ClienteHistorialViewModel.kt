package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.data.Venta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClienteHistorialViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _ventas = MutableStateFlow<List<Venta>>(emptyList())
    val ventas: StateFlow<List<Venta>> = _ventas.asStateFlow()

    private val _totalCompras = MutableStateFlow(0.0)
    val totalCompras: StateFlow<Double> = _totalCompras.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init {
        recargar()
    }

    fun recargar() {
        _cargando.value = true
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (email == null) {
            _ventas.value = emptyList()
            _totalCompras.value = 0.0
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
                _totalCompras.value = lista.sumOf { it.total }
                _cargando.value = false
            }
            .addOnFailureListener {
                _ventas.value = emptyList()
                _totalCompras.value = 0.0
                _cargando.value = false
            }
    }
}
