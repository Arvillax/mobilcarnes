# Carne Fresca - App de Carniceria Premium

Aplicacion movil Android desarrollada con **Jetpack Compose** y **Firebase** para la gestion integral de una carniceria premium.

## Funcionalidades por Rol

### ADMIN
- **Dashboard** con KPIs en tiempo real: total usuarios, total productos, ventas del dia
- **Gestion de usuarios**: CRUD completo con busqueda, filtro por rol, creacion con contraseña
- **Gestion de productos**: CRUD con imagenes locales, busqueda, habilitar/deshabilitar
- **Reportes**: 6 graficos animados conectados a Firestore (barras, dona, linea)

### GESTOR
- **Dashboard** con KPIs en tiempo real: ventas hoy, stock bajo (<15 kg), pedidos del dia
- **Gestion de stock**: lista de productos con dialogo para añadir o registrar perdidas
- **Reportes operativos**: graficos de inventario, ventas semanales, top productos, estado de pedidos

### CLIENTE
- **Catalogo** de productos habilitados con imagenes
- **Detalle** de producto con selector de cantidad
- **Carrito** de compras con ajuste de cantidades
- **Factura** estilo recibo que guarda en Firestore y descuenta stock automaticamente
- **Historial** de compras
- **Dashboard** con graficos de gastos y pedidos

## Arquitectura del Codigo

```
app/src/main/java/com/example/fblogin/
├── MainActivity.kt                          # Entry point
├── data/
│   ├── AuthRepository.kt                    # Firebase Auth wrapper
│   ├── ProductoRepository.kt                # Firestore CRUD + imagenes locales + seed
│   ├── UsuarioRepository.kt                 # Firestore CRUD + Firebase Auth create
│   └── VentaRepository.kt                   # Firestore ventas + descontar stock
├── viewmodel/
│   ├── AuthViewModel.kt                     # Login, registro, roles (Source of Truth: Firestore)
│   ├── CarritoViewModel.kt                  # Estado del carrito en memoria
│   ├── ProductosViewModel.kt                # CRUD productos + recarga automatica
│   ├── UsuariosViewModel.kt                 # CRUD usuarios con busqueda y filtro
│   ├── DashboardViewModel.kt                # KPIs admin (usuarios, productos, ventas dia)
│   ├── GestorDashboardViewModel.kt          # KPIs gestor (stock, ventas, pedidos)
│   ├── ClienteDashboardViewModel.kt         # KPIs cliente (mock)
│   └── ReportesViewModel.kt                 # 6 reportes admin desde Firestore
├── ui/
│   ├── auth/
│   │   ├── LoginScreen.kt                   # Login con gradiente
│   │   └── RegisterScreen.kt                # Registro de usuarios
│   ├── admin/
│   │   ├── AdminDashboard.kt                # KPIs admin + navegacion
│   │   ├── AdminUsersScreen.kt              # CRUD usuarios + dialogos
│   │   ├── AdminProductsScreen.kt           # CRUD productos + galeria
│   │   ├── AdminGraficosScreen.kt           # Selector de reportes + graficos
│   │   └── charts/
│   │       ├── ChartSamples.kt              # Data classes (BarData, PieData, LineData)
│   │       ├── BarChart.kt                  # Grafico de barras (vertical y horizontal)
│   │       ├── PieChart.kt                  # Grafico de dona
│   │       └── LineChart.kt                 # Grafico de lineas/area
│   ├── gestor/
│   │   ├── GestorDashboard.kt               # KPIs gestor + navegacion
│   │   ├── GestorDashboardScreen.kt         # Reportes operativos con graficos
│   │   ├── GestorStockScreen.kt             # Lista stock + dialogo añadir/perdida
│   │   └── GestorVentasScreen.kt            # Historial de ventas (mock)
│   ├── cliente/
│   │   ├── CatalogoScreen.kt                # Catalogo de productos
│   │   ├── ProductoDetalleScreen.kt         # Detalle con selector cantidad
│   │   ├── CarritoScreen.kt                 # Carrito con totales
│   │   ├── FacturaScreen.kt                 # Factura + guardado en Firestore
│   │   ├── ClienteHistorialScreen.kt        # Historial de compras (mock)
│   │   └── ClienteDashboardScreen.kt        # Dashboard cliente con graficos (mock)
│   ├── components/
│   │   └── DashboardComponents.kt           # Componentes compartidos
│   ├── navigation/
│   │   └── NavGraph.kt                      # Navegacion principal + roles
│   └── theme/
│       ├── Color.kt                         # Paleta de colores
│       ├── Theme.kt                         # Tema (comentado, sin uso)
│       └── Type.kt                          # Tipografia
```

