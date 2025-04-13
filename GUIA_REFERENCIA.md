# 📖 Guía de Referencia del Proyecto: CryptoWallet App 🪙

1.  [ℹ️ Introducción](#1--introducción)
2.  [🏗️ Patrón Arquitectónico: MVVM](#2--patrón-arquitectónico:-mvvm)
3.  [📁 Estructura de Directorios (Paquetes)](#3--estructura-de-directorios-paquetes)
4.  [🧩 Componentes Principales y Responsabilidades](#4--componentes-principales-y-responsabilidades)
5.  [🌊 Flujos de Datos](#5--flujos-de-datos)
6.  [🔗 Dependencias](#6--dependencias)

## 1. ℹ️ Introducción

Este documento sirve como referencia técnica para la aplicación Android CryptoWallet. Describe la arquitectura, los componentes principales, los flujos de datos clave y las decisiones de diseño implementadas. El objetivo es facilitar el mantenimiento, la extensión y la comprensión del código base.

La aplicación es un simulador de cartera de criptomonedas desarrollado en Kotlin, siguiendo el patrón arquitectónico MVVM.

## 2. 🏗️ Patrón Arquitectónico: MVVM

La aplicación adopta el patrón **Model-View-ViewModel (MVVM)** para separar las responsabilidades y mejorar la mantenibilidad y testeabilidad:

1.  **Model (Modelo):** 🧱
    *   **Responsabilidad:** Representa los datos y la lógica de negocio de la aplicación. Es independiente de la interfaz de usuario. Gestiona el acceso a los datos, ya sea desde una fuente local (base de datos, archivos) o remota (API).
    *   **En este proyecto:**
        *   **Clases de Datos:** `Crypto.kt`, `Holdings.kt`, `Transaction.kt`, `TransactionType.kt`. Definen la estructura de los objetos de datos utilizados en la aplicación.
        *   **Fuente de Datos:** `CryptoProvider.kt`. Actúa como un repositorio *simplificado* y estático que provee la lista inicial de criptomonedas. En una aplicación real, esta capa contendría clases `Repository` que interactuarían con APIs de red (Retrofit, Ktor) y/o bases de datos locales (Room).

2.  **View (Vista):** 📱
    *   **Responsabilidad:** Es la capa de interfaz de usuario (UI). Muestra los datos al usuario y captura sus interacciones (clics, entradas de texto, etc.). En Android, esto corresponde a Activities, Fragments y sus Layouts XML. La Vista *observa* los cambios en los datos expuestos por el ViewModel (generalmente a través de `LiveData` o `StateFlow`) y actualiza la UI en consecuencia. Delega la lógica de las interacciones del usuario al ViewModel.
    *   **En este proyecto:**
        *   `MainActivity.kt`: Actúa como el host principal para los fragments y configura la navegación (Toolbar, Navigation Drawer).
        *   Fragments (`MarketFragment.kt`, `HoldingsFragment.kt`, `TransactionFragment.kt`): Cada fragment representa una pantalla específica de la aplicación. Son responsables de inflar sus layouts, configurar los `RecyclerViews` y *observar* los `LiveData` del `CryptoViewModel`.
        *   Layouts XML (`fragment_market.xml`, `item_crypto.xml`, `add_transaction_dialog.xml`, etc.): Definen la estructura visual de las pantallas y los elementos de lista.
        *   Adapters (`CryptoAdapter.kt`, `HoldingsAdapter.kt`, `TransactionAdapter.kt`): Aunque residen en su propio paquete, son componentes auxiliares de la Vista. Adaptan los datos (recibidos indirectamente del ViewModel a través del Fragment) para ser mostrados en los `RecyclerViews`. También manejan los clics en los items y la lógica de los diálogos (delegando la acción final al ViewModel).

3.  **ViewModel (Vista-Modelo):** 🧠
    *   **Responsabilidad:** Actúa como un intermediario entre el Model y la View. Expone los datos relevantes para la UI (generalmente a través de `LiveData` o `StateFlow`) de manera que la Vista pueda observarlos. Contiene la lógica de presentación y responde a las interacciones del usuario delegadas por la Vista, interactuando con el Model para obtener o modificar datos. Está diseñado para sobrevivir a cambios de configuración (como rotaciones de pantalla), manteniendo el estado de la UI.
    *   **En este proyecto:**
        *   `CryptoViewModel.kt`: Es el único ViewModel en esta aplicación.
            *   Mantiene el estado de la UI (`_availableBalance`, `_holdingsList`, `_transactionHistory`, etc.) usando `MutableLiveData`.
            *   Expone estos datos a los Fragments como `LiveData` inmutables.
            *   Contiene la lógica para:
                *   ➕➖ Añadir/Retirar saldo (`addBalance`, `withdrawBalance`).
                *   🛒 Procesar transacciones (`addTransaction`), incluyendo la actualización del saldo y las tenencias.
                *   📈 Calcular valores derivados (`calculateTotalHoldingsValue`, `calculateHoldings` para profit/loss).
                *   🔎 Obtener datos del "Model" (`CryptoProvider.cryptoList`, `getCryptoPrice`).

## 3. 📁 Estructura de Directorios (Paquetes)

```css
com.example.cryptowallet
│
├── MainActivity.kt             # 🏠 Host principal, configuración de navegación
│
├── adapters/                   # 🔌 Adaptadores para RecyclerViews
│   ├── CryptoAdapter.kt       #    Muestra lista de mercado, maneja clics de compra
│   ├── HoldingsAdapter.kt     #    Muestra lista de cartera, maneja clics de venta
│   └── TransactionAdapter.kt  #    Muestra historial de transacciones
│
├── data/                       # 💾 Acceso y gestión de datos
│   └── CryptoProvider.kt       #    Fuente de datos ESTÁTICA para precios de mercado
│
├── models/                     # 📦 Definiciones de objetos de datos (data classes)
│   ├── Crypto.kt
│   ├── Holdings.kt
│   ├── Transaction.kt
│   └── TransactionType.kt     #    Enum para tipos de transacción
│
├── viewModel/                  # 🧠 ViewModels
│   └── CryptoViewModel.kt      #    Único ViewModel, gestiona todo el estado y lógica
│
└── views/                      # 🖼️ Fragments (Pantallas de la UI)
    ├── HoldingsFragment.kt
    ├── MarketFragment.kt
    └── TransactionFragment.kt

res/                            # 🎨 Recursos
├── layout/                     #    Diseños XML (Activity, Fragments, Items, Dialogs)
├── drawable/                   #    Recursos gráficos (iconos cripto)
├── values/                     #    Strings, colores, dimens, styles
└── navigation/                 #    🗺️ Gráfico de navegación (nav_graph.xml)
```

## 4. 🧩 Componentes Principales y Responsabilidades

### 4.1. Models (`com.example.cryptowallet.models`) 📦

*   **`Crypto.kt`:** `data class` que representa una criptomoneda en el mercado (rank, logo, nombre, símbolo, precio). Fuente: `CryptoProvider`.

    ```kotlin
    data class Crypto(
        val rank: Int,
        val logo: Int, // Referencia a Drawable
        val name: String,
        val symbol: String,
        val price: Double
    )
    ```

*   **`Holdings.kt`:** `data class` que representa una criptomoneda poseída en la cartera (logo, nombre, símbolo, cantidad, precio promedio de compra, P/L). Mantenida y calculada por `CryptoViewModel`. Usa valores por defecto.

    ```kotlin
    data class Holdings(
        val logo: Int, // Referencia a Drawable
        val name: String,
        val symbol: String,
        var totalQuantity: Double = 0.0,
        var averagePrice: Double = 0.0,
        var profitLoss: Double = 0.0
    )
    ```
*   **`Transaction.kt`:** `data class` que representa una operación de compra o venta (logo, nombre, símbolo, tipo, cantidad, precio/unidad, fecha). Creada por `CryptoViewModel`.

    ```kotlin
    data class Transaction(
        val logo: Int, // Referencia a Drawable
        val name: String,
        val symbol: String,
        val type: TransactionType,
        val quantity: Double,
        val pricePerUnit: Double,
        val date: Date, // Fecha de la transacción
    )
    ```
*   **`TransactionType.kt`:** `enum` simple (`BUY`, `SELL`).

    ```kotlin
    enum class TransactionType {
        BUY, SELL
    }
    ```

### 4.2. Data Source (`com.example.cryptowallet.data`) 💾

*   **`CryptoProvider.kt`:** Objeto `companion` que actúa como un **repositorio simulado y estático**. Proporciona una lista predefinida de objetos `Crypto`. `CryptoViewModel` depende de él para obtener la lista de mercado y los precios "actuales".

    ```kotlin
    class CryptoProvider {
        companion object {
            val cryptoList = listOf<Crypto>(
                Crypto(1, R.drawable.btc, "Bitcoin", "BTC", 85251.57),
                Crypto(2, R.drawable.eth, "Ethereum", "ETH", 2110.37),
                // ... más criptos ...
            )
        }
    }
    ```

### 4.3. ViewModel (`com.example.cryptowallet.viewModel`) 🧠

*   **`CryptoViewModel.kt`:**
    *   **Gestión de Estado:** Mantiene el estado mutable (`_availableBalance`, `_holdingsList`, `_transactionHistory`, etc.) usando `MutableLiveData`.
    *   **Exposición de Datos:** Expone el estado como `LiveData<List<...>>` inmutable.
    *   **Lógica de Negocio/Presentación:** Ver descripción detallada en la sección [Patrón Arquitectónico](#2-patrón-arquitectónico-mvvm).

### 4.4. Views (`com.example.cryptowallet.views`) 🖼️

*   **`MainActivity.kt`:** Configura navegación global (Toolbar, Drawer, NavController). Contenedor principal.
*   **`MarketFragment.kt`:** Muestra lista de `Crypto` (`CryptoAdapter`), implementa búsqueda (`SearchView`), observa `LiveData`.
*   **`HoldingsFragment.kt`:** Muestra lista de `Holdings` (`HoldingsAdapter`), muestra totales de cartera, botones de saldo, observa `LiveData`.
*   **`TransactionFragment.kt`:** Muestra lista de `Transaction` (`TransactionAdapter`), observa `LiveData`.

### 4.5. Adapters (`com.example.cryptowallet.adapters`) 🔌

*   Vinculan datos a items de `RecyclerView`.
*   **Manejo de Interacción:** Capturan clics (`setOnClickListener`).
*   **Gestión de Diálogos:** Muestran `AlertDialog` personalizados para compra/venta.
*   **Delegación:** Llaman a métodos del `ViewModel` para procesar acciones.

## 5. 🌊 Flujos de Datos

### 5.1. Carga Inicial de Datos ➡️

1.  `Fragment` (View) se crea.
2.  `Fragment` obtiene `CryptoViewModel`.
3.  `Fragment` observa `LiveData` (ej. `viewModel.holdingsList.observe(...)`).
4.  `ViewModel` expone datos (de `CryptoProvider` o estado interno).
5.  `Fragment` actualiza UI (poblando `Adapter`).

### 5.2. Proceso de Compra 🛒➡️

1.  Usuario toca item en `MarketFragment`.
2.  `CryptoAdapter` muestra diálogo de compra.
3.  Usuario introduce datos y confirma.
4.  `CryptoAdapter` llama a `viewModel.addTransaction(...)`.
5.  `CryptoViewModel`:
    - a. Valida saldo (`withdrawBalance`).
    - b. Si ok, resta costo a `_availableBalance`.
    - c. **Crea** `Transaction`.
    - d. Añade a `_transactionHistory` (y ordena).
    - e. Llama a `updateHoldings(newTransaction)`.
    - f. `updateHoldings`: **Crea/Actualiza** `Holdings` en `_holdingsList`.
    - g. Llama a `calculateHoldings()` y `calculateTotalHoldingsValue()`.
6.  `LiveData` modificados notifican a los `Fragments`.
7.  `HoldingsFragment` y `TransactionFragment` actualizan sus vistas.

### 5.3. Cálculo de Ganancias/Pérdidas (P/L) 📈➡️

1.  Ocurre tras transacción (`updateHoldings` llama a `calculateHoldings`).
2.  `CryptoViewModel.calculateHoldings()`:
    - a. Itera sobre `holdingsList`.
    - b. Obtiene precio actual (`getCryptoPrice`).
    - c. Calcula P/L para cada holding.
    - d. Actualiza `holding.profitLoss` (idealmente creando nueva instancia con `copy()`).
3.  Actualiza `_holdingsList` para notificar cambios.
4.  Suma P/L individuales y actualiza `_totalProfitLoss`.

## 6. 🔗 Dependencias

*   **Kotlin Standard Library** (`org.jetbrains.kotlin:kotlin-stdlib`)
*   **AndroidX Core KTX** (`androidx.core:core-ktx`)
*   **AndroidX AppCompat** (`androidx.appcompat:appcompat`)
*   **Material Components** (`com.google.android.material:material`)
*   **AndroidX Lifecycle (ViewModel & LiveData KTX)** (`androidx.lifecycle:lifecycle-viewmodel-ktx`, `androidx.lifecycle:lifecycle-livedata-ktx`)
*   **AndroidX Navigation (Fragment & UI KTX)** (`androidx.navigation:navigation-fragment-ktx`, `androidx.navigation:navigation-ui-ktx`)
*   **AndroidX RecyclerView** (`androidx.recyclerview:recyclerview`)

---
