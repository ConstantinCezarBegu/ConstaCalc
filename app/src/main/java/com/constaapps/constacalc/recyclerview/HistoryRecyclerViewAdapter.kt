package com.constaapps.constacalc.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.constaapps.constacalc.R
import com.constaapps.constacalc.db.historyTable.HistoryEntity
import com.constaapps.constacalc.ui.main.MainViewModel
import com.constaapps.constacalc.util.update
import kotlinx.android.synthetic.main.layout_list_history_item.view.*


class HistoryRecyclerViewAdapter (private val mainViewModel: MainViewModel) :
    ListAdapter<HistoryEntity, RecyclerView.ViewHolder>(diffCallback) {

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<HistoryEntity>() {

            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.layout_list_history_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AchievementViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
        }
    }

    inner class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(historyEntity: HistoryEntity) {
            with(itemView) {
                val stringAnswer = historyEntity.answer

                formulaText.text = historyEntity.formula
                answerText.text = stringAnswer
                itemView.setOnClickListener {
                    if (historyEntity.isValid) {
                        mainViewModel.currentFormula.update(stringAnswer)
                        mainViewModel.displayFormula.update(stringAnswer)
                    }
                }
            }
        }
    }
}


fun ViewGroup.inflate(
    @LayoutRes layoutId: Int,
    inflater: LayoutInflater = LayoutInflater.from(context),
    attachToRoot: Boolean = false
): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}