package com.naufal.gdtcalculator.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.naufal.gdtcalculator.data.model.Topic
import com.naufal.gdtcalculator.navigation.Screen
import com.naufal.gdtcalculator.ui.bookmark.BookmarkViewModel
import com.naufal.gdtcalculator.ui.topic.RatingText
import com.naufal.gdtcalculator.ui.topic.TopicViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavHostController,
    viewModel: TopicViewModel = viewModel(),
    bookmarkViewModel: BookmarkViewModel = viewModel()
) {
    val selectedTopic by viewModel.selectedTopic.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val selectedSecondGenre by viewModel.selectedSecondGenre.collectAsState()
    val selectedAudience by viewModel.selectedAudience.collectAsState()
    val isMultiGenre = selectedSecondGenre.isNotBlank()
    val bookmarks by bookmarkViewModel.bookmarks.collectAsState()
    val isBookmarked = remember(bookmarks, selectedTopic, selectedGenre) {
        bookmarks.any {
            it.topic == (selectedTopic?.name ?: "") && it.primaryGenre == selectedGenre
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val topic = selectedTopic ?: return@IconButton
                            bookmarkViewModel.toggleBookmark(
                                topic = topic.name,
                                primaryGenre = selectedGenre,
                                secondaryGenre = selectedSecondGenre,
                                audience = selectedAudience
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked)
                                Icons.Filled.Bookmark
                            else
                                Icons.Filled.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
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
            // ── Guard ──────────────────────────────────────────────
            if (selectedTopic == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No topic selected. Please go back.")
                }
                return@Column
            }

            val topic = selectedTopic!!

            Spacer(modifier = Modifier.height(16.dp))

            // ── Section: Your Selection ────────────────────────────
            ReviewSectionHeader(title = "Your Selection")
            ReviewRow(label = "Topic", value = topic.name)

            if (isMultiGenre) {
                ReviewRow(label = "Primary Genre", value = selectedGenre)
                ReviewRow(label = "Secondary Genre", value = selectedSecondGenre)
                ReviewRow(label = "Genre Combo", value = "$selectedGenre-$selectedSecondGenre")
            } else {
                ReviewRow(label = "Genre", value = selectedGenre)
            }

            ReviewRow(label = "Target Audience", value = selectedAudience.ifBlank { "—" })

            Spacer(modifier = Modifier.height(24.dp))

            // ── Section: Genre Affinity ────────────────────────────
            ReviewSectionHeader(title = "Genre Affinity")
            GenreAffinityTable(
                topic = topic,
                selectedGenre = selectedGenre,
                selectedSecondGenre = if (isMultiGenre) selectedSecondGenre else ""
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Section: Target Audience Affinity ──────────────────
            ReviewSectionHeader(title = "Target Audience Affinity")
            AudienceAffinityTable(
                topic = topic,
                selectedAudience = selectedAudience
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Next Button ────────────────────────────────────────
            Button(
                onClick = { navController.navigate(Screen.Platform.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Next → Platform Suggestion", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Section Header ─────────────────────────────────────────────────
@Composable
fun ReviewSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
}

// ── Simple Key-Value Row ───────────────────────────────────────────
@Composable
fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

// ── Genre Affinity Table ───────────────────────────────────────────
@Composable
fun GenreAffinityTable(
    topic: Topic,
    selectedGenre: String,
    selectedSecondGenre: String = ""
) {
    val genres = remember(topic) {
        listOf(
            "Action"     to topic.action,
            "Adventure"  to topic.adventure,
            "RPG"        to topic.rpg,
            "Simulation" to topic.simulation,
            "Strategy"   to topic.strategy,
            "Casual"     to topic.casual
        )
    }

    genres.forEach { (genre, rating) ->
        val isPrimary = genre == selectedGenre
        val isSecondary = genre == selectedSecondGenre
        val isSelected = isPrimary || isSecondary

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
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSelected) {
                        Text(
                            text = "▶ ",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = genre,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    // Label Primary / Secondary
                    if (isPrimary || isSecondary) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (isPrimary)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (isPrimary) "1st" else "2nd",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(
                                    horizontal = 6.dp,
                                    vertical = 2.dp
                                )
                            )
                        }
                    }
                }
                RatingText(rating = rating, isHighlighted = false)
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}

// ── Audience Affinity Table ────────────────────────────────────────
@Composable
fun AudienceAffinityTable(topic: Topic, selectedAudience: String = "") {
    val audiences = remember(topic) {
        listOf(
            "Young (Y)"    to topic.audienceYoung,
            "Everyone (E)" to topic.audienceEveryone,
            "Mature (M)"   to topic.audienceMature
        )
    }

    audiences.forEach { (audience, rating) ->
        val isSelected = audience == selectedAudience

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
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSelected) {
                        Text(
                            text = "▶ ",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = audience,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
                RatingText(rating = rating, isHighlighted = false)
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}