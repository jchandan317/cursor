package com.pgsc.tracko.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val status: String,
    val category: String,
    val createdAt: String,
    val updatedAt: String,
    val assigneeId: String?,
    val agencyId: String,
    val priority: String
)
