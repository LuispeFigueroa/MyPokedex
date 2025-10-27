package com.uvg.mypokedex.ui.features.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.ui.state.SortField
import com.uvg.mypokedex.ui.state.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolsDialog(
    modifier: Modifier = Modifier,
    sortField: SortField,
    sortOrder: SortOrder,
    onSortFieldChange: (SortField) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onApply: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Herramientas de Búsqueda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Button(
                        onClick = onApply,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Aplicar")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            SectionTitle("Ordenar por")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    RadioRow(
                        text = "Número",
                        selected = sortField == SortField.BY_NUMBER,
                        onClick = { onSortFieldChange(SortField.BY_NUMBER) }
                    )
                    Divider()
                    RadioRow(
                        text = "Nombre",
                        selected = sortField == SortField.BY_NAME,
                        onClick = { onSortFieldChange(SortField.BY_NAME) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle("Orden")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(Modifier.padding(12.dp)) {
                    SegmentedRow(
                        leftLabel = "Ascendente",
                        rightLabel = "Descendente",
                        selectedLeft = (sortOrder == SortOrder.ASC),
                        onLeft = { onSortOrderChange(SortOrder.ASC) },
                        onRight = { onSortOrderChange(SortOrder.DESC) }
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
private fun RadioRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}

@Composable
private fun SegmentedRow(
    leftLabel: String,
    rightLabel: String,
    selectedLeft: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant
    val selectedBg = MaterialTheme.colorScheme.background
    val unselectedText = MaterialTheme.colorScheme.onSurface
    val selectedText = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left segment
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (selectedLeft) selectedBg else bg)
                .clickable(onClick = onLeft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                leftLabel,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (selectedLeft) selectedText else unselectedText
                )
            )
        }

        // Right segment
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (!selectedLeft) selectedBg else bg)
                .clickable(onClick = onRight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                rightLabel,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (!selectedLeft) selectedText else unselectedText
                )
            )
        }
    }
}
