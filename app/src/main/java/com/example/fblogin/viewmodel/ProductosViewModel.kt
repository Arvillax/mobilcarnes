package com.example.fblogin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fblogin.ui.admin.Producto
import com.example.fblogin.ui.admin.productosMock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// viewmodel de productos
class ProductosViewModel : ViewModel() {

    // estado de la lista
    private val _productos = MutableStateFlow(productosMock)
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // busqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // productos filtrados (solo habilitados por defecto + busqueda)
    val productosFiltrados: StateFlow<List<Producto>> = combine(
        _productos, _busqueda
    ) { productos, texto ->
        productos.filter { u ->
            val habilitado = u.habilitado
            val coincideBusqueda = texto.isEmpty() ||
                u.nombre.contains(texto, ignoreCase = true) ||
                u.descripcion.contains(texto, ignoreCase = true)
            habilitado && coincideBusqueda
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = productosMock.filter { it.habilitado }
    )

    // todos los productos (incluye deshabilitados para admin)
    val todosLosProductos: StateFlow<List<Producto>> = combine(
        _productos, _busqueda
    ) { productos, texto ->
        productos.filter { u ->
            texto.isEmpty() ||
                u.nombre.contains(texto, ignoreCase = true) ||
                u.descripcion.contains(texto, ignoreCase = true)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = productosMock
    )

    // buscar
    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    // siguiente ID
    private fun siguienteId(): String {
        val maxId = _productos.value.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
        return (maxId + 1).toString()
    }

    // agregar producto
    fun agregarProducto(
        nombre: String,
        precioKg: Double,
        stock: Int,
        descripcion: String,
        imagenRes: Int? = null,
        imagenUri: Uri? = null
    ) {
        val nuevo = Producto(
            id = siguienteId(),
            nombre = nombre,
            precioKg = precioKg,
            stock = stock,
            descripcion = descripcion,
            imagenRes = imagenRes,
            imagenUri = imagenUri?.toString(),
            habilitado = true
        )
        _productos.value = _productos.value + nuevo
    }

    // editar producto
    fun editarProducto(
        id: String,
        nombre: String,
        precioKg: Double,
        stock: Int,
        descripcion: String,
        imagenRes: Int? = null,
        imagenUri: Uri? = null
    ) {
        _productos.value = _productos.value.map { p ->
            if (p.id == id) p.copy(
                nombre = nombre,
                precioKg = precioKg,
                stock = stock,
                descripcion = descripcion,
                imagenRes = imagenRes,
                imagenUri = imagenUri?.toString()
            ) else p
        }
    }

    // deshabilitar producto
    fun deshabilitarProducto(id: String) {
        _productos.value = _productos.value.map { p ->
            if (p.id == id) p.copy(habilitado = false) else p
        }
    }

    // habilitar producto
    fun habilitarProducto(id: String) {
        _productos.value = _productos.value.map { p ->
            if (p.id == id) p.copy(habilitado = true) else p
        }
    }
}
