package com.pgsc.tracko.ui.theme

import androidx.compose.ui.graphics.Color
import com.pgsc.tracko.domain.model.TicketStatus

fun TicketStatus.toColor(): Color = when (this) {
    TicketStatus.OPEN -> StatusOpen
    TicketStatus.IN_PROGRESS -> StatusInProgress
    TicketStatus.RESOLVED -> StatusResolved
    TicketStatus.CLOSED -> StatusClosed
}
