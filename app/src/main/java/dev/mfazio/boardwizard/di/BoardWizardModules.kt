package dev.mfazio.boardwizard.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mfazio.bgg.api.repositories.BGGXMLServiceRepository
import dev.mfazio.bgg.api.repositories.BGGXMLServiceRepositoryImpl
import dev.mfazio.boardwizard.data.BoardWizardDAO
import dev.mfazio.boardwizard.data.BoardWizardDB
import dev.mfazio.boardwizard.data.BoardWizardRepository
import dev.mfazio.boardwizard.data.BoardWizardRepositoryImpl
import dev.mfazio.boardwizard.data.settings.BoardWizardSettings
import dev.mfazio.boardwizard.data.settings.BoardWizardSettingsImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class BoardWizardApplicationModules {
    @Provides
    @Singleton
    fun provideBoardWizardDB(@ApplicationContext appContext: Context): BoardWizardDB =
        Room.databaseBuilder(
            appContext,
            BoardWizardDB::class.java,
            "BoardWizardDB"
        ).build()

    @Provides
    fun provideBoardWizardDAO(boardWizardDB: BoardWizardDB): BoardWizardDAO = boardWizardDB.dao()

    @Provides
    @Singleton
    fun provideBoardWizardSettings(@ApplicationContext appContext: Context): BoardWizardSettings =
        BoardWizardSettingsImpl(appContext)

    @Singleton
    @Provides
    fun provideBGGXMLServiceRepository(): BGGXMLServiceRepository = BGGXMLServiceRepositoryImpl()
}

@InstallIn(SingletonComponent::class)
@Module
internal abstract class BoardWizardAbstractModules {

    @Singleton
    @Binds
    abstract fun bindsBoardWizardRepository(
        boardWizardRepositoryImpl: BoardWizardRepositoryImpl
    ) : BoardWizardRepository
}