package com.uvg.mypokedex.data2.pokemon.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Clase con el trabajo de leer y modificar la preferencia de orden de la lista de pokemones
class PokemonSortOrderDataSource(private val dataStore: DataStore<Preferences>) {
    // Clave para la preferencia de orden
    private val KEY_SORT = stringPreferencesKey("sort_order")

    // Variable que expone el flow con el orden actual de la lista de pokemones
    val sortOrderFlow: Flow<SortOrder> = dataStore.data
        .map { prefs ->
            prefs[KEY_SORT]?.let { runCatching { SortOrder.valueOf(it) }.getOrNull() }
                ?: SortOrder.ID_ASC
        }

    // Función para cambiar el orden de la lista de pokemones
    suspend fun setSortOrder(order: SortOrder) {
        dataStore.edit { it[KEY_SORT] = order.name }
    }
}
