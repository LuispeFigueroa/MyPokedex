package com.uvg.mypokedex.presentation.features.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import com.uvg.mypokedex.presentation.components.FieldRow
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.pokemon.prefs.SortOrder

// Clase de ayuda para ordenar por nombre/número
enum class SortField { Number, Name }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSheet(
    current: SortOrder,
    onApply: (SortOrder) -> Unit,
    onCancel: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var field by remember(current) { mutableStateOf(current.toField()) }
    var ascending by remember(current) { mutableStateOf(current.isAscending()) }

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Pokemon Order", style = MaterialTheme.typography.headlineSmall)

            ElevatedCard {
                Column(Modifier.padding(16.dp)) {
                    Text("Order by", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    FieldRow("Number", field == SortField.Number) { field = SortField.Number }
                    FieldRow("Name", field == SortField.Name) { field = SortField.Name }
                }
            }

            ElevatedCard {
                Column(Modifier.padding(16.dp)) {
                    Text("Direction", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = ascending,
                            onClick = { ascending = true },
                            shape = SegmentedButtonDefaults.itemShape(0, 2)
                        ) { Text("Ascending") }
                        SegmentedButton(
                            selected = !ascending,
                            onClick = { ascending = false },
                            shape = SegmentedButtonDefaults.itemShape(1, 2)
                        ) { Text("Descending") }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) { Text("Cancelar") }
                Button(onClick = { onApply(sortOrderOf(field, ascending)) }) {
                    Text("Aplicar")
                }
            }
        }
    }
}

// Funciones de ayuda para mapear a la clase SortField
private fun SortOrder.toField(): SortField = when (this) {
    SortOrder.ID_ASC, SortOrder.ID_DESC -> SortField.Number
    SortOrder.NAME_ASC, SortOrder.NAME_DESC -> SortField.Name
}

private fun SortOrder.isAscending(): Boolean = when (this) {
    SortOrder.ID_ASC, SortOrder.NAME_ASC -> true
    SortOrder.ID_DESC, SortOrder.NAME_DESC -> false
}

private fun sortOrderOf(field: SortField, ascending: Boolean): SortOrder = when (field) {
    SortField.Number -> if (ascending) SortOrder.ID_ASC else SortOrder.ID_DESC
    SortField.Name   -> if (ascending) SortOrder.NAME_ASC else SortOrder.NAME_DESC
}