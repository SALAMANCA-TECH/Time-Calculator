package com.example.itemidentifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.itemidentifier.navigation.Screen
import com.example.itemidentifier.ui.screens.BrandScreen
import com.example.itemidentifier.ui.screens.CategoryScreen
import com.example.itemidentifier.ui.screens.DiagnosticFunnelScreen
import com.example.itemidentifier.ui.screens.ModelScreen
import com.example.itemidentifier.ui.theme.ItemIdentifierTheme
import com.example.itemidentifier.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemIdentifierTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = Screen.Brand.route) {
                        composable(Screen.Brand.route) {
                            BrandScreen(
                                viewModel = viewModel,
                                onNavigate = { brandId ->
                                    navController.navigate(Screen.Category.createRoute(brandId))
                                }
                            )
                        }
                        composable(Screen.Category.route) { backStackEntry ->
                            val brandId = backStackEntry.arguments?.getString("brandId")!!
                            CategoryScreen(
                                viewModel = viewModel,
                                brandId = brandId,
                                onNavigate = { categoryId ->
                                    navController.navigate(Screen.Model.createRoute(categoryId))
                                }
                            )
                        }
                        composable(Screen.Model.route) { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId")!!
                            ModelScreen(
                                viewModel = viewModel,
                                categoryId = categoryId,
                                onNavigate = { modelId ->
                                    navController.navigate(Screen.DiagnosticFunnel.createRoute(modelId))
                                }
                            )
                        }
                        composable(Screen.DiagnosticFunnel.route) { backStackEntry ->
                            val modelId = backStackEntry.arguments?.getString("modelId")!!
                            DiagnosticFunnelScreen(
                                viewModel = viewModel,
                                modelId = modelId
                            )
                        }
                    }
                }
            }
        }
    }
}
