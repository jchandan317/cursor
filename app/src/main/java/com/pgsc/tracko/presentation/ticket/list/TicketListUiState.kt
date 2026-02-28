package com.pgsc.tracko.presentation.ticket.list

import androidx.paging.PagingData
import com.pgsc.tracko.domain.model.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

sealed class TicketListUiState {
    data object Idle : TicketListUiState()
    data object Loading : TicketListUiState()
    data class Success(val tickets: Flow<PagingData<Ticket>>) : TicketListUiState()
    data class Error(val message: String) : TicketListUiState()
}
