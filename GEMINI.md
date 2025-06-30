Project Name: Pluggo Modular Template

Description: A scalable, testable Android application template using a hybrid modular architecture. It showcases feature isolation, bottom navigation with independent back stacks, internal and cross-feature navigation via sealed contracts, and modern UI with Compose inside fragments.

Primary Language: Kotlin

Architecture: Modular Clean Architecture with MVVM, Hilt DI, feature isolation (API, Impl, Wiring), and hybrid navigation (Fragment + Activity-based sealed contracts).

Key Libraries & Frameworks:

UI: Jetpack Compose (inside Fragments), Material 3, ViewBinding (for Activities)

Asynchronicity: Kotlin Coroutines + Flow

Dependency Injection: Hilt with multibindings

Database: Not specified (can be plugged via Wiring, not present in template)

Networking: Custom ApiResult sealed class abstraction (no Retrofit/OkHttp included yet)

Navigation: Jetpack Navigation Component with XML graphs (Fragment-based per feature), sealed contract-based cross-feature Activity navigation

Testing: JUnit for unit testing, AndroidX Test for instrumented tests

2. Coding Style and Conventions
Null Safety: Strict Kotlin null-safety is followed across all modules. Nullable types are used explicitly when required.

Immutability: Preferential use of val and data class for state objects and model layers.

Formatting: Kotlin official style with .kts build scripts, consistent 2-space indentation. Compose components follow clear structure with preview support.

Naming:

Modules: feature/<name>/{api,impl,wiring,demo}

Interfaces: *Entry, *Navigator, *UseCase

UI: HomeScreen, HomeFragment, HomeViewModel, UiIntent, UiEffect, UiState

Navigation: Sealed Destination interfaces per feature (e.g., HomeDestination)

DI: Dagger bindings via @Binds @IntoSet in Wiring modules