package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

@Composable
fun PokemonCard(
 pokemon: Pokemon,
 onClick: (Pokemon) -> Unit = {}
) {
 val typeName = pokemon.types.firstOrNull()?.lowercase()
 val typeColor = when (typeName) {
  "agua", "water" -> Color.Cyan
  "fuego", "fire" -> Color.Red
  "planta", "grass" -> Color.Green
  "electrico", "eléctrico", "electric" -> Color.Yellow
  else -> Color.Gray
 }

 Card(
  onClick = { onClick(pokemon) },   // mejor que Modifier.clickable
  modifier = Modifier
   .padding(8.dp)
   .fillMaxWidth(),
  shape = MaterialTheme.shapes.medium,
  elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  colors = CardDefaults.cardColors(containerColor = typeColor)
 ) {
  Column(
   modifier = Modifier.padding(16.dp),
   horizontalAlignment = Alignment.CenterHorizontally,
   verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
   AsyncImage(
    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
    contentDescription = "Imagen de ${pokemon.name}",
    modifier = Modifier.fillMaxWidth().height(200.dp),
    contentScale = ContentScale.Crop
   )
   Text(
    text = pokemon.name,
    style = MaterialTheme.typography.bodyLarge,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Center
   )
   Text(
    text = "#${pokemon.id}",
    style = MaterialTheme.typography.bodyMedium,
    textAlign = TextAlign.Center
   )
  }
 }
}
