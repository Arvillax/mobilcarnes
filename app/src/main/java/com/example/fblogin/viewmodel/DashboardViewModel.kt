package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// viewmodel para KPIs del dashboard admin
class DashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _totalUsuarios = MutableStateFlow(0)
    val totalUsuarios: StateFlow<Int> = _totalUsuarios

    private val _totalProductos = MutableStateFlow(0)
    val totalProductos: StateFlow<Int> = _totalProductos

    private val _ventasDelDia = MutableStateFlow(0.0)
    val ventasDelDia: StateFlow<Double> = _ventasDelDia

    init {
        recargar()
    }

    fun recargar() {
        contarUsuarios()
        contarProductos()
        sumarVentasDelDia()
    }

    private fun contarUsuarios() {
        db.collection("usuarios").get()
            .addOnSuccessListener { result ->
                _totalUsuarios.value = result.size()
            }
            .addOnFailureListener {
                _totalUsuarios.value = 0
            }
    }

    private fun contarProductos() {
        db.collection("productos").get()
            .addOnSuccessListener { result ->
                _totalProductos.value = result.size()
            }
            .addOnFailureListener {
                _totalProductos.value = 0
            }
    }

    private fun sumarVentasDelDia() {
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        db.collection("ventas").get()
            .addOnSuccessListener { result ->
                val suma = result.documents.sumOf { doc ->
                    val fecha = doc.getString("fecha") ?: ""
                    if (fecha == hoy) {
                        (doc.getDouble("total") ?: 0.0)
                    } else {
                        0.0
                    }
                }
                _ventasDelDia.value = suma
            }
            .addOnFailureListener {
                _ventasDelDia.value = 0.0
            }
    }
}
