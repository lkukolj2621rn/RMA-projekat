package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileData(
    val name: String,
    val nickname: String,
    val email: String,
) {
    companion object {
        val EMPTY = ProfileData(name = "", nickname = "", email = "")
    }
}
