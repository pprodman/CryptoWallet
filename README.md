# CryptoWallet App 🪙

![Kotlin](https://img.shields.io/badge/Kotlin-1.8.x-blue?logo=kotlin) ![Android](https://img.shields.io/badge/Android-Studio-green?logo=androidstudio) ![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange)

Una aplicación Android sencilla desarrollada en Kotlin que simula una cartera de criptomonedas. Permite a los usuarios ver precios de mercado (datos de ejemplo), gestionar sus tenencias (comprar/vender criptomonedas simuladas), rastrear transacciones y administrar su saldo.

## ✨ Características Principales

- 📊 Exploración del mercado de criptomonedas
- 💰 Compra y venta de criptomonedas
- 📈 Seguimiento de tenencias de criptomonedas
- 📜 Historial completo de transacciones
- 📅 Selección de fechas para transacciones

Para descubrir la aplicación de CryptoWallet, sigue el siguiente [Tutorial](./TUTORIAL.md).


## 🏗️ Arquitectura y Tecnologías Utilizadas

-   **Lenguaje:** Kotlin
-   **Arquitectura:** MVVM (Model-View-ViewModel)
-   **Componentes de Android Jetpack:**
    -   `ViewModel`
    -   `LiveData`
    -   `Navigation Component` (para la navegación entre fragments)
    -   `RecyclerView` (para mostrar listas eficientemente)
-   **Interfaz de Usuario:**
    -   Layouts XML
    -   `Material Components` (Buttons, TextViews, ImageView, CardView, Dialogs, Toolbar, NavigationView, DrawerLayout, Spinner)
-   **Gestión de Dependencias:** Gradle

Encontrarás información más detallada sobre la arquitectura y el desarrollo de la aplicación en la [Guia de Referencia](./GUIA_REFERENCIA.md)


## 🚀 Instalación y Ejecución

1.  **Clona el repositorio:**
    ```bash
    git clone https://github.com/pprodman/CryptoWallet.git
    ```
2.  **Abre el proyecto:** Abre Android Studio e importa el proyecto clonado.
3.  **Sincroniza Gradle:** Espera a que Android Studio descargue y sincronice todas las dependencias necesarias.
4.  **Ejecuta la aplicación:** Selecciona un emulador o conecta un dispositivo físico y presiona el botón 'Run'.

Una vez listo para su distribución o instalación, sigue la [Guia de Lanzamiento](./LANZAMIENTO.md).

➡️ **[Acceder a Releases](https://github.com/pprodman/CryptoWallet/releases)** ⬅️

## 📄 Licencia

Este proyecto es para fines educativos y de demostración. Siéntete libre de usarlo y modificarlo.

---
