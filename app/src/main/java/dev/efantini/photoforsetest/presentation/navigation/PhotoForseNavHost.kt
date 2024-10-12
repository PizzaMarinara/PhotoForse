package dev.efantini.photoforsetest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.efantini.photoforsetest.presentation.countries.ui.CountriesScreenUi
import dev.efantini.photoforsetest.presentation.photopicker.ui.PhotoPickerScreenUi
import dev.efantini.photoforsetest.presentation.upload.ui.UploadScreenUi

@Composable
fun PhotoForseNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CountriesScreen,
    ) {
        composable<CountriesScreen> {
            CountriesScreenUi(navController)
        }
        composable<PhotoPickerScreen> {
            PhotoPickerScreenUi(navController)
        }
        composable<UploadScreen> {
            UploadScreenUi()
        }
    }
}
