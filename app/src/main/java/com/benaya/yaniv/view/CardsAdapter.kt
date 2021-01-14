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

    private var cardList: List<Card> = emptyList()
    private var isClickEnabled = true
    internal val  selectedList = mutableListOf<Card>()

    internal var selectedChangedListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CardViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.card, parent, false)
    )

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val thisCard = cardList[position]

        holder.bind(thisCard.value.toString(), thisCard.suit,thisCard in selectedList)
        holder.itemView.setOnClickListener{
            addOrRemoveSelectedList(thisCard)
        }
        holder.itemView.isEnabled = isClickEnabled
    }
   private fun addOrRemoveSelectedList(card : Card){
       if (card in selectedList){
           selectedList.remove(card)
       }else{
           if (selectedList.size==0 || card.value == selectedList[0].value){
               selectedList.add(card)
           }
       }
       selectedChangedListener?.invoke()
       notifyDataSetChanged()
    }
    override fun getItemCount() = cardList.size

    fun submitList(cardsList: List<Card>) {
        this.cardList = cardsList
        notifyDataSetChanged()
    }

    fun clearSelectedCards() {
        selectedList.clear()
        notifyDataSetChanged()
    }

    fun setClickable() {

    }
}



class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageCard: ImageView = itemView.findViewById(R.id.imageCard)
    private val numberCard: TextView = itemView.findViewById(R.id.numCard)


    fun bind(number:String, image:CardShape, selectedCard: Boolean = false ) {
        numberCard.text = number

        if (selectedCard){
            itemView.setBackgroundResource(R.color.select_card)
        }else{
            itemView.setBackgroundResource(R.color.white)
        }

        imageCard.setImageResource(
                when (image) {
                    CardShape.CLUBS -> R.drawable.ic_suitclubs
                    CardShape.DIAMONDS ->R.drawable.ic_suitdiamonds
                    CardShape.SPADES -> R.drawable.ic_suitspades
                    CardShape.HEARTS -> R.drawable.ic_card_heart
                    CardShape.JOKER -> R.drawable.ic_suit_joker
                    else ->R.drawable.ic_launcher_background
                }
        )

    }

}
