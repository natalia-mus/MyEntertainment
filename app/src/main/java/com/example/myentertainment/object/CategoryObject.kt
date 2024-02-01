package com.example.myentertainment.`object`

enum class CategoryObject(val categoryName: String) {
    MOVIES("movies"),
    BOOKS("books"),
    GAMES("games"),
    MUSIC("music");

    companion object {
        fun getCategoryByName(categoryName: String?): CategoryObject? {
            var result: CategoryObject? = null

            if (!categoryName.isNullOrEmpty()) {
                for (categoryObject in values()) {
                    if (categoryObject.categoryName == categoryName) {
                        result = categoryObject
                        break
                    }
                }
            }

            return result
        }
    }
}