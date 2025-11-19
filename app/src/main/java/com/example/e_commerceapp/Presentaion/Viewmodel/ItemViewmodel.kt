package com.example.minicommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minicommerce.data.local.AppDatabase
import com.example.minicommerce.data.local.CartDao
import com.example.minicommerce.data.remote.FakeStoreApi
import com.example.minicommerce.model.CartItem
import com.example.minicommerce.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val api = FakeStoreApi.create()
     private val cartDao: CartDao = AppDatabase.getDatabase(application).cartDao()

     private val _productState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val productState: StateFlow<ProductUiState> = _productState.asStateFlow()

     private var allProductsCache = listOf<Product>()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

     val cartItems: StateFlow<List<CartItem>> = cartDao.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

     val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        fetchProducts()
    }

     private fun fetchProducts() {
        viewModelScope.launch {
            _productState.value = ProductUiState.Loading
            try {
                val products = api.getAllProducts()
                allProductsCache = products
                _productState.value = ProductUiState.Success(products)
            } catch (e: Exception) {
                _productState.value = ProductUiState.Error("Failed to load: ${e.message}")
            }
        }
    }

     fun searchProducts(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _productState.value = ProductUiState.Success(allProductsCache)
        } else {
            val filtered = allProductsCache.filter {
                it.title.contains(query, ignoreCase = true)
            }
            _productState.value = ProductUiState.Success(filtered)
        }
    }

     fun addToCart(product: Product) {
        viewModelScope.launch {
            val existing = cartDao.getCartItemById(product.id)
            if (existing != null) {
                cartDao.updateQuantity(product.id, existing.quantity + 1)
            } else {
                cartDao.addToCart(
                    CartItem(product.id, product.title, product.price, product.image, 1)
                )
            }
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch { cartDao.removeFromCart(id) }
    }

    fun incrementQuantity(item: CartItem) {
        viewModelScope.launch { cartDao.updateQuantity(item.id, item.quantity + 1) }
    }

     fun decrementQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                cartDao.updateQuantity(item.id, item.quantity - 1)
            } else {
                cartDao.removeFromCart(item.id)
            }
        }
    }
}