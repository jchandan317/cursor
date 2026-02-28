package com.pgsc.tracko.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pgsc.tracko.data.mapper.toDomain
import com.pgsc.tracko.data.remote.dto.TicketDto
import com.pgsc.tracko.domain.model.Ticket
import com.pgsc.tracko.domain.model.TicketFilter

class TicketPagingSource(
    private val api: TicketApiService,
    private val pageSize: Int,
    private val filter: TicketFilter?
) : PagingSource<Int, Ticket>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Ticket> {
        return try {
            val page = params.key ?: 1
            val response = api.getTickets(
                page = page,
                pageSize = params.loadSize.coerceAtLeast(1).coerceAtMost(pageSize),
                status = filter?.status?.name,
                category = filter?.category?.name
            )
            LoadResult.Page(
                data = response.data.map(TicketDto::toDomain),
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.data.size >= params.loadSize) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Ticket>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
