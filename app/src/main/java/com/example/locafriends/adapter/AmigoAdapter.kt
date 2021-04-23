package com.example.locafriends.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.locafriends.*
import com.example.locafriends.modelo.Usuario
import kotlinx.android.synthetic.main.amigo_item.view.*

class AmigoAdapter(
    private val context: Context,
    private val dataset: List<Usuario>)
    : RecyclerView.Adapter<AmigoAdapter.AmigoViewHolder>() {

    class AmigoViewHolder( val view: View) : RecyclerView.ViewHolder(view) {
        val item: FrameLayout = view.findViewById(R.id.amigo_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.amigo_item, parent, false)

        return AmigoViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {

        val item = dataset[position]
        holder.item.nickname_amigo.text = item.nickname
        holder.item.email_amigo.text = item.email

        holder.item.setOnClickListener{
            val con = holder.view.context

            val i = Intent(con, MapitaUno::class.java)
            i.putExtra("amigo_email", item.email.toString())
            con.startActivity(i)
        }
    }

    override fun getItemCount(): Int = dataset.size

}