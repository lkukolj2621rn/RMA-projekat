package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.ProfileData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.ProfileDataStore
import javax.inject.Inject

@HiltViewModel
class ProfileDataViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileDataState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileDataState.() -> ProfileDataState) =
        _state.getAndUpdate(reducer)

    fun observeEvent(event: ProfileDataUiEvent) {
        when (event) {
            is ProfileDataUiEvent.NameChanged -> setState { copy(name=event.name,
                nameError = if (event.name.isBlank()) "Must not be blank." else null) }
            is ProfileDataUiEvent.EmailChanged -> setState { copy(email=event.email,
                emailError = if (event.email.isBlank()) "Must not be blank."
                            else if (event.email.matches(Regex("[^@]+@[^@.]+\\.[^@]+"))) null
                            else "Must be a vaild email.") }
            is ProfileDataUiEvent.NicknameChanged -> setState { copy(nickname=event.nickname,
                nicknameError = if (event.nickname.isBlank()) "Must not be blank."
                else if (event.nickname.matches(Regex("[a-zA-Z0-9_]+"))) null
                else "Must only contain letters, numbers and underscores.") }
            is ProfileDataUiEvent.Submit -> viewModelScope.launch {
                val state = state.value
                withContext(Dispatchers.IO) {
                    profileDataStore.updateProfileData {
                        ProfileData(
                            name = state.name,
                            nickname = state.nickname,
                            email = state.email
                        )
                    }
                }
                setState {
                    state.copy(registered = true, submitted = true)
                }
            }
        }
    }

    init {
        val p = profileDataStore.data.value
        if (p.name.isNotEmpty())
            setState { copy(registered = true, name = p.name, nickname = p.nickname, email = p.email) }
    }


}