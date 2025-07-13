# 📄 Feature Scaffolding System Documentation

## ✅ Purpose

This system allows developers to instantly generate a fully-wired feature module (e.g., offers,
coupons, orders) using a reusable `.template/feature` folder and either the shell script
`create-feature.sh` or the Kotlin script `create-feature.kts`.

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
├── create-feature.sh      ← Shell script to generate new feature
└── settings.gradle.kts
```

## ✅ What the .template/feature Contains

The `.template/feature` is structured to match a real, working product feature module exactly:

- All `build.gradle.kts` files, `sourceSets`, dependencies, and Gradle options are in sync with what
  works in production.
- All class/files use `TemplateFeature` naming (singular) or `TemplateFeatures` (plural) as
  appropriate.
- Use `TemplateFeatures` only where English plural is correct; this will avoid issues when
  generating new features via string replacement (e.g., prevents `Orderss`).

### 📁 .template/feature/api/
- `TemplateFeatureEntry.kt` (implements BottomNavEntry or FeatureEntry)
- `TemplateFeatureNavigator.kt` (navigation contract)
- `domain/` (models and use cases)
- **Mock sourceSet support**

### 📁 .template/feature/impl/
- `ui/` with TemplateFeatureFragment, TemplateFeatureViewModel
- `repository/` with TemplateFeatureRepository, MockTemplateFeatureRepositoryImpl,
  RealTemplateFeatureRepositoryImpl
- `remote/` with TemplateFeatureApi
- `domain/usecase/` with one sample use case
- `mock/` sourceSet with assets/mock_data.json and a MockRepository that reads it
- `test/` directory pre-wired for ViewModel & API service tests
- `impl/build.gradle.kts` includes:
    - `test-utils` as testImplementation
  - main and mock sourceSet configuration for java/assets
  - robust API test pattern (ApiAbstract, MainCoroutinesRule)

### 📁 .template/feature/wiring/
- `TemplateFeatureModule.kt` — prod DI
- `MockTemplateFeatureModule.kt` — mock DI
- Use `@Provides` bindings for TemplateFeatureRepository
- **Mock sourceSet support for DI**

### 📁 .template/feature/demo/
- `TemplateFeatureDemoActivity.kt`
- `TemplateFeatureDemoApplication.kt`
- `activity_template_feature_demo.xml`
- Manifest and applicationIdSuffix
- Loads navigation via NavHostFragment.create(...)
- **Demo mock buildType** config mirrors the product module

## 🛠️ Usage: Creating a New Feature

### 1. Using the Shell Script (Recommended for Most Developers)

```bash
./create-feature.sh Orders
```

This creates `feature/orders/` fully configured and updates `settings.gradle.kts`.

### 2. Using the Kotlin Gradle script (Advanced)
```bash
./gradlew -q -p . -b scripts/create-feature.kts -PfeatureName=Orders
```

### Examples
```bash
./create-feature.sh Offers
./create-feature.sh Coupons
./create-feature.sh Orders
```

## ✅ Naming Conventions

| Placeholder                    | Replaced With           | Example                      |
|--------------------------------|-------------------------|------------------------------|
| `TemplateFeature`              | PascalCase feature name | `Offers`                     |
| `templatefeature`              | lowercase feature name  | `offers`                     |
| `TemplateFeatures`             | PascalCase plural name  | `Offers`                     |
| `templatefeatures`             | lowercase plural        | `offers`                     |
| `com.example.feature.template` | updated package path    | `com.example.feature.offers` |

**Important:**

- If you expect to generate correct plural method or file names, use `TemplateFeatures` (plural) in
  `.template` where appropriate. This will avoid the generation of names like `getOrderss` instead
  of `getOrders` (because TemplateFeatures → Orders, not Orderss).
- If custom pluralization is required (e.g., not simply an "s"), adjust after feature generation.

## ✅ What Gets Created

A new feature module directory structured just like a product feature, including:
```
feature/
└── offers/
    ├── api/                 # main & mock/java support
    ├── impl/                # main & mock/java/assets, test support
    ├── wiring/              # main & mock/java for DI
    └── demo/                # main & mock/java/assets; robust mock buildType config
```

And automatically updates:

- `settings.gradle.kts`
- All class/package/resource names

## 🧪 Testing the Generated Feature

1. **Run the demo:**
   ```bash
   ./gradlew :feature:offers:demo:installDebug
   ```
2. **Run/build/test:**
   ```bash
   ./gradlew :feature:offers:demo:assembleDebug
   ./gradlew :feature:offers:impl:testDebugUnitTest
   ```
3. **Use mock data:** Add to `impl/src/mock/assets/` and reference in repository/tests.

## ✅ Gradle Requirements

Your generated modules will follow all product conventions, including:

- Standard plugins
- test-utils added to impl test dependencies
- Main & mock sourceSets everywhere
- **Demo module:** robust mock buildType configuration

## 🔧 Script Responsibilities and Known Limitations

- Accepts a feature name as a script argument
- Copies `.template/feature` → `feature/<feature>/`
- Does simple text replacements for naming
- **Pluralization caveat:**
    - Use `TemplateFeatures` for plural, `TemplateFeature` for singular in the template. Otherwise,
      you may get names like `getOrderss`. Manually adjust after generation if needed.
    - Script does not perform English pluralization logic; it is a direct string replacement.

## 🚀 Next Steps After Feature Creation

1. Sync project: `./gradlew build`
2. Run demo: `./gradlew :feature:yourfeature:demo:installDebug`
3. Implement domain logic in `impl/`
4. Add mock data in `impl/src/mock/assets/`
5. Write tests in `impl/src/test/`
6. Wire feature into main app navigation

## 📝 Example Generated Files

> See module folders for actual up-to-date files: api, impl, wiring, demo.

## 🎯 Benefits
- ⚡ **Instant feature creation** - Zero manual boilerplate
- 🏗️ **Consistent architecture** - Same pattern as real product features
- 🧪 **Ready-to-go testing** - Builtin structure for mocks and tests
- 📱 **Demo works** - Runnable demo for each feature
- 🔧 **DI ready** - Hilt modules pre-configured
- 🎨 **Mock/asset support** - JSON-based mock data

---
*This scaffolding system ensures all features in your modular Android architecture follow consistent
patterns and best practices.*