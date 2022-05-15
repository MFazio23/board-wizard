package dev.mfazio.boardwizard.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BoardWizardSettingsImpl @Inject constructor(
    @ApplicationContext context: Context
) : BoardWizardSettings {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "boardWizardSettings"
    )

    private val dataStore = context.dataStore

    override suspend fun userNameFlow(newUserName: String?): Flow<String?> {
        newUserName?.let { userName ->
            dataStore.edit { preferences ->
                preferences[userNameKey] = userName
            }
        }

        return dataStore.data.map { preferences ->
            preferences[userNameKey]
        }
    }

    override fun userNameLiveData() = dataStore.data.map {  preferences ->
        preferences[userNameKey]
    }.asLiveData()

    override suspend fun userName(newUserName: String?): String? =
        userNameFlow(newUserName).firstOrNull()

    companion object {
        private val userNameKey = stringPreferencesKey("USER_NAME")
    }

}