## Modelos de Datos

### Producto
```kotlin
data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precioKg: Double = 0.0,
    val stock: Int = 0,
    val descripcion: String = "",
    val imagenRes: Int? = null,
    val imagenUri: String? = null,    // path local en filesDir/productos/
    val habilitado: Boolean = true
)
```

### Usuario
```kotlin
data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val rol: String = "CLIENTE"       // ADMIN | GESTOR | CLIENTE
)
```

### Venta
```kotlin
data class Venta(
    val id: String = "",
    val fecha: String = "",           // "dd/MM/yyyy"
    val total: Double = 0.0,
    val clienteEmail: String = "",
    val items: List<Map<String, Any>> // [{productoId, nombre, cantidad, precioKg}]
)
```

### CarritoItem
```kotlin
data class CarritoItem(val producto: Producto, val cantidad: Double)
```

## Colecciones Firestore

### `productos`
| Campo | Tipo | Descripcion |
|---|---|---|
| `nombre` | String | Nombre del producto |
| `precioKg` | Double | Precio por kilogramo |
| `stock` | Int | Stock actual en kg |
| `descripcion` | String | Descripcion del producto |
| `imagenRes` | Int? | Siempre null (usa almacenamiento local) |
| `imagenUri` | String? | Path local de la imagen |
| `habilitado` | Boolean | Si esta visible para clientes |

**Seed automatico**: 7 productos iniciales al detectar Firestore vacio.

### `usuarios`
| Campo | Tipo | Descripcion |
|---|---|---|
| `nombre` | String | Nombre del usuario |
| `email` | String | Email (unico) |
| `rol` | String | ADMIN, GESTOR o CLIENTE |

### `ventas`
| Campo | Tipo | Descripcion |
|---|---|---|
| `fecha` | String | Fecha en formato "dd/MM/yyyy" |
| `total` | Double | Total de la venta |
| `clienteEmail` | String | Email del comprador |
| `items` | Array | Lista de productos comprados |

**Descuento de stock**: Al guardar una venta, se ejecuta un batch de Firestore con `FieldValue.increment(-cantidad)` por cada producto vendido.

## Navegacion

| Ruta | Pantalla | Rol |
|---|---|---|
| `login` | LoginScreen | Todos |
| `register` | RegisterScreen | Todos |
| `admin/dashboard` | AdminDashboard | ADMIN |
| `admin/users` | AdminUsersScreen | ADMIN |
| `admin/products` | AdminProductsScreen | ADMIN |
| `admin/graficos` | AdminGraficosScreen | ADMIN |
| `gestor/dashboard` | GestorDashboard | GESTOR |
| `gestor/stock` | GestorStockScreen | GESTOR |
| `gestor/ventas` | GestorVentasScreen | GESTOR |
| `gestor/reportes` | GestorDashboardScreen | GESTOR |
| `cliente/catalogo` | CatalogoScreen | CLIENTE |
| `cliente/detalle/{id}` | ProductoDetalleScreen | CLIENTE |
| `cliente/carrito` | CarritoScreen | CLIENTE |
| `cliente/factura` | FacturaScreen | CLIENTE |
| `cliente/historial` | ClienteHistorialScreen | CLIENTE |
| `cliente/reportes` | ClienteDashboardScreen | CLIENTE |

