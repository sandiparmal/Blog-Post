package com.jet2travel.blogpost.database

import android.content.Context

// Dependency Injector for DatabaseCache
object Injector {

    fun provideCache(context: Context): DatabaseCache {
        val database: AppDatabase = AppDatabase.getDatabaseInstance(context)
        return DatabaseCache(database)
    }
}

