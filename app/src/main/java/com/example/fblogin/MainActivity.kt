package com.example.fblogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fblogin.ui.navigation.NavGraph
import com.example.fblogin.viewmodel.AuthViewModel

// activity principal
class MainActivity : ComponentActivity() {

    // viewModel
    private val viewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // modo edge-to-edge
        enableEdgeToEdge()

        // contenido
        setContent {
            NavGraph(viewModel)
        }
    }
}
