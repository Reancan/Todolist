package com.example.todolist

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
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
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete :Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveorUpdateBottunText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()


    val message : LiveData<Event<String>>
    get() = statusMessage

    init {
        saveorUpdateBottunText.value = "Save"
        clearAllOrDeleteButtonText.value = "CLear All"
    }

    fun saveOrUpdate(){

        if(isUpdateOrDelete){
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        }else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(id = 0, name = name, email = email))
            inputName.value = null
            inputEmail.value = null
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{
            clearAll()
        }


    }

    fun insert(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.insert(subscriber)
        
        statusMessage.value = Event("Subscriber Inserted Successfully")

        }

    fun update(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.update(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveorUpdateBottunText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
        statusMessage.value = Event("Subscriber Updated Successfully")

    }

    fun delete(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveorUpdateBottunText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
        statusMessage.value = Event("Subscriber Deletedd Successfully")
    }

    fun clearAll():Job = viewModelScope.launch {
        repository.deleteAll()
        statusMessage.value = Event("All Subscribers Deleted Successfully")

    }

    fun initUpdateAndDelete(subscriber: Subscriber){
       inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveorUpdateBottunText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}