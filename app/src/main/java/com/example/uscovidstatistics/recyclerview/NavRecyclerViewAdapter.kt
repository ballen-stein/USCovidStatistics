package com.example.uscovidstatistics.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.databinding.NavigationCountrySelectionBinding
import kotlinx.android.synthetic.main.navigation_country_selection.view.*

class NavRecyclerViewAdapter internal constructor(private val countryList: Array<String>) : RecyclerView.Adapter<NavRecyclerViewAdapter.ViewHolder>(), ViewBinding {

    lateinit var binding: NavigationCountrySelectionBinding

    companion object{
        private var clickListener : OnClickListener? = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val countryName = if (countryList[position] == "Burma") "Myanmar/Burma" else countryList[position]
        holder.bind(countryName)
        holder.itemView.country_name.text = countryName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = NavigationCountrySelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    class ViewHolder(binding: NavigationCountrySelectionBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var currentData: String

        init {
            super.itemView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener!!.onCountryClick(adapterPosition, currentData, view)
        }

        fun bind(countryName: String) {
            currentData = countryName
        }
    }

    fun setOnClickListener(cListener : OnClickListener){
        clickListener = cListener
    }

    interface OnClickListener{
        fun onCountryClick(position : Int, countryName: String, v : View)
    }

    override fun getRoot(): View {
        return binding.root
    }

}