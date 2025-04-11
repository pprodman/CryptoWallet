# Guía de Referencia del Proyecto: CryptoWallet App

**Versión:** 1.0 (Fecha: [Inserta Fecha Actual])

**Autor/es:** [Tu Nombre/Equipo]

1. [Introducción](#1-introducción)
2. [Patrón Arquitectónico: MVVM](#2-patrón-arquitectónico-mvvm)
3. [Estructura de Directorios (Paquetes)](#3-estructura-de-directorios-paquetes)
4. [Componentes Principales y Responsabilidades](#4-componentes-principales-y-responsabilidades)
5. [Flujos de Datos](#5-flujos-de-datos)
6. [Dependencias](#6-dependencias)
7. [Consideraciones y Puntos de Interés](#7-consideraciones-y-puntos-de-interés)


## 1. Introducción

Este documento sirve como referencia técnica para la aplicación Android CryptoWallet. Describe la arquitectura, los componentes principales, los flujos de datos clave y las decisiones de diseño implementadas. El objetivo es facilitar el mantenimiento, la extensión y la comprensión del código base.

La aplicación es un simulador de cartera de criptomonedas desarrollado en Kotlin, siguiendo el patrón arquitectónico MVVM.

## 2. Patrón Arquitectónico: MVVM

La aplicación adopta el patrón **Model-View-ViewModel (MVVM)** para separar las responsabilidades y mejorar la mantenibilidad y testeabilidad:

1.  **Model (Modelo):**
    *   **Responsabilidad:** Representa los datos y la lógica de negocio de la aplicación. Es independiente de la interfaz de usuario. Gestiona el acceso a los datos, ya sea desde una fuente local (base de datos, archivos) o remota (API).
    *   **En este proyecto:**
        *   **Clases de Datos:** `Crypto.kt`, `Holdings.kt`, `Transaction.kt`, `TransactionType.kt`. Definen la estructura de los objetos de datos utilizados en la aplicación.
        *   **Fuente de Datos:** `CryptoProvider.kt`. Actúa como un repositorio *simplificado* y estático que provee la lista inicial de criptomonedas. En una aplicación real, esta capa contendría clases `Repository` que interactuarían con APIs de red (Retrofit, Ktor) y/o bases de datos locales (Room).

2.  **View (Vista):**
    *   **Responsabilidad:** Es la capa de interfaz de usuario (UI). Muestra los datos al usuario y captura sus interacciones (clics, entradas de texto, etc.). En Android, esto corresponde a Activities, Fragments y sus Layouts XML. La Vista *observa* los cambios en los datos expuestos por el ViewModel (generalmente a través de `LiveData` o `StateFlow`) y actualiza la UI en consecuencia. Delega la lógica de las interacciones del usuario al ViewModel.
    *   **En este proyecto:**
        *   `MainActivity.kt`: Actúa como el host principal para los fragments y configura la navegación (Toolbar, Navigation Drawer).
        *   Fragments (`MarketFragment.kt`, `HoldingsFragment.kt`, `TransactionFragment.kt`): Cada fragment representa una pantalla específica de la aplicación. Son responsables de inflar sus layouts, configurar los `RecyclerViews` y *observar* los `LiveData` del `CryptoViewModel`.
        *   Layouts XML (`fragment_market.xml`, `item_crypto.xml`, `add_transaction_dialog.xml`, etc.): Definen la estructura visual de las pantallas y los elementos de lista.
        *   Adapters (`CryptoAdapter.kt`, `HoldingsAdapter.kt`, `TransactionAdapter.kt`): Aunque residen en su propio paquete, son componentes auxiliares de la Vista. Adaptan los datos (recibidos indirectamente del ViewModel a través del Fragment) para ser mostrados en los `RecyclerViews`. También manejan los clics en los items y la lógica de los diálogos (delegando la acción final al ViewModel).

3.  **ViewModel (Vista-Modelo):**
    *   **Responsabilidad:** Actúa como un intermediario entre el Model y la View. Expone los datos relevantes para la UI (generalmente a través de `LiveData` o `StateFlow`) de manera que la Vista pueda observarlos. Contiene la lógica de presentación y responde a las interacciones del usuario delegadas por la Vista, interactuando con el Model para obtener o modificar datos. Está diseñado para sobrevivir a cambios de configuración (como rotaciones de pantalla), manteniendo el estado de la UI.
    *   **En este proyecto:**
        *   `CryptoViewModel.kt`: Es el único ViewModel en esta aplicación.
            *   Mantiene el estado de la UI (`_availableBalance`, `_holdingsList`, `_transactionHistory`, etc.) usando `MutableLiveData`.
            *   Expone estos datos a los Fragments como `LiveData` inmutables.
            *   Contiene la lógica para:
                *   Añadir/Retirar saldo (`addBalance`, `withdrawBalance`).
                *   Procesar transacciones (`addTransaction`), incluyendo la actualización del saldo y las tenencias.
                *   Calcular valores derivados (`calculateTotalHoldingsValue`, `calculateHoldings` para profit/loss).
                *   Obtener datos del "Model" (`CryptoProvider.cryptoList`, `getCryptoPrice`).


## 3. Estructura de Directorios (Paquetes)

```css
com.example.cryptowallet
│
├── MainActivity.kt             # Host principal, configuración de navegación
│
├── adapters/                   # Adaptadores para RecyclerViews
│   ├── CryptoAdapter.kt       # Muestra lista de mercado, maneja clics de compra
│   ├── HoldingsAdapter.kt     # Muestra lista de cartera, maneja clics de venta
│   └── TransactionAdapter.kt  # Muestra historial de transacciones
│
├── data/                       # Acceso y gestión de datos
│   └── CryptoProvider.kt       # Fuente de datos ESTÁTICA para precios de mercado
│
├── models/                     # Definiciones de objetos de datos (data classes)
│   ├── Crypto.kt
│   ├── Holdings.kt
│   ├── Transaction.kt
│   └── TransactionType.kt     # Enum para tipos de transacción
│
├── viewModel/                  # ViewModels
│   └── CryptoViewModel.kt      # Único ViewModel, gestiona todo el estado y lógica
│
└── views/                      # Fragments (Pantallas de la UI)
    ├── HoldingsFragment.kt
    ├── MarketFragment.kt
    └── TransactionFragment.kt

res/                            # Recursos
├── layout/                     # Diseños XML (Activity, Fragments, Items, Dialogs)
├── drawable/                   # Recursos gráficos (iconos cripto)
├── values/                     # Strings, colores, dimens, styles
└── navigation/                 # Gráfico de navegación (nav_graph.xml)
```

## 4. Componentes Principales y Responsabilidades

### 4.1. Models (`com.example.cryptowallet.models`)

*   **`Crypto.kt`:** `data class` que representa una criptomoneda en el mercado (rank, logo, nombre, símbolo, precio). Fuente: `CryptoProvider`.

    ```kotlin
    data class Crypto(
        val rank: Int,
        val logo: Int,
        val name: String,
        val symbol: String,
        val price: Double
    )
    ```

*   **`Holdings.kt`:** `data class` que representa una criptomoneda poseída en la cartera (logo, nombre, símbolo, cantidad, precio promedio de compra, P/L). Mantenida y calculada por `CryptoViewModel`. Usa valores por defecto para facilitar la inicialización.

    ```kotlin
    data class Holdings(
        val logo: Int,
        val name: String,
        val symbol: String,
        var totalQuantity: Double = 0.0,
        var averagePrice: Double = 0.0,
        var profitLoss: Double = 0.0,
    )
    ```
*   **`Transaction.kt`:** `data class` que representa una operación de compra o venta (logo, nombre, símbolo, tipo, cantidad, precio/unidad, fecha). Creada por `CryptoViewModel` tras acción del usuario.

    ```kotlin
    data class Transaction(
        val logo: Int,
        val name: String,
        val symbol: String,
        val type: TransactionType,
        val quantity: Double,
        val pricePerUnit: Double,
        val date: Date,
    )
    ```
*   **`TransactionType.kt`:** `enum` simple (`BUY`, `SELL`) para definir el tipo de transacción.

    ```kotlin
    enum class TransactionType {
        BUY, SELL
    }
    ```

### 4.2. Data Source (`com.example.cryptowallet.data`)

*   **`CryptoProvider.kt`:** Objeto `companion` que actúa como un **repositorio simulado y estático**. Proporciona una lista predefinida de objetos `Crypto`. En una implementación real, sería reemplazado por un `Repository` que interactúe con una API o base de datos. `CryptoViewModel` depende de él para obtener la lista de mercado y los precios "actuales".

    ```kotlin
    class CryptoProvider {
        companion object {
            val cryptoList = listOf<Crypto>(
                Crypto(1, R.drawable.btc, "Bitcoin", "BTC", 85251.57),
                Crypto(2, R.drawable.eth, "Ethereum", "ETH", 2110.37),
                Crypto(3, R.drawable.xrp, "Ripple", "XRP", 2.47),
                Crypto(4, R.drawable.usdt, "Tether USDt", "USDT", 1.0),
                ...
                ...
            )
        }
    }
    ```

### 4.3. ViewModel (`com.example.cryptowallet.viewModel`)

*   **`CryptoViewModel.kt`:**
    *   **Gestión de Estado:** Mantiene el estado de la UI (`availableBalance`, `holdingsList`, `transactionHistory`, `totalHoldingsValue`, `totalProfitLoss`) usando `MutableLiveData`.
    *   **Exposición de Datos:** Expone el estado a las Vistas como `LiveData` inmutables para observación segura.
    *   **Lógica de Negocio/Presentación:**
        *   `addBalance`/`withdrawBalance`: Modifica el saldo disponible.
        *   `addTransaction`: Orquesta el proceso de añadir una transacción: valida (saldo/cantidad), actualiza el saldo, crea el objeto `Transaction`, actualiza el historial y llama a `updateHoldings`.
        *   `updateHoldings`: Lógica clave para crear o actualizar (`copy()`) las instancias de `Holdings` basándose en una `Transaction`. Recalcula el `averagePrice` en las compras.
        *   `calculateHoldings`: Itera sobre `holdingsList`, obtiene el precio actual de `CryptoProvider` (`getCryptoPrice`), calcula y actualiza la propiedad `profitLoss` de cada `Holdings`.
        *   `calculateTotalHoldingsValue`/`_totalProfitLoss`: Calcula los valores agregados de la cartera.
        *   `getCryptoPrice`: Método de conveniencia para buscar el precio actual en `CryptoProvider.cryptoList`.

### 4.4. Views (`com.example.cryptowallet.views`)

*   **`MainActivity.kt`:** Configura la `Toolbar`, `DrawerLayout`, `NavigationView` y el `NavController`. Actúa como contenedor principal.
*   **`MarketFragment.kt`:** Muestra la lista de `Crypto` usando `CryptoAdapter`. Implementa la funcionalidad de búsqueda (`SearchView`). Observa `viewModel.cryptoList` (aunque en este caso es estática) y `viewModel.availableBalance` (para mostrar en diálogo).
*   **`HoldingsFragment.kt`:** Muestra la lista de `Holdings` usando `HoldingsAdapter`. Muestra los valores totales (saldo, valor cartera, P/L total) observando los `LiveData` correspondientes del `ViewModel`. Contiene los botones y diálogos para añadir/retirar saldo.
*   **`TransactionFragment.kt`:** Muestra la lista de `Transaction` usando `TransactionAdapter`. Observa `viewModel.transactionHistory`.

### 4.5. Adapters (`com.example.cryptowallet.adapters`)

*   Responsables de vincular los datos de las listas (`List<Crypto>`, `List<Holdings>`, `List<Transaction>`) a las vistas de los items (`item_crypto.xml`, `item_holdings.xml`, `item_transaction.xml`) dentro de los `RecyclerView`.
*   **Manejo de Interacción:** Capturan los clics en los items (`itemView.setOnClickListener`).
*   **Gestión de Diálogos:** Crean y muestran los `AlertDialog` personalizados (`showBuyCryptoDialog`, `showSellCryptoDialog`) para la entrada de datos de compra/venta.
*   **Delegación:** Recopilan la información del diálogo y llaman a los métodos correspondientes del `CryptoViewModel` para procesar la acción (compra/venta). No contienen lógica de negocio crítica.

## 5. Flujos de Datos

### 5.1. Carga Inicial de Datos

1.  `Fragment` (View) se crea/adjunta.
2.  `Fragment` obtiene instancia de `CryptoViewModel`.
3.  `Fragment` observa (`.observe()`) los `LiveData` necesarios (ej., `holdingsList`, `availableBalance`).
4.  `ViewModel` expone los datos actuales (que obtiene de `CryptoProvider` o su estado interno).
5.  `Fragment` recibe los datos iniciales y actualiza la UI (ej., poblando el `Adapter`).

### 5.2. Proceso de Compra

1.  Usuario toca un item en `MarketFragment`.
2.  `CryptoAdapter` (`onBindViewHolder`/`setOnClickListener`) muestra `showBuyCryptoDialog`.
3.  Usuario introduce cantidad, precio, fecha y confirma ("ADD").
4.  `CryptoAdapter` recopila datos y llama a `viewModel.addTransaction(...)` (o una función envoltorio).
5.  `CryptoViewModel`:
    - a. Valida si hay saldo suficiente (`withdrawBalance`).
    - b. Si es válido, resta el costo del `_availableBalance`.
    - c. **Crea** una nueva instancia de `Transaction`.
    - d. Añade la `Transaction` a `_transactionHistory`.
    - e. Llama a `updateHoldings(newTransaction)`.
    - f. `updateHoldings`: **Crea** un nuevo `Holdings` o **actualiza** uno existente (recalculando `averagePrice`). Actualiza `_holdingsList`.
    - g. Llama a `calculateHoldings()` y `calculateTotalHoldingsValue()` para actualizar P/L y valor total.
6.  Los `LiveData` modificados (`_availableBalance`, `_transactionHistory`, `_holdingsList`, `_totalHoldingsValue`, `_totalProfitLoss`) notifican a los `Fragments` observadores.
7.  `HoldingsFragment` y `TransactionFragment` actualizan sus `RecyclerViews` y `TextViews`.

### 5.3. Cálculo de Ganancias/Pérdidas (P/L)

1.  Ocurre después de cada transacción (`updateHoldings` llama a `calculateHoldings`) o podría ser llamado periódicamente si los precios fueran dinámicos.
2.  `CryptoViewModel.calculateHoldings()`:
    - a. Itera sobre cada `holding` en `_holdingsList.value`.
    - b. Para cada `holding`, llama a `getCryptoPrice(holding.symbol)` para obtener el precio "actual" de `CryptoProvider`.
    - c. Calcula: `profitLoss = (currentPrice - holding.averagePrice) * holding.totalQuantity`.
    - d. Actualiza la propiedad `holding.profitLoss`.
3.  `_holdingsList.value = _holdingsList.value` (se reasigna para notificar al observador que el *contenido* de los objetos ha cambiado, aunque las referencias en la lista sean las mismas si no se usó `copy()`). *Nota: Es más robusto asegurar que `updateHoldings` siempre produzca nuevas instancias o que `calculateHoldings` devuelva una nueva lista.*
4.  `calculateTotalProfitLoss()` suma los `profitLoss` individuales y actualiza `_totalProfitLoss`.

## 6. Dependencias

*   **Kotlin Standard Library**
*   **AndroidX Core KTX:** Extensiones de Kotlin para APIs de Android.
*   **AndroidX AppCompat:** Compatibilidad hacia atrás para componentes de UI.
*   **Material Components for Android:** Implementación de Material Design.
*   **AndroidX Lifecycle (ViewModel & LiveData):** Componentes centrales de MVVM.
*   **AndroidX Navigation (Fragment & UI):** Para la navegación entre Fragments.
*   **AndroidX RecyclerView:** Para mostrar listas eficientemente.

## 7. Consideraciones y Puntos de Interés

*   **Fuente de Datos Estática:** `CryptoProvider` es un cuello de botella para funcionalidad real. Reemplazarlo con una API real y un `Repository` sería la principal mejora.
*   **Inmutabilidad:** Se usa `data class` y `copy()` en `updateHoldings` para promover la inmutabilidad, lo cual facilita el manejo del estado y la detección de cambios en `LiveData`.
*   **Manejo de Estado:** Todo el estado mutable reside dentro del `CryptoViewModel` y se expone reactivamente a través de `LiveData`.
*   **Precisión Flotante:** Las operaciones con `Double` para moneda y cantidad pueden tener problemas de precisión. Para aplicaciones financieras reales, usar `BigDecimal` es imperativo.
*   **Persistencia:** No hay persistencia. El estado se pierde al cerrar la app. Se necesitaría Room, DataStore o similar.
*   **Manejo de Errores:** El manejo de errores es mínimo (ej., mensajes `Toast`). Una app robusta necesitaría un manejo más explícito (estados de error en `LiveData`, diálogos de error, etc.).
*   **Testing:** No hay tests unitarios ni de instrumentación. El patrón MVVM facilita la creación de tests unitarios para el `ViewModel`.

---
