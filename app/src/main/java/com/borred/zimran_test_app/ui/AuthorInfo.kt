package com.borred.zimran_test_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.borred.ktor_client.network.search.users.model.GitUser

@Composable
fun AuthorInfo(
    owner: GitUser,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = owner.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(150.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            DefinitionAndInfoRow(
                definition = "id",
                info = "${owner.id}"
            )
            DefinitionAndInfoRow(
                definition = "login",
                info = owner.login
            )
        }
    }
}
