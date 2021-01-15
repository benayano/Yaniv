package com.benaya.yaniv

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.benaya.yaniv.data.Card
import com.benaya.yaniv.data.CardShape
import com.benaya.yaniv.data.Player
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

    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var playersAdapter: PlayersAdapter
    private lateinit var cardsListView: RecyclerView
    private lateinit var playersListView: RecyclerView
    private lateinit var actionsButtons: LinearLayout
    private lateinit var currentPlayerTV: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var numOpenCard: TextView
    private lateinit var imageOpenCard: ImageView
    private lateinit var inDeckCard: ImageView
    private lateinit var yanivButton: Button

    lateinit var statusTV: TextView

    var gameId: Int? = null

    private val apiKey: String = "168a0b51-5459-42ea-a002-02d7e388340b"
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

        currentPlayerTV = findViewById(R.id.userNameTV)

        progressBar = findViewById(R.id.progressBar)

        imageOpenCard = findViewById(R.id.imageCard)
        numOpenCard = findViewById(R.id.numCard)

        actionsButtons = findViewById(R.id.actions)

        cardsListView.forEach {
            setEnabled(it, false)
            Log.i(MainActivity::javaClass.name, "$it -> ${it.isEnabled}")
        }

        inDeckCard = findViewById(R.id.inDeck)

        setEnabled(inDeckCard, false)
        cardsAdapter.selectedChangedListener = {
            setEnabled(inDeckCard, cardsAdapter.selectedList.isNotEmpty())
            setEnabled(imageOpenCard, cardsAdapter.selectedList.isNotEmpty())
            setEnabled(numOpenCard, cardsAdapter.selectedList.isNotEmpty())
        }

        yanivButton = findViewById(R.id.yanivButton)

        statusTV = findViewById(R.id.statusTV)

        numOpenCard.setOnClickListener {
            if (getSelectedCards().size > 0) {
                move(TakeCardFrom.Open)
            }
        }
        imageOpenCard.setOnClickListener {
            if (getSelectedCards().size > 0) {
                move(TakeCardFrom.Open)
            }
        }
        inDeckCard.setOnClickListener {
            if (getSelectedCards().size > 0) {
                move(TakeCardFrom.Deck)
            }
        }

        joinGame()
    }

    private fun getSelectedCards() = cardsAdapter.selectedList

    private fun joinGame() {
        val call = GameApiServiceImpl
            .service
            .postGamesStatus(apiKey)

        Log.d(MainActivity::javaClass.name, "in joinGame")
        call.enqueue(GameCallback())

        setVisibility(progressBar, true)
    }

    private fun loadGame(gameId: Int) {
        val call = GameApiServiceImpl
            .service
            .getGameStatus(gameId, apiKey)

        Log.d(MainActivity::javaClass.name, "in loadGame")
        call.enqueue(GameCallback())
    }

    private fun loadGamePeriodically(gameId: Int?) {
        gameId?.let {
            Handler(mainLooper).postDelayed({
                loadGame(gameId)
            }, 5000)
        }
        Log.d(MainActivity::javaClass.name, "in loadGamePeriodically")
    }


    inner class GameCallback : Callback<Game> {

        override fun onResponse(call: Call<Game>, response: Response<Game>) {
            Log.d(MainActivity::javaClass.name, "onResponse fetching game")

            if (response.isSuccessful) {
                response.body()?.let { game ->
                    if (gameId != game.id) {
                        gameId = game.id
                    }

                    game.players.find(this::isPlayerMe)?.let {
                        cardsAdapter.submitList(it.cards)

                        val sum = sumCardsValues(it.cards)
                        setEnabled(yanivButton, sum <= 7)
                    }

                    val opponents = game.players.filterNot(this::isPlayerMe)
                    if (opponents.isNotEmpty()) {
                        playersAdapter.submitList(opponents)
                    }else {
                        playersAdapter.submitList(listOf(Player(0, "Waiting for opponents", emptyList(), 0)))
                    }

                    currentPlayerTV.text =
                        "currentPlayer: ${game.players.find { it.userId == game.currentPlayer }?.name}"

                    imageOpenCard.setImageResource(when (game.deck.open.suit) {
                        CardShape.CLUBS -> R.drawable.ic_suitclubs
                        CardShape.DIAMONDS ->R.drawable.ic_suitdiamonds
                        CardShape.SPADES -> R.drawable.ic_suitspades
                        CardShape.HEARTS -> R.drawable.ic_card_heart
                        CardShape.JOKER -> R.drawable.ic_suit_joker
                        else ->R.drawable.ic_launcher_background
                    })

                    numOpenCard.text = game.deck.open.value.toString()

                    //TODO: turn cardsListView back to be clickable
                }
                Log.d(MainActivity::javaClass.name, "response is successful; game fetched.")
            } else {
                //TODO: set cardsListView to be un-clickable
                statusTV.text =
                    "response.isSuccessful = ${response.isSuccessful}.  ${response.message()}"
            }
            setVisibility(progressBar, false)
            loadGamePeriodically(gameId)
        }

        private fun isPlayerMe(player: Player) = player.userId == userId

        override fun onFailure(call: Call<Game>, t: Throwable) {
            Log.e(MainActivity::javaClass.name, "game request failed", t)
            setVisibility(progressBar, false)
            loadGamePeriodically(gameId)
        }
    }

    fun sumCardsValues(cards: List<Card>): Int {
        //https://grokonez.com/kotlin/kotlin-sum-sumby-method-list-map-objects-example
        return cards.map { it.value }.sum()
    }

    private fun move(takeCardFrom: TakeCardFrom) {
        gameId?.let { gameId ->
            val bodyMove = BodyMove(
                playerId = userId,
                gameId,
                getSelectedCards(),
                takeCardFrom
            )

            val call = GameApiServiceImpl
                .service
                .postMove(apiKey, bodyMove)

            call.enqueue(GameCallback())

            cardsAdapter.clearSelectedCards()
        }
        setEnabled(inDeckCard, false)
        setEnabled(numOpenCard, false)
        setEnabled(imageOpenCard, false)
    }

    private fun setEnabled(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    private fun setVisibility(view: View, isVisible: Boolean) {
        view.isVisible = isVisible
    }
}
