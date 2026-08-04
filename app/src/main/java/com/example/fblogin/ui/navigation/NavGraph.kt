package com.example.fblogin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fblogin.ui.admin.AdminDashboard
import com.example.fblogin.ui.admin.AdminGraficosScreen
import com.example.fblogin.ui.admin.AdminProductsScreen
import com.example.fblogin.ui.admin.AdminUsersScreen
import com.example.fblogin.ui.auth.LoginScreen
import com.example.fblogin.ui.auth.RegisterScreen
import com.example.fblogin.ui.cliente.CarritoScreen
import com.example.fblogin.ui.cliente.CatalogoScreen
import com.example.fblogin.ui.cliente.ClienteDashboardScreen
import com.example.fblogin.ui.cliente.ClienteHistorialScreen
import com.example.fblogin.ui.cliente.FacturaScreen
import com.example.fblogin.ui.cliente.ProductoDetalleScreen
import com.example.fblogin.ui.gestor.GestorDashboard
import com.example.fblogin.ui.gestor.GestorDashboardScreen
import com.example.fblogin.ui.gestor.GestorStockScreen
import com.example.fblogin.ui.gestor.GestorVentasScreen
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.CarritoViewModel
import com.example.fblogin.viewmodel.ClienteDashboardViewModel
import com.example.fblogin.viewmodel.DashboardViewModel
import com.example.fblogin.viewmodel.GestorDashboardViewModel
import com.example.fblogin.viewmodel.ProductosViewModel
import com.example.fblogin.viewmodel.ReportesViewModel
import com.example.fblogin.viewmodel.UsuariosViewModel
import com.example.fblogin.viewmodel.ClienteHistorialViewModel
import com.example.fblogin.viewmodel.GestorVentasViewModel

// navegacion principal
@Composable
fun NavGraph(viewModel: AuthViewModel) {

    // controlador
    val navController = rememberNavController()

    // viewmodel del carrito (compartido entre pantallas cliente)
    val carritoViewModel: CarritoViewModel = viewModel()

    // viewmodel de usuarios (compartido para admin)
    val usuariosViewModel: UsuariosViewModel = viewModel()

    // viewmodel de productos (compartido)
    val productosViewModel: ProductosViewModel = viewModel()

    // viewmodels de dashboards
    val gestorDashboardViewModel: GestorDashboardViewModel = viewModel()
    val gestorVentasViewModel: GestorVentasViewModel = viewModel()
    val clienteDashboardViewModel: ClienteDashboardViewModel = viewModel()
    val dashboardViewModel: DashboardViewModel = viewModel()
    val reportesViewModel: ReportesViewModel = viewModel()
    val clienteHistorialViewModel: ClienteHistorialViewModel = viewModel()

    // observar estado
    val logged by viewModel.logged.collectAsState()
    val role by viewModel.role.collectAsState()

    // rutas
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // auth
        composable("login") {
            LoginScreen(navController, viewModel)
        }
        composable("register") {
            RegisterScreen(navController, viewModel)
        }

        // admin
        composable("admin/dashboard") {
            AdminDashboard(navController, viewModel, dashboardViewModel)
        }
        composable("admin/users") {
            AdminUsersScreen(navController, viewModel, usuariosViewModel)
        }
        composable("admin/products") {
            AdminProductsScreen(navController, viewModel, productosViewModel)
        }
        composable("admin/graficos") {
            AdminGraficosScreen(navController, viewModel, reportesViewModel)
        }

        // gestor
        composable("gestor/dashboard") {
            GestorDashboard(navController, viewModel, gestorDashboardViewModel)
        }
        composable("gestor/stock") {
            GestorStockScreen(navController, viewModel, productosViewModel)
        }
        composable("gestor/ventas") {
            GestorVentasScreen(navController, viewModel, gestorVentasViewModel)
        }
        composable("gestor/reportes") {
            GestorDashboardScreen(navController, viewModel, gestorDashboardViewModel)
        }

        // cliente
        composable("cliente/catalogo") {
            CatalogoScreen(navController, viewModel, productosViewModel)
        }
        composable("cliente/detalle/{productoId}") { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            ProductoDetalleScreen(navController, viewModel, productoId, carritoViewModel, productosViewModel)
        }
        composable("cliente/carrito") {
            CarritoScreen(navController, viewModel, carritoViewModel)
        }
        composable("cliente/factura") {
            FacturaScreen(navController, carritoViewModel, viewModel, productosViewModel)
        }
        composable("cliente/historial") {
            ClienteHistorialScreen(navController, viewModel, clienteHistorialViewModel)
        }
        composable("cliente/reportes") {
            ClienteDashboardScreen(navController, viewModel, clienteDashboardViewModel)
        }
    }
}
