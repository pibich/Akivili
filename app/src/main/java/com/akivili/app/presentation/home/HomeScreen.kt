package com.akivili.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.data.dummy.DummyData
import com.akivili.app.domain.model.AppNotification
import com.akivili.app.domain.model.Game
import com.akivili.app.presentation.components.AkiviliTopBar
import com.akivili.app.presentation.components.NotificationBottomSheet
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    notifications: List<AppNotification>,
    onGameSelected: (String) -> Unit,
    onMarkAllNotificationsRead: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var showNotificationSheet by remember { mutableStateOf(false) }

    val categories = listOf("Semua", "Mobile", "RPG", "PC", "Voucher")
    val unreadCount = remember(notifications) { notifications.count { !it.isRead } }

    val filteredGames = remember(searchQuery, selectedCategory) {
        DummyData.games.filter { game ->
            val matchesCategory = selectedCategory == "Semua" || game.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = game.title.contains(searchQuery, ignoreCase = true) ||
                    game.publisher.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            AkiviliTopBar(
                title = "Akivili.",
                showBackButton = false,
                showNotificationIcon = true,
                unreadNotificationCount = unreadCount,
                onNotificationClick = { showNotificationSheet = true }
            )
        }
    ) { paddingValues ->
        if (showNotificationSheet) {
            NotificationBottomSheet(
                notifications = notifications,
                onDismiss = { showNotificationSheet = false },
                onMarkAllRead = onMarkAllNotificationsRead
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
        ) {
            // 1. Search Bar
            item(span = { GridItemSpan(3) }) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari game atau voucher...", fontSize = 13.sp, color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AkiviliPrimary) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Hapus Pencarian", tint = TextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = AkiviliPrimary,
                        unfocusedBorderColor = AkiviliOutline
                    )
                )
            }

            // 2. Banner Carousel
            item(span = { GridItemSpan(3) }) {
                HomeBannerCarousel(banners = DummyData.promoBanners)
            }

            // 3. Category Filter Chips
            item(span = { GridItemSpan(3) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = category },
                            label = { Text(category, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AkiviliPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) AkiviliPrimary else AkiviliOutline
                            )
                        )
                    }
                }
            }

            // 4. Section Title
            item(span = { GridItemSpan(3) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Populer Sekarang",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "${filteredGames.size} Item",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted, fontSize = 12.sp)
                    )
                }
            }

            // 5. Empty State when no games match
            if (filteredGames.isEmpty()) {
                item(span = { GridItemSpan(3) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Game \"$searchQuery\" tidak ditemukan",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Coba gunakan kata kunci lain atau pilih kategori berbeda.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {
                                    searchQuery = ""
                                    selectedCategory = "Semua"
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Reset Pencarian")
                            }
                        }
                    }
                }
            } else {
                // 6. Game Grid Items (3 columns)
                items(filteredGames, key = { it.id }) { game ->
                    GameCardItem(
                        game = game,
                        onClick = { onGameSelected(game.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBannerCarousel(banners: List<String>) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-scroll effect
    LaunchedEffect(key1 = pagerState.currentPage) {
        delay(4000)
        val nextPage = (pagerState.currentPage + 1) % banners.size
        pagerState.animateScrollToPage(nextPage)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .clip(RoundedCornerShape(14.dp))
        ) { page ->
            AsyncImage(
                model = banners[page],
                contentDescription = "Promo Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicator dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 18.dp else 6.dp, 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) AkiviliPrimary else Color(0xFFCBD5E1))
                )
            }
        }
    }
}

@Composable
fun GameCardItem(
    game: Game,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AkiviliSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = game.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = TextPrimary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Text(
                text = game.publisher,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 9.5.sp,
                    color = TextMuted
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
