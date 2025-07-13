#!/bin/bash

# Create Carts feature manually
FEATURE_NAME="Carts"
FEATURE_NAME_LOWER="carts"

echo "🚀 Creating feature: $FEATURE_NAME"

# Check if template exists
if [ ! -d ".template/feature" ]; then
    echo "❌ Error: Template directory not found at .template/feature"
    exit 1
fi

# Check if target already exists
if [ -d "feature/$FEATURE_NAME_LOWER" ]; then
    echo "❌ Error: Feature '$FEATURE_NAME_LOWER' already exists"
    exit 1
fi

echo "📋 Step 1: Copying template..."
cp -r .template/feature feature/$FEATURE_NAME_LOWER

echo "📋 Step 2: Updating file contents..."
# Use different approach for sed on macOS vs Linux
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    find feature/$FEATURE_NAME_LOWER -type f \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" \) -exec sed -i '' "s/TemplateFeature/$FEATURE_NAME/g" {} +
    find feature/$FEATURE_NAME_LOWER -type f \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" \) -exec sed -i '' "s/templatefeature/$FEATURE_NAME_LOWER/g" {} +
else
    # Linux
    find feature/$FEATURE_NAME_LOWER -type f \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" \) -exec sed -i "s/TemplateFeature/$FEATURE_NAME/g" {} +
    find feature/$FEATURE_NAME_LOWER -type f \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" \) -exec sed -i "s/templatefeature/$FEATURE_NAME_LOWER/g" {} +
fi

echo "📋 Step 3: Renaming directories and files..."
# Rename package directories
find feature/$FEATURE_NAME_LOWER -type d -name "*templatefeature*" | while read dir; do
    newdir=$(echo "$dir" | sed "s/templatefeature/$FEATURE_NAME_LOWER/g")
    if [ "$dir" != "$newdir" ]; then
        mv "$dir" "$newdir" 2>/dev/null || true
    fi
done

# Rename files
find feature/$FEATURE_NAME_LOWER -type f -name "*TemplateFeature*" | while read file; do
    newfile=$(echo "$file" | sed "s/TemplateFeature/$FEATURE_NAME/g")
    if [ "$file" != "$newfile" ]; then
        mv "$file" "$newfile" 2>/dev/null || true
    fi
done

find feature/$FEATURE_NAME_LOWER -type f -name "*templatefeature*" | while read file; do
    newfile=$(echo "$file" | sed "s/templatefeature/$FEATURE_NAME_LOWER/g")
    if [ "$file" != "$newfile" ]; then
        mv "$file" "$newfile" 2>/dev/null || true
    fi
done

echo "📋 Step 4: Updating settings.gradle.kts..."
if ! grep -q ":feature:$FEATURE_NAME_LOWER:" settings.gradle.kts; then
    echo "" >> settings.gradle.kts
    echo "// $FEATURE_NAME Feature" >> settings.gradle.kts
    echo "include(\":feature:$FEATURE_NAME_LOWER:api\")" >> settings.gradle.kts
    echo "include(\":feature:$FEATURE_NAME_LOWER:impl\")" >> settings.gradle.kts
    echo "include(\":feature:$FEATURE_NAME_LOWER:wiring\")" >> settings.gradle.kts
    echo "include(\":feature:$FEATURE_NAME_LOWER:demo\")" >> settings.gradle.kts
    echo "✅ Added modules to settings.gradle.kts"
fi

echo "📋 Step 5: Cleaning build artifacts..."
./gradlew clean > /dev/null 2>&1 || true

echo ""
echo "🎉 Successfully created feature: $FEATURE_NAME"
echo "📦 Modules created:"
echo "   • :feature:$FEATURE_NAME_LOWER:api"
echo "   • :feature:$FEATURE_NAME_LOWER:impl"
echo "   • :feature:$FEATURE_NAME_LOWER:wiring"
echo "   • :feature:$FEATURE_NAME_LOWER:demo"
echo ""
echo "🏃‍♂️ Next steps:"
echo "   1. Sync project: ./gradlew build"
echo "   2. Run demo: ./gradlew :feature:$FEATURE_NAME_LOWER:demo:installDebug"