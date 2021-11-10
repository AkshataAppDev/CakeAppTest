package com.example.cake.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cake.databinding.CakeItemFragmentDataBinding
import com.example.cake.model.CakeModel
import android.view.animation.AlphaAnimation

//TODO  : Add divider between each entry
class ItemClickListener(val clickListener: (cakeItem: CakeModel) -> Unit) {
    fun onClick(cakeItem: CakeModel) = clickListener(cakeItem)
}

class Adapter(private val clickListener: ItemClickListener) :
    ListAdapter<CakeModel, Adapter.ItemViewHolder>(
        DiffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CakeItemFragmentDataBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val cakeItem = getItem(position)
        holder.bind(cakeItem, clickListener)
        setFadeAnimation(holder.itemView)
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 3000
        view.startAnimation(anim)
    }

    class ItemViewHolder(private var binding: CakeItemFragmentDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            cakeItem: CakeModel,
            clickListener: ItemClickListener
        ) {
            binding.cakeItem = cakeItem
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    //Use of DiffUtil to manage differences in the old and new list when data changes
    companion object DiffCallback : DiffUtil.ItemCallback<CakeModel>() {
        override fun areItemsTheSame(oldItem: CakeModel, newItem: CakeModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CakeModel, newItem: CakeModel): Boolean {
            return oldItem.title == newItem.title
        }
    }
}