package com.uvg.mypokedex.core.common

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.uvg.mypokedex.core.network.NetworkClient
import com.uvg.mypokedex.core.network.monitor.DefaultNetworkMonitor
import com.uvg.mypokedex.core.network.monitor.NetworkMonitor
import com.uvg.mypokedex.data.pokemon.local.PokemonLocalDataSource
import com.uvg.mypokedex.data.pokemon.local.db.AppDatabase
import com.uvg.mypokedex.data.pokemon.prefs.PokemonSortOrderDataSource
import com.uvg.mypokedex.data.pokemon.remote.PokemonRemoteDataSource
import com.uvg.mypokedex.data.pokemon.remote.api.PokemonApiService
import com.uvg.mypokedex.data.pokemon.repo.PokemonRepositoryImpl
import com.uvg.mypokedex.domain.repo.PokemonRepository
import kotlinx.coroutines.Dispatchers

object RepositoryProvider {
    // Init
    private lateinit var app: Application
    fun init(application: Application) { app = application }

    // DataStore (Preferences)
    private val Application.dataStore by preferencesDataStore(name = "settings")

    /*
    SINGLETONS
     */

    // Singleton de la base de datos
    private val db: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Room.databaseBuilder(app, AppDatabase::class.java, "mypokedex.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Singleton del PokemonApiService
    private val apiService: PokemonApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        NetworkClient.pokemonRetrofit.create(PokemonApiService::class.java)
    }

    // Singleton del LocalDataSource
    private val localDs: PokemonLocalDataSource by lazy {
        PokemonLocalDataSource(db.pokemonDao())
    }

    // Singleton del RemoteDataSource
    private val remoteDs: PokemonRemoteDataSource by lazy {
        PokemonRemoteDataSource(apiService, io = Dispatchers.IO)
    }

    // Singleton del SortOrderDataSource
    private val sortPrefs: PokemonSortOrderDataSource by lazy {
        PokemonSortOrderDataSource(app.dataStore)
    }

    // Singleton del monitor de conexión
    private val networkMonitor: NetworkMonitor by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DefaultNetworkMonitor(app)
    }

    // Función que expone el monitor de conexión
    fun provideNetworkMonitor(): NetworkMonitor = networkMonitor

    /*
    REPOSITORIOS
     */

    // PokemonRepository
    private val pokemonRepo: PokemonRepository by lazy {
        PokemonRepositoryImpl(
            remote = remoteDs,
            local = localDs,
            prefs = sortPrefs
        )
    }

    // Función que expone el repositorio
    fun providePokemonRepository(): PokemonRepository = pokemonRepo
}