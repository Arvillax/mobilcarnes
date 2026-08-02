package com.example.fblogin.data

import android.content.Context
import android.net.Uri
import com.example.fblogin.R
import com.example.fblogin.ui.admin.Producto
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

// repositorio de productos con Firestore + almacenamiento local
class ProductoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("productos")

    // copiar imagen a almacenamiento interno y devolver path
    fun copiarImagenLocal(context: Context, uri: Uri): String {
        val dir = File(context.filesDir, "productos")
        if (!dir.exists()) dir.mkdirs()

        val filename = "${UUID.randomUUID()}.jpg"
        val file = File(dir, filename)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    // copiar imagen de drawable a almacenamiento interno
    fun copiarDrawableLocal(context: Context, drawableRes: Int, nombre: String): String {
        val dir = File(context.filesDir, "productos")
        if (!dir.exists()) dir.mkdirs()

        val safeName = nombre.replace(" ", "_").lowercase()
        val file = File(dir, "${safeName}.jpg")

        if (file.exists()) return file.absolutePath

        context.resources.openRawResource(drawableRes).use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    // productos iniciales con imagenes locales
    fun seedProductos(context: Context, callback: (Boolean) -> Unit) {
        collection.get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                callback(false)
                return@addOnSuccessListener
            }
            // codigo basura
            val productos = listOf(
                Triple("Costillas de Res", 85.0, R.drawable.producto_costillas_de_res),
                Triple("Pechuga de Pollo", 45.0, R.drawable.producto_pechuga_pollo),
                Triple("Lomo de Cerdo", 75.0, R.drawable.producto_lomo),
                Triple("Bistec de Res", 70.0, R.drawable.producto_bistec_res),
                Triple("Alitas de Pollo", 40.0, R.drawable.producto_alitas_de_pollo),
                Triple("Beef Ribs", 95.0, R.drawable.producto_beef_ribs),
                Triple("Pierna de Pollo", 35.0, R.drawable.producto_pierna_pollo)
            )
            //  fin codigo basura
            var completados = 0
            productos.forEach { (nombre, precio, drawableRes) ->
                val path = copiarDrawableLocal(context, drawableRes, nombre)
                val data = hashMapOf(
                    "nombre" to nombre,
                    "precioKg" to precio,
                    "stock" to 50,
                    "descripcion" to "$nombre fresco de primera calidad",
                    "imagenRes" to null,
                    "imagenUri" to path,
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
    // COMENTADO: no se usa, la UI solo deshabilita productos
    /*
    fun eliminarProducto(id: String, callback: (Boolean) -> Unit) {
        collection.document(id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
    */
}
