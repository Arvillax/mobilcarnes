package com.example.fblogin.data

import com.google.firebase.auth.FirebaseAuth

// repositorio de autenticacion
class AuthRepository {

    // firebase auth
    private val auth = FirebaseAuth.getInstance()

    // login
    fun login(email: String, password: String, result: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) result(true, null)
                else result(false, it.exception?.message)
            }
    }

    // registro
    fun register(email: String, password: String, result: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) result(true, null)
                else result(false, it.exception?.message)
            }
    }

    // cerrar sesion
    fun logout() = auth.signOut()

    // verificar sesion
    fun isLogged() = auth.currentUser != null
}
