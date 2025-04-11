# 📖 Tutorial de CryptoWallet: ¡Gestiona tu Cartera Cripto! 🪙

¡Bienvenido a CryptoWallet! 👋 Esta guía te mostrará cómo usar las funciones principales de la aplicación para seguir el mercado, comprar y vender criptomonedas (simuladas) y gestionar tu cartera.

1.  [🚀 Primeros Pasos y Navegación](#1--primeros-pasos-y-navegación)
2.  [🛒 Explorando el Mercado (Market)](#2--explorando-el-mercado-market)
3.  [📊 Gestionando tu Cartera (Holdings)](#3--gestionando-tu-cartera-holdings)
4.  [📜 Revisando tu Historial (Transactions)](#4--revisando-tu-historial-transactions)

**⚠️ Nota:** Esta aplicación utiliza datos de ejemplo y simula transacciones. No se maneja dinero real.

---

## 1. 🚀 Primeros Pasos y Navegación

Al abrir la aplicación, verás la pantalla principal (probablemente el Mercado o tu Cartera).

*   **⬆️ Barra Superior (Toolbar):** Muestra el título de la sección actual y el icono del menú (☰).
*   **🧭 Menú Lateral (Navigation Drawer):** Toca el icono (☰) en la esquina superior izquierda para abrir el menú. Desde aquí puedes navegar a las secciones principales:
    *   🛒 **Market:** Para ver la lista de criptomonedas disponibles y comprar.
    *   📊 **Holdings:** Para ver tu cartera, gestionar tu saldo y vender.
    *   📜 **Transactions:** Para ver tu historial de compras y ventas.
*   👤 **Selector de Cuenta (Spinner):** En la parte superior del menú lateral, puedes ver un selector (Spinner) con direcciones de correo electrónico (ejemplo).

<img src="https://github.com/pprodman/CryptoWallet/blob/master/misc/mainmenu.png" alt="Main Menu" width="250"/>

---

## 2. 🛒 Explorando el Mercado (Market)

Esta sección te muestra una lista de criptomonedas disponibles con información clave.

*   **📋 Lista de Criptomonedas:** Cada fila muestra:
    *   **#️⃣ Rank:** La clasificación de la criptomoneda.
    *   **🖼️ Logo:** El icono identificativo.
    *   **📛 Name:** El nombre completo.
    *   **🔣 Symbol:** El ticker (ej. BTC, ETH).
    *   **💲 Price:** El precio actual simulado por unidad.
*   **🔍 Buscar:** Utiliza la barra de búsqueda en la parte superior para filtrar la lista por nombre o símbolo. Escribe "Bit" y verás cómo la lista se reduce a las criptomonedas que coinciden.

<img src="https://github.com/pprodman/CryptoWallet/blob/master/misc/market.png" alt="Market" width="250"/>
<img src="https://github.com/pprodman/CryptoWallet/blob/master/misc/marketsearch.png" alt="Market Seach" width="250"/>

### 🌱 Cómo Comprar Criptomonedas:

1.  **👆 Selecciona una Cripto:** En la pantalla "Market", toca la fila de la criptomoneda que deseas comprar (por ejemplo, Bitcoin).
2.  ** P̶o̶p̶u̶p̶  ** Abre el Diálogo de Compra:** Se abrirá una ventana emergente (diálogo).
    *   Verás el logo y símbolo de la cripto.
    *   Se mostrará tu **💰 Saldo Disponible (Available Balance)** actual.
    *   **🔢 Quantity:** Introduce la cantidad de criptomoneda que quieres comprar (ej. 0.05).
    *   **🏷️ Price:** Introduce el precio por unidad al que estás comprando.
    *   **📅 Date:** Toca este campo para seleccionar la fecha de la transacción usando un calendario.
3.  **✅ Confirma:** Toca el botón **"ADD"**.
    *   La aplicación comprobará si tienes saldo suficiente. Si no, recibirás un mensaje 🚫.
    *   Si tienes saldo, la transacción se registrará, tu saldo disminuirá y la criptomoneda aparecerá en tu sección "Holdings". ¡Éxito! ✨

`[IMAGEN: Captura de pantalla del diálogo de compra (add_transaction_dialog.xml), con campos para cantidad, precio y fecha señalados.]`

---

## 3. 📊 Gestionando tu Cartera (Holdings)

Aquí es donde ves las criptomonedas que posees y gestionas tu saldo.

*   **🔝 Resumen Superior:** En la parte superior, verás:
    *   **💵 Balance:** Tu saldo disponible actual en $.
    *   **🏦 Total Holdings Value:** El valor total actual de todas tus criptomonedas.
    *   **📈 Total Profit/Loss:** La ganancia o pérdida total acumulada (🟢 si es ganancia, 🔴 si es pérdida).
*   **📑 Lista de Tenencias (Holdings):** Debajo del resumen, cada fila representa una criptomoneda que posees:
    *   **🖼️ Name (Symbol):** Identificación de la cripto.
    *   **💲 Precio Actual:** El precio de mercado actual.
    *   **📉 AVG Buy Price:** El precio promedio al que compraste.
    *   **🔢 Amount:** La cantidad total que posees.
    *   **💰 Value:** El valor total actual de tu tenencia.
    *   **📊 Profit/Loss:** La ganancia o pérdida específica (🟢/🔴).

`[IMAGEN: Captura de pantalla de la pantalla Holdings, señalando el resumen superior y una fila de ejemplo en la lista de tenencias.]`

### ➕ Cómo Añadir Saldo:

1.  En la pantalla "Holdings", toca el botón **"ADD BALANCE"**.
2.  Introduce la cantidad de saldo que deseas añadir en el diálogo.
3.  Toca **"ADD"**. Tu saldo se actualizará.

`[IMAGEN: Captura de pantalla del diálogo para añadir saldo (add_saldo_dialog.xml).]`

### ➖ Cómo Retirar Saldo:

1.  En la pantalla "Holdings", toca el botón **"WITHDRAW BALANCE"**.
2.  Introduce la cantidad que deseas retirar (verás tu saldo actual).
3.  Toca **"WITHDRAW"**.
    *   La app comprobará que no retiras más de lo que tienes 🚫.
    *   Si es válido, tu saldo disminuirá.

`[IMAGEN: Captura de pantalla del diálogo para retirar saldo (withdraw_saldo_dialog.xml), mostrando el saldo actual.]`

### 💸 Cómo Vender Criptomonedas:

1.  **👆 Selecciona una Tenencia:** En la pantalla "Holdings", toca la fila de la criptomoneda que deseas vender.
2.  ** P̶o̶p̶u̶p̶  ** Abre el Diálogo de Venta:** Se abrirá un diálogo similar al de compra.
    *   Verás el logo y símbolo.
    *   **🔢 Quantity:** Introduce la cantidad a vender (verás la cantidad máxima disponible). **¡No puedes vender más de lo que tienes!**
    *   **🏷️ Price:** Introduce el precio por unidad al que estás vendiendo.
    *   **📅 Date:** Selecciona la fecha de la venta.
3.  **✅ Confirma:** Toca el botón **"ADD"**.
    *   La app comprobará si la cantidad es válida 🚫.
    *   Si es válida, la transacción se registrará, tu saldo aumentará, y tu tenencia disminuirá (o desaparecerá si vendiste todo). ¡Éxito! ✨

`[IMAGEN: Captura de pantalla del diálogo de venta (es el mismo add_transaction_dialog.xml, pero usado para vender), señalando la pista de cantidad disponible.]`

---

## 4. 📜 Revisando tu Historial (Transactions)

Esta pantalla muestra un registro de todas las compras y ventas que has realizado, ordenadas por fecha (¡la más reciente primero!).

*   **⏳ Lista de Transacciones:** Cada fila representa una transacción pasada:
    *   **🖼️ Name (Symbol):** Identificación de la cripto.
    *   **↔️ Type:** Indica si fue **BUY** (🟢 Compra) o **SELL** (🔴 Venta).
    *   **🔢 Amount:** La cantidad de criptomoneda involucrada.
    *   **💲 Value:** El valor total en $ de esa transacción específica.
    *   **🗓️ Date:** La fecha en que se realizó.

`[IMAGEN: Captura de pantalla de la pantalla Transactions, mostrando algunas transacciones de compra y venta con sus detalles.]`

---
