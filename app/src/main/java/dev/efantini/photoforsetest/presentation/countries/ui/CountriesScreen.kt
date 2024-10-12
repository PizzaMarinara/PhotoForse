package dev.efantini.photoforsetest.presentation.countries.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.efantini.photoforsetest.presentation.countries.CountriesViewModel
import dev.efantini.photoforsetest.presentation.navigation.PhotoPickerScreen

@Composable
fun CountriesScreenUi(
    navController: NavHostController,
    viewModel: CountriesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.loadPhotoPickerScreenEvent?.consume()?.run {
        navController.navigate(PhotoPickerScreen(this))
    }

    CountriesList(
        uiState = uiState,
        onCountrySelect = viewModel::onCountrySelected,
        onNavigateNext = viewModel::onNextButtonClicked,
    )
}

@Composable
fun CountriesList(
    uiState: CountriesUiState,
    onCountrySelect: (CountryUiElement) -> Unit,
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDropDownExpanded =
        rememberSaveable {
            mutableStateOf(false)
        }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box {
            if (uiState.error != null) {
                Text(text = "An error occurred: ${uiState.error}")
            } else {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(100.dp).align(Alignment.Center),
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = uiState.selectedElement?.name ?: "No country selected")
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        enabled = uiState.isLoading.not(),
                        onClick = { isDropDownExpanded.value = true },
                    ) {
                        Text("Pick a Country!")
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        enabled = uiState.selectedElement != null,
                        onClick = onNavigateNext,
                    ) {
                        Text("Next")
                    }
                }
                DropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = {
                        isDropDownExpanded.value = false
                    },
                ) {
                    Box(modifier = Modifier.size(width = 500.dp, height = 300.dp)) {
                        LazyColumn {
                            items(uiState.countries) { country ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = country.name)
                                    },
                                    onClick = {
                                        isDropDownExpanded.value = false
                                        onCountrySelect(country)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
