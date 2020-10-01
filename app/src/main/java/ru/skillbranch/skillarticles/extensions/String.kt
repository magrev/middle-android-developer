package ru.skillbranch.skillarticles.extensions

/**
 * Created by Reva on 07.09.2020
 */

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    if (substr == null || substr.isEmpty()) {
        return emptyList()
    }
    var fromIndex = 0
    val list = ArrayList<Int>()
    return if (this != null) {
        while (this.indexOf(substr, fromIndex, ignoreCase = ignoreCase) > -1) {
            fromIndex = this.indexOf(substr, fromIndex, ignoreCase = ignoreCase)
            list.add(fromIndex)
//            println("Found at => $fromIndex")
            fromIndex++
        }
        list
    } else {
        emptyList()
    }

//    return listOf(0, 8, 16, 30)
}