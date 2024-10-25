package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz

sealed class QuizUiEvent {
    data object Back : QuizUiEvent()
    data object ConfirmBack : QuizUiEvent()
    data object DismissDialog : QuizUiEvent()
    data class Answer(val answer: String) : QuizUiEvent()
    data object YesSend : QuizUiEvent()
    data object NoSend : QuizUiEvent()
}