package com.naufal.gdtcalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.naufal.gdtcalculator.ui.bookmark.BookmarkScreen
import com.naufal.gdtcalculator.ui.bookmark.BookmarkViewModel
import com.naufal.gdtcalculator.ui.development.DevScreen
import com.naufal.gdtcalculator.ui.home.HomeScreen
import com.naufal.gdtcalculator.ui.platform.PlatformScreen
import com.naufal.gdtcalculator.ui.review.ReviewScreen
import com.naufal.gdtcalculator.ui.topic.TopicScreen
import com.naufal.gdtcalculator.ui.topic.TopicViewModel


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Topic : Screen("topic")
    object Review : Screen("review")
    object Platform : Screen("platform")
    object PlatformStandalone : Screen("platform_standalone")
    object Bookmark : Screen("bookmark")
    object DevPhase : Screen("dev_phase/{phaseNumber}") {
        fun createRoute(phase: Int) = "dev_phase/$phase"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // Shared ViewModel
    val topicViewModel: TopicViewModel = viewModel()
    val bookmarkViewModel: BookmarkViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            LaunchedEffect(Unit) {
                topicViewModel.clearSelection()
            }
            HomeScreen(navController = navController)
        }
        composable(Screen.Topic.route) {
            LaunchedEffect(Unit) { topicViewModel.clearSelection() }
            TopicScreen(
                navController = navController,
                viewModel = topicViewModel
            )
        }
        composable(Screen.Review.route) {
            ReviewScreen(
                navController = navController,
                viewModel = topicViewModel,
                bookmarkViewModel = bookmarkViewModel
            )
        }
        composable(Screen.Platform.route) {
            val genre by topicViewModel.selectedGenre.collectAsState()
            val secondGenre by topicViewModel.selectedSecondGenre.collectAsState()
            val audience by topicViewModel.selectedAudience.collectAsState()
            PlatformScreen(
                navController = navController,
                selectedGenre = genre,
                selectedSecondGenre = secondGenre,
                selectedAudience = audience
            )
        }
        composable(Screen.PlatformStandalone.route) {
            PlatformScreen(
                navController = navController,
                isStandalone = true
            )
        }
        composable(
            route = Screen.DevPhase.route,
            arguments = listOf(
                navArgument("phaseNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val phase = backStackEntry.arguments?.getInt("phaseNumber") ?: 1
            DevScreen(
                navController = navController,
                phaseNumber = phase,
                topicViewModel = topicViewModel
            )
        }
        composable(Screen.Bookmark.route) {
            BookmarkScreen(
                navController = navController,
                onBookmarkSelected = { bookmark ->
                    topicViewModel.loadFromBookmark(bookmark)
                    navController.navigate(Screen.Review.route)
                },
                viewModel = bookmarkViewModel
            )
        }
    }
}