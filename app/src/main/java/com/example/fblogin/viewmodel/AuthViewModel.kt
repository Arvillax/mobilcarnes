package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.data.AuthRepository
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

    // repositorio
    private val repo = AuthRepository()

    // estado del mensaje
    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    // estado de login
    private val _logged = MutableStateFlow(repo.isLogged())
    val logged: StateFlow<Boolean> = _logged

    // estado del rol
    private val _role = MutableStateFlow(UserRole.CLIENTE)
    val role: StateFlow<UserRole> = _role

    // login
    fun login(email: String, password: String) {
        repo.login(email, password) { success, error ->
            if (success) {
                _logged.value = true
                _state.value = "SUCCESS"
                // asignar rol
                _role.value = when {
                    email.contains("admin") -> UserRole.ADMIN
                    email.contains("gestor") -> UserRole.GESTOR
                    else -> UserRole.CLIENTE
                }
            } else {
                _state.value = error ?: "ERROR"
            }
        }
    }

    // registro
    fun register(email: String, password: String) {
        repo.register(email, password) { success, error ->
            if (success) {
                _logged.value = true
                _state.value = "SUCCESS"
                // asignar rol
                _role.value = when {
                    email.contains("admin") -> UserRole.ADMIN
                    email.contains("gestor") -> UserRole.GESTOR
                    else -> UserRole.CLIENTE
                }
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
