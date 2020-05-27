/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.tabtray

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mozilla.components.browser.tabstray.TabTouchCallback
import mozilla.components.concept.tabstray.TabsTray
import mozilla.components.support.base.observer.Observable
import kotlin.math.abs

private class FenixTabTouchCallback(observable: Observable<TabsTray.Observer>) : TabTouchCallback(observable) {
    override fun alphaForItemSwipe(dX: Float, distanceToAlphaMin: Int): Float {
        return 1f - 2f * abs(dX) / distanceToAlphaMin
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder !is TabTrayViewHolder) {
            return ItemTouchHelper.ACTION_STATE_IDLE
        }

        return super.getMovementFlags(recyclerView, viewHolder)
    }
}

class TabsTouchHelper(
    observable: Observable<TabsTray.Observer>
) : ItemTouchHelper(FenixTabTouchCallback(observable))
