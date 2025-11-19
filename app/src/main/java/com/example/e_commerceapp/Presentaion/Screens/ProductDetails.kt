package com.example.minicommerce.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.minicommerce.viewmodel.MainViewModel
import com.example.minicommerce.viewmodel.ProductUiState

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
     val uiState by viewModel.productState.collectAsState()
    val product = (uiState as? ProductUiState.Success)?.products?.find { it.id == productId }

    if (product != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier.height(300.dp).fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(product.title, style = MaterialTheme.typography.headlineMedium)
            Text(product.category, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$${product.price}", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF4CAF50))

            Spacer(modifier = Modifier.height(16.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.addToCart(product) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    } else {
         Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}