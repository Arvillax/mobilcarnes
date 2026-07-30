package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.ui.admin.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// item del carrito
data class CarritoItem(val producto: Producto, val cantidad: Double)

// viewmodel del carrito
class CarritoViewModel : ViewModel() {

    // estado del carrito
    private val _items = MutableStateFlow<List<CarritoItem>>(emptyList())
    val items: StateFlow<List<CarritoItem>> = _items.asStateFlow()

    // agregar producto al carrito
    fun agregarProducto(producto: Producto, cantidad: Double) {
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            // ya existe: sumar cantidad
            val existente = current[index]
            val nuevaCantidad = (existente.cantidad + cantidad).coerceAtMost(producto.stock.toDouble())
            current[index] = existente.copy(cantidad = nuevaCantidad)
        } else {
            // nuevo item
            val cantidadFinal = cantidad.coerceAtMost(producto.stock.toDouble())
            current.add(CarritoItem(producto, cantidadFinal))
        }

        _items.value = current
    }

    // actualizar cantidad de un item
    fun actualizarCantidad(producto: Producto, nuevaCantidad: Double) {
        val cantidadFinal = nuevaCantidad.coerceIn(1.0, producto.stock.toDouble())
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            current[index] = current[index].copy(cantidad = cantidadFinal)
            _items.value = current
        }
    }

    // eliminar producto del carrito
    fun eliminarProducto(productoId: String) {
        _items.value = _items.value.filter { it.producto.id != productoId }
    }

    // vaciar carrito
    fun vaciarCarrito() {
        _items.value = emptyList()
    }

    // total del carrito
    fun total(): Double {
        return _items.value.sumOf { it.cantidad * it.producto.precioKg }
    }
}
