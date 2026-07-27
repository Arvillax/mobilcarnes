package com.example.fblogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fblogin.ui.navigation.NavGraph
import com.example.fblogin.viewmodel.AuthViewModel

// Activity principal - punto de entrada de la app
class MainActivity : ComponentActivity() {

    // ViewModel de autenticación (se comparte entre todas las pantallas)
    private val viewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar modo edge-to-edge (pantalla completa)
        enableEdgeToEdge()

        // Establecer el contenido con Compose
        setContent {
            NavGraph(viewModel) // Iniciar navegación
        }
    }
}
