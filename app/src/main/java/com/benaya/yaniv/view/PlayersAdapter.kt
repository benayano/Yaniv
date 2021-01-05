package com.benaya.yaniv.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.Data.Player
import com.benaya.yaniv.R

class PlayersAdapter() : RecyclerView.Adapter<PlayerViewHolder>() {

    var playersList: List<Player> = emptyList()
//    var playersList: List<Player> = listOf(
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40),
//        Player("Moshe", 30), Player("David", 29), Player("Haim", 40)
//    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlayerViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.players, parent, false)
    )

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val thisPlayer = playersList[position]

        holder.bind(thisPlayer.name, thisPlayer.score.toString())

    }

    override fun getItemCount() = playersList.size

    fun submitList(playersList: List<Player>) {
        this.playersList = playersList
        notifyDataSetChanged()
    }
}

class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playerName: TextView = itemView.findViewById(R.id.playerName)
    private val playerScores: TextView = itemView.findViewById(R.id.playerScore)


    fun bind(name: String, score: String) {
        playerName.text = name
        playerScores.text =" : $score"
    }
}