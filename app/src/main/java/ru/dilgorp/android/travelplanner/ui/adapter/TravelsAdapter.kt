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
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.databinding.ListItemTravelBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule

class TravelsAdapter(
    private val credentials: String,
    private val baseUrl: String
) : ListAdapter<Travel, TravelsAdapter.ViewHolder>(TravelDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Main)
    var clickListener = TravelClickListener {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), credentials, baseUrl, clickListener)
    }

    fun setData(list: List<Travel>) {
        adapterScope.launch {
            submitList(list)
        }
    }

    class ViewHolder private constructor(
        private val binding: ListItemTravelBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemTravelBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            travel: Travel, credentials: String,
            baseUrl: String, clickListener: TravelClickListener
        ) {
            with(binding) {
                name.text = travel.name
                citiesCount.text = travel.citiesCount.toString()
                placesCount.text = travel.placesCount.toString()
                travelClickListener = clickListener
                this.travel = travel

                val path = GET_TRAVEL_IMAGE_PATH.replace("{user_uuid}", travel.userUuid.toString())
                    .replace("{travel_uuid}", travel.uuid.toString())

                val glideUrl = GlideUrl(
                    "$baseUrl$path",
                    LazyHeaders.Builder()
                        .addHeader(NetworkModule.AUTHORIZATION_HEADER_NAME, credentials).build()
                )

                Glide.with(root.context)
                    .load(glideUrl)
                    .timeout(30000)
                    .into(travelPhoto)
            }
        }
    }

    class TravelDiffCallback : DiffUtil.ItemCallback<Travel>() {

        override fun areItemsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem == newItem
        }
    }

    class TravelClickListener(private val clickListener: (travel: Travel) -> Unit) {
        fun onClick(travel: Travel) = clickListener(travel)
    }

    companion object {
        private const val GET_TRAVEL_IMAGE_PATH = "user/{user_uuid}/travel/{travel_uuid}/photo"
    }
}