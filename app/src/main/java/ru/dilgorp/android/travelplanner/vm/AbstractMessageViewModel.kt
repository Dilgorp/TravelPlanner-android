package ru.dilgorp.android.travelplanner.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class AbstractMessageViewModel : ViewModel(){
    abstract val message: LiveData<String>

    abstract fun messageShown()
}