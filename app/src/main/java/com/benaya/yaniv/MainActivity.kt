package com.benaya.yaniv

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.Data.Card
import com.benaya.yaniv.Data.Player
import com.benaya.yaniv.model.network.Game
import com.benaya.yaniv.model.network.GameApiServiceImpl
import com.benaya.yaniv.view.CardsAdapter
import com.benaya.yaniv.view.PlayersAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var cardsAdapter: CardsAdapter
    lateinit var playersAdapter: PlayersAdapter
    lateinit var cardsListView: RecyclerView
    lateinit var playersListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardsListView = findViewById(R.id.cardsList)
        playersListView = findViewById(R.id.playersList)

        cardsAdapter = CardsAdapter()
        cardsListView.adapter = cardsAdapter
//        cardsListView.adapter?.notifyDataSetChanged()

        playersAdapter = PlayersAdapter()
        playersListView.adapter = playersAdapter
//        playersListView.adapter?.notifyDataSetChanged()

        loadGame()
    }

    private fun loadGame() {
        val call = GameApiServiceImpl
            .service
            .getGameStatus(15, "168a0b51-5459-42ea-a002-02d7e388340b")

        call.enqueue(GameCallback())

        //setProgressVisibility(true)
    }

    inner class GameCallback : Callback<Game> {
        private val statusTV: TextView = findViewById<TextView>(R.id.statusTV)

        override fun onResponse(call: Call<Game>, response: Response<Game>) {
            //TODO: update progressBar
            statusTV.text = "fetching game"

            if (response.isSuccessful) {
                response.body()?.let { game ->
                    game.players.find(this::isPlayerMe)?.let {
                        cardsAdapter.submitList(it.cards)
                    }
                    val opponents = game.players.filterNot(this::isPlayerMe)
                    playersAdapter.submitList(opponents)
                }
                statusTV.text = "game fetched"
            } else {
                statusTV.text =
                    "response.isSuccessful = ${response.isSuccessful}.  ${response.message()}"
            }
        }

        private fun isPlayerMe(player: Player) = player.userId == 4

        override fun onFailure(call: Call<Game>, t: Throwable) {
            statusTV.text = "onFailure\n"
            Log.e(MainActivity::javaClass.name, "game request failed", t)
        }
    }

    fun sumCardsValues(cards: List<Card>): Int {
        //https://grokonez.com/kotlin/kotlin-sum-sumby-method-list-map-objects-example
        return cards.map { it.value }.sum()
    }
}




