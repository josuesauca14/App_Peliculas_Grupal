# 🎬 MovieVault

Aplicación Android para gestionar tu colección personal de películas. Permite agregar, editar, eliminar y marcar películas como favoritas, todo persistido localmente con Room (SQLite).

---

## 📱 Capturas de pantalla

| Home | Detalle | Formulario | Favoritos |
|------|---------|------------|-----------|
| Lista de películas con rating | Info completa de la película | Crear / Editar película | Películas marcadas como ❤️ |

---

## 🚀 Características

- **Listado de películas** con título, fecha, calificación y descripción.
- **Búsqueda en tiempo real** por título desde la pantalla principal.
- **Filtrado por género** mediante chips interactivos.
- **Gestión de estados de UI** (Carga, Éxito, Error y Lista Vacía) para una mejor UX.
- **Detalle completo** de cada película (género, duración, poster, overview).
- **Crear y editar películas** mediante un formulario con selector de fecha y género.
- **Marcar favoritos** y acceder a ellos desde una pantalla dedicada.
- **Eliminar películas** con confirmación mediante diálogo.
- **Datos iniciales** precargados automáticamente al instalar la app.
- **Persistencia local** con Room (SQLite) — sin conexión a internet requerida.

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM (Model - View - ViewModel)** con una arquitectura en capas:

```
com.example.movievault
├── data
│   ├── local          # Room: MovieDatabase, MovieDao, MovieEntity
│   ├── model          # Modelo de dominio: Movie
│   └── repository     # MovieRepository, MovieMapper
├── ui
│   ├── components     # MovieCard, MovieRatingBar
│   ├── screens        # HomeScreen, DetailScreen, FavoritesScreen, MovieFormScreen
│   └── theme          # Color, Type, Theme
└── viewmodel          # HomeViewModel, DetailViewModel, FavoritesViewModel, MovieFormViewModel, MovieViewModelFactory
```

### Flujo de datos

```
UI (Compose) ──► ViewModel (StateFlow<UiState>) ──► Repository ──► DAO (Room)
                                          ◄── Flow<List<Movie>> ◄──
```

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Kotlin | 1.9.22 | Lenguaje principal |
| Jetpack Compose | BOM 2023.10.01 | UI declarativa |
| Material 3 | — | Componentes visuales |
| Navigation Compose | 2.7.7 | Navegación entre pantallas |
| Room (SQLite) | 2.6.1 | Persistencia local |
| ViewModel + StateFlow | 2.7.0 | Gestión de estado |
| Coil | 2.5.0 | Carga de imágenes desde URL |

---

## ⚙️ Requisitos

- **Android Studio** Hedgehog o superior
- **SDK mínimo:** API 24 (Android 7.0)
- **SDK objetivo:** API 34 (Android 14)
- **JDK:** 11

---

## 📦 Instalación y ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/MovieVault.git
   ```

2. Abre el proyecto en **Android Studio**.

3. Sincroniza las dependencias de Gradle:
   ```bash
   ./gradlew build
   ```

4. Conecta un dispositivo físico o inicia un emulador (API 24+).

5. Ejecuta la app:
   ```bash
   ./gradlew installDebug
   ```

---

## 🗄️ Base de datos

La base de datos local utiliza **Room** con una única tabla `movies`. Al instalar la app por primera vez, se precargan automáticamente 3 películas de ejemplo:

| ID | Título | Género | Calificación |
|----|--------|--------|--------------|
| 1 | Oppenheimer | Drama | 8.2 |
| 2 | Barbie | Comedia | 7.5 |
| 3 | Spider-Man | Animación | 8.8 |

---

## 🗺️ Navegación

```
home
 ├──► detail/{movieId}
 │        └──► form?movieId={movieId}   (editar)
 ├──► favorites
 │        └──► detail/{movieId}
 └──► form                              (nueva película)
```

---

## 📁 Estructura de archivos clave

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/movievault/
│   ├── MainActivity.kt
│   ├── data/local/
│   │   ├── MovieDatabase.kt
│   │   ├── MovieDao.kt
│   │   └── MovieEntity.kt
│   ├── data/model/Movie.kt
│   ├── data/repository/
│   │   ├── MovieRepository.kt
│   │   └── MovieMapper.kt
│   ├── ui/
│   │   ├── components/
│   │   │   ├── MovieCard.kt
│   │   │   └── MovieRatingBar.kt
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── DetailScreen.kt
│   │   │   ├── FavoritesScreen.kt
│   │   │   └── MovieFormScreen.kt
│   │   └── theme/
│   │       ├── Color.kt
│   │       ├── Type.kt
│   │       └── Theme.kt
│   └── viewmodel/
│       ├── HomeViewModel.kt
│       ├── DetailViewModel.kt
│       ├── FavoritesViewModel.kt
│       ├── MovieFormViewModel.kt
│       └── MovieViewModelFactory.kt
└── res/
    ├── values/strings.xml
    ├── values/themes.xml
    └── xml/
        ├── backup_rules.xml
        └── data_extraction_rules.xml
```

---
