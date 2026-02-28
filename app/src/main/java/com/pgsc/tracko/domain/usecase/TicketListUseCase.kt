package com.pgsc.tracko.domain.usecase

import com.pgsc.tracko.domain.model.Ticket
import com.pgsc.tracko.domain.model.TicketFilter
import com.pgsc.tracko.domain.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData

class TicketListUseCase(
    private val repository: TicketRepository
) {

    operator fun invoke(
        pageSize: Int = 20,
        filter: TicketFilter? = null
    ): Flow<PagingData<Ticket>> =
        repository.getTickets(pageSize = pageSize, filter = filter)
}
