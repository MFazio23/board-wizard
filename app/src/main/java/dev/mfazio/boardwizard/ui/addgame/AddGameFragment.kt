package dev.mfazio.boardwizard.ui.addgame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dev.mfazio.boardwizard.R

class AddGameFragment : Fragment() {

    private val viewModel by activityViewModels<AddGameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_game, container, false)
    }

}