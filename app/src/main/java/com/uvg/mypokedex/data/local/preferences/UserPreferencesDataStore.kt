package com.uvg.mypokedex.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uvg.mypokedex.domain.sort.PokemonOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_prefs"
)

class UserPreferencesDataStore(
    private val context: Context
) {
    private val ORDER_KEY = stringPreferencesKey("pokemon_order")

    val orderFlow: Flow<PokemonOrder> =
        context.dataStore.data.map { prefs: Preferences ->
            val raw = prefs[ORDER_KEY] ?: PokemonOrder.NUMBER_ASC.rawValue
            PokemonOrder.fromRaw(raw)
        }

    suspend fun setOrder(newOrder: PokemonOrder) {
        context.dataStore.edit { prefs ->
            prefs[ORDER_KEY] = newOrder.rawValue
        }
    }
}