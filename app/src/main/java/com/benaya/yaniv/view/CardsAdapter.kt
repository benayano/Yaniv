package com.benaya.yaniv.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.Card
import com.benaya.yaniv.R

class CardsAdapter() : RecyclerView.Adapter<CardViewHolder>() {


    private val cardList:List<Card> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CardViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.card,parent,false)
    )

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
       val thisCard = cardList[position]

        holder.bind(thisCard.value.toString(),thisCard.color)
    }

    override fun getItemCount() = cardList.size

}
class CardViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    private val imageCard:ImageView = itemView.findViewById(R.id.imageCard)
    private val numberCard:TextView = itemView.findViewById(R.id.numCard)

    fun bind(number:String, image:String){
        numberCard.text = number

        imageCard = when(image){
            "a"-> R.drawable.ic_card_heart
            "b"-> R.drawable.ic_suitclubs
            "c"-> R.drawable.ic_suitdiamonds
            else-> R.drawable.ic_suitspades
        }
    }
}













