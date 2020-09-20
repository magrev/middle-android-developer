package ru.skillbranch.skillarticles.ui

/**
 * Created by Reva on 07.09.2020
 */

interface IArticleView {

    fun renderSearchResult(searchResult: List<Pair<Int, Int>>)

    fun renderSearchPosition(searchPosition: Int)

    fun clearSearchResult()

    fun showSearchBar()

    fun hideSearchBar()

}