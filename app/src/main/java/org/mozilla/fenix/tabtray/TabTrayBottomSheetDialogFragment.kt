package org.mozilla.fenix.tabtray

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_tab_tray_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.fragment_tab_tray_bottom_sheet_dialog.view.tabLayout
import mozilla.components.concept.tabstray.Tab
import mozilla.components.feature.tabs.tabstray.TabsFeature
import org.mozilla.fenix.R

class TabTrayBottomSheetDialogFragment(
): AppCompatDialogFragment(), TabTrayInteractor {
    interface Interactor {
        fun onTabSelected(tab: Tab)
        fun onNewTabTapped(private: Boolean)
    }

    var interactor: Interactor? = null
    private lateinit var tabsFeature: TabsFeature
    private var _tabTrayView: TabTrayView? = null
    private val tabTrayView: TabTrayView
        get() = _tabTrayView!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TabTrayDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tab_tray_bottom_sheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.tabLayout.setOnClickListener { dismiss() }

        _tabTrayView = TabTrayView(
            view.tabLayout,
            this
        )

        tabTrayView.show()

        tabLayout.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(
                bottom = insets.systemWindowInsetBottom
            )
            insets
        }
    }

    override fun onTabSelected(tab: Tab) {
        interactor?.onTabSelected(tab)
    }

    override fun onNewTabTapped(private: Boolean) {
        interactor?.onNewTabTapped(private)
    }
}