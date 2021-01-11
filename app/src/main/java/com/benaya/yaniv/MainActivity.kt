package com.benaya.yaniv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.Data.Card
import com.benaya.yaniv.Data.Player
import com.benaya.yaniv.model.network.GameApiServiceImpl
import com.benaya.yaniv.model.network.Game
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
    private val handler = Handler()

    //TODO: Ask Orel what is the right way to initialize gameId
    var gameId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardsListView = findViewById(R.id.cardsList)
        playersListView = findViewById(R.id.playersList)

        cardsAdapter = CardsAdapter()
        cardsListView.adapter = cardsAdapter

        playersAdapter = PlayersAdapter()
        playersListView.adapter = playersAdapter

        joinGame()

    }

    private fun joinGame() {
        val call = GameApiServiceImpl
            .service
            .postGamesStatus("168a0b51-5459-42ea-a002-02d7e388340b")

        Log.e(MainActivity::javaClass.name, "in joinGame")
        call.enqueue(GameCallback())
    }

    private fun loadGame(gameId: Int) {
        val call = GameApiServiceImpl
            .service
            .getGameStatus(gameId, "168a0b51-5459-42ea-a002-02d7e388340b")

        findViewById<TextView>(R.id.statusTV).text = "loadGame"
        Log.e(MainActivity::javaClass.name, "in loadGame")
        call.enqueue(GameCallback())
    }

    private fun loadGamePeriodically(gameId: Int?) {
        gameId?.let {
            handler.postDelayed({
                loadGame(gameId)
            }, 5000)
        }
        Log.e(MainActivity::javaClass.name, "in loadGamePeriodically")
        //setProgressVisibility(true)
    }

    inner class GameCallback : Callback<Game> {
        private val statusTV: TextView = findViewById<TextView>(R.id.statusTV)

        override fun onResponse(call: Call<Game>, response: Response<Game>) {
            //TODO: update progressBar
            Log.e(MainActivity::javaClass.name, "onResponse fetching game")

            if (response.isSuccessful) {
                response.body()?.let { game ->
                    if (gameId != game.id)
                        gameId = game.id
                    game.players.find(this::isPlayerMe)?.let {
                        cardsAdapter.submitList(it.cards)

                        val sum = sumCardsValues(it.cards)
                        //https://kotlinlang.org/docs/reference/control-flow.html
                        findViewById<Button>(R.id.yanivButton).isEnabled = sum <= 7

                        //statusTV.text = "The sum of your cards' values is: $sum in game#: $gameId"
                    }

                    val opponents = game.players.filterNot(this::isPlayerMe)
                    playersAdapter.submitList(opponents)
                }
                Log.e(MainActivity::javaClass.name, "response is successful; game fetched.")
            } else {
                statusTV.text =
                    "response.isSuccessful = ${response.isSuccessful}.  ${response.message()}"
            }

            loadGamePeriodically(gameId)
        }

        private fun isPlayerMe(player: Player) = player.userId == 4

        override fun onFailure(call: Call<Game>, t: Throwable) {
            //statusTV.text = "onFailure\n"
            Log.e(MainActivity::javaClass.name, "game request failed", t)
            loadGamePeriodically(gameId)
        }
    }

    fun sumCardsValues(cards: List<Card>): Int {
        //https://grokonez.com/kotlin/kotlin-sum-sumby-method-list-map-objects-example
        return cards.map { it.value }.sum()
    }
}




