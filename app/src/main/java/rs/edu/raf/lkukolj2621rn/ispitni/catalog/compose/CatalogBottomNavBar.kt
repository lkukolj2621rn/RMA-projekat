package rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBox
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

@Composable
fun CatalogBottomNavigationBar(path: String, navigate: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(selected = path == "quiz/start",
            label = { Text("Quiz") },
            onClick = { navigate("quiz/start") },
            icon = { Icon(imageVector = Icons.TwoTone.CheckCircle, contentDescription = "Quiz") })
        NavigationBarItem(selected = path == "leaderboard",
            label = { Text("Leaderboard") },
            onClick = { navigate("leaderboard") },
            icon = { Icon(imageVector = Icons.TwoTone.Star, contentDescription = "Leaderboard") })
        NavigationBarItem(selected = path == "breeds",
            label = { Text("Breed List") },
            onClick = { navigate("breeds") },
            icon = { Icon(imageVector = Icons.TwoTone.Info, contentDescription = "Breed List") })
        NavigationBarItem(selected = path == "profile",
            label = { Text("Profile") },
            onClick = { navigate("profile") },
            icon = { Icon(imageVector = Icons.TwoTone.AccountBox, contentDescription = "Profile") })
    }
}

@Preview
@Composable
fun CatalogBottomNavigationBarPreview() {
    CatalogTheme {
        CatalogBottomNavigationBar("quiz/start") {

        }
    }
}