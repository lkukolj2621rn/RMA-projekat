package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme


fun NavGraphBuilder.profileDataScreen(
    route: String,
    navController: NavController,
    register: Boolean,
) = composable(route = route) {
    val profileDataViewModel = hiltViewModel<ProfileDataViewModel>()
    val state by profileDataViewModel.state.collectAsState()

    if (register && state.registered)
        navController.navigate("quiz/start") {
            popUpTo(route) {
                inclusive = true
            }
        }
    else if (state.submitted)
        navController.navigateUp()
    else
        ProfileDataScreen(
            state = state,
            observeEvent = profileDataViewModel::observeEvent
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDataScreen(state: ProfileDataState, observeEvent: (ProfileDataUiEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if(state.registered) "Edit Profile" else "Register Account") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
        content = {
            Column (Modifier.padding(it)) {
                if(!state.registered)
                    Text(style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                        text = "Please fill in this form in order to use the application.")
                AccountInfoField(
                    observeEvent,
                    "Name",
                    state.name,
                    ProfileDataUiEvent::NameChanged,
                    state.nameError)
                AccountInfoField(
                    observeEvent,
                    "Nickname",
                    state.nickname,
                    ProfileDataUiEvent::NicknameChanged,
                    state.nicknameError)
                AccountInfoField(
                    observeEvent,
                    "E-mail",
                    state.email,
                    ProfileDataUiEvent::EmailChanged,
                    state.emailError)
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                    enabled = state.nameError == null && state.nicknameError  == null && state.emailError == null
                            && state.name.isNotBlank() && state.nickname.isNotBlank() && state.email.isNotBlank(),
                    onClick = { observeEvent(ProfileDataUiEvent.Submit) }) {
                    Text(text = "Submit")
                }
            }
        }
    )
}

@Composable
private fun AccountInfoField(
    observeEvent: (ProfileDataUiEvent) -> Unit,
    label: String,
    value: String,
    changed: (String) -> ProfileDataUiEvent,
    errorText: String?
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = value,
        supportingText = {
                         if(errorText != null)
                             Text(
                                 text = errorText,
                                 modifier = Modifier.fillMaxWidth(),
                                 color = MaterialTheme.colorScheme.error
                             )
        },
        onValueChange = {
            observeEvent(changed(it))
        },
        label = {
            Text(label)
        },
        singleLine = true,
        isError = errorText != null
    )
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    CatalogTheme {
        ProfileDataScreen(
            state = ProfileDataState(),
            observeEvent = {

            }
        )
    }
}