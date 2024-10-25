package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.register

data class ProfileDataState (
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val nameError: String? = null,
    val nicknameError: String? = null,
    val emailError: String? = null,
    val registered: Boolean = false,
    val submitted: Boolean = false,
)