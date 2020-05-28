package com.example.todolist.db

class SubscriberRepository(private val dao : SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()


    suspend fun insert(subscriber: Subscriber):Long{
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber){
        dao.updateSubcsriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

}