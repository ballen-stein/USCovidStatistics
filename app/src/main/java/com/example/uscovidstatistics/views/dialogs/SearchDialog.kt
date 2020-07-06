package com.example.uscovidstatistics.views.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.SearchDialogFragmentBinding
import com.example.uscovidstatistics.recyclerview.NavRecyclerView
import com.example.uscovidstatistics.recyclerview.SearchRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.search_dialog_fragment.*

class SearchDialog(private val mContext: Context): BottomSheetDialogFragment(), ViewBinding {

    private lateinit var recyclerSearchData: SearchRecyclerView

    private lateinit var binding: SearchDialogFragmentBinding

    private lateinit var dialog: BottomSheetDialog

    fun newInstance(): SearchDialog {
        return SearchDialog(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SearchDialogFragmentBinding.inflate(inflater)
        recyclerSearchData = SearchRecyclerView(mContext as Activity, this, binding.searchRecyclerResults)
        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                sheet.parent.parent.requestLayout()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigation_view.setNavigationItemSelectedListener {
            CountryListDialog(mContext = mContext,
                dialogFragment = this,
                continentName = when (it.itemId) {
                R.id.menu_na -> {
                    "North America"
                }
                R.id.menu_sa -> {
                    "South America"
                }
                R.id.menu_eu -> {
                    "Europe"
                }
                R.id.menu_af -> {
                    "Africa"
                }
                R.id.menu_as -> {
                    "Asia"
                }
                R.id.menu_au -> {
                    "Australia/Oceania"
                }
                else -> "North America"
            }).newInstance().show(parentFragmentManager, "CountryListDialog")
            true
        }

        country_search.findViewById<AppCompatImageView>(R.id.search_close_btn).setOnClickListener {
            country_search.onActionViewCollapsed()
            navigation_view.visibility = View.VISIBLE
            search_recycler_results.visibility = View.GONE
            recyclerSearchData.clearResults()
        }

        country_search.setOnSearchClickListener {
            navigation_view.visibility = View.GONE
            search_recycler_results.visibility = View.VISIBLE
            recyclerSearchData.displayResults()
        }

        country_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                AppConstants.Searched_Country = "$query"
                search_recycler_results.visibility = View.VISIBLE
                navigation_view.visibility = View.GONE
                recyclerSearchData.displayResults()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                AppConstants.Searched_Country = "$newText"
                search_recycler_results.visibility = View.VISIBLE
                navigation_view.visibility = View.GONE
                recyclerSearchData.displayResults()
                return true
            }

        })
    }

    override fun getRoot(): View {
        return binding.root
    }

    fun getSearchResults(): ArrayList<String> {
        return AppUtils().searchResultsList(mContext)
    }
}