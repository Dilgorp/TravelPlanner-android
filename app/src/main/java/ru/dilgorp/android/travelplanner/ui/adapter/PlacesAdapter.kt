package ru.dilgorp.android.travelplanner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.R
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.databinding.ListItemPlaceBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule

class PlacesAdapter(
    private val baseUrl: String,
    private val credentials: String
) : ListAdapter<Place, PlacesAdapter.ViewHolder>(PlaceDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Main)

    private val _selectedPlaces: MutableList<Place> = ArrayList(0)
    val selectedPlaces: List<Place> = _selectedPlaces

    private val _selectedPlacesSize = MutableLiveData<Int>()
    val selectedPlacesSize: LiveData<Int>
        get() = _selectedPlacesSize

    init {
        _selectedPlacesSize.postValue(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), baseUrl, credentials, _selectedPlaces){
            updateSelectedPlaceSize()
        }
    }

    private fun updateSelectedPlaceSize() {
        _selectedPlacesSize.value = _selectedPlaces.size
    }

    fun setData(list: List<Place>) {
        adapterScope.launch {
            submitList(list)
        }
    }

    fun dropSelection() {
        _selectedPlaces.clear()
        updateSelectedPlaceSize()
        notifyDataSetChanged()
    }

    class ViewHolder private constructor(
        private val binding: ListItemPlaceBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemPlaceBinding.inflate(inflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
            private const val SEARCH_PLACE_PHOTO_PATH =
                "search/places/photo/"
        }

        fun bind(
            place: Place,
            baseUrl: String,
            credentials: String,
            selectedPlaces: MutableList<Place>,
            selectItemCallback: () -> Unit
        ) {
            with(binding) {
                name.text = place.name
                description.text = place.description

                val glideUrl = GlideUrl(
                    "$baseUrl$SEARCH_PLACE_PHOTO_PATH${place.uuid}",
                    LazyHeaders.Builder()
                        .addHeader(NetworkModule.AUTHORIZATION_HEADER_NAME, credentials).build()
                )

                Glide.with(root.context)
                    .load(glideUrl)
                    .timeout(30000)
                    .into(placePhoto)

                setBackground(selectedPlaces, place)

                mainLayout.setOnClickListener {
                    onPlaceClickListener(selectedPlaces, place)
                    selectItemCallback()
                }
            }
        }

        private fun ListItemPlaceBinding.onPlaceClickListener(
            selectedPlaces: MutableList<Place>,
            place: Place
        ) {
            if (selectedPlaces.contains(place)) {
                selectedPlaces.remove(place)
            } else {
                selectedPlaces.add(place)
            }
            setBackground(selectedPlaces, place)
        }

        private fun ListItemPlaceBinding.setBackground(
            selectedPlaces: MutableList<Place>,
            place: Place
        ) {
            if (selectedPlaces.contains(place)) {
                card.setBackgroundColor(context.getColor(R.color.selectedBackgroundColor))
            } else {
                card.setBackgroundColor(context.getColor(R.color.cardBackgroundColor))
            }
        }
    }

    class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }
    }
}