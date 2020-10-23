package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.databinding.FragmentPlacesBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule
import ru.dilgorp.android.travelplanner.navigator.Navigator
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.ui.adapter.PlacesAdapter
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.PlacesViewModel
import javax.inject.Inject
import javax.inject.Named

class PlacesFragment : Fragment() {
    private lateinit var binding: FragmentPlacesBinding
    private lateinit var adapter: PlacesAdapter
    private lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PlacesViewModel by viewModels { viewModelFactory }

    @Inject
    @Named(NetworkModule.BASE_URL_NAME)
    lateinit var baseUrl: String

    @Inject
    @Named(NetworkModule.SEARCH_PLACE_PHOTO_PATH_NAME)
    lateinit var searchPhotoPath: String

    @Inject
    lateinit var navigator: Navigator

    private lateinit var city: City

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

        city = arguments?.getSerializable(City.ARG_NAME) as City

        setupViews()
        setupObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        city.userRequestUuid?.let {
            viewModel.refreshPlaces(it)
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            places.observe(viewLifecycleOwner) {
                adapter.setData(it)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(requireView(), getString(R.string.error_message), it) {
                    messageShown()
                }
            }
            loading.observe(viewLifecycleOwner){
                setProgressBarVisibility(it)
            }
            navigation.observe(viewLifecycleOwner){
                if(it) {
                    navigateToCityFragment()
                    navigationDone()
                }
            }
        }

        adapter.selectedPlacesSize.observe(viewLifecycleOwner) {
            binding.selected.text = it.toString()
            binding.commandPanel.visibility = if (it == 0) GONE else VISIBLE
        }
    }

    private fun setupViews() {
        navController = findNavController()
        with(binding) {
            placesRv.adapter = adapter
            dropSelection.setOnClickListener {
                adapter.dropSelection()
            }
            acceptSelection.setOnClickListener {
                viewModel.addPlaces(city, adapter.selectedPlaces)
            }
        }
    }

    private fun setProgressBarVisibility(visible: Boolean) {
        with(binding.progressBar) {
            visibility = if (visible) VISIBLE else GONE
            isIndeterminate = visible
        }
    }

    private fun navigateToCityFragment() {
        val args = Bundle()
        args.putSerializable(City.ARG_NAME, city)
        navigator.navigateWithoutBackStack(
            navController,
            R.id.cityFragment,
            args
        )
    }
}