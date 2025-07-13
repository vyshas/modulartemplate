# 📄 Feature Scaffolding System Documentation

## ✅ Purpose

This system allows developers to instantly generate a fully-wired feature module (e.g., offers,
coupons, orders) using a reusable `.template/feature` folder and a Kotlin script (
`create-feature.kts`).

The goal is to eliminate boilerplate and let developers immediately start building data, UI, or mock
layers.

## 🧩 Project Structure Overview

```
project-root/
├── .template/
│   └── feature/           ← base feature module template
│       ├── api/
│       ├── impl/
│       ├── wiring/
│       └── demo/
├── scripts/
│   └── create-feature.kts ← Kotlin script to generate new feature
└── settings.gradle.kts
```

## ✅ What the .template/feature Contains

This is the boilerplate-free foundation for every new feature. All classes use `TemplateFeature`
naming and get replaced by the script.

### 📁 .template/feature/api/

- `TemplateFeatureEntry.kt` (implements BottomNavEntry or FeatureEntry)
- `TemplateFeatureNavigator.kt` (navigation contract)
- `domain/` (models and use cases)

### 📁 .template/feature/impl/

- `ui/` with TemplateFeatureFragment, TemplateFeatureViewModel
- `repository/` with TemplateFeatureRepository, MockTemplateFeatureRepositoryImpl,
  RealTemplateFeatureRepositoryImpl
- `remote/` with TemplateFeatureApi
- `domain/usecase/` with one sample use case
- `mock/` sourceSet:
    - `assets/mock_data.json`
    - `MockTemplateFeatureRepositoryImpl.kt` that reads from JSON
- `test/` directory pre-wired for ViewModel test
- `impl/build.gradle.kts` includes:
    - `test-utils` as testImplementation
    - mock sourceSet configuration
    - buildConfigField for base URL

### 📁 .template/feature/wiring/

- `TemplateFeatureModule.kt` — prod DI
- `MockTemplateFeatureModule.kt` — mock DI
- Use `@Provides` bindings for TemplateFeatureRepository

### 📁 .template/feature/demo/

- `TemplateFeatureDemoActivity.kt`
- `TemplateFeatureDemoApplication.kt`
- `activity_template_feature_demo.xml`
- Manifest and applicationIdSuffix
- Loads navigation via NavHostFragment.create(...)

## 🛠️ Usage: Creating a New Feature

### Command Format

```bash
./gradlew -q -p . -b scripts/create-feature.kts -PfeatureName=YourFeatureName
```

### Examples

```bash
# Create an "Offers" feature
./gradlew -q -p . -b scripts/create-feature.kts -PfeatureName=Offers

# Create a "Coupons" feature  
./gradlew -q -p . -b scripts/create-feature.kts -PfeatureName=Coupons

# Create an "Orders" feature
./gradlew -q -p . -b scripts/create-feature.kts -PfeatureName=Orders
```

## ✅ Naming Conventions

| Placeholder | Replaced With | Example |
|------------|---------------|---------|
| `TemplateFeature` | PascalCase feature name | `Offers` |
| `templatefeature` | lowercase feature name | `offers` |
| `com.example.feature.template` | updated package path | `com.example.feature.offers` |

## ✅ What Gets Created

Running the script creates:

```
feature/
└── offers/                    ← (example for "Offers")
    ├── api/
    │   ├── build.gradle.kts
    │   └── src/main/java/com/example/feature/offers/api/
    ├── impl/
    │   ├── build.gradle.kts
    │   ├── src/main/java/com/example/feature/offers/impl/
    │   ├── src/mock/java/com/example/feature/offers/impl/
    │   └── src/test/java/com/example/feature/offers/impl/
    ├── wiring/
    │   ├── build.gradle.kts
    │   └── src/main/java/com/example/feature/offers/wiring/
    └── demo/
        ├── build.gradle.kts
        └── src/main/java/com/example/feature/offers/demo/
```

And automatically updates:

- `settings.gradle.kts` with new module declarations
- `applicationIdSuffix` in demo module
- All class names, package names, and resources

## 🧪 Testing the Generated Feature

Once created, you can:

1. **Run the demo module** for that feature directly:
   ```bash
   ./gradlew :feature:offers:demo:installDebug
   ```

2. **Write tests** under `impl/src/test/`:
   ```bash
   ./gradlew :feature:offers:impl:test
   ```

3. **Use mock data** via `impl/src/mock/assets/`:
    - Add JSON files for mock responses
    - Mock repository reads from these assets

## ✅ Gradle Requirements

The generated modules automatically follow:

- Standard convention plugin use
- `test-utils` added to impl test dependencies
- Mock sourceSet in `impl/build.gradle.kts`:
  ```kotlin
  sourceSets {
      create("mock") {
          java.srcDir("src/mock/kotlin")
          resources.srcDir("src/mock/assets")
      }
  }
  ```

## 🔧 Script Responsibilities

The `create-feature.kts` script:

1. ✅ Accepts a feature name (e.g. `Offers`)
2. ✅ Copies `.template/feature/` → `feature/offers/`
3. ✅ Renames:
    - `TemplateFeature` → `Offers`
    - `templatefeature` → `offers`
    - Package names in files and folders
4. ✅ Renames files and directories
5. ✅ Appends new modules to `settings.gradle.kts`
6. ✅ Optionally injects feature base URL into `build.gradle.kts`

## 🚀 Next Steps After Feature Creation

1. **Sync project**: `./gradlew build`
2. **Run demo**: `./gradlew :feature:yourfeature:demo:installDebug`
3. **Implement your domain logic** in `impl/`
4. **Add mock data** in `impl/src/mock/assets/`
5. **Write tests** in `impl/src/test/`
6. **Wire into main app** by adding to app's navigation

## 📝 Example Generated Files

### OffersRepository.kt (was TemplateFeatureRepository.kt)

```kotlin
package com.example.feature.offers.api.data

interface OffersRepository {
    suspend fun getOffers(): List<Offer>
}
```

### OffersViewModel.kt (was TemplateFeatureViewModel.kt)

```kotlin
package com.example.feature.offers.impl.ui.offerslist

class OffersViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase
) : ViewModel() {
    // Implementation
}
```

## 🎯 Benefits

- ⚡ **Instant feature creation** - Zero manual boilerplate
- 🏗️ **Consistent architecture** - Every feature follows the same pattern
- 🧪 **Built-in testing** - Test structure and mocks ready to go
- 📱 **Immediate demo** - Runnable demo app for each feature
- 🔧 **DI ready** - Hilt modules pre-configured
- 🎨 **Mock support** - JSON-based mock data system

---

*This scaffolding system ensures all features in your modular Android architecture follow consistent
patterns and best practices.*