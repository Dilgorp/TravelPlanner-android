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
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.databinding.ListItemCityBinding
import ru.dilgorp.android.travelplanner.di.NetworkModule

class CitiesAdapter(
    private val credentials: String,
    private val baseUrl: String
) : ListAdapter<City, CitiesAdapter.ViewHolder>(CityDiffCallback()){

    private val adapterScope = CoroutineScope(Dispatchers.Main)
    var clickListener = CityClickListener {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), credentials, baseUrl, clickListener)
    }

    fun setData(list: List<City>) {
        adapterScope.launch {
            submitList(list)
        }
    }

    class ViewHolder private  constructor(
        private val binding: ListItemCityBinding
    ): RecyclerView.ViewHolder(binding.root){

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemCityBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            city: City, credentials: String,
            baseUrl: String, clickListener: CityClickListener
        ){
            with(binding){
                name.text = city.name
                description.text = city.description
                placesCount.text = city.placesCount.toString()
                cityClickListener = clickListener
                this.city = city

                val path = GET_CITY_IMAGE_PATH
                    .replace("{user_uuid}", city.userUuid.toString())
                    .replace("{travel_uuid}", city.travelUuid.toString())
                    .replace("{city_uuid}", city.uuid.toString())

                val glideUrl = GlideUrl(
                    "$baseUrl$path",
                    LazyHeaders.Builder()
                        .addHeader(NetworkModule.AUTHORIZATION_HEADER_NAME, credentials).build()
                )

                Glide.with(root.context)
                    .load(glideUrl)
                    .timeout(30000)
                    .into(cityPhoto)
            }
        }
    }

    class CityDiffCallback: DiffUtil.ItemCallback<City>(){
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }

    }

    class CityClickListener(private val clickListener: (city: City) -> Unit){
        fun onClick(city: City) = clickListener(city)
    }

    companion object{
        private const val GET_CITY_IMAGE_PATH =
            "user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/photo"
    }


}