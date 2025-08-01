package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
 fun PokemonCard( ){
 Card( modifier = Modifier
   .padding(8.dp)
   .fillMaxWidth(),
  shape = MaterialTheme.shapes.medium,
  elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  colors = CardDefaults.cardColors(
   containerColor = typeColor
  ){

  }
 )
}

