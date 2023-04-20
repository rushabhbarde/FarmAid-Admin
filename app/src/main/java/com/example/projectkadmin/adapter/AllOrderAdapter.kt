package com.example.projectkadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectkadmin.databinding.AllOrderItemLayoutBinding
import com.example.projectkadmin.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(val list : ArrayList<AllOrderModel>, val context: Context)
    : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>(){

    inner class AllOrderViewHolder(val binding: AllOrderItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = list[position].price
        holder.binding.cancelbtn.setOnClickListener {
            //holder.binding.proceedbtn.text = "Canceled"
            holder.binding.proceedbtn.visibility = GONE

            updateStatus("Canceled", list[position].orderId!!)
        }

        when(list[position].status){
            "Ordered" -> {
                holder.binding.proceedbtn.text = "Dispatched"
                holder.binding.proceedbtn.setOnClickListener {
                    updateStatus("Dispatched", list[position].orderId!!)
                }
            }
            "Dispatched" -> {
                holder.binding.proceedbtn.text = "Delivered"
                holder.binding.proceedbtn.setOnClickListener {
                    updateStatus("Delivered", list[position].orderId!!)
                }

            }
            "Delivered" -> {
                holder.binding.cancelbtn.visibility = GONE
                holder.binding.proceedbtn.isEnabled = false
                holder.binding.proceedbtn.text = "Already Delivered"
               // holder.binding.proceedbtn.setOnClickListener {
                //    updateStatus("Canceled", list[position].orderId!!)
                //}
            }
            "Canceled" -> {
                holder.binding.proceedbtn.visibility = GONE
                holder.binding.cancelbtn.isEnabled = false
            }
        }
    }

    fun updateStatus(str : String, doc : String){
        val data = hashMapOf<String, Any>()
        data["status"] = str
        Firebase.firestore.collection("allOrders")
            .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context, "Status Updated!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

}