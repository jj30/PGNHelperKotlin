package bldg5.jj.pgnbase

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import bldg5.jj.pgnbase.adapters.OnSwipeTouchListener
import kotlinx.android.synthetic.main.content_pgn.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select


class PGNActivity: Activity() {
    private var nMoveNumber = 0

    // Access property for Context
    val Context.database: PGNDBHelper
        get() = bldg5.jj.pgnbase.PGNDBHelper.getInstance(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_pgn)

        val gameId = intent.getStringExtra("GameID")
        var fullGame = Game()

        database.use {
            fullGame = select("Games")
                    .whereArgs("id = {gameId}", "gameId" to gameId)
                    .distinct()
                    .limit(1)
                    .exec { parseSingle(classParser()) }
        }

        boardShowing.game = fullGame
        boardShowing.initBoard(fullGame)

        // if it's long, make the font smaller
        // if it's really long trim it.
        // under 170, 20 size
        // between 170 and 300, 15 size
        // higher than 300, 12 size
        val desc = fullGame.fullDescription()
        txtCurrentMove.text = desc
        txtCurrentMove.textSize = if (desc.length > 300) 12.0f else
            if (desc.length > 170) 15.0f else 20.0f

        btnNext.setOnClickListener { next(txtCurrentMove) }

        btnPrev.setOnClickListener { prev(txtCurrentMove) }

        btnFirst.setOnClickListener { first(txtCurrentMove) }

        ///TODO, this is not resulting in the same board as
        ///if you go to the end of the game one move at a time.
        btnLast.setOnClickListener { last(txtCurrentMove) }

        btnSwitch.setOnClickListener { boardShowing.switchSides() }

        btnShare.setOnClickListener {
            val pgn = fullGame.PGN

            // First, copy the PGN to the clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PGN", pgn)
            clipboard.primaryClip = clip

            // then, toast that this has been completed.
            val context = applicationContext
            val text = "PGN added to clipboard."
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(context, text, duration)
            toast.show()

            // Finally, share it with the text intent
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, pgn)
                type = "text/plain"
            }

            startActivity(sendIntent)
        }

        // set the swipe listener
        boardShowing.setOnTouchListener(object : OnSwipeTouchListener(applicationContext) {
            override fun onSwipeTop() {
                last(txtCurrentMove)
            }

            override fun onSwipeRight() {
                prev(txtCurrentMove)
            }

            override fun onSwipeLeft() {
                next(txtCurrentMove)
            }

            override fun onSwipeBottom() {
                first(txtCurrentMove)
            }
        })

        // what is my screen density?
        /* http://stackoverflow.com/questions/3166501/getting-the-screen-density-programmatically-in-android
        0.75 - ldpi
        1.0 - mdpi
        1.5 - hdpi
        2.0 - xhdpi
        3.0 - xxhdpi
        4.0 - xxxhdpi
        */
        val scale = applicationContext.resources.displayMetrics.density

        if (BuildConfig.DEBUG)
            Log.i(javaClass.name.toString(), scale.toString())
    }


    private fun next(txtMove: TextView) {
        nMoveNumber = boardShowing.nMoveNumber + 1
        boardShowing.nMoveNumber = nMoveNumber

        // the pgn is set on instancing of CB so set the text view
        // but it has to be after the move number is set.
        txtMove.text = boardShowing.getMove()
        boardShowing.halfMove()
    }

    private fun prev(txtMove: TextView) {
        nMoveNumber = boardShowing.nMoveNumber - 1
        nMoveNumber = Math.max(0, nMoveNumber)
        boardShowing.nMoveNumber = nMoveNumber

        // the pgn is set on instancing of CB so set the text view
        // but it has to be after the move number is set.
        txtMove.text = boardShowing.getMove()
        boardShowing.halfMoveBackwards()
    }

    private fun first(txtMove: TextView) {
        nMoveNumber = 0
        boardShowing.nMoveNumber = nMoveNumber
        txtMove.text = boardShowing.getMove()
        boardShowing.initBoard()
    }

    private fun last(txtMove: TextView) {
        // if the game ends on a white move, this will be higher than
        // the max # of UI moves by one.
        nMoveNumber = 2 * boardShowing.nNumMovesInGame
        boardShowing.nMoveNumber = nMoveNumber
        txtMove.text = boardShowing.getMove()
        boardShowing.toTheEnd()
    }
}