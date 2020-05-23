package dev.mfazio.boardwizard.ui.gamelist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.mfazio.boardwizard.R
import dev.mfazio.boardwizard.ui.gamelist.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 */
class GameListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = GameListRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }
}