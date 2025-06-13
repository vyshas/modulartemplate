package com.example.core.di

import com.squareup.anvil.annotations.compat.MergeModules
import dagger.hilt.components.SingletonComponent

@MergeModules(SingletonComponent::class)
interface FeatureEntryModule