package com.example.fblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fblogin.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Roles disponibles en el sistema
enum class UserRole {
    ADMIN,   // Administrador total
    GESTOR,  // Gestor de stock y ventas
    CLIENTE  // Cliente que compra
}

// ViewModel de autenticación - conecta la UI con la lógica de login
class AuthViewModel : ViewModel() {

    // Instancia del repositorio
    private val repo = AuthRepository()

    // Estado del mensaje (éxito, error, etc.)
    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    // Estado de login (true = logueado)
    private val _logged = MutableStateFlow(repo.isLogged())
    val logged: StateFlow<Boolean> = _logged

    // Estado del rol del usuario actual
    private val _role = MutableStateFlow(UserRole.CLIENTE)
    val role: StateFlow<UserRole> = _role

    // Función de login - valida credenciales con Firebase
    fun login(email: String, password: String) {
        repo.login(email, password) { success, error ->
            if (success) {
                _logged.value = true
                _state.value = "SUCCESS"
                // Mock: asignar rol según contenido del email
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

    // Función de registro - crea nueva cuenta
    fun register(email: String, password: String) {
        repo.register(email, password) { success, error ->
            if (success) {
                _logged.value = true
                _state.value = "SUCCESS"
                // Mock: asignar rol según contenido del email
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

    // Cerrar sesión - limpia todos los estados
    fun logout() {
        repo.logout()
        _logged.value = false
        _state.value = ""
        _role.value = UserRole.CLIENTE
    }
}
