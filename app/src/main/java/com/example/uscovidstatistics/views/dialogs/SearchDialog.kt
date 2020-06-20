package com.example.uscovidstatistics.views.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.databinding.SearchDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.search_dialog_fragment.*

class SearchDialog(private val mContext: Context): BottomSheetDialogFragment(), ViewBinding {

    private lateinit var binding: SearchDialogFragmentBinding

    fun newInstance(): SearchDialog {
        return SearchDialog(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SearchDialogFragmentBinding.inflate(inflater)
        return root
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
    }

    override fun getRoot(): View {
        return binding.root
    }
}