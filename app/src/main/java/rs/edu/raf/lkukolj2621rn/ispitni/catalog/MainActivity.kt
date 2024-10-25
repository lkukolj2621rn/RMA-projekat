package rs.edu.raf.lkukolj2621rn.ispitni.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose.AppNavigation
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CatalogTheme {
                AppNavigation()
            }
        }
    }
}