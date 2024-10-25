package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProfileDataStore @Inject constructor(
    private val dataStore: DataStore<ProfileData>
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val data = dataStore.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { dataStore.data.first() },
        )

    val dataFlow = dataStore.data

    suspend fun updateProfileData(
        f: suspend (ProfileData) -> ProfileData
    ) {
        dataStore.updateData(f)
    }
}