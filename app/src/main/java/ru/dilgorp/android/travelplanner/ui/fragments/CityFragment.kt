package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.Place.Companion.CITY_UUID_NAME
import ru.dilgorp.android.travelplanner.databinding.FragmentCityBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.BASE_URL_NAME
import ru.dilgorp.android.travelplanner.navigator.Navigator
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.provider.CredentialsProvider
import ru.dilgorp.android.travelplanner.ui.adapter.CityPlacesAdapter
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.CityViewModel
import javax.inject.Inject
import javax.inject.Named

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private lateinit var credentials: String
    private lateinit var adapter: CityPlacesAdapter
    private lateinit var navController: NavController
    private lateinit var city: City

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CityViewModel by viewModels { viewModelFactory }

    @Inject
    @Named(BASE_URL_NAME)
    lateinit var baseUrl: String

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
        binding = FragmentCityBinding.inflate(inflater, container, false)
        credentials = (requireActivity() as CredentialsProvider).provideCredentials()
        city = arguments?.getSerializable(City.ARG_NAME) as City
        adapter = CityPlacesAdapter(credentials, baseUrl)

        setupViews()
        setupObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCityPlaces(city)
        loadPhoto()
    }

    private fun setupObservers() {
        with(viewModel) {
            searching.observe(viewLifecycleOwner) {
                setProgressBarVisibility(it)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(requireView(), getString(R.string.error_message), it) {
                    messageShown()
                }
            }
            city.observe(viewLifecycleOwner) {
                setupCityFields(it)
                this@CityFragment.city = it
                loadPhoto()
            }
            places.observe(viewLifecycleOwner) {
                adapter.setData(it)
            }
        }
    }

    private fun setupCityFields(city: City) {
        binding.name.editText?.setText(city.name)
        binding.description.editText?.setText(city.description)
    }

    private fun setupViews() {
        navController = findNavController()

        setupCityFields(city)

        with(binding) {
            name.setEndIconOnClickListener {
                name.editText?.let { et ->
                    hideKeyboard()
                    viewModel.getCityInfo(city, et.text.toString())
                }
            }
            placesTitleLayout.setOnClickListener {
                val args = Bundle()
                args.putSerializable(CITY_UUID_NAME, city.userRequestUuid)
                navigator.navigateToDestination(
                    navController,
                    R.id.placesFragment,
                    args
                )
            }
            placesRv.adapter = adapter
        }
    }

    private fun hideKeyboard() {
        val view = requireView()
        val inputMethodManager =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun loadPhoto() {

        if(city.name.isEmpty()){
            return
        }

        val path = GET_CITY_IMAGE_PATH
            .replace("{user_uuid}", city.userUuid.toString())
            .replace("{travel_uuid}", city.travelUuid.toString())
            .replace("{city_uuid}", city.uuid.toString())

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.nophoto)

        val glideUrl = GlideUrl(
            "$baseUrl$path",
            LazyHeaders.Builder()
                .addHeader(AUTHORIZATION_HEADER_NAME, credentials).build()
        )

        setProgressBarVisibility(true)
        Glide.with(requireContext())
            .load(glideUrl)
            .apply(options)
            .timeout(30000)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    setProgressBarVisibility(false)
                    viewModel.setMessage(e?.localizedMessage)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    setProgressBarVisibility(false)
                    return false
                }
            })
            .into(binding.photo)
    }

    private fun setProgressBarVisibility(visible: Boolean) {
        with(binding.progressBar) {
            visibility = if (visible) VISIBLE else GONE
            isIndeterminate = visible
        }
    }

    companion object {
        private const val GET_CITY_IMAGE_PATH =
            "user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/photo"
    }
}