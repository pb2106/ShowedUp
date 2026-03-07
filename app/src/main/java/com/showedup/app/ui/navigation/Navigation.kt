package com.showedup.app.ui.navigation

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
import com.showedup.app.ui.screens.permission.PermissionScreen
import com.showedup.app.ui.screens.schedule.ScheduleScreen

sealed class Screen(val route: String) {
    data object Permission : Screen("permission")
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Calendar : Screen("calendar")
    data object Schedule : Screen("schedule")
    data object DayOff : Screen("dayoff")
    data object AttendanceLog : Screen("attendance_log")
    data object Export : Screen("export")
}

private const val PREFS_NAME = "showedup_prefs"
private const val KEY_PERMISSIONS_HANDLED = "permissions_handled"
private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

@Composable
fun ShowedUpNavHost(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val permissionsHandled = prefs.getBoolean(KEY_PERMISSIONS_HANDLED, false)
    val onboardingCompleted = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    val startDest = when {
        !permissionsHandled -> Screen.Permission.route
        !onboardingCompleted -> Screen.Onboarding.route
        else -> Screen.Home.route
    }

    NavHost(
        navController = navController,
        startDestination = startDest,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(Screen.Permission.route) {
            PermissionScreen(
                onComplete = {
                    prefs.edit().putBoolean(KEY_PERMISSIONS_HANDLED, true).apply()
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Permission.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
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

