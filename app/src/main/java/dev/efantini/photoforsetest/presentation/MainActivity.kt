package dev.efantini.photoforsetest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.efantini.photoforsetest.presentation.navigation.PhotoForseNavHost
import dev.efantini.photoforsetest.presentation.theme.PhotoForseTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            PhotoForseTestTheme {
                PhotoForseNavHost()
            }
        }
    }
}
