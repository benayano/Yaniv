package com.benaya.yaniv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.view.CardsAdapter

class MainActivity : AppCompatActivity() {

    lateinit var cardsListView :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardsListView = findViewById(R.id.cardsList)



        cardsListView.adapter = CardsAdapter()
        cardsListView.adapter?.notifyDataSetChanged()
    }
}



