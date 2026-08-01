package com.example.fblogin.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fblogin.data.ProductoRepository
import com.example.fblogin.ui.admin.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// viewmodel de productos con Firestore + seed automatico
class ProductosViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ProductoRepository()
    private val context = application

    // estado de la lista
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // busqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // cargar productos desde Firestore + seed si vacio
    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        repo.obtenerProductos { lista ->
            if (lista.isEmpty()) {
                // Firestore vacio, hacer seed con productos iniciales
                repo.seedProductos(context) { _ ->
                    // despues del seed, recargar
                    repo.obtenerProductos { listaFinal ->
                        _productos.value = listaFinal
                    }
                }
            } else {
                _productos.value = lista
            }
        }
    }

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
        initialValue = emptyList()
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
        initialValue = emptyList()
    )

    // buscar
    fun buscar(texto: String) {
        _busqueda.value = texto
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
        // si hay imagen de galeria, copiar a almacenamiento local
        val pathLocal = if (imagenUri != null) {
            repo.copiarImagenLocal(context, imagenUri)
        } else {
            null
        }

        val nuevo = Producto(
            id = "",
            nombre = nombre,
            precioKg = precioKg,
            stock = stock,
            descripcion = descripcion,
            imagenRes = null,
            imagenUri = pathLocal,
            habilitado = true
        )
        repo.agregarProducto(nuevo) { exito ->
            if (exito) cargarProductos()
        }
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
        // si hay imagen nueva de galeria, copiar a almacenamiento local
        val pathLocal = if (imagenUri != null) {
            repo.copiarImagenLocal(context, imagenUri)
        } else {
            null
        }

        val producto = Producto(
            id = id,
            nombre = nombre,
            precioKg = precioKg,
            stock = stock,
            descripcion = descripcion,
            imagenRes = null,
            imagenUri = pathLocal
        )
        repo.editarProducto(id, producto) { exito ->
            if (exito) cargarProductos()
        }
    }

    // deshabilitar producto
    fun deshabilitarProducto(id: String) {
        repo.cambiarEstado(id, false) { exito ->
            if (exito) cargarProductos()
        }
    }

    // habilitar producto
    fun habilitarProducto(id: String) {
        repo.cambiarEstado(id, true) { exito ->
            if (exito) cargarProductos()
        }
    }
}
