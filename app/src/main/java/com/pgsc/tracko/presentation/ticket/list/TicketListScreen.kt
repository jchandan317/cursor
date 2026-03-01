package com.pgsc.tracko.presentation.ticket.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pgsc.tracko.domain.model.Ticket
import com.pgsc.tracko.domain.model.TicketCategory
import com.pgsc.tracko.domain.model.TicketFilter
import com.pgsc.tracko.domain.model.TicketStatus
import com.pgsc.tracko.navigation.Routes
import com.pgsc.tracko.ui.theme.toColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavController,
    viewModel: TicketListViewModel = hiltViewModel()
) {
    val pagingItems: LazyPagingItems<Ticket> = viewModel.pagingFlow.collectAsLazyPagingItems()
    val filter by viewModel.filter.collectAsStateWithLifecycle(initialValue = null)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(pagingItems.loadState) {
        (pagingItems.loadState.refresh as? androidx.paging.LoadState.Error)?.error?.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tickets") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterChipsRow(
                currentFilter = filter,
                onStatusSelected = { viewModel.setStatusFilter(it) },
                onCategorySelected = { viewModel.setCategoryFilter(it) }
            )
            Box(Modifier.fillMaxSize()) {
                when (val refresh = pagingItems.loadState.refresh) {
                    is androidx.paging.LoadState.Loading -> {
                        if (pagingItems.itemCount == 0) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            TicketListContent(
                                pagingItems = pagingItems,
                                onTicketClick = { navController.navigate(Routes.TicketDetail.createRoute(it.id)) }
                            )
                        }
                    }
                    is androidx.paging.LoadState.Error -> {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                refresh.error.localizedMessage ?: "Error loading tickets",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    else -> {
                        TicketListContent(
                            pagingItems = pagingItems,
                            onTicketClick = { navController.navigate(Routes.TicketDetail.createRoute(it.id)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChipsRow(
    currentFilter: TicketFilter?,
    onStatusSelected: (TicketStatus?) -> Unit,
    onCategorySelected: (TicketCategory?) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Status", style = MaterialTheme.typography.labelLarge)
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentFilter?.status == null,
                onClick = { onStatusSelected(null) },
                label = { Text("All") }
            )
            TicketStatus.entries.forEach { status ->
                FilterChip(
                    selected = currentFilter?.status == status,
                    onClick = { onStatusSelected(status) },
                    label = { Text(status.name.replace('_', ' ')) }
                )
            }
        }
        Text("Category", style = MaterialTheme.typography.labelLarge)
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentFilter?.category == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") }
            )
            TicketCategory.entries.forEach { category ->
                FilterChip(
                    selected = currentFilter?.category == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.name) }
                )
            }
        }
    }
}

@Composable
private fun TicketListContent(
    pagingItems: LazyPagingItems<Ticket>,
    onTicketClick: (Ticket) -> Unit
) {
    if (pagingItems.itemCount == 0) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No tickets",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems.peek(index)?.id ?: index }
        ) { index ->
            pagingItems[index]?.let { ticket ->
                TicketCard(
                    ticket = ticket,
                    onClick = { onTicketClick(ticket) }
                )
            }
        }
        when (val append = pagingItems.loadState.append) {
            is androidx.paging.LoadState.Loading -> {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is androidx.paging.LoadState.Error -> {
                item {
                    Text(
                        append.error.localizedMessage ?: "Error",
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                ticket.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                ticket.description.take(120).let { if (ticket.description.length > 120) "$it…" else it },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            androidx.compose.foundation.layout.Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .background(
                            ticket.status.toColor(),
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        ticket.status.name.replace('_', ' '),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
                Text(
                    ticket.category.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    ticket.updatedAt.take(10),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
