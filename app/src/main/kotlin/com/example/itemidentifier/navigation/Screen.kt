package com.example.itemidentifier.navigation

sealed class Screen(val route: String) {
    object Brand : Screen("brand")
    object Category : Screen("category/{brandId}") {
        fun createRoute(brandId: String) = "category/$brandId"
    }
    object Model : Screen("model/{categoryId}") {
        fun createRoute(categoryId: String) = "model/$categoryId"
    }
    object DiagnosticFunnel : Screen("funnel/{modelId}") {
        fun createRoute(modelId: String) = "funnel/$modelId"
    }
}
