package com.example.todolist.db

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R

import com.example.todolist.databinding.ListItemBinding
import com.example.todolist.db.Subscriber
import kotlinx.android.synthetic.main.list_item.view.*

class MyRecycleViewAdapter(private val subscriberList: List<Subscriber>): RecyclerView.Adapter<MyviewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
    val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val binding :ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item,parent, false)
    return MyviewHolder(binding)
    }

    override fun getItemCount(): Int {
    return  subscriberList.size
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
    holder.bind(subscriberList[position])
    }

}

class MyviewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(subscriber: Subscriber){
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
    }
}