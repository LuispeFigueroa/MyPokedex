package com.uvg.mypokedex.data.repository

import android.content.Context
import androidx.room.Room
import com.uvg.mypokedex.core.network.NetworkMonitor
import com.uvg.mypokedex.data.local.db.AppDatabase
import com.uvg.mypokedex.data.local.preferences.UserPreferencesDataStore
import com.uvg.mypokedex.data.remote.RemoteDataSourceImpl
import com.uvg.mypokedex.data.remote.di.NetworkModule

object RepositoryProvider {

    @Volatile
    private var instance: PokemonRepository? = null

    fun providePokemonRepository(context: Context): PokemonRepository {
        return instance ?: synchronized(this) {
            val api = NetworkModule.pokemonApi
            val prefs = UserPreferencesDataStore(context)
            val remote = RemoteDataSourceImpl(api)
            val networkMonitor = NetworkMonitor(context)

            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "pokemon_database.db"
            )
                .fallbackToDestructiveMigration()
                .build()

            val dao = db.pokemonDao()

            val repo = PokemonRepositoryImpl(
                prefs = prefs,
                remote = remote,
                networkMonitor = networkMonitor,
                pokemonDao = dao
            )

            instance = repo
            repo
        }
    }
}
