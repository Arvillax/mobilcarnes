package com.example.fblogin.ui.gestor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fblogin.ui.admin.charts.AreaLineChart
import com.example.fblogin.ui.admin.charts.DonutChart
import com.example.fblogin.ui.admin.charts.HorizontalBarChart
import com.example.fblogin.ui.admin.charts.VerticalBarChart
import com.example.fblogin.ui.components.SectionTitle
import com.example.fblogin.ui.components.StatCard
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.GestorDashboardViewModel
import com.example.fblogin.viewmodel.UserRole

@Composable
fun GestorDashboardScreen(
    nav: NavController,
    authVm: AuthViewModel,
    dashboardVm: GestorDashboardViewModel
) {
    // seguridad
    if (authVm.role.value != UserRole.GESTOR && authVm.role.value != UserRole.ADMIN) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Acceso no autorizado", color = Color.Gray)
        }
        return
    }

    val totalStock by dashboardVm.totalStock.collectAsState()
    val stockCritico by dashboardVm.stockCritico.collectAsState()
    val ventasHoy by dashboardVm.ventasHoy.collectAsState()
    val pedidosHoy by dashboardVm.pedidosHoy.collectAsState()

    val inventarioData by dashboardVm.inventarioData.collectAsState()
    val ventasSemanales by dashboardVm.ventasSemanales.collectAsState()
    val topProductos by dashboardVm.productosMasVendidos.collectAsState()
    val ventasPorProducto by dashboardVm.ventasPorProducto.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Reportes Operativos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver al Panel", fontSize = 16.sp, color = Carmesi)
            }

            SectionTitle("Resumen Operativo")

            // KPIs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(totalStock, "Total Stock", Modifier.weight(1f))
                StatCard(stockCritico, "Stock Crítico", Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(ventasHoy, "Ventas Hoy", Modifier.weight(1f))
                StatCard(pedidosHoy, "Pedidos Hoy", Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))

            // Gráficos con transiciones animadas (opcional pero solicitado mantener estilo)
            AnimatedContent(
                targetState = true,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "charts_gestor"
            ) { _ ->
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    // 1. Estado del Inventario
                    Column {
                        SectionTitle("Estado del Inventario")
                        HorizontalBarChart(inventarioData)
                    }

                    // 2. Ventas últimos 7 días
                    Column {
                        SectionTitle("Ventas de los últimos 7 días")
                        AreaLineChart(ventasSemanales)
                    }

                    // 3. Productos más vendidos
                    Column {
                        SectionTitle("Productos más vendidos")
                        VerticalBarChart(
                            data = topProductos,
                            title = "Frecuencia de venta",
                            valuePrefix = ""
                        )
                    }

                    // 4. Ventas por producto
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SectionTitle("Ventas por producto", Modifier.fillMaxWidth())
                        DonutChart(ventasPorProducto)
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
        }
    }
}
