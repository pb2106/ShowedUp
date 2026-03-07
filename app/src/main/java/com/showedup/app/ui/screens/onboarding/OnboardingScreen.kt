package com.showedup.app.ui.screens.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.showedup.app.ui.components.ButtonVariant
import com.showedup.app.ui.components.ShowedUpButton
import com.showedup.app.ui.theme.*
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val iconColor: androidx.compose.ui.graphics.Color,
    val title: String,
    val description: String
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Default.Fingerprint,
        iconColor = Emerald500,
        title = "Welcome to ShowedUp",
        description = "Your attendance, your proof — recorded automatically with tamper-proof signals. No one can fake it."
    ),
    OnboardingPage(
        icon = Icons.Default.Schedule,
        iconColor = Violet500,
        title = "Set Up Your Schedule",
        description = "Add your classes, set breaks, pick active days. The timetable adapts to your college."
    ),
    OnboardingPage(
        icon = Icons.Default.Sensors,
        iconColor = SignalGps,
        title = "Auto-Record Attendance",
        description = "Open the app during class — GPS, Wi-Fi, Bluetooth & audio fingerprints are captured automatically."
    ),
    OnboardingPage(
        icon = Icons.Default.CalendarMonth,
        iconColor = StatusHoliday,
        title = "Calendar & Events",
        description = "View your attendance history, plan holidays, and mark events — all in one place."
    ),
    OnboardingPage(
        icon = Icons.Default.PictureAsPdf,
        iconColor = SignalAudio,
        title = "Export & Share",
        description = "Generate tamper-proof PDF reports of your attendance anytime. Share with your college."
    )
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Gray950, Gray900)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenHorizontal, vertical = Spacing.sm),
                horizontalArrangement = Arrangement.End
            ) {
                if (!isLastPage) {
                    TextButton(onClick = onComplete) {
                        Text(
                            "Skip",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                val page = pages[pageIndex]
                OnboardingPageContent(page = page)
            }

            // Page indicator + button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenHorizontal)
                    .padding(bottom = Spacing.xxxl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = Spacing.xxl)
                ) {
                    pages.forEachIndexed { index, _ ->
                        val isActive = index == pagerState.currentPage
                        val color by animateColorAsState(
                            if (isActive) Emerald500 else Gray600,
                            label = "dot"
                        )
                        Box(
                            modifier = Modifier
                                .size(if (isActive) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                if (isLastPage) {
                    ShowedUpButton(
                        text = "Get Started",
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth(),
                        icon = {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                } else {
                    ShowedUpButton(
                        text = "Next",
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.OUTLINED
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconPulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.screenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon circle
        Surface(
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            shape = CircleShape,
            color = page.iconColor.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(
                2.dp, page.iconColor.copy(alpha = 0.3f)
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    tint = page.iconColor
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xxxl))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Spacing.md)
        )
    }
}
