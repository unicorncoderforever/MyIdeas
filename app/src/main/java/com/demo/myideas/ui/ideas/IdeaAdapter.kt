package com.demo.myideas.ui.ideas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.myideas.R
import com.demo.myideas.data.model.Idea
import com.demo.myideas.databinding.IdeaItemLayoutBinding
import com.demo.myideas.databinding.ProgressHolderBinding


class IdeaAdapter(var mIdeas: MutableList<Idea>, val onOptionClick: (Idea) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = true
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: IdeaItemLayoutBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.idea_item_layout, parent, false
                )
                return ViewHolder(binding)
            }

            VIEW_TYPE_LOADING -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ProgressHolderBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.progress_holder, parent, false
                )
                return ProgressHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ProgressHolderBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.progress_holder, parent, false
                )
                return ProgressHolder(binding)
            }


        }
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding: IdeaItemLayoutBinding = DataBindingUtil.inflate(
//            layoutInflater, R.layout.idea_item_layout, parent, false
//        )

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ViewHolder) {
            holder.mItemBinding.content.text = mIdeas[position].content
            holder.mItemBinding.imapactValue.text = mIdeas[position].impact.toString()
            holder.mItemBinding.easeValue.text = mIdeas[position].ease.toString()
            holder.mItemBinding.confidenceValue.text =
                mIdeas[position].confidence.toString()
            holder.mItemBinding.avg.text = mIdeas[position].avg.toString()
            holder.mItemBinding.option.setTag(mIdeas[position])
            holder.mItemBinding.option.setOnClickListener {
                onOptionClick(it.getTag() as Idea)
            }
        } else {
            holder as ProgressHolder
            holder.progressBinding.progressBar.visibility = View.VISIBLE
        }


    }

    override fun getItemCount(): Int {
        return mIdeas.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == mIdeas?.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    fun addLoading() {
        isLoaderVisible = true
        mIdeas.add(Idea(""));
        notifyItemInserted(mIdeas.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false

//        val item: PostItem = getItem(position)
//        if (item != null) {
//            mPostItems.remove(position)
//            notifyItemRemoved(position)
//        }
    }

    fun clear() {
        mIdeas.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(var mItemBinding: IdeaItemLayoutBinding) :
        RecyclerView.ViewHolder(mItemBinding.root)

    inner class ProgressHolder(var progressBinding: ProgressHolderBinding) :
        RecyclerView.ViewHolder(progressBinding.root)

}