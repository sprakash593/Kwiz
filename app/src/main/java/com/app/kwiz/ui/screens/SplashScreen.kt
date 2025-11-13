package com.app.kwiz.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.kwiz.ui.components.AnimatedLogo
import com.app.kwiz.ui.components.ParticleBurst
import com.app.kwiz.ui.states.KwizUiState
import com.app.kwiz.ui.viewmodel.KwizViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoaded: () -> Unit,
    viewModel: KwizViewModel
) {

    var startAnim by remember { mutableStateOf(false) }
    var showLogoText by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        val dataJob = async {
            viewModel.load(context = context)
            while (viewModel.uiState !is KwizUiState.Ready && viewModel.uiState !is KwizUiState.Error) {
                delay(100)
            }
        }
        val animJob = async {
            startAnim = true
            delay(1000)
            showLogoText = true
            delay(1000)
            showTagline = true
            delay(1000)
        }
        awaitAll(dataJob, animJob)
        onLoaded()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // --- Animated background particle burst ---
        if (startAnim) ParticleBurst()

        // --- Center logo + text ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Animated logo (e.g., “K” in a glowing circle)
            AnimatedLogo(startAnim)

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = showLogoText,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { 40 }),
            ) {
                Text(
                    text = "Kwiz",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = showTagline,
                enter = fadeIn(animationSpec = tween(1200))
            ) {
                Text(
                    text = "Test. Learn. Grow.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


