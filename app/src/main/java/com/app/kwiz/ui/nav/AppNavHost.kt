package com.app.kwiz.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.kwiz.ui.viewmodel.KwizViewModel
import androidx.navigation.compose.composable
import com.app.kwiz.ui.screens.SplashScreen
import com.app.kwiz.ui.screens.QuestionScreen
import com.app.kwiz.ui.screens.ResultScreen
import com.app.kwiz.ui.states.Screen

@Composable
fun AppNavHost(viewModel: KwizViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onLoaded = {
                    navController.popBackStack()
                    navController.navigate(Screen.Quiz.route)
                }, viewModel = viewModel
            )
        }
        composable(Screen.Quiz.route) {
            QuestionScreen(
                viewModel = viewModel,
                onFinish = { navController.navigate(Screen.Result.route) },
                onRestart = { restartQuiz(navController, viewModel) }
            )
        }
        composable(Screen.Result.route) {
            ResultScreen(
                viewModel = viewModel,
                onRestart = { restartQuiz(navController, viewModel) }
            )
        }
    }
}

private fun restartQuiz(navController: NavHostController, viewModel: KwizViewModel) {
    viewModel.restart()
    navController.popBackStack(Screen.Quiz.route, inclusive = false)
    navController.navigate(Screen.Quiz.route)
}