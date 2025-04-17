# 🚀 Guía de Lanzamiento y Generación de Artefacto Firmado (CryptoWallet)

Este documento detalla el proceso para generar los artefactos firmados (`.apk` y/o `.aab`) de la aplicación CryptoWallet, listos para su distribución o instalación. También describe la configuración relevante en el proyecto para los builds de *release*.

## Índice

1.  [Configuración del Proyecto para Release](#1-configuración-del-proyecto-para-release)
    *   [Archivo `build.gradle` (app)](#archivo-buildgradle-app)
    *   [Ofuscación y Optimización (ProGuard/R8)](#ofuscación-y-optimización-proguardr8)
2.  [Generación de la Clave de Firma (Keystore)](#2-generación-de-la-clave-de-firma-keystore)
3.  [Configuración de Firma en `build.gradle`](#3-configuración-de-firma-en-buildgradle)
4.  [Generación del Artefacto Firmado (AAB/APK)](#4-generación-del-artefacto-firmado-aabapk)
    *   [Usando Android Studio (GUI)](#usando-android-studio-gui)
    *   [Usando Gradle (Línea de Comandos)](#usando-gradle-línea-de-comandos)
5.  [Disponibilidad del Artefacto Firmado (GitHub Releases)](#5-disponibilidad-del-artefacto-firmado-github-releases)

---

## 1. Configuración del Proyecto para Release

Antes de generar un artefacto de lanzamiento, es importante revisar y configurar adecuadamente el archivo `build.gradle` a nivel de módulo (`app`).

### Archivo `build.gradle` (app)

Asegúrate de que los siguientes parámetros estén correctamente definidos en `android { defaultConfig { ... } }`:

*   `applicationId`: Identificador único de la aplicación (ej., `com.pprodman.cryptowallet`).
*   `versionCode`: Un número entero incremental que representa la versión del código. Debe aumentarse para cada nuevo lanzamiento.
*   `versionName`: Una cadena de texto visible para el usuario que representa la versión (ej., `"1.0.0"`).

```groovy
// En app/build.gradle
android {
    // ...
    defaultConfig {
        applicationId "com.pprodman.cryptowallet" // Asegúrate que sea el tuyo
        minSdk 24 // O la versión mínima que soportes
        targetSdk 34 // La última versión estable
        versionCode 1     // Incrementar para cada release
        versionName "1.0.0" // Versión visible

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    // ...
}
```

### Ofuscación y Optimización (ProGuard/R8)

Para proteger el código y reducir el tamaño de la aplicación en *release*, se recomienda habilitar R8 (el sucesor de ProGuard).

```groovy
// En app/build.gradle -> android { buildTypes { ... } }
release {
    minifyEnabled true // Habilita la reducción y ofuscación de código
    shrinkResources true // Elimina recursos no utilizados
    // Archivos de reglas de ProGuard/R8:
    // - getDefaultProguardFile('proguard-android-optimize.txt'): Reglas por defecto de Android con optimizaciones.
    // - 'proguard-rules.pro': Tus reglas personalizadas (si necesitas alguna para librerías específicas o reflexión).
    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    // La configuración de firma se definirá más adelante
    // signingConfig signingConfigs.release
}
```

**Nota:** Si habilitas `minifyEnabled`, prueba tu aplicación en modo release exhaustivamente, ya que R8 podría eliminar código que cree necesario pero que es accedido mediante reflexión, lo que podría causar crashes. Podrías necesitar añadir reglas `@Keep` o configuraciones específicas en `proguard-rules.pro`. Para CryptoWallet en su estado actual, es probable que las reglas por defecto sean suficientes.

## 2. Generación de la Clave de Firma (Keystore)

Una clave de firma es esencial para verificar la autenticidad e integridad de tu aplicación.

**¡IMPORTANTE!** El archivo Keystore (`.jks`) y sus contraseñas son **confidenciales**. Guárdalos en un lugar seguro **FUERA** del control de versiones (Git). ¡Perder esta clave te impedirá actualizar tu aplicación en el futuro!

**Si NO tienes una clave:**

1.  Abre Android Studio.
2.  Ve a `Build` > `Generate Signed Bundle / APK...`.
3.  Selecciona `Android App Bundle` o `APK` y haz clic en `Next`.
4.  Haz clic en `Create new...`.
5.  Rellena el formulario:
    *   **Key store path:** Elige dónde guardar el archivo `.jks` (fuera del proyecto).
    *   **Password:** Contraseña FUERTE para el keystore.
    *   **Alias:** Nombre para tu clave (ej., `cryptowalletkey`).
    *   **Password (Key):** Contraseña FUERTE para la clave (puede ser diferente a la del keystore).
    *   **Validity:** 25 años o más.
    *   **Certificate:** Rellena al menos un campo identificativo.
6.  Haz clic en `OK`. Guarda bien las contraseñas y la ubicación del archivo.

**Si YA tienes una clave:** Simplemente localiza tu archivo `.jks` existente.

## 3. Configuración de Firma en `build.gradle`

Para facilitar la generación de builds firmados (especialmente desde línea de comandos o CI), puedes configurar la firma en tu archivo `build.gradle`. **¡CUIDADO AL MANEJAR LAS CONTRASEÑAS!**

**Método Seguro (Usando `gradle.properties`):**

1.  **Crea o Edita `gradle.properties`:** Este archivo se encuentra en la raíz de tu proyecto Android (al mismo nivel que `settings.gradle`). Si no existe, créalo.
2.  **Añade las Variables (NO comitees este archivo si es público):**
    ```properties
    # Archivo: gradle.properties (¡AÑADIR A .gitignore!)
    CRYPTO_RELEASE_STORE_FILE=ruta/absoluta/o/relativa/a/tu/keystore.jks # Cambia esto
    CRYPTO_RELEASE_STORE_PASSWORD=TuContraseñaDelKeystore             # Cambia esto
    CRYPTO_RELEASE_KEY_ALIAS=TuAliasDeClave                           # Cambia esto
    CRYPTO_RELEASE_KEY_PASSWORD=TuContraseñaDeClave                   # Cambia esto
    ```
    *   **¡IMPORTANTE!** Añade `gradle.properties` a tu archivo `.gitignore` para no subir tus contraseñas al repositorio.
    *   La ruta al `CRYPTO_RELEASE_STORE_FILE` puede ser absoluta o relativa al directorio raíz del proyecto.
3.  **Configura `build.gradle` (app):**
    ```groovy
    // En app/build.gradle
    android {
        // ...
        signingConfigs {
            release {
                // Leer desde gradle.properties
                storeFile file(CRYPTO_RELEASE_STORE_FILE)
                storePassword CRYPTO_RELEASE_STORE_PASSWORD
                keyAlias CRYPTO_RELEASE_KEY_ALIAS
                keyPassword CRYPTO_RELEASE_KEY_PASSWORD
            }
        }

        buildTypes {
            release {
                // ... (minifyEnabled, etc.)
                signingConfig signingConfigs.release // Aplicar la configuración de firma al build de release
            }
        }
        // ...
    }
    ```

**Alternativa (Menos Segura - Contraseñas en Variables de Entorno):** Podrías usar variables de entorno del sistema en lugar de `gradle.properties`, lo cual es común en sistemas de CI/CD.

## 4. Generación del Artefacto Firmado (AAB/APK)

Una vez configurada la firma, puedes generar el artefacto de varias maneras:

### Usando Android Studio (GUI)

1.  Ve a `Build` > `Generate Signed Bundle / APK...`.
2.  Selecciona `Android App Bundle` (recomendado) o `APK`. Haz clic en `Next`.
3.  **Si configuraste la firma en `build.gradle`:** Android Studio podría pre-rellenar la información. Verifica que sea correcta.
    **Si NO configuraste la firma:** Selecciona tu archivo keystore, introduce el alias y las contraseñas manualmente (como se describe en el Paso 3 de la [guía anterior](command:_github.copilot.openSymbolFromReferences?%5B%7B%22uri%22%3A%7B%22scheme%22%3A%22file%22%2C%22path%22%3A%22c%3A%2FUsers%2FUSUARIO%2FChatGPT%20Files%2Ffile-M4Y5HIl3R4O4x3lQpE9wO97W%22%2C%22_formatted%22%3A%22file%3A%2F%2F%2Fc%253A%2FUsers%2FUSUARIO%2FChatGPT%2520Files%2Ffile-M4Y5HIl3R4O4x3lQpE9wO97W%22%2C%22query%22%3A%22%22%2C%22fragment%22%3A%22%22%7D%2C%22range%22%3A%7B%22start%22%3A%7B%22line%22%3A72%2C%22character%22%3A0%7D%2C%22end%22%3A%7B%22line%22%3A96%2C%22character%22%3A0%7D%7D%7D%5D "c:\\Users\\USUARIO\\ChatGPT Files\\file-M4Y5HIl3R4O4x3lQpE9wO97W")).
4.  Haz clic en `Next`.
5.  Selecciona la variante `release`.
6.  Haz clic en `Finish`.
7.  Busca el archivo `.aab` o `.apk` en la carpeta de salida (normalmente `app/release` o `app/build/outputs/.../release/`).

### Usando Gradle (Línea de Comandos)

Si has configurado la firma en `build.gradle` (Paso 3), puedes usar la terminal:

1.  Abre una terminal o símbolo del sistema en la **carpeta raíz** de tu proyecto Android (donde está `gradlew`).
2.  **Para generar un AAB:**
    ```bash
    ./gradlew bundleRelease
    # En Windows: gradlew.bat bundleRelease
    ```
3.  **Para generar un APK:**
    ```bash
    ./gradlew assembleRelease
    # En Windows: gradlew.bat assembleRelease
    ```
4.  Los artefactos generados se encontrarán en:
    *   AAB: `app/build/outputs/bundle/release/`
    *   APK: `app/build/outputs/apk/release/`

## 5. Disponibilidad del Artefacto Firmado (GitHub Releases)

Para compartir el artefacto firmado como parte de la entrega del proyecto, se utiliza la función "Releases" de GitHub:

1.  **Ve a tu repositorio** en GitHub (`pprodman/CryptoWallet`).
2.  Haz clic en la pestaña **"Releases"**.
3.  Haz clic en **"Draft a new release"**.
4.  **Crea o elige una etiqueta** (tag) para la versión (ej., `v1.0.0`).
5.  **Añade un título** para el release.
6.  **Adjunta el archivo `.aab`** (o `.apk`) generado en la sección "Attach binaries".
7.  **Publica el release**.

✅ El artefacto firmado estará ahora disponible públicamente para su descarga desde la página de Releases de tu repositorio:

➡️ **[Acceder a Releases](https://github.com/pprodman/CryptoWallet/releases)** ⬅️
*(Asegúrate de que este enlace sea correcto y el release exista)*

---
