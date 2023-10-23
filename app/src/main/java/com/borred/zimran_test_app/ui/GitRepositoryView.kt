package com.borred.zimran_test_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.zimran_test_app.prettyNumber
import com.borred.zimran_test_app.repositories.format

@Composable
fun GitRepositoryView(
    item: GitRepository,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = item.owner.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.owner.login,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = remember(item) {
                            item.updatedAt.format()
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                CountAndIcon(
                    count = item.stargazersCount,
                    icon = Icons.Default.Star,
                    tint = Color.Yellow
                )
                CountAndIcon(
                    count = item.forksCount,
                    icon = Icons.Default.Share,
                    tint = Color.DarkGray
                )
                CountAndIcon(
                    count = item.openIssuesCount,
                    icon = Icons.Default.ExitToApp,
                    tint = Color.Red
                )
            }
        }
        Divider()
    }
}

@Composable
private fun CountAndIcon(
    count: Int,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = count.prettyNumber(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignByBaseline()
        )
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = "stars",
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}
