package ru.skillbranch.skillarticles.extensions

/**
 * Created by Reva on 07.09.2020
 */

fun String?.indexesOf(query: String): List<Int> {
    if (query == null || query.isEmpty()) {
        return emptyList()
    }
    var fromIndex = 0
    val list = ArrayList<Int>()
    return if (this != null) {
        while (this.indexOf(query, fromIndex, ignoreCase = true) > -1) {
            fromIndex = this.indexOf(query, fromIndex, ignoreCase = true)
            list.add(fromIndex)
            println("Found at => $fromIndex")
            fromIndex++
        }
        list
    } else {
        emptyList()
    }

//    return listOf(0, 8, 16, 30)
}