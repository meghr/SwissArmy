package com.attri.swissarmy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.attri.swissarmy.core.ui.theme.SwissArmyTheme
import com.attri.swissarmy.feature.home.HomeRoute
import com.attri.swissarmy.feature.scanner.ScannerRoute
import com.attri.swissarmy.feature.pdftools.PdfToolsRoute
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @javax.inject.Inject
    lateinit var settingsRepository: com.attri.swissarmy.feature.settings.domain.SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by settingsRepository.themeMode.collectAsState(initial = "System")
            val isDarkTheme = when(themeMode) {
                "Dark" -> true
                "Light" -> false
                else -> isSystemInDarkTheme()
            }

            SwissArmyTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeRoute(
                            onFeatureClick = { featureId ->
                                when(featureId) {
                                    "scanner" -> navController.navigate("scanner")
                                    "pdf_compress" -> navController.navigate("pdf_tools")
                                    "image_tools" -> navController.navigate("image_tools")
                                    "sip_calculator" -> navController.navigate("calculators")
                                    "ascii" -> navController.navigate("ascii_table")
                                    "alarm" -> navController.navigate("world_clock")
                                    "rcs_cleaner" -> navController.navigate("cleaner")
                                    "dictionary" -> navController.navigate("dictionary")
                                    else -> { /* TODO: Implement other features */ }
                                }
                            },
                            onSettingsClick = {
                                navController.navigate("settings")
                            }
                        )
                    }
                    
                    composable("scanner") {
                        ScannerRoute()
                    }
                    
                    composable("pdf_tools") {
                        PdfToolsRoute()
                    }

                    composable("image_tools") {
                        com.attri.swissarmy.feature.imagetools.ImageToolsRoute()
                    }

                    composable("calculators") {
                        com.attri.swissarmy.feature.calculators.CalculatorRoute()
                    }

                    composable("ascii_table") {
                        com.attri.swissarmy.feature.utilities.ascii.AsciiTableRoute()
                    }

                    composable("world_clock") {
                        com.attri.swissarmy.feature.utilities.alarm.WorldClockRoute()
                    }

                    composable("cleaner") {
                        com.attri.swissarmy.feature.utilities.cleaner.CleanerRoute()
                    }

                    composable("dictionary") {
                        com.attri.swissarmy.feature.dictionary.DictionaryRoute()
                    }

                    composable("settings") {
                        com.attri.swissarmy.feature.settings.SettingsRoute(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}