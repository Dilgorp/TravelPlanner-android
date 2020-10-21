package ru.dilgorp.android.travelplanner

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.dilgorp.android.travelplanner.databinding.ActivityMainBinding
import ru.dilgorp.android.travelplanner.navigator.Navigator
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.provider.CredentialsProvider
import ru.dilgorp.android.travelplanner.vm.MainActivityViewModel
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CredentialsProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var navigator: Navigator

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private lateinit var credentials: String

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as AppComponentProvider).getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        setupViews()
        setContentView(binding.root)

        credentials = viewModel.credentials.value ?: ""

        setupObservers()

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
        }
    }

    override fun provideCredentials(): String {
        return credentials
    }

    private fun setupViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding.toolbar) {
            inflateMenu(R.menu.toolbar_menu)
            menu.findItem(R.id.logout_item).setOnMenuItemClickListener {
                viewModel.logout()
                true
            }
        }
    }

    private fun setupObservers() {
        viewModel.credentials.observe(this) {
            if (it.isEmpty()) {
                navigator.navigateWithoutBackStack(navController, R.id.loginFragment)
            } else {
                navigator.navigateWithoutBackStack(navController, R.id.travelsFragment)
            }
            binding.toolbar.menu.findItem(R.id.logout_item).isVisible = it.isNotEmpty()
            credentials = it
        }
    }
}