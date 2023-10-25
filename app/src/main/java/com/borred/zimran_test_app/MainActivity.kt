package com.borred.zimran_test_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import com.borred.zimran_test_app.auth.AuthScreenState
import com.borred.zimran_test_app.auth.AuthorizationViewModel
import com.borred.zimran_test_app.auth.authorizeViaGithub
import com.borred.zimran_test_app.error.ErrorsFlow
import com.borred.zimran_test_app.ui.LoadingView
import com.borred.zimran_test_app.ui.NotAuthorizedView
import com.borred.zimran_test_app.ui.snackbar.MessageContent
import com.borred.zimran_test_app.ui.snackbar.MessageSnackbar
import com.borred.zimran_test_app.ui.snackbar.MessageType
import com.borred.zimran_test_app.ui.snackbar.showSnackbarWithContent
import com.borred.zimran_test_app.ui.theme.Zimran_test_appTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AuthorizationViewModel>()

    @Inject
    lateinit var errorsFlow: ErrorsFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Zimran_test_appTheme {
                val uiController = rememberSystemUiController()
                val backgroundColor = MaterialTheme.colorScheme.background
                LaunchedEffect(backgroundColor, uiController) {
                    uiController.setSystemBarsColor(backgroundColor)
                }
                when (viewModel.screenState.collectAsState().value) {
                    AuthScreenState.Loading -> LoadingView()
                    AuthScreenState.Main -> MainScreen(
                        onShowError = {
                            viewModel.viewModelScope.launch {
                                errorsFlow.sendError(it)
                            }
                        },
                        onLogOut = viewModel::onLogOut
                    )
                    AuthScreenState.NotAuthorized -> {
                        NotAuthorizedView(
                            authorizeViaGithub = ::authorizeViaGithub
                        )
                    }
                }
                val snackbarHostState = remember { SnackbarHostState() }
                MessageSnackbar(
                    snackbarHostState = snackbarHostState
                )
                LaunchedEffect(Unit) {
                    errorsFlow.collectErrors { error ->
                        error.printStackTrace()
                        snackbarHostState.showSnackbarWithContent(
                            MessageContent(
                                title = "Some error occured",
                                subtitle = error.localizedMessage ?: "unknown",
                                type = MessageType.Error
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri != null) {
            val authCode = uri.getQueryParameter("code") ?: return
            viewModel.getAccessToken(code = authCode)
        }
    }
}
