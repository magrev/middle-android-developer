package ru.skillbranch.skillarticles.ui

import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.text.getSpans
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_bottombar.*
import kotlinx.android.synthetic.main.layout_submenu.*
import kotlinx.android.synthetic.main.search_view_layout.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.setMarginOptionally
import ru.skillbranch.skillarticles.ui.base.BaseActivity
import ru.skillbranch.skillarticles.ui.base.Binding
import ru.skillbranch.skillarticles.ui.custom.SearchFocusSpan
import ru.skillbranch.skillarticles.ui.custom.SearchSpan
import ru.skillbranch.skillarticles.ui.delegates.AttrValue
import ru.skillbranch.skillarticles.ui.delegates.ObserveProb
import ru.skillbranch.skillarticles.ui.delegates.RenderProp
import ru.skillbranch.skillarticles.viewmodels.ArticleState
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.Notify
import ru.skillbranch.skillarticles.viewmodels.base.ViewModelFactory

class RootActivity : BaseActivity<ArticleViewModel>(), IArticleView {
    override val layout: Int = R.layout.activity_root
    override val viewModel: ArticleViewModel by lazy {
        val vmFactory = ViewModelFactory("0")
        ViewModelProviders.of(this, vmFactory).get(ArticleViewModel::class.java)
    }
    public override val binding: ArticleBinding by lazy { ArticleBinding() }

//    private var searchQuery: String? = null
//    private var isSearching = false

//    private var searchView: SearchView? = null

