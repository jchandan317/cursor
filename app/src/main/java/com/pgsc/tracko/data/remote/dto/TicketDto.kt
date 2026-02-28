package com.pgsc.tracko.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "status") val status: String,
    @Json(name = "category") val category: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "assignee_id") val assigneeId: String? = null,
    @Json(name = "agency_id") val agencyId: String,
    @Json(name = "priority") val priority: String? = "MEDIUM"
)

@JsonClass(generateAdapter = true)
data class TicketListResponseDto(
    @Json(name = "data") val data: List<TicketDto>,
    @Json(name = "total") val total: Int? = null,
    @Json(name = "page") val page: Int? = null
)
