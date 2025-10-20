package com.uvg.mypokedex.ui.components

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
import com.uvg.mypokedex.data.remote.mapper.PokemonListItem

@Composable
fun PokemonCard(
 pokemon: PokemonListItem,
 onClick: (Int) -> Unit = {}
) {
 val cardColor = MaterialTheme.colorScheme.surfaceVariant

 Card(
  onClick = { onClick(pokemon.id) },
  modifier = Modifier
   .padding(8.dp)
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

    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
    contentDescription = "Imagen de ${pokemon.name}",
    modifier = Modifier
     .fillMaxWidth()
     .height(120.dp),
    contentScale = ContentScale.Fit
   )

   Text(
    text = pokemon.name.replaceFirstChar { it.uppercase() },
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Center
   )

   Text(
    text = "#%03d".format(pokemon.id),
    style = MaterialTheme.typography.bodyMedium,
    textAlign = TextAlign.Center,
    color = MaterialTheme.colorScheme.onSurfaceVariant
   )
  }
 }
}