    val bgColor by AttrValue(R.attr.colorSecondary)
    val fgColor by AttrValue(R.attr.colorOnSecondary)


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        viewModel.observeState(this) {
//            renderUi(it)
//////            setupToolbar()
////            if (it.isSearch) {
////                isSearching = true
////                searchQuery = it.searchQuery
////            }
//        }
//
//        viewModel.observeNotifications(this) {
//            renderNotification(it)
//        }
//    }

    override fun setupViews() {
        setupToolbar()
        setupBottomBar()
        setupSubmenu()
    }

    override fun renderSearchResult(searchResult: List<Pair<Int, Int>>) {
        val content = tv_text_content.text as Spannable

        //clear entry search result
        clearSearchResult()

        searchResult.forEach { (start, end) ->
            content.setSpan(
                SearchSpan(bgColor, fgColor),
                start,
                end,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // scroll to first searched element
        renderSearchPosition(0)
    }

    override fun renderSearchPosition(searchPosition: Int) {
        val content = tv_text_content.text as Spannable

        val spans = content.getSpans<SearchSpan>()
        // clear last  search position
        content.getSpans<SearchFocusSpan>().forEach { content.removeSpan(it) }

        if (spans.isNotEmpty()) {
            //find position span
            val result = spans[searchPosition]
            Selection.setSelection(content, content.getSpanStart(result))
            content.setSpan(
                SearchFocusSpan(bgColor, fgColor),
                content.getSpanStart(result),
                content.getSpanEnd(result),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        println("renderSearchPosition: $searchPosition")
    }

    override fun clearSearchResult() {
        val content = tv_text_content.text as Spannable
        content.getSpans<SearchSpan>()
            .forEach { content.removeSpan(it) }
    }

    override fun showSearchBar() {
        bottombar.setSearchState(true)
        scroll.setMarginOptionally(bottom = dpToIntPx(56))
    }

    override fun hideSearchBar() {
        bottombar.setSearchState(false)
        scroll.setMarginOptionally(bottom = dpToIntPx(0))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = (menuItem?.actionView as? SearchView)
        searchView?.queryHint = getString(R.string.article_search_placeholder)

        if (binding.isSearch) {
            menuItem?.expandActionView()
            searchView?.setQuery(binding.searchQuery, false)
            if (binding.isFocusedSearch) searchView?.requestFocus()
            else searchView?.clearFocus()
        }

        menuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(false)
                return true
            }

        })

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearch(newText)
                println("onQueryTextChange: $newText")
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val logo = if (toolbar.childCount > 2) toolbar.getChildAt(2) as ImageView else null
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP
        val lp = logo?.layoutParams as? Toolbar.LayoutParams
        lp?.let {
            it.width = this.dpToIntPx(40)
            it.height = this.dpToIntPx(40)
            it.marginEnd = this.dpToIntPx(16)
            logo.layoutParams = it
        }
    }

//    private fun renderUi(data: ArticleState) {
//
//        if (data.isSearch) showSearchBar() else hideSearchBar()
//
//        if (data.searchResults.isNotEmpty()) renderSearchResult(data.searchResults)
//
//        if (data.searchResults.isNotEmpty()) renderSearchPosition(data.searchPosition)
//
//        //bind submenu state
//        btn_settings.isChecked = data.isShowMenu
//        if (data.isShowMenu) submenu.open() else submenu.close()
//
//        //bind article person data
//        btn_like.isChecked = data.isLike
//        btn_bookmark.isChecked = data.isBookmark
//
//        //bind submenu views
//        switch_mode.isChecked = data.isDarkMode
//        delegate.localNightMode =
//            if (data.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
//
//        if (data.isBigText) {
//            tv_text_content.textSize = 18f
//            btn_text_up.isChecked = true
//            btn_text_down.isChecked = false
//        } else {
//            tv_text_content.textSize = 14f
//            btn_text_up.isChecked = false
//            btn_text_down.isChecked = true
//        }
//
//        // bind content
//        if (data.isLoadingContent) {
//            tv_text_content.text = "loading"
//        } else if (tv_text_content.text == "loading") { //don`t override content
//            val content = data.content.first() as String
//            tv_text_content.setText(content, TextView.BufferType.SPANNABLE)
//            tv_text_content.movementMethod = ScrollingMovementMethod()
//        }
//
//        // bind toolbar
//        toolbar.title = data.title ?: "Still Articles"
//        toolbar.subtitle = data.category ?: "loading"
//        if (data.categoryIcon != null) toolbar.logo = getDrawable(data.categoryIcon as Int)
//    }

    override fun renderNotification(notify: Notify) {
        val snackbar = Snackbar.make(coordinator_container, notify.message, Snackbar.LENGTH_LONG)
            .setAnchorView(bottombar)
            .setActionTextColor(getColor(R.color.color_accent_dark))

        when (notify) {
            is Notify.TextMessage -> { /*nothong*/
            }

            is Notify.ActionMessage -> {
                snackbar.setActionTextColor(getColor(R.color.color_accent_dark))
                snackbar.setAction(notify.actionLabel) {
                    notify.actionHandler?.invoke()
                }
            }

            is Notify.ErrorMessage -> {
                with(snackbar) {
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    setActionTextColor(getColor(android.R.color.white))
                    setAction(notify.errLabel) {
                        notify.errHandler?.invoke()
//                        notify.errHandler.invoke()
                    }
                }
            }
        }
        snackbar.show()
    }

    private fun setupSubmenu() {
        btn_text_up.setOnClickListener { viewModel.handleUpText() }
        btn_text_down.setOnClickListener { viewModel.handleDownText() }
        switch_mode.setOnClickListener { viewModel.handleNightMode() }
    }

    private fun setupBottomBar() {
        btn_like.setOnClickListener { viewModel.handleLike() }
        btn_bookmark.setOnClickListener { viewModel.handleBookmark() }
        btn_share.setOnClickListener { viewModel.handleShare() }
        btn_settings.setOnClickListener { viewModel.handleToggleMenu() }

        btn_result_up.setOnClickListener {
            if (search_view.hasFocus()) search_view.clearFocus()
            viewModel.handleUpResult()
        }

        btn_result_down.setOnClickListener {
            if (search_view.hasFocus()) search_view.clearFocus()
            viewModel.handleDownResult()
        }

        btn_search_close.setOnClickListener {
            viewModel.handleSearchMode(false)
            invalidateOptionsMenu()
        }
    }

    inner class ArticleBinding() : Binding() {
        var isFocusedSearch: Boolean = false
        var searchQuery: String? = null
//        private var isSearching = false

        private var isLoadingContent by ObserveProb(true)

        private var isLike: Boolean by RenderProp(false) { btn_like.isChecked = it }
        private var isBookmark: Boolean by RenderProp(false) { btn_bookmark.isChecked = it }
        private var isShowMenu: Boolean by RenderProp(false) {
            btn_settings.isChecked = it
            if (it) submenu.open() else submenu.close()
        }
        private var title: String by RenderProp("loading") { toolbar.title = it }
        private var category: String by RenderProp("loading") { toolbar.subtitle = it }
        private var categoryIcon: Int by RenderProp(R.drawable.logo_placeholder) {
            toolbar.logo = getDrawable(it)
        }

        private var isBigText: Boolean by RenderProp(false) {
            if (it) {
                tv_text_content.textSize = 18f
                btn_text_up.isChecked = true
                btn_text_down.isChecked = false
            } else {
                tv_text_content.textSize = 14f
                btn_text_up.isChecked = false
                btn_text_down.isChecked = true
            }
        }

        private var isDarkMode: Boolean by RenderProp(false, false) {
            switch_mode.isChecked = it
            delegate.localNightMode = if (it) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        }

        var isSearch: Boolean by ObserveProb(false) {
            if (it) showSearchBar() else hideSearchBar()
        }

        private var searchResult: List<Pair<Int, Int>> by ObserveProb(emptyList())
        private var searchPosition: Int by ObserveProb(0)

        private var content: String by ObserveProb("loading") {
            tv_text_content.setText(it, TextView.BufferType.SPANNABLE)
            tv_text_content.movementMethod = ScrollingMovementMethod()
        }

        override fun onFinishInflate() {
            dependsOn<Boolean, Boolean, List<Pair<Int, Int>>, Int>(
                ::isLoadingContent,
                ::isSearch,
                ::searchResult,
                ::searchPosition
            ) { ilc, iss, sr, sp ->
                if (!ilc && iss) {
                    renderSearchResult(sr)
                    renderSearchPosition(sp)
                }
                if (!ilc && !iss) {
                    clearSearchResult()
                }
                bottombar.bindSearchInfo(sr.size, sp)
            }
        }

        override fun bind(data: IViewModelState) {
            data as ArticleState

            isLike = data.isLike
            isBookmark = data.isBookmark
            isShowMenu = data.isShowMenu
            isBigText = data.isBigText
            isDarkMode = data.isDarkMode

            if (data.title != null) title = data.title
            if (data.category != null) category = data.category
            if (data.categoryIcon != null) categoryIcon = data.categoryIcon as Int
            if (data.content.isNotEmpty()) content = data.content.first() as String

            isLoadingContent = data.isLoadingContent
            isSearch = data.isSearch
            searchQuery = data.searchQuery
            searchPosition = data.searchPosition
            searchResult = data.searchResults

        }

        override fun saveUi(outState: Bundle) {
            outState.putBoolean(::isFocusedSearch.name, search_view?.hasFocus() ?: false)
        }

        override fun restoreUi(savedState: Bundle) {
            isFocusedSearch = savedState.getBoolean(::isFocusedSearch.name)
        }
    }


}
