package bldg5.jj.pgnbase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import bldg5.jj.pgnbase.adapters.GamesAdapter
import kotlinx.android.synthetic.main.list_games.*
import kotlinx.android.synthetic.main.listed_game.view.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select

class ListGames: Activity() {
    // Access property for Context
    val Context.database: PGNDBHelper
        get() = bldg5.jj.pgnbase.PGNDBHelper.getInstance(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_games)

        val strWhite = intent.getStringExtra("White") as String
        val strBlack = intent.getStringExtra("Black") as String
        val bIC = intent.getIntExtra("IgnoreColor", 0) == 1
        var listGames: List<Game> = ArrayList()
        val strWhereCore = "White LIKE '%$strWhite%' AND Black LIKE '%$strBlack%'"
        val strWhere = if (!bIC) strWhereCore
                else "($strWhereCore) OR (White LIKE '%$strBlack%' AND Black LIKE '%$strWhite%')"

        database.use {
            listGames = select("Games")
                    .whereArgs(strWhere)
                    .exec { parseList(classParser()) }
        }

        val noGamesFound = listGames.size == 0
        listGames = if (!noGamesFound) listGames
            else {
                val noGames = Game(Event = "No games were found. Click to go back to the search screen.")
                listGames = arrayListOf(noGames)
                listGames
            }

        val adapter = GamesAdapter(this, listGames)
        gamesList.adapter = adapter
        adapter.notifyDataSetChanged()

        gamesList.setOnItemClickListener { adapterView, view, pos, id ->
            if (noGamesFound)
                this.finish()
            else
            {
                val params = Bundle()
                params.putString("GameID", view._id.text.toString())

                val navShowGame = Intent(this@ListGames, PGNActivity::class.java)
                navShowGame.putExtras(params)
                this@ListGames.startActivity(navShowGame)
            }
        }
    }
}