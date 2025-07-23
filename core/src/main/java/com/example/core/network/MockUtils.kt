package com.example.core.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

inline fun <reified T> parseJsonFromAssets(context: Context, fileName: String): T? {
    return try {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(T::class.java)
        adapter.fromJson(json)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T> parseJsonListFromAssets(context: Context, fileName: String): List<T> {
    return try {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter = moshi.adapter<List<T>>(type)
        adapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
