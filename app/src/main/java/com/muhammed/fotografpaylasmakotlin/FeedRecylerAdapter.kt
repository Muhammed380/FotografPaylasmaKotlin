package com.muhammed.fotografpaylasmakotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.ArrayList

class FeedRecylerAdapter(val postList: ArrayList<Post> ) : RecyclerView.Adapter<FeedRecylerAdapter.ViewHolder>() {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.row_item_kullanıcı_email.text = postList[position].kullanıcıEmail
        holder.itemView.row_item_kullanıcı_yorum.text = postList[position].kullanıcıYorum
        Picasso.get().load(postList[position].gorselUrl).centerCrop().into(holder.itemView.row_item_imageview)
    }

    override fun getItemCount(): Int {
        return  postList.size

    }
}