package com.pgsc.tracko.domain.model

/**
 * Domain model for a ticket. Pure Kotlin, no framework dependencies.
 */
data class Ticket(
    val id: String,
    val title: String,
    val description: String,
    val status: TicketStatus,
    val category: TicketCategory,
    val createdAt: String,
    val updatedAt: String,
    val assigneeId: String?,
    val agencyId: String,
    val priority: TicketPriority = TicketPriority.MEDIUM
)

enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

enum class TicketCategory {
    MAINTENANCE,
    COMPLAINT,
    REQUEST,
    OTHER
}

enum class TicketPriority {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Filter criteria for ticket list (status and/or category).
 */
data class TicketFilter(
    val status: TicketStatus? = null,
    val category: TicketCategory? = null
)
