package com.pgsc.tracko.domain.repository

import com.pgsc.tracko.domain.model.Ticket
import com.pgsc.tracko.domain.model.TicketFilter
import com.pgsc.tracko.domain.model.TicketStatus
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData

interface TicketRepository {

    /**
     * Paginated ticket list with optional filters.
     */
    fun getTickets(
        pageSize: Int,
        filter: TicketFilter? = null
    ): Flow<PagingData<Ticket>>

    suspend fun getTicketById(id: String): Result<Ticket>

    suspend fun createTicket(ticket: Ticket): Result<Ticket>

    suspend fun updateTicketStatus(id: String, status: TicketStatus): Result<Ticket>
}
