package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.databinding.FragmentTravelBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule
import ru.dilgorp.android.travelplanner.navigator.Navigator
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.provider.CredentialsProvider
import ru.dilgorp.android.travelplanner.ui.adapter.CitiesAdapter
import ru.dilgorp.android.travelplanner.ui.callback.DeleteItemCallback
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.TravelViewModel
import javax.inject.Inject
import javax.inject.Named

class TravelFragment : Fragment() {
    private lateinit var binding: FragmentTravelBinding
    private lateinit var credentials: String
    private lateinit var adapter: CitiesAdapter
    private lateinit var navController: NavController
    private lateinit var travel: Travel

    private var start = true

    @Inject
    @Named(NetworkModule.BASE_URL_NAME)
    lateinit var baseUrl: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TravelViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var navigator: Navigator

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
        credentials = (requireActivity() as CredentialsProvider).provideCredentials()

        travel = arguments?.getSerializable(Travel.ARG_NAME) as Travel

        setupViews()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        with(viewModel) {
            cities.observe(viewLifecycleOwner) {
                proceedCities(it)
            }
            city.observe(viewLifecycleOwner) {
                it?.let {
                    navigateToCityFragment(it)
                }
            }
            message.observe(viewLifecycleOwner) {
                showMessage(requireView(), getString(R.string.error_message), it) {
                    messageShown()
                }
            }
            loading.observe(viewLifecycleOwner) {
                setProgressBarVisibility(it)
            }
        }
    }

    private fun proceedCities(cities: List<City>) {
        adapter.setData(cities)
        setupBackground(cities.isEmpty())
        if(cities.isEmpty()){
            navigateToTravelsFragment()
        }
    }

    private fun setupViews() {
        navController = findNavController()
        adapter = CitiesAdapter(credentials, baseUrl)

        adapter.clickListener = CitiesAdapter.CityClickListener {
            viewModel.selectCity(it)
        }

        val itemTouchHelper = ItemTouchHelper(DeleteItemCallback{
            viewModel.deleteCity(adapter.currentList[it])
        })

        with(binding) {
            addCityButton.setOnClickListener {
                viewModel.addCity(travel.uuid)
            }
            citiesRv.adapter = adapter
            itemTouchHelper.attachToRecyclerView(citiesRv)
        }
    }

    override fun onResume() {
        super.onResume()

        if(travel.citiesCount == 0 && start){
            viewModel.addCity(travel.uuid)
            start = false
        }else{
            viewModel.updateCities(travel.uuid)
        }
    }

    private fun setupBackground(empty: Boolean) {
        val background = if (empty) {
            AppCompatResources
                .getDrawable(
                    requireContext(),
                    R.drawable.background
                )
        } else {
            AppCompatResources
                .getDrawable(
                    requireContext(),
                    R.drawable.transparent
                )
        }
        binding.citiesLayout.background = background
    }

    private fun navigateToCityFragment(city: City) {
        val args = Bundle()
        args.putSerializable(City.ARG_NAME, city)
        navigator.navigateToDestination(
            navController,
            R.id.cityFragment,
            args
        )
        viewModel.navigateDone()
    }

    private fun navigateToTravelsFragment() {
        navigator.navigateWithoutBackStack(
            navController,
            R.id.travelsFragment
        )
    }

    private fun setProgressBarVisibility(visible: Boolean) {
        with(binding.progressBar) {
            visibility = if (visible) View.VISIBLE else View.GONE
            isIndeterminate = visible
        }
    }
}