package com.example.fblogin.ui.admin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.fblogin.ui.theme.Vino
import com.example.fblogin.ui.theme.Carmesi
import com.example.fblogin.viewmodel.AuthViewModel
import com.example.fblogin.viewmodel.ReportesViewModel
import com.example.fblogin.viewmodel.UserRole

// opciones de reportes
private val reportOptions = listOf(
    "Ventas por mes",
    "Ventas por producto",
    "Tendencia mensual",
    "Top productos",
    "Inventario actual",
    "Distribución de usuarios"
)

// pantalla de graficos admin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGraficosScreen(
    nav: NavController,
    vm: AuthViewModel,
    reportesVm: ReportesViewModel
) {
    // seguridad: solo admin
    if (vm.role.value != UserRole.ADMIN) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "Acceso no autorizado",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver")
            }
        }
        return
    }

    var selectedReportIndex by remember { mutableIntStateOf(0) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // datos reales de Firestore
    val ventasPorMes by reportesVm.ventasPorMes.collectAsState()
    val ventasPorProducto by reportesVm.ventasPorProducto.collectAsState()
    val tendenciaMensual by reportesVm.tendenciaMensual.collectAsState()
    val topProductos by reportesVm.topProductos.collectAsState()
    val inventarioActual by reportesVm.inventarioActual.collectAsState()
    val distribucionUsuarios by reportesVm.distribucionUsuarios.collectAsState()

    LaunchedEffect(Unit) {
        reportesVm.recargar()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // header gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Vino, Carmesi)))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Reportes",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // volver
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Volver", fontSize = 16.sp)
            }

            Spacer2(modifier = Modifier.height(12.dp))

            // dropdown selector
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = reportOptions[selectedReportIndex],
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar reporte") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Carmesi,
                        focusedLabelColor = Carmesi
                    )
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    reportOptions.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedReportIndex = index
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer2(modifier = Modifier.height(16.dp))

            // grafico con animacion de transicion
            AnimatedContent(
                targetState = selectedReportIndex,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "chart_transition"
            ) { reportIndex ->
                when (reportIndex) {
                    0 -> VerticalBarChart(
                        data = ventasPorMes,
                        modifier = Modifier.fillMaxWidth()
                    )
                    1 -> DonutChart(
                        data = ventasPorProducto,
                        modifier = Modifier.fillMaxWidth()
                    )
                    2 -> AreaLineChart(
                        data = tendenciaMensual,
                        modifier = Modifier.fillMaxWidth()
                    )
                    3 -> Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "Top productos vendidos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalBarChart(
                            data = topProductos,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    4 -> Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "Inventario actual",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalBarChart(
                            data = inventarioActual,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    5 -> Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Distribución de usuarios",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        DonutChart(
                            data = distribucionUsuarios,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// helper para Spacer con modifier
@Composable
private fun Spacer2(modifier: Modifier) {
    androidx.compose.foundation.layout.Spacer(modifier = modifier)
}
