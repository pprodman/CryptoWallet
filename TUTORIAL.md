# Tutorial de CryptoWallet: ¡Gestiona tu Cartera Cripto! 🪙

¡Bienvenido a CryptoWallet! Esta guía te mostrará cómo usar las funciones principales de la aplicación para seguir el mercado, comprar y vender criptomonedas (simuladas) y gestionar tu cartera.

1. [Primeros Pasos y Navegación](#1-📦-primeros-pasos-y-navegación)
2. [Explorando el Mercado](#2-💰-explorando-el-mercado-market)
3. [Gestionando tu Cartera](#3-📈-gestionando-tu-cartera-holdings)
4. [Revisando tu historial](#4-📜-revisando-tu-historial-transactions)

**Nota:** Esta aplicación utiliza datos de ejemplo y simula transacciones. No se maneja dinero real.

---

## 1. 📦 Primeros Pasos y Navegación

Al abrir la aplicación, verás la pantalla principal (probablemente el Mercado o tu Cartera, dependiendo de la última vista o configuración inicial).

*   **Barra Superior (Toolbar):** Muestra el título de la sección actual y el icono del menú (☰).
*   **Menú Lateral (Navigation Drawer):** Toca el icono (☰) en la esquina superior izquierda para abrir el menú. Desde aquí puedes navegar a las secciones principales:
    *   **Market:** Para ver la lista de criptomonedas disponibles y comprar.
    *   **Holdings:** Para ver tu cartera, gestionar tu saldo y vender.
    *   **Transactions:** Para ver tu historial de compras y ventas.
*   **Selector de Cuenta (Spinner):** En la parte superior del menú lateral, puedes ver un selector (Spinner) con direcciones de correo electrónico (ejemplo).

`[IMAGEN: Captura de pantalla con el Navigation Drawer abierto, señalando las opciones Market, Holdings, Transactions y el Spinner.]`

---

## 2. 💰 Explorando el Mercado (Market)

Esta sección te muestra una lista de criptomonedas disponibles con información clave.

*   **Lista de Criptomonedas:** Cada fila muestra:
    *   **#Rank:** La clasificación de la criptomoneda.
    *   **Logo:** El icono identificativo.
    *   **Name:** El nombre completo.
    *   **Symbol:** El ticker (ej. BTC, ETH).
    *   **Price:** El precio actual simulado por unidad.
*   **Buscar:** Utiliza la barra de búsqueda en la parte superior para filtrar la lista por nombre o símbolo. Escribe "Bit" y verás cómo la lista se reduce a las criptomonedas que coinciden.

`[IMAGEN: Captura de pantalla de la pantalla Market, mostrando la lista y señalando la barra de búsqueda.]`
`[IMAGEN: Captura de pantalla mostrando resultados filtrados después de usar la búsqueda.]`

### Cómo Comprar Criptomonedas:

1.  **Selecciona una Cripto:** En la pantalla "Market", toca la fila de la criptomoneda que deseas comprar (por ejemplo, Bitcoin).
2.  **Abre el Diálogo de Compra:** Se abrirá una ventana emergente (diálogo).
    *   Verás el logo y símbolo de la cripto.
    *   Se mostrará tu **Saldo Disponible (Available Balance)** actual.
    *   **Quantity:** Introduce la cantidad de criptomoneda que quieres comprar (ej. 0.05).
    *   **Price:** Introduce el precio por unidad al que estás comprando (la app podría pre-rellenarlo con el precio de mercado, pero puedes ajustarlo si es una simulación histórica).
    *   **Date:** Toca este campo para seleccionar la fecha de la transacción usando un calendario.
3.  **Confirma:** Toca el botón **"ADD"** (o "Comprar").
    *   La aplicación comprobará si tienes saldo suficiente (`Cantidad * Precio <= Saldo Disponible`). Si no es suficiente, recibirás un mensaje.
    *   Si tienes saldo, la transacción se registrará, tu saldo disminuirá y la criptomoneda aparecerá en tu sección "Holdings".

`[IMAGEN: Captura de pantalla del diálogo de compra (add_transaction_dialog.xml), con campos para cantidad, precio y fecha señalados.]`

---

## 3. 📈 Gestionando tu Cartera (Holdings)

Aquí es donde ves las criptomonedas que posees y gestionas tu saldo.

*   **Resumen Superior:** En la parte superior, verás:
    *   **Balance:** Tu saldo disponible actual en $.
    *   **Total Holdings Value:** El valor total actual de todas tus criptomonedas.
    *   **Total Profit/Loss:** La ganancia o pérdida total acumulada de tu cartera (verde si es ganancia, rojo si es pérdida).
*   **Lista de Tenencias (Holdings):** Debajo del resumen, cada fila representa una criptomoneda que posees:
    *   **Logo, Name (Symbol):** Identificación de la cripto.
    *   **Precio Actual:** El precio de mercado actual por unidad.
    *   **AVG Buy Price:** El precio promedio al que compraste esa criptomoneda.
    *   **Amount:** La cantidad total que posees de esa cripto.
    *   **Value:** El valor total actual de tu tenencia (`Cantidad * Precio Actual`).
    *   **Profit/Loss:** La ganancia o pérdida para esa tenencia específica (verde/rojo).

`[IMAGEN: Captura de pantalla de la pantalla Holdings, señalando el resumen superior y una fila de ejemplo en la lista de tenencias.]`

### Cómo Añadir Saldo:

1.  En la pantalla "Holdings", toca el botón **"ADD BALANCE"**.
2.  Se abrirá un diálogo. Introduce la cantidad de saldo que deseas añadir.
3.  Toca **"ADD"**. Tu saldo disponible se actualizará.

`[IMAGEN: Captura de pantalla del diálogo para añadir saldo (add_saldo_dialog.xml).]`

### Cómo Retirar Saldo:

1.  En la pantalla "Holdings", toca el botón **"WITHDRAW BALANCE"**.
2.  Se abrirá un diálogo que muestra tu saldo actual. Introduce la cantidad que deseas retirar.
3.  Toca **"WITHDRAW"**.
    *   La app comprobará que no intentas retirar más de lo que tienes.
    *   Si es válido, tu saldo disminuirá.

`[IMAGEN: Captura de pantalla del diálogo para retirar saldo (withdraw_saldo_dialog.xml), mostrando el saldo actual.]`

### Cómo Vender Criptomonedas:

1.  **Selecciona una Tenencia:** En la pantalla "Holdings", toca la fila de la criptomoneda que deseas vender (por ejemplo, la fila de Ethereum si posees ETH).
2.  **Abre el Diálogo de Venta:** Se abrirá un diálogo similar al de compra.
    *   Verás el logo y símbolo.
    *   **Quantity:** Introduce la cantidad que quieres vender. El campo te dará una pista (`hint`) de la cantidad máxima que posees. **No puedes vender más de lo que tienes.**
    *   **Price:** Introduce el precio por unidad al que estás vendiendo.
    *   **Date:** Selecciona la fecha de la venta.
3.  **Confirma:** Toca el botón **"ADD"** (o "Vender").
    *   La aplicación comprobará si la cantidad a vender es válida (menor o igual a tu tenencia).
    *   Si es válida, la transacción se registrará, tu saldo aumentará (`Cantidad * Precio`), y la cantidad de esa criptomoneda en tus "Holdings" disminuirá (o desaparecerá si vendiste todo).

`[IMAGEN: Captura de pantalla del diálogo de venta (es el mismo add_transaction_dialog.xml, pero usado para vender), señalando la pista de cantidad disponible.]`

---

## 4. 📜 Revisando tu Historial (Transactions)

Esta pantalla muestra un registro de todas las compras y ventas que has realizado.

*   **Lista de Transacciones:** Cada fila representa una transacción pasada:
    *   **Logo, Name, Symbol:** Identificación de la cripto.
    *   **Type:** Indica si fue **BUY** (Compra, usualmente en verde) o **SELL** (Venta, usualmente en rojo).
    *   **Amount:** La cantidad de criptomoneda involucrada en esa transacción.
    *   **Value:** El valor total en $ de esa transacción (`Cantidad * Precio Por Unidad` de esa transacción).
    *   **Date:** La fecha en que se realizó la transacción.

`[IMAGEN: Captura de pantalla de la pantalla Transactions, mostrando algunas transacciones de compra y venta con sus detalles.]`

---

¡Eso es todo! Ahora sabes cómo navegar por CryptoWallet, explorar el mercado, comprar y vender criptomonedas simuladas, gestionar tu saldo y revisar tu historial. ¡Disfruta gestionando tu cartera virtual!
