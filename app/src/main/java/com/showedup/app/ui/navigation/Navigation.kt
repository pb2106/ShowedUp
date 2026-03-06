package com.showedup.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.showedup.app.ui.screens.calendar.CalendarScreen
import com.showedup.app.ui.screens.dayoff.DayOffScreen
import com.showedup.app.ui.screens.export.ExportScreen
import com.showedup.app.ui.screens.home.HomeScreen
import com.showedup.app.ui.screens.log.AttendanceLogScreen
import com.showedup.app.ui.screens.onboarding.OnboardingScreen
import com.showedup.app.ui.screens.schedule.ScheduleScreen

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Calendar : Screen("calendar")
    data object Schedule : Screen("schedule")
    data object DayOff : Screen("dayoff")
    data object AttendanceLog : Screen("attendance_log")
    data object Export : Screen("export")
}

@Composable
fun ShowedUpNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                onNavigateToSchedule = { navController.navigate(Screen.Schedule.route) },
                onNavigateToLog = { navController.navigate(Screen.AttendanceLog.route) },
                onNavigateToExport = { navController.navigate(Screen.Export.route) },
                onNavigateToDayOff = { navController.navigate(Screen.DayOff.route) }
            )
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Schedule.route) {
            ScheduleScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.DayOff.route) {
            DayOffScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AttendanceLog.route) {
            AttendanceLogScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Export.route) {
            ExportScreen(onBack = { navController.popBackStack() })
        }
    }
}
