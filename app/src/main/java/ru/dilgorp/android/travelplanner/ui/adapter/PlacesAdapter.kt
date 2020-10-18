package ru.dilgorp.android.travelplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.databinding.ListItemPlaceBinding

class PlacesAdapter(
    private val baseUrl: String,
    private val searchPhotoPath: String
) : ListAdapter<Place, PlacesAdapter.ViewHolder>(PlaceDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), baseUrl, searchPhotoPath)
    }

    fun setData(list: List<Place>){
        adapterScope.launch {
            submitList(list)
        }
    }

    class ViewHolder private constructor(
        private val binding: ListItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemPlaceBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
        fun bind(place: Place, baseUrl: String, searchPhotoPath: String){
            with(binding){
                name.text = place.name
                description.text = place.description
                Glide.with(root.context)
                    .load("$baseUrl$searchPhotoPath${place.uuid}")
                    .timeout(30000)
                    .into(placePhoto)
            }
        }
    }

    class PlaceDiffCallback : DiffUtil.ItemCallback<Place>(){

        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }
    }
}