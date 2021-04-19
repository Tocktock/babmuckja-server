package com.toyproject.babmuckja.product.constants

val randomMenu = listOf("짜장면", "볶음밥", "깐풍기", "국밥", "햄버거", "불고기", "초밥")
val randomPrice = listOf(3000, 4500, 5000, 6000, 8000, 9000, 12000)

class SeedProduct(
) {
    val menu: List<String> = randomMenu
    val price: List<Int> = randomPrice

    fun getRandomMenu(): String {
        return randomMenu.random()
    }

    fun getRandomPrice(): Int {
        return randomPrice.random()
    }
}