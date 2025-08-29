 # 📱 SplitBill – Desarrollo paso a paso

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue?logo=kotlin)]()
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-green?logo=gradle)]()
[![Android](https://img.shields.io/badge/Android-API_24+-brightgreen?logo=android)]()

Proyecto del reto de arquitectura de Antonio Leiva en **Android (Kotlin + Jetpack Compose)** siguiendo **Clean Architecture** y aplicando buenas prácticas de:
- ✅ Separación de capas (data, domain, presentation)
- ✅ Uso de DB de DataStore (persistencia local)
- ✅ Inyección de dependencias manual (sin librerías externas, excepto Firebase)
- ✅ Uso de ViewModel + UDF (Unidirectional Data Flow)
- ✅ Tests unitarios con `kotlinx.coroutines.test`

---

## 🚀 Progreso del Proyecto (por días / commits)

### 📌 Día 1 – *(commit: `dia 0`)*
🔹 **Configuración inicial del proyecto**
- Integración con **Firebase** (`google-services.json`)
- Eliminación de *warnings*, comentarios y código innecesario
- Actualización de todas las librerías en `libs.versions.toml`

📷 Ejemplo:  
![Captura Firebase](docs/images/firebase-setup.png)  
![Commit inicial](docs/images/git-commit-dia0.png)

---

### 📌 Día 2 – *(commit: `dia 1-2 Capa de datos`)*
🔹 **Estructura de Clean Architecture (solo capa de datos):**
- 📂 `data/scan` y `data/ticket` → data sources y repositorios
- 📂 `app/data/...` → implementación real y mocks
- Modificación de las pantallas (`HomeScreen`, `ReceiptScreen`) para consumir el repositorio

📷 Ejemplo:  
![Estructura de datos](docs/images/data-layer.png)

---

### 📌 Día 3 – *(commit: `dia 2-3 Capa de dominio`)*
🔹 **Implementación de la capa de dominio (use cases):**
- Modelo `TicketData` movido a `data`
- 📂 `domain/useCases` con lógica de negocio
- Pantallas actualizadas para llamar a casos de uso en vez de repositorios
- `MainActivity` pasa los casos de uso por constructor

---

### 📌 Día 4 – *(commit: `dia 3-4 UI Inteligente`)*
🔹 **Capa de presentación con ViewModel + UDF:**
- `HomeViewModel` y `ReceiptViewModel` usando `StateFlow`
- Pantallas observando `uiState` (`loading`, `error`, `data`)
- UI modular y reutilizable para lista de tickets
- `MainActivity` conecta dependencias y ViewModels

📷 Ejemplo:  
![Demo UI](docs/images/app-ui.png)

---

### 📌 Día 5 – *(commit: `dia 5 Inyección de dependencias y Testing`)*
🔹 **Inyección manual de dependencias + Tests unitarios:**
- 📂 `di/AppModule` con dependencias manuales de la app
- ViewModels inyectados desde `MainActivity`
- Tests (4) de casos de uso y ViewModels con `runTest`
 
🧪 Tests incluidos:
- Verificar número de ítems procesados
- Validar integridad de un ítem
- Comprobar que el total coincide con la suma de ítems
- Procesar un ticket mock y comprobar que el **total esperado (272.20)** es correcto

📷 Ejemplo test:  
![Tests](docs/images/tests.png)

---

### 📌 Día 6 – *(commit: `Final mejoras + README.md`)*
🔹 **Últimos cambios y limpieza:**
- Carpeta `ui` → renombrada a `presentation`
- `TicketData` → renombrado a `Ticket`
- Strings movidos a `strings.xml` con **plurals**

---

## 📲 Resultado final
Ejecutable en **emulador o dispositivo Android real**  
📷  
![App funcionando](docs/images/app-final.png)

---

## 📂 Estructura final del proyecto
```bash
app/
 ├── app/
 │   ├── data/
 │   │   ├── scan/
 │   │   └── ticket/
 ├── data/
 │   ├── scan/
 │   └── ticket/
 ├── di/
 ├── domain/
 │   └── useCases/
 ├── presentation/
 │   ├── home/
 │   ├── receipt/
 │   └── theme/
 └── MainActivity.kt