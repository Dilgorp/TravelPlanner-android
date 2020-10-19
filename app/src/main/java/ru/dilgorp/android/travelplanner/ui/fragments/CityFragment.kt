package ru.dilgorp.android.travelplanner.ui.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Credentials
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
import ru.dilgorp.android.travelplanner.data.Place.Companion.CITY_UUID_NAME
import ru.dilgorp.android.travelplanner.databinding.FragmentCityBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.BASE_URL_NAME
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.SEARCH_PHOTO_PATH_NAME
import ru.dilgorp.android.travelplanner.navigator.Navigator
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.fragment.CityViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CityViewModel by viewModels { viewModelFactory }

    @Inject
    @Named(BASE_URL_NAME)
    lateinit var baseUrl: String

    @Inject
    @Named(SEARCH_PHOTO_PATH_NAME)
    lateinit var searchPhotoPath: String

    @Inject
    lateinit var navigator: Navigator

    private lateinit var cityUUID: UUID
    private lateinit var navController: NavController
    private lateinit var credentials: String

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

        setupViews()
        setupObservers()

        return binding.root
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
            name.observe(viewLifecycleOwner) {
                binding.name.editText?.setText(it)
            }
            description.observe(viewLifecycleOwner) {
                binding.description.editText?.setText(it)
            }
            photoId.observe(viewLifecycleOwner) {
                loadPhoto(it)
                cityUUID = UUID.fromString(it)
            }
            credentials.observe(viewLifecycleOwner) {
                this@CityFragment.credentials = it
            }
        }
    }

    private fun setupViews() {
        navController = findNavController()

        with(binding) {
            name.setEndIconOnClickListener {
                name.editText?.let { et ->
                    hideKeyboard()
                    viewModel.getCityInfo(et.text.toString())
                }
            }
            placesTitleLayout.setOnClickListener {
                val args = Bundle()
                args.putSerializable(CITY_UUID_NAME, cityUUID)
                navigator.navigateToDestination(
                    navController,
                    R.id.placesFragment,
                    args
                )
            }
        }
    }

    private fun hideKeyboard() {
        val view = requireView()
        val inputMethodManager =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun loadPhoto(photoId: String) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.nophoto)

        val glideUrl = GlideUrl(
            "$baseUrl$searchPhotoPath$photoId",
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
}