package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.register

sealed class ProfileDataUiEvent {
    data class NameChanged(val name: String) : ProfileDataUiEvent()
    data class NicknameChanged(val nickname: String) : ProfileDataUiEvent()
    data class EmailChanged(val email: String) : ProfileDataUiEvent()
    data object Submit : ProfileDataUiEvent()
}