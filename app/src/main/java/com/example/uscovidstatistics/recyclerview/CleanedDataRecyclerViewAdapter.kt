package com.example.uscovidstatistics.recyclerview

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.CountryLayoutBinding
import com.example.uscovidstatistics.model.CleanedUpData
import kotlinx.android.synthetic.main.country_layout.view.*

class CleanedDataRecyclerViewAdapter internal constructor(private val cleanedUpData: List<CleanedUpData>) : RecyclerView.Adapter<CleanedDataRecyclerViewAdapter.ViewHolder>(), ViewBinding {

    private lateinit var binding: CountryLayoutBinding

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = cleanedUpData[position]
        if (currentData.name == "Territories" || currentData.name == "Other" || currentData.name == "States & DC") {
            holder.bind(currentData)
            holder.itemView.rv_region.text = currentData.name
            holder.itemView.rv_cases.visibility = View.GONE
            holder.itemView.rv_recovered.visibility = View.GONE
            holder.itemView.rv_deaths.visibility = View.GONE
            bold(holder)
        } else {
            holder.bind(currentData)
            holder.itemView.rv_region.text = currentData.name
            holder.itemView.rv_cases.text = currentData.cases
            holder.itemView.rv_recovered.text = currentData.recovered
            holder.itemView.rv_deaths.text = currentData.deaths

            if (currentData.name == "Totals") {
                holder.itemView.bottom_divider.visibility = View.GONE
                bold(holder)
            } else if (currentData.name == "Wyoming" || currentData.name == "United \nStates \nVirgin \nIslands") {
                holder.itemView.bottom_fill.visibility = View.VISIBLE
                //holder.itemView.bottom_divider.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CountryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cleanedUpData.size
    }

    class ViewHolder(binding: CountryLayoutBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        private lateinit var currentData : CleanedUpData

        init {
            super.itemView
            if (AppConstants.Usa_Check)
                itemView.setOnClickListener(this)
        }

        override fun onClick(view : View) {
            if (!AppConstants.Non_Click_Ids.contains(currentData.name)) {
                clickListener!!.onRegionClick(adapterPosition, currentData, view)
            }
        }

        fun bind(cleanedUpData: CleanedUpData) {
            currentData = cleanedUpData
        }
    }

    fun setOnClickListener(cListener : OnClickListener){
        clickListener = cListener
    }

    interface OnClickListener {
        fun onRegionClick(position : Int, cleanedUpData: CleanedUpData, v : View)
    }

    override fun getRoot(): View {
        return binding.root
    }

    private fun bold(holder: ViewHolder) {
        holder.itemView.rv_region.typeface =  Typeface.defaultFromStyle(Typeface.BOLD)
        holder.itemView.rv_cases.typeface =  Typeface.defaultFromStyle(Typeface.BOLD);
        holder.itemView.rv_recovered.typeface =  Typeface.defaultFromStyle(Typeface.BOLD);
        holder.itemView.rv_deaths.typeface =  Typeface.defaultFromStyle(Typeface.BOLD);
    }

    companion object{
        private var clickListener : OnClickListener? = null
    }

}