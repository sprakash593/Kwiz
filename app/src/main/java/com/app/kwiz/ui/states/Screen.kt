package com.app.kwiz.ui.states

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Quiz : Screen("quiz")
    object Result : Screen("result")
}