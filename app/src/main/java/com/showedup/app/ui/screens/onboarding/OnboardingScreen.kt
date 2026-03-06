package com.showedup.app.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
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

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: androidx.compose.ui.graphics.Color
)

private val pages = listOf(
    OnboardingPage(
        title = "Never Miss\nAnother Class",
        description = "ShowedUp automatically records your attendance so you can focus on learning.",
        icon = Icons.Default.Check,
        accentColor = Emerald500
    ),
    OnboardingPage(
        title = "Private &\nSecure",
        description = "Your data stays on your device.\nNo servers, no accounts, no tracking.",
        icon = Icons.Default.Shield,
        accentColor = Violet500
    ),
    OnboardingPage(
        title = "Set Up\nYour Schedule",
        description = "Add your classes and ShowedUp will do the rest.",
        icon = Icons.Default.Schedule,
        accentColor = SignalGps
    )
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

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
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                OnboardingPageContent(
                    page = pages[page],
                    isVisible = pagerState.currentPage == page
                )
            }

            // Page indicators + button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenHorizontal)
                    .padding(bottom = Spacing.xxxl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dot indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = Spacing.xxl)
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 24.dp else 8.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            label = "dotWidth"
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) pages[index].accentColor
                                    else Gray700
                                )
                        )
                    }
                }

                val isLastPage = pagerState.currentPage == pages.size - 1

                ShowedUpButton(
                    text = if (isLastPage) "Get Started" else "Next",
                    onClick = {
                        if (isLastPage) {
                            onComplete()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(
                            imageVector = if (isLastPage) Icons.Default.ArrowForward
                            else Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    isVisible: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.screenHorizontal)
            .scale(scale),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon circle
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = page.accentColor.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                page.accentColor.copy(alpha = 0.3f)
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = page.accentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xxxl))

        Text(
            text = page.title,
            style = MaterialTheme.typography.displayMedium,
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
            modifier = Modifier.padding(horizontal = Spacing.xl)
        )
    }
}
