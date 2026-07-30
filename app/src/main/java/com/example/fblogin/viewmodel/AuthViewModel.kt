package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.data.AuthRepository
import com.example.fblogin.data.UsuarioRepository
import com.example.fblogin.ui.admin.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// roles
enum class UserRole {
    ADMIN,   // administrador
    GESTOR,  // gestor
    CLIENTE  // cliente
}

// viewmodel de autenticacion
class AuthViewModel : ViewModel() {

    // repositorios
    private val repo = AuthRepository()
    private val usuarioRepo = UsuarioRepository()

    // estado del mensaje
    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    // estado de login
    private val _logged = MutableStateFlow(repo.isLogged())
    val logged: StateFlow<Boolean> = _logged

    // estado del rol
    private val _role = MutableStateFlow(UserRole.CLIENTE)
    val role: StateFlow<UserRole> = _role

    // credenciales del admin (para re-login despues de crear usuario)
    private var adminEmail: String = ""
    private var adminPassword: String = ""

    // login
    fun login(email: String, password: String) {
        repo.login(email, password) { success, error ->
            if (success) {
                _logged.value = true
                // guardar credenciales para re-login
                adminEmail = email
                adminPassword = password
                // buscar usuario en Firestore para obtener el rol ANTES de navegar
                usuarioRepo.buscarPorEmail(email) { usuario ->
                    if (usuario != null) {
                        // usuario encontrado en Firestore, usar su rol guardado
                        _role.value = UserRole.valueOf(usuario.rol)
                    } else {
                        // usuario no existe en Firestore, crearlo con rol por defecto
                        val rol = when {
                            email.contains("admin") -> "ADMIN"
                            email.contains("gestor") || email == "pr@test.com" -> "GESTOR"
                            else -> "CLIENTE"
                        }
                        val nombre = email.substringBefore("@")
                        val nuevoUsuario = Usuario(nombre = nombre, email = email, rol = rol)
                        usuarioRepo.agregarUsuario(nuevoUsuario) {}
                        _role.value = UserRole.valueOf(rol)
                    }
                    // ahora sí, con el rol ya seteado
                    _state.value = "SUCCESS"
                }
            } else {
                _state.value = error ?: "ERROR"
            }
        }
    }

    // re-login del admin despues de crear usuario
    fun reLoginAdmin(callback: (Boolean) -> Unit) {
        if (adminEmail.isEmpty() || adminPassword.isEmpty()) {
            callback(false)
            return
        }
        repo.login(adminEmail, adminPassword) { success, _ ->
            callback(success)
        }
    }

    // registro
    fun register(email: String, password: String) {
        repo.register(email, password) { success, error ->
            if (success) {
                _logged.value = true
                _state.value = "SUCCESS"
                // asignar rol segun email
                val rol = when {
                    email.contains("admin") -> "ADMIN"
                    email.contains("gestor") || email == "pr@test.com" -> "GESTOR"
                    else -> "CLIENTE"
                }
                _role.value = UserRole.valueOf(rol)
                // guardar usuario en Firestore
                val nombre = email.substringBefore("@")
                val usuario = Usuario(nombre = nombre, email = email, rol = rol)
                usuarioRepo.agregarUsuario(usuario) {}
            } else {
                _state.value = error ?: "ERROR"
            }
        }
    }

    // cerrar sesion
    fun logout() {
        repo.logout()
        _logged.value = false
        _state.value = ""
        _role.value = UserRole.CLIENTE
    }
}
