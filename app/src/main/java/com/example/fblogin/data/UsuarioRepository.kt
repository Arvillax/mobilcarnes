package com.example.fblogin.data

import com.example.fblogin.ui.admin.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// repositorio de usuarios con Firestore
class UsuarioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")
    private val auth = FirebaseAuth.getInstance()

    // obtener todos los usuarios
    fun obtenerUsuarios(callback: (List<Usuario>) -> Unit) {
        collection.get()
            .addOnSuccessListener { result ->
                val usuarios = result.mapNotNull { doc ->
                    doc.toObject(Usuario::class.java)?.copy(id = doc.id)
                }
                callback(usuarios)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    // agregar usuario solo en Firestore
    fun agregarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        val data = hashMapOf(
            "nombre" to usuario.nombre,
            "email" to usuario.email,
            "rol" to usuario.rol
        )
        collection.add(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // crear usuario en Firebase Auth + Firestore
    fun crearUsuarioAuth(
        nombre: String,
        email: String,
        password: String,
        rol: String,
        callback: (Boolean) -> Unit
    ) {
        // 1. Primero guardar en Firestore (admin sigue logueado)
        val usuario = Usuario(nombre = nombre, email = email, rol = rol)
        agregarUsuario(usuario) { firestoreOk ->
            if (firestoreOk) {
                // 2. Despues crear en Firebase Auth (cambia sesion al nuevo usuario)
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        callback(task.isSuccessful)
                    }
            } else {
                callback(false)
            }
        }
    }

    // re-login del admin despues de crear usuario
    // COMENTADO: no se usa, AuthViewModel maneja re-login directamente
    /*
    fun reLoginAdmin(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
    */

    // editar usuario
    fun editarUsuario(id: String, usuario: Usuario, callback: (Boolean) -> Unit) {
        val data = hashMapOf(
            "nombre" to usuario.nombre,
            "email" to usuario.email,
            "rol" to usuario.rol
        )
        collection.document(id).set(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // eliminar usuario
    fun eliminarUsuario(id: String, callback: (Boolean) -> Unit) {
        collection.document(id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // cambiar rol
    fun cambiarRol(id: String, nuevoRol: String, callback: (Boolean) -> Unit) {
        collection.document(id).update("rol", nuevoRol)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // buscar usuario por email
    fun buscarPorEmail(email: String, callback: (Usuario?) -> Unit) {
        collection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    callback(null)
                } else {
                    val doc = result.documents.first()
                    val usuario = doc.toObject(Usuario::class.java)?.copy(id = doc.id)
                    callback(usuario)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
