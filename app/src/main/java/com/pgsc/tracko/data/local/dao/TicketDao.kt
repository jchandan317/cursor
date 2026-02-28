package com.pgsc.tracko.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pgsc.tracko.data.local.entity.TicketEntity

@Dao
interface TicketDao {

    @Query("SELECT * FROM tickets ORDER BY updatedAt DESC")
    fun getTicketsPaging(): PagingSource<Int, TicketEntity>

    @Query("SELECT * FROM tickets WHERE (:status IS NULL OR status = :status) AND (:category IS NULL OR category = :category) ORDER BY updatedAt DESC")
    fun getTicketsPagingFiltered(
        status: String?,
        category: String?
    ): PagingSource<Int, TicketEntity>

    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun getTicketById(id: String): TicketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<TicketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: TicketEntity)

    @Update
    suspend fun update(ticket: TicketEntity)

    @Query("DELETE FROM tickets")
    suspend fun clearAll()
}
