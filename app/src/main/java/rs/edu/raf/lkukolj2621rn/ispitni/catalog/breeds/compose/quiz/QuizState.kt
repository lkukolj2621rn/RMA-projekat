package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.ImageData

data class QuizState(
    val exitDialogShown: Boolean = false,
    val questionNumber: Int = 1,
    val loading: Boolean = true,
    val result: Float? = null,
    val timeLeft: Int = 300,
    val question: String = "Question",
    val answers: List<String> = listOf("1", "2", "3", "4"),
    val image: ImageData? = null,
    val imageError: String? = null,
    val leaving: Boolean = false,
    val sending: Boolean = false,
    val correct: Int = 0,
    val sendError: String? = null,
    val correctAnswer: String = ""
)
