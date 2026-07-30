package com.example.fblogin.data

import android.content.Context
import android.net.Uri
import com.example.fblogin.R
import com.example.fblogin.ui.admin.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// repositorio de productos con Firestore + Storage
class ProductoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("productos")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference.child("productos")

    // subir imagen a Firebase Storage y devolver URL
    fun subirImagen(uri: Uri, callback: (String?) -> Unit) {
        val filename = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(filename)
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        callback(downloadUri.toString())
                    }
                    .addOnFailureListener {
                        callback(null)
                    }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    // productos iniciales con imagenes de drawable
    fun seedProductos(context: Context, callback: (Boolean) -> Unit) {
        collection.get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                callback(false) // ya hay productos, no hacer seed
                return@addOnSuccessListener
            }

            val productos = listOf(
                Triple("Costillas de Res", 85.0, R.drawable.producto_costillas_de_res),
                Triple("Pechuga de Pollo", 45.0, R.drawable.producto_pechuga_pollo),
                Triple("Lomo de Cerdo", 75.0, R.drawable.producto_lomo),
                Triple("Bistec de Res", 70.0, R.drawable.producto_bistec_res),
                Triple("Alitas de Pollo", 40.0, R.drawable.producto_alitas_de_pollo),
                Triple("Beef Ribs", 95.0, R.drawable.producto_beef_ribs),
                Triple("Pierna de Pollo", 35.0, R.drawable.producto_pierna_pollo)
            )

            var completados = 0
            productos.forEach { (nombre, precio, drawableRes) ->
                val uri = Uri.parse("android.resource://${context.packageName}/$drawableRes")
                subirImagen(uri) { url ->
                    val data = hashMapOf(
                        "nombre" to nombre,
                        "precioKg" to precio,
                        "stock" to 50,
                        "descripcion" to "$nombre fresco de primera calidad",
                        "imagenRes" to null,
                        "imagenUri" to url,
                        "habilitado" to true
                    )
                    collection.add(data)
                        .addOnSuccessListener {
                            completados++
                            if (completados == productos.size) callback(true)
                        }
                        .addOnFailureListener {
                            completados++
                            if (completados == productos.size) callback(true)
                        }
                }
            }
        }.addOnFailureListener {
            callback(false)
        }
    }

    // obtener todos los productos
    fun obtenerProductos(callback: (List<Producto>) -> Unit) {
        collection.get()
            .addOnSuccessListener { result ->
                val productos = result.mapNotNull { doc ->
                    doc.toObject(Producto::class.java)?.copy(id = doc.id)
                }
                callback(productos)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    // agregar producto
    fun agregarProducto(producto: Producto, callback: (Boolean) -> Unit) {
        val data = hashMapOf(
            "nombre" to producto.nombre,
            "precioKg" to producto.precioKg,
            "stock" to producto.stock,
            "descripcion" to producto.descripcion,
            "imagenRes" to producto.imagenRes,
            "imagenUri" to producto.imagenUri,
            "habilitado" to producto.habilitado
        )
        collection.add(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // editar producto
    fun editarProducto(id: String, producto: Producto, callback: (Boolean) -> Unit) {
        val data = hashMapOf(
            "nombre" to producto.nombre,
            "precioKg" to producto.precioKg,
            "stock" to producto.stock,
            "descripcion" to producto.descripcion,
            "imagenRes" to producto.imagenRes,
            "imagenUri" to producto.imagenUri,
            "habilitado" to producto.habilitado
        )
        collection.document(id).set(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // cambiar estado habilitado/deshabilitado
    fun cambiarEstado(id: String, habilitado: Boolean, callback: (Boolean) -> Unit) {
        collection.document(id).update("habilitado", habilitado)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // eliminar producto
    fun eliminarProducto(id: String, callback: (Boolean) -> Unit) {
        collection.document(id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}
