package com.example.fblogin.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

// modelo venta
data class Venta(
    val id: String = "",
    val fecha: String = "",
    val total: Double = 0.0,
    val clienteEmail: String = "",
    val items: List<Map<String, Any>> = emptyList()
)

// repositorio de ventas con Firestore
class VentaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("ventas")

    // guardar venta
    fun guardarVenta(venta: Venta, callback: (Boolean) -> Unit) {
        val data = hashMapOf(
            "fecha" to venta.fecha,
            "total" to venta.total,
            "clienteEmail" to venta.clienteEmail,
            "items" to venta.items
        )
        collection.add(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // descontar stock de productos vendidos (batch atomico)
    fun descontarStock(items: List<Map<String, Any>>, callback: (Boolean) -> Unit) {
        val batch = db.batch()
        items.forEach { item ->
            val productoId = item["productoId"] as? String ?: return@forEach
            val cantidad = (item["cantidad"] as? Number)?.toDouble() ?: 0.0
            val ref = db.collection("productos").document(productoId)
            batch.update(ref, "stock", FieldValue.increment(-cantidad.toLong()))
        }
        batch.commit()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // obtener todas las ventas
    // COMENTADO: no se usa, los reportes usan ReportesViewModel directamente
    /*
    fun obtenerVentas(callback: (List<Venta>) -> Unit) {
        collection.orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val ventas = result.mapNotNull { doc ->
                    doc.toObject(Venta::class.java)?.copy(id = doc.id)
                }
                callback(ventas)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
    */
}