**Redireccion post-login**: ADMIN → `admin/dashboard`, GESTOR → `gestor/dashboard`, CLIENTE → `cliente/catalogo`

## Graficos y Reportes

### Admin (ReportesViewModel - datos reales de Firestore)
1. **Ventas por mes** - Barras verticales: total de ventas agrupado por mes
2. **Ventas por producto** - Dona: cantidad vendida por producto
3. **Tendencia mensual** - Lineas: evolucion de ventas mensuales
4. **Top productos** - Barras horizontales: productos mas vendidos por frecuencia
5. **Inventario actual** - Barras horizontales: stock por producto (verde >30, amarillo 15-30, rojo <15)
6. **Distribucion usuarios** - Dona: usuarios por rol

### Gestor (GestorDashboardViewModel - mixto)
- KPIs en tiempo real (ventas hoy, stock bajo, pedidos)
- Graficos con datos mock (se connecteran en el futuro)

## Tecnologias

| Componente | Tecnologia | Version |
|---|---|---|
| UI | Jetpack Compose + Material3 | BOM 2026.02.01 |
| Navegacion | Navigation Compose | 2.9.8 |
| Auth | Firebase Authentication | BOM 33.7.0 |
| Base de datos | Cloud Firestore | BOM 33.7.0 |
| Imagenes | Coil Compose | 2.6.0 |
| Charts | Canvas personalizado | - |
| State | StateFlow + ViewModel | - |
| Lenguaje | Kotlin | 2.2.10 |
| Build | Gradle | 9.4.1 |
| SDK | compileSdk 36, minSdk 26 | - |

> **Nota**: Las imagenes se almacenan localmente en `context.filesDir/productos/`, NO en Firebase Storage.

## Paleta de Colores

| Color | Hex | Uso |
|---|---|---|
| Vino | `#610000` | Headers, rol Admin |
| Carmesi | `#9C0720` | Botones primarios |
| Crimson | `#DC143C` | Acentos, alertas, stock critico |
| Coral | `#F1666D` | Hover, secundario |
| Rosa | `#FF9EA2` | Cards, backgrounds |
| Verde | `#2E7D32` | Stock ok, confirmaciones |
| Amarillo | `#F9A825` | Stock medio |

## Configuracion

1. Clonar el repositorio
2. Abrir en Android Studio
3. Asegurar que `google-services.json` esta en `app/`
4. Habilitar en Firebase Console:
   - **Authentication** (Email/Password)
   - **Cloud Firestore** (reglas de prueba)
5. Build y run

## Reglas de Firestore (Pruebas)

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

## Credenciales de Prueba

| Rol | Email | Password |
|---|---|---|
| Admin | `admin@test.com` | (min 6 caracteres) |
| Gestor | `gestor@test.com` | (min 6 caracteres) |
| Cliente | `cliente@test.com` | (min 6 caracteres) |

> Los roles se asignan automaticamente por patron de email. El primer login crea el perfil en Firestore.

## Estado de Datos (Mock vs Firestore)

| Pantalla | Estado |
|---|---|
| Admin Dashboard KPIs | Firestore |
| Admin Reportes (6 graficos) | Firestore |
| Admin CRUD Usuarios | Firestore |
| Admin CRUD Productos | Firestore |
| Gestor Dashboard KPIs | Firestore |
| Gestor Stock (añadir/perdida) | Firestore |
| Gestor Reportes (4 graficos) | Mock |
| Gestor Ventas | Mock |
| Cliente Catalogo | Firestore |
| Cliente Carrito/Factura | Firestore |
| Cliente Historial | Mock |
| Cliente Dashboard | Mock |

## Integrantes

Proyecto de clase de Desarrollo Movil.
