package com.borred.zimran_test_app.repodetails

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.borred.ktor_client.network.search.users.model.GitUser
import com.borred.zimran_test_app.repositories.format
import com.borred.zimran_test_app.ui.AuthorInfo
import com.borred.zimran_test_app.ui.DefinitionAndInfoRow
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.animateShimmerColor
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlin.random.Random

@Composable
fun RepoDetailsScreen(
    onGoToUserRepos: (GitUser) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<RepoDetailsViewModel>()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val gitRepository = viewModel.gitRepository
        Header(
            title = gitRepository.name
        )
        Spacer(modifier = Modifier.height(12.dp))
        val authorCardShape = RoundedCornerShape(16.dp)
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clip(authorCardShape)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = authorCardShape
                )
                .clickable { onGoToUserRepos(gitRepository.owner) }
        ) {
            AuthorInfo(owner = gitRepository.owner)
        }
        gitRepository.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        LangsState(
            langsState = viewModel.langsState.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        remember(gitRepository) {
            persistentMapOf(
                "forks" to "${gitRepository.forksCount}",
                "issues" to "${gitRepository.openIssuesCount}",
                "stars" to "${gitRepository.stargazersCount}",
                "created at" to gitRepository.createdAt.format(),
                "updated at" to gitRepository.updatedAt.format()
            )
        }.forEach { (definition, info) ->
            DefinitionAndInfoRow(
                definition = definition,
                info = info,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            Divider()
        }
    }
}

@Composable
private fun LangsState(
    langsState: LangState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(12.dp))
    ) {
        when (langsState) {
            LangState.Empty -> {
                Text(
                    text = "No data about languages used",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            is LangState.Loaded -> {
                Text(
                    text = "Languages used:",
                    modifier = Modifier
                        .padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                LangStateLoaded(
                    map = langsState.map,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            LangState.Loading -> {
                val color by animateShimmerColor()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color)
                        .height(100.dp)
                )
            }
        }
    }
}

@Composable
private fun LangStateLoaded(
    map: ImmutableMap<String, Int>,
    modifier: Modifier = Modifier
) {
    val size = map.size
    val max = remember(map) {
        map.maxBy { it.value }.value
    }
    Layout(
        modifier = modifier,
        content = {
            map.forEach { (name, value) ->
                Box {
                    Text(
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Box {
                    Progress(
                        color = getRandomColor(except = "#ffffff"),
                        value = value,
                        max = max
                    )
                }
            }
        }
    ) { measurables, constraints ->
        val textPlaceables = mutableListOf<Placeable>()
        for (i in 0 until size * 2 step 2) {
            val text = measurables[i].measure(
                constraints.copy(
                    minHeight = 0,
                    maxWidth = constraints.maxWidth / 4
                )
            )
            textPlaceables.add(text)
            Log.e("1---> HERE!!", "LangStateLoaded: text = $text")
        }
        val textWidth = textPlaceables.maxBy { it.width }.width
        val progressPlaceables = mutableListOf<Placeable>()
        for (i in 0 until size * 2 step 2) {
            val progress = measurables[i + 1].measure(
                constraints.copy(
                    minHeight = 0,
                    maxWidth = constraints.maxWidth - textWidth
                )
            )
            progressPlaceables.add(progress)
            Log.e("2---> HERE!!", "LangStateLoaded: progress = $progress")
        }
        var y = 0
        val height = maxOf(
            progressPlaceables.sumOf { it.height },
            textPlaceables.sumOf { it.height }
        )
        layout(constraints.maxWidth, height) {
            for (i in 0 until size) {
                val text = textPlaceables[i]
                text.placeRelative(
                    x = 0,
                    y = y
                )
                val progress = progressPlaceables[i]
                progress.placeRelative(
                    x = textWidth,
                    y = y + text.height / 2
                )
                y += maxOf(text.height, progress.height)
                Log.e("3---> HERE!!", "LangStateLoaded: placing y = $y")
            }
        }
    }
}

@Composable
private fun getRandomColor(
    except: String? = null,
    defaultColor: Color = Color.Transparent
): Color {
    return remember {
        runCatching {
            var generated: String
            do {
                generated = String.format("#%06x", Random.nextInt(0xFFFFFF + 1))
            } while (generated == except)
            parseHexColor(generated)
        }.getOrDefault(defaultColor)
    }
}

private fun parseHexColor(colorString: String) = Color(android.graphics.Color.parseColor(colorString))

@Composable
fun Progress(
    color: Color,
    value: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .height(5.dp)
            .background(Color.LightGray)
    ) {
        val weight = value.toFloat() / max
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight()
                .fillMaxWidth(fraction = weight)
                .background(color)
        )
    }
}
