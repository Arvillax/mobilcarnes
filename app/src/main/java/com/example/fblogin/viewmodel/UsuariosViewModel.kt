package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fblogin.ui.admin.Usuario
import com.example.fblogin.ui.admin.usuariosMock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// viewmodel de usuarios
class UsuariosViewModel : ViewModel() {

    // estado de la lista
    private val _usuarios = MutableStateFlow(usuariosMock)
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    // busqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // filtro por rol (null = todos)
    private val _filtroRol = MutableStateFlow<String?>(null)
    val filtroRol: StateFlow<String?> = _filtroRol.asStateFlow()

    // usuarios filtrados (busqueda + rol)
    val usuariosFiltrados: StateFlow<List<Usuario>> = combine(
        _usuarios, _busqueda, _filtroRol
    ) { usuarios, texto, rol ->
        usuarios.filter { u ->
            val coincideBusqueda = texto.isEmpty() ||
                u.nombre.contains(texto, ignoreCase = true) ||
                u.email.contains(texto, ignoreCase = true)
            val coincideRol = rol == null || u.rol == rol
            coincideBusqueda && coincideRol
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = usuariosMock
    )

    // buscar
    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    // filtrar por rol
    fun filtrarPorRol(rol: String?) {
        _filtroRol.value = rol
    }

    // siguiente ID
    private fun siguienteId(): String {
        val maxId = _usuarios.value.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
        return (maxId + 1).toString()
    }

    // agregar usuario
    fun agregarUsuario(nombre: String, email: String, rol: String) {
        val nuevo = Usuario(id = siguienteId(), nombre = nombre, email = email, rol = rol)
        _usuarios.value = _usuarios.value + nuevo
    }

    // editar usuario
    fun editarUsuario(id: String, nombre: String, email: String, rol: String) {
        _usuarios.value = _usuarios.value.map { u ->
            if (u.id == id) u.copy(nombre = nombre, email = email, rol = rol) else u
        }
    }

    // eliminar usuario
    fun eliminarUsuario(id: String) {
        _usuarios.value = _usuarios.value.filter { it.id != id }
    }

    // cambiar rol
    fun cambiarRol(id: String, nuevoRol: String) {
        _usuarios.value = _usuarios.value.map { u ->
            if (u.id == id) u.copy(rol = nuevoRol) else u
        }
    }
}
