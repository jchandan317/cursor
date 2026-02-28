package com.pgsc.tracko.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pgsc.tracko.data.mapper.toDomain
import com.pgsc.tracko.data.mapper.toEntity
import com.pgsc.tracko.data.remote.TicketApiService
import com.pgsc.tracko.data.remote.TicketPagingSource
import com.pgsc.tracko.data.remote.dto.TicketDto
import com.pgsc.tracko.domain.model.Ticket
import com.pgsc.tracko.domain.model.TicketFilter
import com.pgsc.tracko.domain.model.TicketStatus
import com.pgsc.tracko.domain.repository.TicketRepository
import com.pgsc.tracko.data.local.dao.TicketDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor(
    private val api: TicketApiService,
    private val ticketDao: TicketDao
) : TicketRepository {

    override fun getTickets(
        pageSize: Int,
        filter: TicketFilter?
    ): Flow<PagingData<Ticket>> {
        val source = TicketPagingSource(
            api = api,
            pageSize = pageSize,
            filter = filter
        )
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = { source }
        ).flow
    }

    override suspend fun getTicketById(id: String): Result<Ticket> = runCatching {
        try {
            val dto = api.getTicketById(id)
            dto.toDomain()
        } catch (e: Exception) {
            ticketDao.getTicketById(id)?.toDomain()
                ?: throw e
        }
    }

    override suspend fun createTicket(ticket: Ticket): Result<Ticket> = runCatching {
        val dto = TicketDto(
            id = ticket.id,
            title = ticket.title,
            description = ticket.description,
            status = ticket.status.name,
            category = ticket.category.name,
            createdAt = ticket.createdAt,
            updatedAt = ticket.updatedAt,
            assigneeId = ticket.assigneeId,
            agencyId = ticket.agencyId,
            priority = ticket.priority.name
        )
        api.createTicket(dto).toDomain().also { created ->
            ticketDao.insert(created.toEntity())
        }
    }

    override suspend fun updateTicketStatus(id: String, status: TicketStatus): Result<Ticket> = runCatching {
        api.updateTicketStatus(id, mapOf("status" to status.name)).toDomain().also { updated ->
            ticketDao.insert(updated.toEntity())
        }
    }
}
