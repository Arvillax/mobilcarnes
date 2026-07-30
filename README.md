# Carne Fresca - App de Carniceria Premium

Aplicacion movil Android desarrollada con **Jetpack Compose** y **Firebase** para la gestion integral de una carniceria premium.

## Funcionalidades

### Autenticacion y Roles
- Login y registro con **Firebase Auth**
- 3 roles: **ADMIN**, **GESTOR**, **CLIENTE**
- Asignacion automatica de rol por email
- Persistencia de perfiles en **Firestore**

### Panel Admin
- **Dashboard** con estadisticas de usuarios y productos
- **Gestion de usuarios**: CRUD completo con busqueda, filtro por rol, y creacion de usuarios con contraseña
- **Gestion de productos**: CRUD con imagenes, busqueda, habilitar/deshabilitar
- **Reportes**: 6 graficas animadas (barras, dona, linea) con Canvas

### Panel Gestor
- Dashboard con estadisticas
- Gestion de stock de productos
- Vista de ventas

### Panel Cliente
- Catalogo de productos con imagenes
- Detalle de producto con selector de cantidad
- Carrito de compras funcional
- Factura estilo recibo
- Historial de compras
- Dashboard de reportes

### Persistencia
- **Firebase Auth**: autenticacion de usuarios
- **Cloud Firestore**: productos, usuarios y perfiles
- **Firebase Storage**: imagenes de productos
- Seed automatico de 7 productos iniciales con imagenes

## Arquitectura

```
app/src/main/java/com/example/fblogin/
├── data/
│   ├── AuthRepository.kt          # Firebase Auth
│   ├── ProductoRepository.kt      # Firestore + Storage
│   └── UsuarioRepository.kt       # Firestore + Auth
├── viewmodel/
│   ├── AuthViewModel.kt           # Login, registro, roles
│   ├── CarritoViewModel.kt        # Estado del carrito
│   ├── ProductosViewModel.kt      # CRUD productos
│   └── UsuariosViewModel.kt       # CRUD usuarios
├── ui/
│   ├── auth/                      # Login y registro
│   ├── admin/                     # Dashboard, usuarios, productos, reportes
│   │   └── charts/                # Graficas Canvas (Bar, Pie, Line)
│   ├── gestor/                    # Dashboard, stock, ventas
│   ├── cliente/                   # Catalogo, carrito, factura, historial
│   ├── components/                # Componentes compartidos
│   └── navigation/NavGraph.kt     # Navegacion principal
└── ui/theme/                      # Colores, tipografia, tema
```

## Tecnologias

| Componente | Tecnologia |
|---|---|
| UI | Jetpack Compose + Material3 |
| Navegacion | Navigation Compose |
| Auth | Firebase Authentication |
| Base de datos | Cloud Firestore |
| Imagenes | Firebase Storage + Coil |
| Charts | Canvas personalizado |
| State | StateFlow + ViewModel |

## Paleta de Colores

| Color | Hex | Uso |
|---|---|---|
| Vino | `#610000` | Headers, rol Admin |
| Carmesi | `#9C0720` | Botones primarios |
| Crimson | `#DC143C` | Acentos, alertas |
| Coral | `#F1666D` | Hover, secundario |
| Rosa | `#FF9EA2` | Cards, backgrounds |

## Configuracion

1. Clonar el repositorio
2. Abrir en Android Studio
3. Asegurar que `google-services.json` esta en `app/`
4. Habilitar en Firebase Console:
   - **Authentication** (Email/Password)
   - **Cloud Firestore** (reglas de prueba: `allow read, write: if true`)
   - **Storage** (reglas de prueba: `allow read, write: if true`)
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

## Reglas de Storage (Pruebas)

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;
    }
  }
}
```

## Credenciales de Prueba

| Rol | Email | Password |
|---|---|---|
| Admin | `admin@test.com` | (cualquier, min 6 caracteres) |
| Gestor | `gestor@test.com` | (cualquier, min 6 caracteres) |
| Cliente | `cliente@test.com` | (cualquier, min 6 caracteres) |

> Los roles se asignan automaticamente por el email. El primer login crea el perfil en Firestore.

## Integrantes

Proyecto de clase de Desarrollo Movil.
