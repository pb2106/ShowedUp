package com.showedup.app.ui.navigation

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.showedup.app.ui.screens.permission.PermissionScreen
import com.showedup.app.ui.screens.schedule.ScheduleScreen
import com.showedup.app.ui.screens.schedule.SubjectsScreen
import com.showedup.app.ui.screens.tutorial.TutorialOverlay

sealed class Screen(val route: String) {
    data object Permission : Screen("permission")
    data object Home : Screen("home")
    data object Calendar : Screen("calendar")
    data object Schedule : Screen("schedule")
    data object Subjects : Screen("subjects")
    data object DayOff : Screen("dayoff")
    data object AttendanceLog : Screen("attendance_log")
    data object Export : Screen("export")
}

private const val PREFS_NAME = "showedup_prefs"
private const val KEY_PERMISSIONS_HANDLED = "permissions_handled"
private const val KEY_TUTORIAL_COMPLETED = "tutorial_completed"

@Composable
fun ShowedUpNavHost(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val permissionsHandled = prefs.getBoolean(KEY_PERMISSIONS_HANDLED, false)
    
    var showTutorial by remember {
        mutableStateOf(!prefs.getBoolean(KEY_TUTORIAL_COMPLETED, false))
    }

    val startDest = when {
        !permissionsHandled -> Screen.Permission.route
        else -> Screen.Home.route
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Permission.route) { inclusive = true }
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
            ScheduleScreen(
                onBack = { navController.popBackStack() },
                onNavigateToSubjects = { navController.navigate(Screen.Subjects.route) }
            )
        }

        composable(Screen.Subjects.route) {
            SubjectsScreen(onBack = { navController.popBackStack() })
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

    if (showTutorial && startDest != Screen.Permission.route) {
        TutorialOverlay(
            navController = navController,
            onComplete = {
                prefs.edit().putBoolean(KEY_TUTORIAL_COMPLETED, true).apply()
                showTutorial = false
            }
        )
    }
    }
}

