package com.example.fblogin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fblogin.ui.admin.AdminDashboard
import com.example.fblogin.ui.admin.AdminProductsScreen
import com.example.fblogin.ui.admin.AdminUsersScreen
import com.example.fblogin.ui.auth.LoginScreen
import com.example.fblogin.ui.auth.RegisterScreen
import com.example.fblogin.ui.cliente.CarritoScreen
import com.example.fblogin.ui.cliente.CatalogoScreen
import com.example.fblogin.ui.cliente.ClienteHistorialScreen
import com.example.fblogin.ui.cliente.ProductoDetalleScreen
import com.example.fblogin.ui.gestor.GestorDashboard
import com.example.fblogin.ui.gestor.GestorStockScreen
import com.example.fblogin.ui.gestor.GestorVentasScreen
import com.example.fblogin.viewmodel.AuthViewModel

// Navegación principal - define todas las rutas de la app
@Composable
fun NavGraph(viewModel: AuthViewModel) {

    // Controlador de navegación
    val navController = rememberNavController()

    // Observar estado de login y rol
    val logged by viewModel.logged.collectAsState()
    val role by viewModel.role.collectAsState()

    // Definir todas las rutas disponibles
    NavHost(
        navController = navController,
        startDestination = "login" // Pantalla inicial
    ) {
        // === RUTAS DE AUTENTICACIÓN ===
        composable("login") {
            LoginScreen(navController, viewModel)
        }
        composable("register") {
            RegisterScreen(navController, viewModel)
        }

        // === RUTAS DE ADMIN ===
        composable("admin/dashboard") {
            AdminDashboard(navController, viewModel)
        }
        composable("admin/users") {
            AdminUsersScreen(navController, viewModel)
        }
        composable("admin/products") {
            AdminProductsScreen(navController, viewModel)
        }

        // === RUTAS DE GESTOR ===
        composable("gestor/dashboard") {
            GestorDashboard(navController, viewModel)
        }
        composable("gestor/stock") {
            GestorStockScreen(navController, viewModel)
        }
        composable("gestor/ventas") {
            GestorVentasScreen(navController, viewModel)
        }

        // === RUTAS DE CLIENTE ===
        composable("cliente/catalogo") {
            CatalogoScreen(navController, viewModel)
        }
        // Ruta con parámetro - recibe el ID del producto
        composable("cliente/detalle/{productoId}") { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            ProductoDetalleScreen(navController, viewModel, productoId)
        }
        composable("cliente/carrito") {
            CarritoScreen(navController, viewModel)
        }
        composable("cliente/historial") {
            ClienteHistorialScreen(navController, viewModel)
        }
    }
}
