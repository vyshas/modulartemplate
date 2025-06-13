package com.example.feature.home.impl.di

import com.squareup.anvil.annotations.MergeComponent
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@MergeComponent(scope = HomeScope::class)
@DefineComponent(parent = SingletonComponent::class)
interface HomeComponent