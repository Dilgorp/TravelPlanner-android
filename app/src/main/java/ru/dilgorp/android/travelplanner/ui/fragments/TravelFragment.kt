package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.dilgorp.android.travelplanner.databinding.FragmentTravelBinding
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider

class TravelFragment : Fragment() {
    private lateinit var binding: FragmentTravelBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelBinding.inflate(inflater, container, false)

        setupViews()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {

    }

    private fun setupViews() {

    }
}