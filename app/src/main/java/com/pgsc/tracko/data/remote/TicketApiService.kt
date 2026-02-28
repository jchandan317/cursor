package com.pgsc.tracko.data.remote

import com.pgsc.tracko.data.remote.dto.TicketDto
import com.pgsc.tracko.data.remote.dto.TicketListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketApiService {

    @GET("tickets")
    suspend fun getTickets(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("status") status: String? = null,
        @Query("category") category: String? = null
    ): TicketListResponseDto

    @GET("tickets/{id}")
    suspend fun getTicketById(@Path("id") id: String): TicketDto

    @POST("tickets")
    suspend fun createTicket(@Body ticket: TicketDto): TicketDto

    @PATCH("tickets/{id}")
    suspend fun updateTicketStatus(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): TicketDto
}
