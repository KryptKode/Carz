package com.kryptkode.carz.ui.carbuilddate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.databinding.ItemCarBuildDateAltBinding
import com.kryptkode.carz.databinding.ItemCarBuildDateBinding
import com.kryptkode.carz.utils.extension.isEven

class CarBuildDateAdapter(
    private val onItemClick: (CarBuildDate) -> Unit
) : ListAdapter<CarBuildDate, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_car_build_date -> CarBuildDateEvenViewHolder(
                ItemCarBuildDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_car_build_date_alt -> CarBuildDateOddViewHolder(
                ItemCarBuildDateAltBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("No expected viewType for $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position.isEven()) {
            R.layout.item_car_build_date
        } else {
            R.layout.item_car_build_date_alt
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CarBuildDateEvenViewHolder -> holder.bind(getItem(position))
            is CarBuildDateOddViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class CarBuildDateEvenViewHolder(private val binding: ItemCarBuildDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarBuildDate) {
            binding.numTextView.text =
                binding.root.context.getString(
                    R.string.number_label,
                    adapterPosition.toString()
                )

            binding.titleTextView.text = item.year
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    inner class CarBuildDateOddViewHolder(private val binding: ItemCarBuildDateAltBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarBuildDate) {
            binding.numTextView.text =
                binding.root.context.getString(
                    R.string.number_label,
                    adapterPosition.toString()
                )
            binding.titleTextView.text = item.year
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CarBuildDate>() {
            override fun areContentsTheSame(oldItem: CarBuildDate, newItem: CarBuildDate): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: CarBuildDate, newItem: CarBuildDate): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
