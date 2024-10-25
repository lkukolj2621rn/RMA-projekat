package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

fun NavGraphBuilder.quizScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val quizViewModel = hiltViewModel<QuizViewModel>()
    val state by quizViewModel.state.collectAsState()

    if (state.leaving)
        navController.navigateUp()
    else
        QuizScreen(state, quizViewModel::observeEvent, quizViewModel.imageLoader)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(state: QuizState, observeEvent: (QuizUiEvent) -> Unit, imageLoader: ImageLoader?) {
    BackHandler {
        observeEvent(QuizUiEvent.Back)
    }
    if (state.sendError != null) {
        AlertDialog(
            onDismissRequest = { observeEvent(QuizUiEvent.ConfirmBack) },
            confirmButton = { TextButton(onClick = { observeEvent(QuizUiEvent.ConfirmBack) }) {
                Text(text = "OK")
            }},
            title = { Text("Error while sending result") },
            text = { Text(state.sendError)}
        )
    }
    else if (!state.sending && state.result != null)
        AlertDialog(
            onDismissRequest = { },
            confirmButton = { TextButton(onClick = { observeEvent(QuizUiEvent.YesSend) }) {
                Text(text = "YES")
            }},
            dismissButton = { TextButton(onClick = { observeEvent(QuizUiEvent.NoSend) }) {
                Text(text = "NO")
            }},
            title = { Text("Quiz completed") },
            text = { Text("Your score is: " + state.result.toString() + "\nDo you want to send it to the public leaderboard?")}
        )
    else if (state.exitDialogShown)
        AlertDialog(
            onDismissRequest = { observeEvent(QuizUiEvent.DismissDialog) },
            confirmButton = { TextButton(onClick = { observeEvent(QuizUiEvent.ConfirmBack) }) {
                Text(text = "CONFIRM")
            }},
            dismissButton = { TextButton(onClick = { observeEvent(QuizUiEvent.DismissDialog) }) {
                Text(text = "DISMISS")
            }},
            title = { Text("Exit quiz?") },
            text = { Text("Are you sure you want to exit the quiz? Your results will not be saved.")}
        )
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Quiz question " + state.questionNumber.toString() + "/20") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                actions = {
                    IconButton(onClick = { observeEvent(QuizUiEvent.Back) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit Quiz")
                    }
                })
        },
        content = {
          Column (modifier = Modifier
              .padding(it)
              .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = (state.timeLeft / 60).toString() + ":" + (state.timeLeft % 60).toString().padStart(2, '0'))
              if (state.loading || state.sending) {
                  Box(
                      modifier = Modifier.fillMaxSize(),
                      contentAlignment = Alignment.Center,
                  ) {
                      CircularProgressIndicator()
                  }
              }
              else if (state.timeLeft != 0) {
                  AnimatedContent(targetState = state, contentKey = {it.answers},
                      label = "", transitionSpec = {
                          (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                  slideInVertically(initialOffsetY = { 2 * (it / 3) }))
                              .togetherWith(fadeOut(animationSpec = tween(90)))
                      }
                  ) { state ->
                      Column(modifier = Modifier.fillMaxSize(),
                          horizontalAlignment = Alignment.CenterHorizontally) {
                          Text(text = state.question, style = MaterialTheme.typography.headlineLarge)

                          Spacer(modifier = Modifier.height(16.dp))

                          Box(
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .height(256.dp)
                                  .background(MaterialTheme.colorScheme.secondaryContainer)
                          ) {
                              if (state.imageError != null) {
                                  Text(text = state.imageError)
                              } else if (state.image == null) {
                                  Box(
                                      modifier = Modifier.fillMaxSize(),
                                      contentAlignment = Alignment.Center,
                                  ) {
                                      CircularProgressIndicator()
                                  }
                              } else {
                                  SubcomposeAsyncImage(
                                      model = state.image.url,
                                      loading = {
                                          Box(
                                              modifier = Modifier.fillMaxSize(),
                                              contentAlignment = Alignment.Center,
                                          ) {
                                              CircularProgressIndicator()
                                          }
                                      },
                                      contentDescription = "Image of a cat",
                                      modifier = Modifier.fillMaxSize(),
                                      imageLoader = imageLoader!!
                                  )
                              }
                          }

                          Spacer(modifier = Modifier.height(16.dp))

                          state.answers.forEach {
                              Button(modifier = Modifier
                                  .padding(bottom = 8.dp)
                                  .padding(horizontal = 16.dp)
                                  .fillMaxWidth(),
                                  onClick = { observeEvent(QuizUiEvent.Answer(it)) }) {
                                  Text(text = it)
                              }
                          }
                      }
                  }
              }
          }
        },
    )
}

@Preview
@Composable
fun QuizScreenPreview() {
    CatalogTheme {
        QuizScreen(QuizState(question = "Guess the temperament!", loading = false), {

        }, null)
    }
}