/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.tabtray

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mozilla.components.concept.tabstray.Tab
import mozilla.components.concept.tabstray.Tabs
import mozilla.components.concept.tabstray.TabsTray
import mozilla.components.support.base.observer.Observable
import mozilla.components.support.base.observer.ObserverRegistry
import org.mozilla.fenix.R

class FenixTabsAdapter(
    delegate: Observable<TabsTray.Observer> = ObserverRegistry()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), TabsTray, Observable<TabsTray.Observer> by delegate {

    private var tabs: Tabs? = null

    private val tabList: List<Tab>
        get() = tabs?.list ?: listOf()

    override fun getItemViewType(position: Int): Int {
        return if (position == tabList.size) {
            TabTraySaveToCollectionsViewHolder.LAYOUT_ID
        } else {
            TabTrayViewHolder.LAYOUT_ID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return when(viewType) {
            TabTrayViewHolder.LAYOUT_ID -> TabTrayViewHolder(view)
            TabTraySaveToCollectionsViewHolder.LAYOUT_ID -> TabTraySaveToCollectionsViewHolder(view)
            else -> throw IllegalStateException("Something went wrong!!")
        }
    }

    override fun getItemCount(): Int = tabs?.list?.let {
        return if (it.isNotEmpty()) {
            it.size + 1
        } else {
            it.size
        }
    } ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tabs = tabs ?: return

        (holder as? TabTrayViewHolder)?.bind(tabs.list[position], tabs.selectedIndex == position, this)
    }

    override fun updateTabs(tabs: Tabs) {
        this.tabs = tabs
    }

    override fun onTabsInserted(position: Int, count: Int) = notifyItemRangeInserted(position, count)

    override fun onTabsRemoved(position: Int, count: Int) = notifyItemRangeRemoved(position, count)

    override fun onTabsMoved(fromPosition: Int, toPosition: Int) = notifyItemMoved(fromPosition, toPosition)

    override fun onTabsChanged(position: Int, count: Int) = notifyItemRangeChanged(position, count)
}

class TabTrayDivider(context: Context) : DividerItemDecoration(context, VERTICAL) {
    init {
        AppCompatResources.getDrawable(context, R.drawable.tab_tray_divider)?.also {
            setDrawable(it)
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + (drawable?.intrinsicHeight ?: 0)

            drawable?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawable?.draw(canvas)
        }
    }
}

class FenixTabsTray(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val tabsAdapter: FenixTabsAdapter = FenixTabsAdapter(),
    layout: LayoutManager = LinearLayoutManager(context)
    ) : RecyclerView(context, attrs, defStyleAttr), TabsTray by tabsAdapter {

    init {
        layoutManager = layout
        this.adapter = tabsAdapter
        addItemDecoration(TabTrayDivider(context))
        TabsTouchHelper(tabsAdapter).attachToRecyclerView(this)
    }
}