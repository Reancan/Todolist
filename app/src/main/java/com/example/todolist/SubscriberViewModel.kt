package com.example.todolist

import android.util.Patterns
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

        if(inputName.value==null) {
            statusMessage.value = Event("Please enter subscriber's Title")
        }else if(inputEmail.value==null){
            statusMessage.value = Event("Please enter subscriber's Kegiatan")
        }else if(Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event(" Please enter a correct Kegiatannya")
        }else{
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

    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{
            clearAll()
        }


    }

    fun insert(subscriber: Subscriber):Job = viewModelScope.launch {
        val newRowId :Long = repository.insert(subscriber)
        if(newRowId>-1) {
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
        }else{
            statusMessage.value = Event("Eror Occurred")
             }
        }

    fun update(subscriber: Subscriber):Job = viewModelScope.launch {
        val noOfRows :Int =  repository.update(subscriber)
        if(noOfRows>0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveorUpdateBottunText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        }else{
            statusMessage.value = Event("Error Occured")
        }
    }

    fun delete(subscriber: Subscriber):Job = viewModelScope.launch {

        val noOfRowsDeleted :Int = repository.delete(subscriber)

        if(noOfRowsDeleted>0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveorUpdateBottunText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Rw Deletedd Successfully")
        }else{
            statusMessage.value = Event("Error Occured")
        }
    }

    fun clearAll():Job = viewModelScope.launch {
        val noOfRowDeleted:Int = repository.deleteAll()
        if(noOfRowDeleted>0) {
            statusMessage.value = Event("$noOfRowDeleted Subscribers Deleted Successfully")
        }else{
            statusMessage.value = Event("Error Occured")
        }
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