package com.pgsc.tracko.presentation.ticket.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgsc.tracko.domain.model.TicketFilter
import com.pgsc.tracko.domain.usecase.TicketListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

private const val PAGE_SIZE = 20

@OptIn(ExperimentalCoroutinesApi::class)

@HiltViewModel
class TicketListViewModel @Inject constructor(
    private val ticketListUseCase: TicketListUseCase
) : ViewModel() {

    private val _filter = MutableStateFlow<TicketFilter?>(null)
    val filter: StateFlow<TicketFilter?> = _filter.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)

    val pagingFlow = combine(_filter, _refreshTrigger) { f, _ -> f }
        .flatMapLatest { ticketListUseCase(PAGE_SIZE, it) }
        .cachedIn(viewModelScope)

    fun setStatusFilter(status: com.pgsc.tracko.domain.model.TicketStatus?) {
        _filter.value = (_filter.value ?: TicketFilter()).copy(status = status)
    }

    fun setCategoryFilter(category: com.pgsc.tracko.domain.model.TicketCategory?) {
        _filter.value = (_filter.value ?: TicketFilter()).copy(category = category)
    }

    fun refresh() {
        _refreshTrigger.value++
    }
}
