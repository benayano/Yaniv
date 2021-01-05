package com.benaya.yaniv.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.Data.Card
import com.benaya.yaniv.Data.CardShape
import com.benaya.yaniv.R

class CardsAdapter() : RecyclerView.Adapter<CardViewHolder>() {


    private var cardList: List<Card> = listOf(
            Card(1,CardShape.CLUB), Card(4,CardShape.DIAMOND), Card(6, CardShape.HEART), Card(8, CardShape.SPADE), Card(9, CardShape.DIAMOND)
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CardViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.card, parent, false)
    )

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val thisCard = cardList[position]

        holder.bind(thisCard.value.toString(), thisCard.shape)
    }

    override fun getItemCount() = cardList.size

    fun submitList(cardsList: List<Card>) {
        this.cardList = cardsList
        notifyDataSetChanged()
    }
}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageCard: ImageView = itemView.findViewById(R.id.imageCard)
    private val numberCard: TextView = itemView.findViewById(R.id.numCard)

    fun bind(number:String, image:CardShape ) {
        numberCard.text = number

        imageCard.setImageResource(
                when (image) {
                    CardShape.CLUB -> R.drawable.ic_suitclubs
                    CardShape.DIAMOND ->R.drawable.ic_suitdiamonds
                    CardShape.SPADE -> R.drawable.ic_suitspades
                    CardShape.HEART -> R.drawable.ic_card_heart
                    else ->R.drawable.ic_launcher_background
                }
        )

    }

}













