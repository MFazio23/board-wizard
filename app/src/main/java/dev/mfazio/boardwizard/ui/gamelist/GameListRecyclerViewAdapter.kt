package dev.mfazio.boardwizard.ui.gamelist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dev.mfazio.boardwizard.R

import dev.mfazio.boardwizard.ui.gamelist.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_game_list_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GameListRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<GameListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_game_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.item_number
        val contentView: TextView = view.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}