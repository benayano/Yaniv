package com.benaya.yaniv

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.Data.Card
import com.benaya.yaniv.Data.Player
import com.benaya.yaniv.model.network.BodyMove
import com.benaya.yaniv.model.network.Game
import com.benaya.yaniv.model.network.GameApiServiceImpl
import com.benaya.yaniv.model.network.TakeCardFrom
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

    var gameId: Int? = null

    val apikay:String ="168a0b51-5459-42ea-a002-02d7e388340b"
    val userId: Int = 4

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

        val inDeck: Button = findViewById(R.id.boxOffice)
        val open: ImageView = findViewById(R.id.mainCard)

        open.setOnClickListener {
            if (getSelectedCards().size > 0) {
                move(TakeCardFrom.Open)
            }
        }
        inDeck.setOnClickListener {
            if (getSelectedCards().size > 0) {
                move(TakeCardFrom.Deck)
            }
        }



    }

    private fun getSelectedCards() = cardsAdapter.selectedList

    private fun joinGame() {
        val call = GameApiServiceImpl
            .service
            .postGamesStatus(apikay)

        Log.d(MainActivity::javaClass.name, "in joinGame")
        call.enqueue(GameCallback())

        setVisibility(findViewById(R.id.progressBar), true)
    }

    private fun loadGame(gameId: Int) {
        val call = GameApiServiceImpl
            .service
            .getGameStatus(gameId, apikay)

        findViewById<TextView>(R.id.statusTV).text = "loadGame"
        Log.d(MainActivity::javaClass.name, "in loadGame")
        call.enqueue(GameCallback())
    }

    private fun loadGamePeriodically(gameId: Int?) {
        gameId?.let {
            handler.postDelayed({
                loadGame(gameId)
            }, 5000)
        }
        Log.d(MainActivity::javaClass.name, "in loadGamePeriodically")
        //setProgressVisibility(true)
    }

//    private fun moveTurn() {
//        val call = GameApiServiceImpl
//            .service
//            .postMove( "168a0b51-5459-42ea-a002-02d7e388340b",)
//
//        call.enqueue(GameCallback())
//    }

    inner class GameCallback : Callback<Game> {
        private val statusTV: TextView = findViewById<TextView>(R.id.statusTV)

        override fun onResponse(call: Call<Game>, response: Response<Game>) {
            //TODO: update progressBar
            Log.d(MainActivity::javaClass.name, "onResponse fetching game")

            if (response.isSuccessful) {
                response.body()?.let { game ->
                    if (gameId != game.id)
                        gameId = game.id
                    game.players.find(this::isPlayerMe)?.let {
                        cardsAdapter.submitList(it.cards)

                        val sum = sumCardsValues(it.cards)
                        setEnabled(findViewById<Button>(R.id.yanivButton), sum <= 7)
                    }

                    val opponents = game.players.filterNot(this::isPlayerMe)
                    if (opponents.isNotEmpty()) {
                        playersAdapter.submitList(opponents)
                    }else {
                        playersAdapter.submitList(listOf(Player(0, "Waiting for opponents", emptyList(), 0)))
                    }

                    findViewById<TextView>(R.id.userNameTV).text =
                        "currentPlayer: " + game.players.find { it.userId == game.currentPlayer }?.name
                }
                Log.d(MainActivity::javaClass.name, "response is successful; game fetched.")
            } else {
                statusTV.text =
                    "response.isSuccessful = ${response.isSuccessful}.  ${response.message()}"
            }
            setVisibility(findViewById(R.id.progressBar), false)
            loadGamePeriodically(gameId)
        }

        private fun isPlayerMe(player: Player) = player.userId == userId

        override fun onFailure(call: Call<Game>, t: Throwable) {
            //statusTV.text = "onFailure\n"
            Log.e(MainActivity::javaClass.name, "game request failed", t)
            setVisibility(findViewById(R.id.progressBar), false)
            loadGamePeriodically(gameId)
        }
    }

    fun sumCardsValues(cards: List<Card>): Int {
        //https://grokonez.com/kotlin/kotlin-sum-sumby-method-list-map-objects-example
        return cards.map { it.value }.sum()
    }

    fun move(takeCardFrom: TakeCardFrom) {
        gameId?.let { gameId ->
            val bodyMove = BodyMove(
                playerId = userId,
                gameId,
                getSelectedCards(),
                takeCardFrom
            )

            val call = GameApiServiceImpl
                .service
                .postMove(apikay, bodyMove)

            call.enqueue(GameCallback())

            cardsAdapter.clearSelectedCards()
        }
        findViewById<Button>(R.id.boxOffice).isEnabled = false
        findViewById<ImageView>(R.id.mainCard).isEnabled = false
    }

    private fun setEnabled(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    private fun setVisibility(view: View, isVisible: Boolean) {
        view.isVisible = isVisible
    }
}




