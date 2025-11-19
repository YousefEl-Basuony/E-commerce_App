package com.example.minicommerce.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.minicommerce.model.Product
import com.example.minicommerce.viewmodel.MainViewModel
import com.example.minicommerce.viewmodel.ProductUiState
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: MainViewModel,
    onProductClick: (Int) -> Unit,
    onGoToCart: () -> Unit
) {
    val uiState by viewModel.productState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mini Market") },
                actions = {
                    IconButton(onClick = onGoToCart) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
             OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchProducts(it) },
                label = { Text("Search products...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

             when (val state = uiState) {
                is ProductUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProductUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = Color.Red)
                    }
                }
                is ProductUiState.Success -> {
                    if (state.products.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No results found")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(state.products) { product ->
                                ProductItem(product, onProductClick)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(product.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier.height(120.dp).fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleMedium)
            Text("$${product.price}", style = MaterialTheme.typography.bodyLarge, color = Color.Green)
        }
    }
}