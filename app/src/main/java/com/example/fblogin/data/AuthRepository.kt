package com.example.fblogin.data

import com.google.firebase.auth.FirebaseAuth

// Repositorio de autenticación - maneja toda la conexión con Firebase Auth
class AuthRepository {

    // Instancia singleton de Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    // Login con email y password - retorna éxito o error via callback
    fun login(email: String, password: String, result: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) result(true, null) // Login exitoso
                else result(false, it.exception?.message) // Error en login
            }
    }

    // Registro de nuevo usuario - crea cuenta en Firebase
    fun register(email: String, password: String, result: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) result(true, null) // Registro exitoso
                else result(false, it.exception?.message) // Error en registro
            }
    }

    // Cerrar sesión - limpia la sesión actual
    fun logout() = auth.signOut()

    // Verificar si hay sesión activa - retorna true si hay usuario logueado
    fun isLogged() = auth.currentUser != null
}
