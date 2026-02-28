package com.pgsc.tracko.di

import android.content.Context
import androidx.room.Room
import com.pgsc.tracko.data.local.TrackoDatabase
import com.pgsc.tracko.data.local.dao.TicketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTrackoDatabase(
        @ApplicationContext context: Context
    ): TrackoDatabase = Room.databaseBuilder(
        context,
        TrackoDatabase::class.java,
        "tracko_db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideTicketDao(database: TrackoDatabase): TicketDao = database.ticketDao()
}
