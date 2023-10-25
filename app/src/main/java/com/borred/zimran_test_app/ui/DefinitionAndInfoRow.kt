package com.borred.zimran_test_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DefinitionAndInfoRow(
    definition: String,
    info: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$definition: ",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.alignByBaseline(),
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = info,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.alignByBaseline(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
