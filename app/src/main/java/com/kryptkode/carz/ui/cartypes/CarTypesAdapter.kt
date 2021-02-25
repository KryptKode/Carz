package com.kryptkode.carz.ui.cartypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.databinding.ItemCarTypeAltBinding
import com.kryptkode.carz.databinding.ItemCarTypeBinding
import com.kryptkode.carz.utils.extension.isEven

class CarTypesAdapter(
    private val onItemClick: (CarType) -> Unit
) : ListAdapter<CarType, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_car_type -> CarTypeEvenViewHolder(
                ItemCarTypeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_car_type_alt -> CarTypeOddViewHolder(
                ItemCarTypeAltBinding.inflate(
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
            R.layout.item_car_type
        } else {
            R.layout.item_car_type_alt
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CarTypeEvenViewHolder -> holder.bind(getItem(position))
            is CarTypeOddViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class CarTypeEvenViewHolder(private val binding: ItemCarTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarType) {
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

    inner class CarTypeOddViewHolder(private val binding: ItemCarTypeAltBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarType) {
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
        val diffUtil = object : DiffUtil.ItemCallback<CarType>() {
            override fun areContentsTheSame(oldItem: CarType, newItem: CarType): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: CarType, newItem: CarType): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
