package dev.mfazio.boardwizard.data.settings

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface BoardWizardSettings {
    suspend fun userNameFlow(newUserName: String? = null): Flow<String?>
    fun userNameLiveData(): LiveData<String?>
    suspend fun userName(newUserName: String? = null): String?
}