package com.app.kwiz.ui.nav

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.kwiz.ui.viewmodel.KwizViewModel
import androidx.navigation.compose.composable
import com.app.kwiz.ui.screens.SplashScreen
import com.app.kwiz.ui.screens.QuestionScreen
import com.app.kwiz.ui.screens.ResultScreen
import com.app.kwiz.ui.states.Screen
import kotlin.system.exitProcess

@Composable
fun AppNavHost(viewModel: KwizViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    var backPressedOnce by remember { mutableStateOf(false) }
    val backPressTimeout = 2000L // 2 seconds

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onLoaded = {
                    navController.popBackStack()
                    navController.navigate(Screen.Quiz.route)
                }, viewModel = viewModel
            )
            // Disable back press on splash
            BackHandler(enabled = true) { /* no-op */ }
        }
        composable(Screen.Quiz.route) {
            QuestionScreen(
                viewModel = viewModel,
                onFinish = { navController.navigate(Screen.Result.route) },
                onRestart = { restartQuiz(navController, viewModel) }
            )
            // Double-back-to-exit handler
            BackHandler(enabled = true) {
                if (backPressedOnce) {
                    exitProcess(0)
                } else {
                    backPressedOnce = true
                    Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        backPressedOnce = false
                    }, backPressTimeout)
                }
            }
        }
        composable(Screen.Result.route) {
            ResultScreen(
                viewModel = viewModel,
                onRestart = { restartQuiz(navController, viewModel) }
            )
            // Double-back-to-exit handler
            BackHandler(enabled = true) {
                if (backPressedOnce) {
                    exitProcess(0)
                } else {
                    backPressedOnce = true
                    Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        backPressedOnce = false
                    }, backPressTimeout)
                }
            }
        }
    }
}

private fun restartQuiz(navController: NavHostController, viewModel: KwizViewModel) {
    viewModel.restart()
    navController.navigate(Screen.Quiz.route) {
        popUpTo(Screen.Splash.route) { inclusive = true }
    }
}
