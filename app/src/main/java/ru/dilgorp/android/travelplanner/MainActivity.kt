package ru.dilgorp.android.travelplanner

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.dilgorp.android.travelplanner.databinding.ActivityMainBinding
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider
import ru.dilgorp.android.travelplanner.ui.dialog.showMessage
import ru.dilgorp.android.travelplanner.vm.MainActivityViewModel
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as AppComponentProvider).getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        setupViews()
        setContentView(binding.root)

        setupObservers()

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
        }
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
                showMessage(
                    binding.root,
                    getString(R.string.user_logout),
                    getString(R.string.user_logout)
                )
            } else {
                showMessage(
                    binding.root,
                    getString(R.string.user_logged),
                    getString(R.string.user_logged)
                )
            }
            binding.toolbar.menu.findItem(R.id.logout_item).isVisible = it.isNotEmpty()
        }
    }
}