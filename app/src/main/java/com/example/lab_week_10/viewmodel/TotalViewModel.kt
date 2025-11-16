package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab_week_10.database.TotalObject

class TotalViewModel : ViewModel() {

    private val _total = MutableLiveData<TotalObject>()
    val total: LiveData<TotalObject> = _total

    init {
        _total.value = TotalObject(0, "")
    }

    fun incrementTotal() {
        val t = _total.value ?: TotalObject(0, "")
        _total.value = TotalObject(t.value + 1, t.date)
    }

    fun setTotal(value: Int, date: String) {
        _total.value = TotalObject(value, date)
    }
}
