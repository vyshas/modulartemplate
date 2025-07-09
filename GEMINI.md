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

## Project Structure and Key Directories

The project follows a modular architecture with features organized under the `feature/` directory. Each feature typically contains the following sub-modules:

*   `feature/<name>/api`: Defines the public interfaces and data models for the feature.
*   `feature/<name>/impl`: Contains the concrete implementation of the feature's logic and UI.
*   `feature/<name>/wiring`: Handles dependency injection and connects the feature to the main application graph.
*   `feature/<name>/demo`: (Optional) Contains a demo application or specific demo components for the feature.

Other important directories:

*   `app/`: The main application module.
*   `core/`: Contains common utilities, base classes, and shared resources.
*   `build-logic/`: Houses custom Gradle plugins and build logic.

## Navigation Overview

Navigation in this project is handled by a hybrid approach:

*   **Fragment-based Navigation:** Within a feature, Jetpack Navigation Component with XML graphs is used for navigating between fragments.
*   **Activity-based Navigation (Cross-Feature):** For navigation between different features (Activities), sealed contract-based navigation is employed. This provides a type-safe and explicit way to define navigation destinations and their required arguments. Navigation interfaces are typically defined in the `api` module of a feature (e.g., `HomeDestination`).

## Build-Logic Files

The `build-logic/` directory contains custom Gradle plugins written in Kotlin (`.kts` files). These plugins are used to:

*   Enforce consistent build configurations across modules.
*   Define common dependencies and versions.
*   Automate repetitive build tasks.

This approach helps in maintaining a clean and scalable build system.

## Testing Guidelines

This project uses JUnit for unit testing and AndroidX Test for instrumented tests.

### Running Tests

*   **Run all tests:**
    ```bash
    ./gradlew test
    ```
*   **Run tests for a specific module (e.g., `feature/order/impl`):**
    ```bash
    ./gradlew :feature:order:impl:test
    ```
    (Replace `:feature:order:impl` with the desired module path)
*   **Run a single test class (e.g., `OrderViewModelTest`):**
    ```bash
    ./gradlew :feature:order:impl:testDebugUnitTest --tests "com.example.feature.order.impl.ui.orderlist.OrderViewModelTest"
    ```
    (Adjust module path and test class name as needed)

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