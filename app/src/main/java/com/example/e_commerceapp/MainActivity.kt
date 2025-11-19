package com.example.e_commerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.minicommerce.ui.CartScreen
import com.example.minicommerce.ui.ProductDetailScreen
import com.example.minicommerce.ui.ProductListScreen
import com.example.minicommerce.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
             val viewModel: MainViewModel = viewModel()

             NavHost(navController = navController, startDestination = "list") {

                 composable("list") {
                    ProductListScreen(
                        viewModel = viewModel,
                        onProductClick = { productId ->
                             navController.navigate("detail/$productId")
                        },
                        onGoToCart = {
                            navController.navigate("cart")
                        }
                    )
                }

                 composable("detail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                    if (productId != null) {
                        ProductDetailScreen(
                            productId = productId,
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                 composable("cart") {
                    CartScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}