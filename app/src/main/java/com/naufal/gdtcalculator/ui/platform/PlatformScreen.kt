package com.naufal.gdtcalculator.ui.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.naufal.gdtcalculator.data.model.Platform
import com.naufal.gdtcalculator.navigation.Screen
import com.naufal.gdtcalculator.ui.base.HighlightHelper
import com.naufal.gdtcalculator.ui.topic.RatingText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformScreen(
    navController: NavHostController,
    isStandalone: Boolean = false,
    selectedGenre: String = "",
    selectedSecondGenre: String = "",
    selectedAudience: String = "",
    viewModel: PlatformViewModel = viewModel()
) {
    // Generate suggestions when screen opens
    LaunchedEffect(selectedGenre, selectedSecondGenre, selectedAudience) {
        if (selectedGenre.isNotBlank()) {
            viewModel.generateSuggestions(
                genre = selectedGenre,
                secondGenre = selectedSecondGenre,
                audience = selectedAudience
            )
        }
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredPlatforms by viewModel.filteredPlatforms.collectAsState()
    val suggestedPlatforms by viewModel.suggestedPlatforms.collectAsState()
    val selectedPlatform by viewModel.selectedPlatform.collectAsState()

    // Platforms not in suggested list (for "Other Platforms" section)
    val otherPlatforms = if (searchQuery.isBlank()) {
        filteredPlatforms.filter { platform ->
            suggestedPlatforms.none { it.name == platform.name }
        }
    } else {
        filteredPlatforms
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isStandalone) "Platform Reference"
                        else "Platform Suggestion"
                    )
                },
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
                .imePadding()
        ) {
            // ── Section 1: Search Bar ──────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = { Text("Search platform...") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // ── Table Header ───────────────────────────────────────
            PlatformTableHeader()
            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // ── Section 2: Suggested Platforms ─────────────────
                if (suggestedPlatforms.isNotEmpty() && searchQuery.isBlank()) {
                    item {
                        SectionLabel(
                            title = "✨ Suggested for $selectedGenre" +
                                    if (selectedAudience.isNotBlank()) " · $selectedAudience" else "",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(suggestedPlatforms, key = { platform -> "suggested_${platform.name}" }) { platform ->
                        PlatformTableRow(
                            platform = platform,
                            isSelected = selectedPlatform?.name == platform.name,
                            selectedGenre = selectedGenre,
                            selectedSecondGenre = selectedSecondGenre,
                            selectedAudience = selectedAudience,
                            onClick = { viewModel.onPlatformSelected(platform) }
                        )
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }

                // ── Section 3: Other Platforms ─────────────────────
                if (otherPlatforms.isNotEmpty()) {
                    item {
                        SectionLabel(
                            title = if (searchQuery.isBlank())
                                "Other Platforms"
                            else
                                "Search Results",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    items(otherPlatforms, key = { platform -> "other_${platform.name}" }) { platform ->
                        PlatformTableRow(
                            platform = platform,
                            isSelected = selectedPlatform?.name == platform.name,
                            selectedGenre = selectedGenre,
                            selectedSecondGenre = selectedSecondGenre,
                            selectedAudience = selectedAudience,
                            onClick = { viewModel.onPlatformSelected(platform) }
                        )
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }
            }

            // ── Next Button ────────────────────────────────────────
            if (!isStandalone && selectedPlatform != null) {
                Button(
                    onClick = {
                        navController.navigate(Screen.DevPhase.createRoute(1))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(52.dp)
                ) {
                    Text("Next → Development Phase 1", fontSize = 16.sp)
                }
            }

            if (isStandalone) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(52.dp)
                ) {
                    Text("Back to Home", fontSize = 16.sp)
                }
            }
        }
    }
}

// ── Section Label ──────────────────────────────────────────────────
@Composable
fun SectionLabel(title: String, color: androidx.compose.ui.graphics.Color) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    )
}

// ── Platform Table Header ──────────────────────────────────────────
@Composable
fun PlatformTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Platform",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f),
            fontSize = 13.sp
        )
        listOf(
            "Act" to 0.6f,
            "Adv" to 0.6f,
            "RPG" to 0.6f,
            "Sim" to 0.6f,
            "Str" to 0.6f,
            "Cas" to 0.6f,
            "Y"   to 0.55f,
            "E"   to 0.55f,
            "M"   to 0.55f
        ).forEach { (label, weight) ->
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(weight),
                fontSize = 11.sp
            )
        }
    }
}

// ── Platform Table Row ─────────────────────────────────────────────
@Composable
fun PlatformTableRow(
    platform: Platform,
    isSelected: Boolean,
    selectedGenre: String = "",
    selectedSecondGenre: String = "",
    selectedAudience: String = "",
    onClick: () -> Unit
) {
    val highlightGenreIndex = remember(selectedGenre) {
        HighlightHelper.getGenreIndex(selectedGenre)
    }
    val highlightSecondGenreIndex = remember(selectedSecondGenre) {
        HighlightHelper.getGenreIndex(selectedSecondGenre)
    }
    val highlightAudienceIndex = remember(selectedAudience) {
        HighlightHelper.getAudienceIndex(selectedAudience)
    }

    val ratings = remember(platform) {
        listOf(
            platform.action, platform.adventure, platform.rpg,
            platform.simulation, platform.strategy, platform.casual,
            platform.audienceYoung, platform.audienceEveryone, platform.audienceMature
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .then(
                if (isSelected)
                    Modifier.background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
                else Modifier
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = platform.name,
                modifier = Modifier
                    .weight(1.5f)
                    .padding(end = 4.dp),
                fontSize = 12.sp
            )
            ratings.forEachIndexed { index, rating ->
                val isHighlighted = (index == highlightGenreIndex
                        || index == highlightSecondGenreIndex
                        || index == highlightAudienceIndex)
                        && rating == "+++"
                val weight = if (index >= 6) 0.55f else 0.6f
                RatingText(
                    rating = rating,
                    modifier = Modifier.weight(weight),
                    isHighlighted = isHighlighted
                )
            }
        }
    }
}