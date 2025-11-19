package com.example.minicommerce.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.minicommerce.model.CartItem
import com.example.minicommerce.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val total by viewModel.totalPrice.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
             BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total: $${String.format("%.2f", total)}", style = MaterialTheme.typography.titleLarge)
                    Button(onClick = {   }) {
                        Text("Checkout")
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Your cart is empty")
            }
        } else {
            LazyColumn(contentPadding = padding) {
                items(cartItems, key = { it.id }) { item ->
                    CartItemRow(item, viewModel)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, viewModel: MainViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )

        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            Text(item.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("$${item.price}", fontWeight = FontWeight.Bold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.decrementQuantity(item) }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Decrease")
            }
            Text("${item.quantity}")
            IconButton(onClick = { viewModel.incrementQuantity(item) }) {
                Icon(Icons.Default.AddCircle, contentDescription = "Increase")
            }
        }

        IconButton(onClick = { viewModel.removeFromCart(item.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red)
        }
    }
}