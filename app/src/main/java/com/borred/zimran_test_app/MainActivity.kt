package com.borred.zimran_test_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.borred.zimran_test_app.auth.AuthorizationViewModel
import com.borred.zimran_test_app.auth.ScreenState
import com.borred.zimran_test_app.auth.authorizeViaGithub
import com.borred.zimran_test_app.error.ErrorsFlow
import com.borred.zimran_test_app.ui.LoadingView
import com.borred.zimran_test_app.ui.NotAuthorizedView
import com.borred.zimran_test_app.ui.theme.Zimran_test_appTheme
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
                // A surface container using the 'background' color from the theme
                when (viewModel.screenState.collectAsState().value) {
                    ScreenState.Loading -> LoadingView()
                    ScreenState.Main -> {
                        Text(text = "Hello")
                    }
                    ScreenState.NotAuthorized -> {
                        NotAuthorizedView(
                            authorizeViaGithub = ::authorizeViaGithub
                        )
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                errorsFlow.collectErrors { error ->
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
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
