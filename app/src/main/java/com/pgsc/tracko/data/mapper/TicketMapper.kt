package com.pgsc.tracko.data.mapper

import com.pgsc.tracko.data.local.entity.TicketEntity
import com.pgsc.tracko.data.remote.dto.TicketDto
import com.pgsc.tracko.domain.model.Ticket
import com.pgsc.tracko.domain.model.TicketCategory
import com.pgsc.tracko.domain.model.TicketPriority
import com.pgsc.tracko.domain.model.TicketStatus

fun TicketDto.toDomain(): Ticket = Ticket(
    id = id,
    title = title,
    description = description,
    status = status.toTicketStatus(),
    category = category.toTicketCategory(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    assigneeId = assigneeId,
    agencyId = agencyId,
    priority = (priority ?: "MEDIUM").toTicketPriority()
)

fun TicketEntity.toDomain(): Ticket = Ticket(
    id = id,
    title = title,
    description = description,
    status = status.toTicketStatus(),
    category = category.toTicketCategory(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    assigneeId = assigneeId,
    agencyId = agencyId,
    priority = priority.toTicketPriority()
)

fun Ticket.toEntity(): TicketEntity = TicketEntity(
    id = id,
    title = title,
    description = description,
    status = status.name,
    category = category.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    assigneeId = assigneeId,
    agencyId = agencyId,
    priority = priority.name
)

private fun String.toTicketStatus(): TicketStatus = when (uppercase()) {
    "OPEN" -> TicketStatus.OPEN
    "IN_PROGRESS" -> TicketStatus.IN_PROGRESS
    "RESOLVED" -> TicketStatus.RESOLVED
    "CLOSED" -> TicketStatus.CLOSED
    else -> TicketStatus.OPEN
}

private fun String.toTicketCategory(): TicketCategory = when (uppercase()) {
    "MAINTENANCE" -> TicketCategory.MAINTENANCE
    "COMPLAINT" -> TicketCategory.COMPLAINT
    "REQUEST" -> TicketCategory.REQUEST
    else -> TicketCategory.OTHER
}

private fun String.toTicketPriority(): TicketPriority = when (uppercase()) {
    "LOW" -> TicketPriority.LOW
    "HIGH" -> TicketPriority.HIGH
    else -> TicketPriority.MEDIUM
}
