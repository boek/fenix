/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.tabtray

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.component_tabstray.view.*
import kotlinx.android.synthetic.main.component_tabstray_fab.view.*
import kotlinx.android.synthetic.main.fragment_tab_tray.*
import mozilla.components.browser.tabstray.BrowserTabsTray
import mozilla.components.concept.tabstray.Tab
import mozilla.components.feature.tabs.tabstray.TabsFeature
import mozilla.components.support.base.feature.UserInteractionHandler
import org.mozilla.fenix.R
import org.mozilla.fenix.ext.components

/**
 * Interface for the AwesomeBarView Interactor. This interface is implemented by objects that want
 * to respond to user interaction on the AwesomebarView
 */
interface TabTrayInteractor {
    fun onTabSelected(tab: Tab)
    fun onNewTabTapped()
}

/**
 * View that contains and configures the BrowserAwesomeBar
 */
class TabTrayView(
    private val container: ViewGroup,
    val interactor: TabTrayInteractor
) : LayoutContainer, UserInteractionHandler {
    val fabView = LayoutInflater.from(container.context)
        .inflate(R.layout.component_tabstray_fab, container, true)

    val view = LayoutInflater.from(container.context)
        .inflate(R.layout.component_tabstray, container, true)

    val behavior = BottomSheetBehavior.from(view.tab_wrapper)
    var tabsFeature: TabsFeature

    override val containerView: View?
        get() = container

    init {
        fabView.new_tab_button.compatElevation = 80.0f
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e("slideOffset", "$slideOffset")
                if (slideOffset > 0) {
                    fabView.new_tab_button.show()
                } else {
                    fabView.new_tab_button.hide()
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })

        tabsFeature = TabsFeature(
            view.tabsTray,
            view.context.components.core.store,
            view.context.components.useCases.tabsUseCases,
            { true },
            { })

        (view.tabsTray as? BrowserTabsTray)?.also { tray ->
            TabsTouchHelper(tray.tabsAdapter).attachToRecyclerView(tray)
        }

        fabView.new_tab_button.setOnClickListener {
            interactor.onNewTabTapped()
        }

        tabsFeature.start()
    }

    fun show() {
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    fun hide() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun toggle() {
        behavior.state = if(behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            BottomSheetBehavior.STATE_HALF_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onBackPressed(): Boolean {
        if(behavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            toggle()
            return true
        }

        return false
    }
}
