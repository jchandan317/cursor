package com.pgsc.tracko.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pgsc.tracko.data.local.dao.TicketDao
import com.pgsc.tracko.data.local.entity.TicketEntity

@Database(
    entities = [TicketEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrackoDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
}
