package com.sakharu.queregardercesoir.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.locale.repository.UserListRepository
import com.sakharu.queregardercesoir.util.USER_LIST_FAVORITE_ID
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