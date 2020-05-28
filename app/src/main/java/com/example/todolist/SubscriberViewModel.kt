package com.example.todolist

import androidx.databinding.Bindable
import androidx.databinding.Observable
import  androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.db.Subscriber
import com.example.todolist.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(),Observable {

    val subscribers = repository.subscribers

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveorUpdateBottunText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveorUpdateBottunText.value = "Save"
        clearAllOrDeleteButtonText.value = "CLear All"
    }

    fun saveOrUpdate(){

        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(id = 0, name = name, email = email))
        inputName.value = null
        inputEmail.value = null

    }

    fun clearAllOrDelete(){
    clearAll()

    }

    fun insert(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.insert(subscriber)
        }

    fun update(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.update(subscriber)
    }

    fun delete(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.delete(subscriber)
    }

    fun clearAll():Job = viewModelScope.launch {
        repository.deleteAll()
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}