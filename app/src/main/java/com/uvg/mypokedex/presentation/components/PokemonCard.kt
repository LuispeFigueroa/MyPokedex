package com.uvg.mypokedex.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uvg.mypokedex.domain.model.FavoritePokemon
import com.uvg.mypokedex.domain.model.NamedItem

@Composable
fun PokemonCard(
    item: NamedItem? = null,
    favorite: FavoritePokemon? = null,
    onClick: (Int) -> Unit = {}
) {
    var pokemon: NamedItem? = null
    pokemon = if (favorite != null) {
        NamedItem(favorite.name, "https://pokeapi.co/api/v2/pokemon/${favorite.id}/")
    } else {
        item
    }

    val id = item?.idFromUrl() ?: return
    val sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    Card(
        onClick = { onClick(id) },
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = sprite,
                contentDescription = "Imagen de ${pokemon?.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            pokemon?.name?.replaceFirstChar { it.uppercase() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "#%03d".format(id),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Extrae el ID desde la URL del elemento
private fun NamedItem.idFromUrl(): Int? {
    val regex = """.*/pokemon/(\d+)/?$""".toRegex()
    return regex.find(this.url)?.groupValues?.getOrNull(1)?.toIntOrNull()
}
