/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.tabtray

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.save_to_collection_button.view.*
import mozilla.components.browser.state.state.MediaState
import mozilla.components.browser.tabstray.TabViewHolder
import mozilla.components.browser.tabstray.thumbnail.TabThumbnailView
import mozilla.components.browser.toolbar.MAX_URI_LENGTH
import mozilla.components.concept.tabstray.Tab
import mozilla.components.concept.tabstray.TabsTray
import mozilla.components.feature.media.ext.pauseIfPlaying
import mozilla.components.feature.media.ext.playIfPaused
import mozilla.components.support.base.observer.Observable
import mozilla.components.support.ktx.kotlin.tryGetHostFromUrl
import org.mozilla.fenix.R
import org.mozilla.fenix.components.metrics.Event
import org.mozilla.fenix.ext.components
import org.mozilla.fenix.ext.increaseTapArea
import org.mozilla.fenix.ext.logDebug
import org.mozilla.fenix.ext.removeAndDisable
import org.mozilla.fenix.ext.removeTouchDelegate
import org.mozilla.fenix.ext.showAndEnable
import org.mozilla.fenix.ext.toTab
import org.mozilla.fenix.home.sessioncontrol.viewholders.SaveTabGroupViewHolder
import org.mozilla.fenix.theme.ThemeManager

/**
 * A RecyclerView ViewHolder implementation for "tab" items.
 */
class TabTraySaveToCollectionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
        view.save_tab_group_button.setOnClickListener {
            logDebug("boek", "save to collections tapped!")
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.save_to_collection_button
    }
}
