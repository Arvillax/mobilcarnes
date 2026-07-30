package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fblogin.data.UsuarioRepository
import com.example.fblogin.ui.admin.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// viewmodel de usuarios con Firestore
class UsuariosViewModel : ViewModel() {

    private val repo = UsuarioRepository()

    // estado de la lista
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    // busqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // filtro por rol (null = todos)
    private val _filtroRol = MutableStateFlow<String?>(null)
    val filtroRol: StateFlow<String?> = _filtroRol.asStateFlow()

    // cargar usuarios desde Firestore
    init {
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        repo.obtenerUsuarios { lista ->
            _usuarios.value = lista
        }
    }

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
        initialValue = emptyList()
    )

    // buscar
    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    // filtrar por rol
    fun filtrarPorRol(rol: String?) {
        _filtroRol.value = rol
    }

    // agregar usuario solo en Firestore
    fun agregarUsuario(nombre: String, email: String, rol: String) {
        val nuevo = Usuario(nombre = nombre, email = email, rol = rol)
        repo.agregarUsuario(nuevo) { exito ->
            if (exito) cargarUsuarios()
        }
    }

    // agregar usuario en Firebase Auth + Firestore
    fun agregarUsuarioConAuth(
        nombre: String,
        email: String,
        password: String,
        rol: String,
        onResultado: (Boolean) -> Unit
    ) {
        repo.crearUsuarioAuth(nombre, email, password, rol) { exito ->
            cargarUsuarios()
            onResultado(exito)
        }
    }

    // editar usuario
    fun editarUsuario(id: String, nombre: String, email: String, rol: String) {
        val usuario = Usuario(id = id, nombre = nombre, email = email, rol = rol)
        repo.editarUsuario(id, usuario) { exito ->
            if (exito) cargarUsuarios()
        }
    }

    // eliminar usuario
    fun eliminarUsuario(id: String) {
        repo.eliminarUsuario(id) { exito ->
            if (exito) cargarUsuarios()
        }
    }

    // cambiar rol
    fun cambiarRol(id: String, nuevoRol: String) {
        repo.cambiarRol(id, nuevoRol) { exito ->
            if (exito) cargarUsuarios()
        }
    }
}
