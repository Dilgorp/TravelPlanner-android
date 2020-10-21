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
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.databinding.FragmentTravelsBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule
import ru.dilgorp.android.travelplanner.navigator.Navigator
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.provider.CredentialsProvider
import ru.dilgorp.android.travelplanner.ui.adapter.TravelsAdapter
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.TravelsViewModel
import javax.inject.Inject
import javax.inject.Named

class TravelsFragment : Fragment() {
    private lateinit var binding: FragmentTravelsBinding
    private lateinit var credentials: String
    private lateinit var adapter: TravelsAdapter

    @Inject
    @Named(NetworkModule.BASE_URL_NAME)
    lateinit var baseUrl: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TravelsViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var navigator: Navigator

    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelsBinding.inflate(inflater, container, false)
        credentials = (requireActivity() as CredentialsProvider).provideCredentials()

        setupViews()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        with(viewModel) {
            travels.observe(viewLifecycleOwner) {
                adapter.setData(it)
                setupBackground(it.isEmpty())
            }
            travel.observe(viewLifecycleOwner) {
                navigateToTravelFragment(it)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(requireView(), getString(R.string.error_message), it) {
                    messageShown()
                }
            }
            loading.observe(viewLifecycleOwner){
                setProgressBarVisibility(it)
            }
        }
    }

    private fun setupViews() {
        navController = findNavController()

        adapter = TravelsAdapter(credentials, baseUrl)
        adapter.clickListener = TravelsAdapter.TravelClickListener {
            viewModel.selectTravel(it)
        }

        with(binding) {
            travelsRv.adapter = adapter
            addTravelButton.setOnClickListener {
                viewModel.addTravel()
                viewModel.loadingStarted()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateTravels()
        viewModel.loadingStarted()
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
        binding.travelsLayout.background = background
    }

    private fun navigateToTravelFragment(travel: Travel) {
        val args = Bundle()
        args.putSerializable(Travel.ARG_NAME, travel)
        navigator.navigateToDestination(
            navController,
            R.id.travelFragment,
            args
        )
    }

    private fun setProgressBarVisibility(visible: Boolean) {
        with(binding.progressBar) {
            visibility = if (visible) View.VISIBLE else View.GONE
            isIndeterminate = visible
        }
    }
}