package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.data.Place.Companion.CITY_UUID_NAME
import ru.dilgorp.android.travelplanner.databinding.FragmentPlacesBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.ui.adapter.PlacesAdapter
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.PlacesViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class PlacesFragment : Fragment() {
    private lateinit var binding: FragmentPlacesBinding
    private lateinit var adapter: PlacesAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PlacesViewModel by viewModels { viewModelFactory }

    @Inject
    @Named(NetworkModule.BASE_URL_NAME)
    lateinit var baseUrl: String

    @Inject
    @Named(NetworkModule.SEARCH_PLACE_PHOTO_PATH_NAME)
    lateinit var searchPhotoPath: String

    private lateinit var cityUUID: UUID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesBinding.inflate(inflater, container, false)
        adapter = PlacesAdapter(baseUrl, searchPhotoPath, viewModel.credentials)

        cityUUID = arguments?.getSerializable(CITY_UUID_NAME) as UUID

        setupViews()
        setupObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPlaces(cityUUID)
        setProgressBarVisibility(true)
    }

    private fun setupObservers() {
        with(viewModel){
            places.observe(viewLifecycleOwner){
                adapter.setData(it)
                setProgressBarVisibility(false)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(requireView(), getString(R.string.error_message), it) {
                    messageShown()
                }
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            placesRv.adapter = adapter
        }
    }

    private fun setProgressBarVisibility(visible: Boolean) {
        with(binding.progressBar) {
            visibility = if (visible) View.VISIBLE else View.GONE
            isIndeterminate = visible
        }
    }
}