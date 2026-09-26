@file:Suppress("SpellCheckingInspection")

package com.naufal.gdtcalculator.ui.development

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.naufal.gdtcalculator.data.model.DevData
import com.naufal.gdtcalculator.data.model.TdBalance
import com.naufal.gdtcalculator.navigation.Screen
import com.naufal.gdtcalculator.ui.topic.RatingText
import com.naufal.gdtcalculator.ui.topic.TopicViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevScreen(
    navController: NavHostController,
    phaseNumber: Int,
    topicViewModel: TopicViewModel = viewModel(),
    devViewModel: DevViewModel = viewModel()
) {
    val selectedGenre by topicViewModel.selectedGenre.collectAsState()
    val selectedSecondGenre by topicViewModel.selectedSecondGenre.collectAsState()
    val isMultiGenre = selectedSecondGenre.isNotBlank()

    // ── Get correct dev data based on mode ─────────────────────────
    val singleDevData = if (!isMultiGenre)
        devViewModel.getSingleDevData(selectedGenre)
    else null

    val multiDevData = if (isMultiGenre)
        devViewModel.getMultiDevData(selectedGenre, selectedSecondGenre)
    else null

    val tdRatio = singleDevData?.tdRatio ?: multiDevData?.tdRatio
    val tdBalances = devViewModel.getTdBalanceForStage(phaseNumber)

    val devData: DevData? = multiDevData ?: singleDevData
    val sliders = if (devData != null) {
        devViewModel.getSlidersForStage(devData, phaseNumber)
    } else {
        emptyList()
    }

    // ── Success notification state ─────────────────────────────────
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            snackbarHostState.showSnackbar(
                message = "🎉 You have succeeded in making a game!",
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(200L)
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Development — Phase $phaseNumber") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Phase Indicator ────────────────────────────────────
            PhaseIndicator(currentPhase = phaseNumber)

            Spacer(modifier = Modifier.height(16.dp))

            // ── Genre Info ─────────────────────────────────────────
            if (isMultiGenre) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Genre Combo:", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "$selectedGenre-$selectedSecondGenre",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Genre:", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = selectedGenre.ifBlank { "Not selected" },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ── T/D Ratio ──────────────────────────────────────────
            if (tdRatio != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("T/D Ratio:", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "$tdRatio",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            // ── No data warning for multi genre ────────────────────
            if (isMultiGenre && multiDevData == null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "⚠️ No data found for combo: $selectedGenre-$selectedSecondGenre. " +
                                "Try swapping Primary and Secondary genre.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Slider Focus Section ───────────────────────────────
            DevSectionHeader(title = "Slider Focus — Phase $phaseNumber")

            if (sliders.isEmpty() && (isMultiGenre && multiDevData != null
                        || !isMultiGenre && singleDevData != null)) {
                Text(
                    text = "No data for this phase.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else if (sliders.isNotEmpty()) {
                sliders.forEach { (sliderName, rating) ->
                    SliderRow(
                        sliderName = sliderName,
                        rating = rating,
                        tdBalance = tdBalances.find { it.sliderName == sliderName }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── TD Balance Section ─────────────────────────────────
            DevSectionHeader(title = "Tech / Design Balance")
            tdBalances.forEach { balance ->
                TdBalanceRow(balance = balance)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Navigation Button ──────────────────────────────────
            if (phaseNumber < 3) {
                Button(
                    onClick = {
                        navController.navigate(
                            Screen.DevPhase.createRoute(phaseNumber + 1)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Next Phase → Phase ${phaseNumber + 1}", fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = { showSuccess = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("🎉 Finish Game!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── Freeze overlay on success ──────────────────────────────
        if (showSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {}
            )
        }
    }
}

// ── Phase Indicator ────────────────────────────────────────────────
@Composable
fun PhaseIndicator(currentPhase: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (1..3).forEach { phase ->
            val isActive = phase == currentPhase
            val isDone = phase < currentPhase
            Surface(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                color = when {
                    isActive -> MaterialTheme.colorScheme.primary
                    isDone   -> MaterialTheme.colorScheme.primaryContainer
                    else     -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Text(
                    text = if (isDone) "✓ Phase $phase" else "Phase $phase",
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ── Dev Section Header ─────────────────────────────────────────────
@Composable
fun DevSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
}

// ── Slider Row ─────────────────────────────────────────────────────
@Composable
fun SliderRow(
    sliderName: String,
    rating: String,
    tdBalance: TdBalance?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sliderName,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        RatingText(
            rating = rating,
            modifier = Modifier.weight(0.8f)
        )
        if (tdBalance != null) {
            Text(
                text = "D:${tdBalance.designPercent}% T:${tdBalance.techPercent}%",
                modifier = Modifier.weight(1.5f),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}

// ── TD Balance Row ─────────────────────────────────────────────────
@Composable
fun TdBalanceRow(balance: TdBalance) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = balance.sliderName,
            modifier = Modifier.weight(1f),
            fontSize = 13.sp
        )
        LinearProgressIndicator(
            progress = { balance.designPercent / 100f },
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = "D${balance.designPercent}%",
            modifier = Modifier
                .weight(0.6f)
                .padding(start = 4.dp),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}