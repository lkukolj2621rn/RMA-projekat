package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard

data class ResultData(
    val nickname: String,
    val result: Float,
    val quizNumber: Int = 0,
    val ranking: Int = 0
)
