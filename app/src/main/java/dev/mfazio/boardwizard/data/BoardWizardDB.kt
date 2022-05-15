package dev.mfazio.boardwizard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.mfazio.boardwizard.data.entities.BoardGameEntity
import dev.mfazio.boardwizard.data.entities.GamePlayEntity

@Database(entities = [BoardGameEntity::class, GamePlayEntity::class], version = 1)
abstract class BoardWizardDB : RoomDatabase() {
    abstract fun dao(): BoardWizardDAO
}