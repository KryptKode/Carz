package com.kryptkode.carz.ui.manufacturer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.databinding.ItemManufacturerAltBinding
import com.kryptkode.carz.databinding.ItemManufacturerBinding
import com.kryptkode.carz.utils.extension.isEven
import java.lang.IllegalArgumentException

class ManufacturerAdapter(
    private val onItemClick: (CarManufacturer) -> Unit
) : ListAdapter<CarManufacturer, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_manufacturer -> ManufacturerEvenViewHolder(
                ItemManufacturerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_manufacturer_alt -> ManufacturerOddViewHolder(
                ItemManufacturerAltBinding.inflate(
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
            R.layout.item_manufacturer
        } else {
            R.layout.item_manufacturer_alt
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ManufacturerEvenViewHolder -> holder.bind(getItem(position))
            is ManufacturerOddViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class ManufacturerEvenViewHolder(private val binding: ItemManufacturerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarManufacturer) {
            binding.numTextView.text =
                binding.root.context.getString(
                    R.string.number_label,
                    item.id
                )

            binding.titleTextView.text = item.name
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    inner class ManufacturerOddViewHolder(private val binding: ItemManufacturerAltBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarManufacturer) {
            binding.numTextView.text =
                binding.root.context.getString(
                    R.string.number_label,
                    item.id
                )
            binding.titleTextView.text = item.name
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CarManufacturer>() {
            override fun areContentsTheSame(oldItem: CarManufacturer, newItem: CarManufacturer): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: CarManufacturer, newItem: CarManufacturer): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
