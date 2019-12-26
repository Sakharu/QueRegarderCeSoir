package com.sakharu.queregardercesoir.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel()
{
    var errorNetwork: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun setError()
    {
        withContext(Dispatchers.Main)
        {
            errorNetwork.value=true
        }
    }
}