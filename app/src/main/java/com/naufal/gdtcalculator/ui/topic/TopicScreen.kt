package com.naufal.gdtcalculator.ui.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.naufal.gdtcalculator.data.model.Topic
import com.naufal.gdtcalculator.data.repository.GDTRepository
import com.naufal.gdtcalculator.navigation.Screen
import com.naufal.gdtcalculator.ui.base.HighlightHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    navController: NavHostController,
    viewModel: TopicViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredTopics by viewModel.filteredTopics.collectAsState()
    val selectedTopic by viewModel.selectedTopic.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val selectedSecondGenre by viewModel.selectedSecondGenre.collectAsState()
    val selectedAudience by viewModel.selectedAudience.collectAsState()
    val isReadyToReview = selectedTopic != null
            && selectedGenre.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Select Topic")
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
            // ── Search Bar ─────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = { Text("Search topic...") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear naufal.gdtcalculator"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // ── Primary Genre ─────────────────────────────────────────
            Text(
                text = "Primary Genre:",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(GDTRepository.getGenres().filter { it != selectedSecondGenre }) { genre ->
                    FilterChip(
                        selected = selectedGenre == genre,
                        onClick = {
                            viewModel.onGenreSelected(
                                if (selectedGenre == genre) "" else genre
                            )
                        },
                        label = { Text(genre, fontSize = 13.sp) }
                    )
                }
            }

            // ── Secondary Genre ─────────────────────────────────────────

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Secondary Genre:",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(GDTRepository.getGenres().filter { it != selectedGenre }) { genre ->
                    FilterChip(
                        selected = selectedSecondGenre == genre,
                        onClick = { viewModel.onSecondGenreSelected(genre) },
                        label = { Text(genre, fontSize = 13.sp) }
                    )
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

            // ── Target Audience ────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "Target Audience:",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    onClick = { if (selectedAudience.isNotEmpty()) viewModel.clearAudience() },
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.alpha(if (selectedAudience.isNotEmpty()) 1f else 0f)
                ) {
                    Text(
                        text = "Reset",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listOf("Young (Y)", "Everyone (E)", "Mature (M)")) { audience ->
                    FilterChip(
                        selected = selectedAudience == audience,
                        onClick = { viewModel.onAudienceSelected(audience) },
                        label = { Text(audience, fontSize = 13.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Table Header ───────────────────────────────────────
            TopicTableHeader()
            HorizontalDivider()

            // ── Topic List ─────────────────────────────────────────
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredTopics, key = { topic -> topic.name }) { topic ->
                    val isHooked = selectedTopic?.name == topic.name
                    TopicTableRow(
                        topic = topic,
                        isSelected = selectedTopic?.name == topic.name,
                        isHooked = isHooked,
                        selectedGenre = selectedGenre,
                        selectedSecondGenre = selectedSecondGenre,
                        selectedAudience = selectedAudience,
                        onClick = { viewModel.onTopicSelected(topic) }
                    )
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }

            // ── OK Button ──────────────────────────────────────────
            if (isReadyToReview) {
                Button(
                    onClick = { navController.navigate(Screen.Review.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(52.dp)
                ) {
                    Text("OK — View Review", fontSize = 16.sp)
                }
            }
        }
    }
}

// ── Table Header ───────────────────────────────────────────────────
@Composable
fun TopicTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Topic",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f)
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

// ── Table Row ─────────────────────────────────────────
@Composable
fun TopicTableRow(
    topic: Topic,
    isSelected: Boolean,
    isHooked: Boolean = false,
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

    val ratings = remember(topic) {
        listOf(
            topic.action, topic.adventure, topic.rpg,
            topic.simulation, topic.strategy, topic.casual,
            topic.audienceYoung, topic.audienceEveryone, topic.audienceMature
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = topic.name,
                modifier = Modifier
                    .weight(1.5f)
                    .padding(end = 4.dp),
                fontSize = 13.sp
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

// ── Rating Text with color ─────────────────────────────────────────
@Composable
fun RatingText(
    rating: String,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val color = when (rating) {
        "+++"  -> if (isHighlighted)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.primary
        "++"   -> MaterialTheme.colorScheme.tertiary
        "+"    -> MaterialTheme.colorScheme.onSurface
        "--"   -> MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
        "---"  -> MaterialTheme.colorScheme.error
        else   -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isHighlighted) {
            Text(
                text = rating,
                color = color,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        } else {
            Text(
                text = rating,
                color = color,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}