package com.benaya.yaniv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.view.CardsAdapter
import com.benaya.yaniv.view.PlayersAdapter

class MainActivity : AppCompatActivity() {

    lateinit var cardsListView :RecyclerView
    lateinit var playersListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardsListView = findViewById(R.id.cardsList)
        playersListView = findViewById(R.id.playersList)


        cardsListView.adapter = CardsAdapter()
        cardsListView.adapter?.notifyDataSetChanged()

        playersListView.adapter =PlayersAdapter()
        playersListView.adapter?.notifyDataSetChanged()
    }
}




