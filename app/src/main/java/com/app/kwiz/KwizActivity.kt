package com.app.kwiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.kwiz.ui.nav.AppNavHost
import com.app.kwiz.ui.viewmodel.KwizViewModel
import com.app.kwiz.utils.AppContainer


class KwizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository by lazy{ AppContainer.provideKwizRepository()}

        setContent {
            MaterialTheme {
                AppNavHost(viewModel = viewModel {
                    KwizViewModel(repository, this@KwizActivity.applicationContext)
                })
            }
        }
    }
}