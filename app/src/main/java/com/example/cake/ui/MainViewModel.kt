package com.example.cake.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.cake.common.Event
import com.example.cake.model.CakeModel
import com.example.cake.network.APIStatus
import com.example.cake.network.CakeApi
import kotlinx.coroutines.*
import javax.inject.Inject

//TODO :  Instead of injecting the api service, a repository layer can be added and injected.
class MainViewModel @Inject constructor(private val apiService: CakeApi) :
    ViewModel() {

    private val _cakeItems = MutableLiveData<List<CakeModel>>()
    val cakeItems: LiveData<List<CakeModel>>
        get() = _cakeItems

    val cakeItemsSorted = Transformations.map(_cakeItems) { it ->
        it.distinct().sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.title }))
    }

    private val _status = MutableLiveData<APIStatus>()
    val status: LiveData<APIStatus>
        get() = _status

    private val _selectedCakeItem = MutableLiveData<CakeModel>()
    val selectedCakeItem: LiveData<CakeModel>
        get() = _selectedCakeItem

    private val _cakeItemClickedEvent = MutableLiveData<Event<CakeModel>>()
    val cakeItemClickedEvent: LiveData<Event<CakeModel>>
        get() = _cakeItemClickedEvent

    init {

        getCakeItems()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun getCakeItems() {

        /***
         *  TODO : A single source of truth strategy can be used here.
         *  UI can receive data updates only from database while the app fetches the data from
         *  network which can be saved to the database and then emitted from database.
         *
         *  The below NetworkBoundResource class is a nice example.
         *
         *  https://github.com/android/architecture-components-samples/blob/88747993139224a4bb6dbe985adf652d557de621/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
         *
         */


        _status.value = APIStatus.LOADING

        viewModelScope.launch {
            try {

                val apiResponse = apiService.getCakeList()
                setCakeItems(apiResponse)
                _status.value = APIStatus.DONE

            } catch (e: Exception) {
                _status.value = APIStatus.ERROR
                _cakeItems.value = ArrayList()
            }
        }
    }

    fun setCakeItems(list: List<CakeModel>) {
        _cakeItems.value = list
    }

    fun userSelectsItem(item: CakeModel) {
        _cakeItemClickedEvent.value =
            Event(item)
        _selectedCakeItem.value = item
    }

}