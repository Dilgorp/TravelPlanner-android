package ru.dilgorp.android.travelplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.databinding.ListItemCityPlaceBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule

class CityPlacesAdapter(
    private val credentials: String,
    private val baseUrl: String
) : ListAdapter<CityPlace, CityPlacesAdapter.ViewHolder>(CityPlaceDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), credentials, baseUrl)
    }

    fun setData(list: List<CityPlace>) {
        adapterScope.launch {
            submitList(list)
        }
    }

    class ViewHolder private constructor(
        private val binding: ListItemCityPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemCityPlaceBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            cityPlace: CityPlace, credentials: String,
            baseUrl: String
        ) {
            with(binding) {
                name.text = cityPlace.name
                description.text = cityPlace.description
                val path = GET_PLACE_IMAGE_PATH
                    .replace("{user_uuid}", cityPlace.userUuid.toString())
                    .replace("{travel_uuid}", cityPlace.travelUuid.toString())
                    .replace("{city_uuid}", cityPlace.cityUuid.toString())
                    .replace("{place_uuid}", cityPlace.uuid.toString())

                val glideUrl = GlideUrl(
                    "$baseUrl$path",
                    LazyHeaders.Builder()
                        .addHeader(NetworkModule.AUTHORIZATION_HEADER_NAME, credentials).build()
                )

                Glide.with(root.context)
                    .load(glideUrl)
                    .timeout(30000)
                    .into(placePhoto)
            }
        }
    }

    class CityPlaceDiffCallback : DiffUtil.ItemCallback<CityPlace>() {
        override fun areItemsTheSame(oldItem: CityPlace, newItem: CityPlace): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: CityPlace, newItem: CityPlace): Boolean {
            return oldItem == newItem
        }

    }

    companion object {
        private const val GET_PLACE_IMAGE_PATH =
            "/user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/places/{place_uuid}/photo"
    }
}