package com.borred.zimran_test_app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.borred.ktor_client.network.search.repos.model.ReposSort
import com.borred.zimran_test_app.ui.actiondialog.ActionDialog
import com.borred.zimran_test_app.ui.actiondialog.ActionItem
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SortKindDialog(
    isVisible: Boolean,
    chosenOne: ReposSort,
    onChoose: (ReposSort) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ActionDialog(
            actionItems = remember(chosenOne) {
                ReposSort.values().map {
                    ActionItem(
                        text = it.value,
                        chosen = chosenOne == it,
                        actionTextColor = Color.Cyan,
                        action = { onChoose(it) }
                    )
                }.toImmutableList()
            },
            onDismiss = onDismiss
        )
    }
}
