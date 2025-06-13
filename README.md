# Android Modular Architecture Sample

This project demonstrates a modular Android application architecture using Kotlin, focusing on feature isolation and clean architecture principles. It showcases how to build scalable Android applications with proper separation of concerns.

## Architecture

The project follows a modular architecture pattern where each feature is isolated into its own module:

- **app**: Main application module containing the app shell and navigation
- **core**: Common utilities and base classes
- **feature/home**: Home feature module
  - **api**: Public API for the home feature
  - **impl**: Implementation of the home feature
  - **wiring**: Dependency injection setup for the home feature
  - **demo**: Standalone demo of the home feature
- **feature/order**: Order feature module
  - **api**: Public API for the order feature
  - **impl**: Implementation of the order feature
  - **wiring**: Dependency injection setup for the order feature

### Module Responsibilities

#### API Module
- Contains only interfaces and data models that other features can depend on
- Defines the public contract for the feature
- No implementation details
- Example: `HomeEntry` interface that defines how to register a home screen

#### Implementation Module
- Contains the actual implementation of the feature
- Implements interfaces defined in the API module
- Contains all the feature's internal components (UI, ViewModels, Repositories)
- Should not be directly accessed by other features

#### Wiring Module
- Contains dependency injection setup for the feature
- Provides implementations to the app module
- Acts as a bridge between the app and implementation
- Example: `HomeModule` that provides `HomeEntry` implementations

### Feature Registration and Navigation

#### Hilt Multibindings
- Uses `@IntoSet` to collect feature entries
- Each feature provides its implementation through wiring module
- App module receives a `Set<FeatureEntry>` for registration
- Example:
  ```kotlin
  @Module
  @InstallIn(SingletonComponent::class)
  abstract class HomeModule {
      @Binds
      @IntoSet
      abstract fun bindHomeEntry(homeEntryImpl: HomeEntryImpl): HomeEntry
  }
  ```

#### Benefits of Multibindings
1. **Decentralized Registration**
   - Each feature registers itself
   - No central registry needed
   - Features can be added/removed without modifying app module

2. **Type Safety**
   - Compile-time checking of implementations
   - No runtime errors from missing registrations
   - Clear contract through interfaces

3. **Loose Coupling**
   - Features don't know about each other
   - App module only knows about interfaces
   - Easy to add/remove features

4. **Independent Navigation**
   - Each feature controls its own navigation
   - No central navigation control
   - Features can be developed independently

### Feature Dependencies

When one feature needs to use another feature:

1. **API-Only Dependencies**
   - Features should only depend on other features' API modules
   - Never depend on implementation or wiring modules
   - Example: If Order feature needs Home feature, it depends on `:feature:home:api`

2. **Implementation Provision**
   - The app module is responsible for providing implementations
   - App module depends on all wiring modules
   - Wiring modules provide implementations to the app
   - Features get implementations through the app module

3. **Dependency Flow**
   ```
   Feature A (impl) → Feature A (api) → Feature B (api) → Feature B (impl)
   App Module → All Wiring Modules → All Implementations
   ```

4. **Benefits**
   - Clear dependency boundaries
   - No circular dependencies
   - Features remain loosely coupled
   - Easier to test and maintain

### Architectural Rules

1. **No Direct Implementation Access**
   - Other features should never depend on implementation modules
   - All dependencies must go through API modules
   - This ensures loose coupling and better maintainability

2. **Wiring Module Usage**
   - Only the app module should depend on wiring modules
   - Wiring modules provide implementations to the app
   - This centralizes dependency injection setup

3. **API Module Design**
   - Keep APIs minimal and focused
   - Only expose what's necessary for other features
   - Use interfaces to hide implementation details

4. **Implementation Isolation**
   - Implementation modules should only depend on their own API
   - Internal changes in implementation should not affect other features
   - This allows for independent evolution of features

### Benefits

1. **Feature Isolation**
   - Features can be developed and tested independently
   - Changes in one feature don't affect others
   - Teams can work on different features in parallel

2. **Clear Dependencies**
   - API modules make dependencies explicit
   - Wiring modules centralize DI setup
   - Easier to understand and maintain

3. **Better Testing**
   - Features can be tested in isolation
   - Demo modules allow for standalone testing
   - Easier to mock dependencies

4. **Scalability**
   - New features can be added without affecting existing ones
   - Teams can work independently
   - Easier to maintain as the app grows

## Features

- **Modular Architecture**: Each feature is isolated in its own module
- **Clean Architecture**: Clear separation of concerns with API, implementation, and wiring modules
- **MVVM**: Uses the Model-View-ViewModel pattern
- **Dependency Injection**: Hilt for dependency injection
- **KSP**: Uses Kotlin Symbol Processing for faster builds
- **Demo Support**: Each feature can be run independently for testing

## Technical Stack

- Kotlin
- Gradle with Kotlin DSL
- Hilt for dependency injection
- KSP for annotation processing
- AndroidX libraries
- Material Design components

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app or any feature demo

## Building

```bash
# Build the main app
./gradlew :app:installDebug

# Build the home feature demo
./gradlew :feature:home:demo:installDebug
```

## Recent Changes

- Migrated from kapt to KSP for better build performance
- Moved DI modules to wiring modules for better organization
- Added proper feature isolation with API modules
- Implemented demo support for features

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
