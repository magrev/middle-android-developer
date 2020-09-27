package ru.skillbranch.skillarticles.ui

/**
 * Created by Reva on 07.09.2020
 */

interface IArticleView {
    /**
     * Отрисовать все вхождения поискового запроса в контент (spannable)
     */
    fun renderSearchResult(searchResult: List<Pair<Int, Int>>)

    /**
     * Отрисовать текущее положение поиска и перевести фокус на него (spannable)
     */
    fun renderSearchPosition(searchPosition: Int)

    /**
     * Очистить результаты поиска (удалить все spannable)
     */
    fun clearSearchResult()

    /**
     * показать searchbar
     */
    fun showSearchBar()

    /**
     * скрыть searchbar
     */
    fun hideSearchBar()

}