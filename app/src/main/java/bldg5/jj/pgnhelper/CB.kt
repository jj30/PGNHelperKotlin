package bldg5.jj.pgnhelper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TableLayout
import bldg5.jj.pgnhelper.common.Piece
import bldg5.jj.pgnhelper.common.Board
import java.util.regex.Pattern


class CB(ctx: Context, attrs: AttributeSet) : TableLayout(ctx, attrs) {
    lateinit var aryPGNs: Array<String>
    private lateinit var currentBoard: Array<Array<String>>
    var bIsFlipped = false
    lateinit var black: String
    lateinit var white: String
    lateinit var round: String
    var nMoveNumber: Int = 0
    var nNumMovesInGame: Int = 0

    init {
        initializeViews(context)

        try {
            currentBoard = Board.PGN2Board(nMoveNumber, aryPGNs)
            Drawboard(currentBoard)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }
    }

    var game = Game()
        set(value) {
            field = value

            ///TODO when DB is clean remove this code
            try {
                val str_regex = "\\d+\\."
                val regex = Pattern.compile(str_regex, Pattern.DOTALL)

                aryPGNs = value.PGN.split(regex.toRegex()).dropWhile{ it -> it.isEmpty() }.toTypedArray()

                val nNumberDots = aryPGNs.size

                for (n in 1..nNumberDots) {
                    val thisNumberString = n.toString() + "."
                    val nextNumberString = (n + 1).toString() + "."

                    // up to and including
                    val upToNPlusOneThMove = value.PGN.split(nextNumberString)[0]
                    val nThMove = upToNPlusOneThMove.split(thisNumberString)[1]

                    aryPGNs[n - 1] = nThMove
                }

            } catch (ex: Exception) {
                // Dot in the comments padded the number.
            }

            // store the number of moves in this game
            nNumMovesInGame = aryPGNs.size - 1
        }

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.board, this)
    }

    fun getMove(): String {
        var strReturn: String
        // nPGNMoveNumber is the UI move number
        // 1st move is white 1, 2nd is black 1
        // 3rd is white 2, 4th is black 2, etc.
        val nPGNMoveNumber = Math.max(1, (nMoveNumber + 1) / 2)

        try {
            // zero-based
            val movePGN = aryPGNs[nPGNMoveNumber - 1].trim()
            val movePGNMinusComments = splitCommentsMove(movePGN)[0]

            // \\s+ splits any number of whitespace, two spaces or one
            val strWhite = movePGNMinusComments.split("\\s+".toRegex())[0]
            val strBlack = movePGNMinusComments.split("\\s+".toRegex())[1]

            if (nMoveNumber % 2 == 1)
                strReturn = "$nPGNMoveNumber. $strWhite"
            else
                strReturn = "$nPGNMoveNumber. ...$strBlack"

            // if there are comments, even one, it's in an array, ie ["just this one comment"]
            if (movePGN.contains("{") && movePGN.contains("}")) {
                val moveComment = splitCommentsMove(movePGN)[1]
                strReturn += "\n" + moveComment
            }
        } catch (ex: IndexOutOfBoundsException) {
            strReturn = if (nPGNMoveNumber == 0) getInfo() else "End of game."
        }

        return strReturn
    }

    private fun splitCommentsMove(strIn: String): Array<String> {
        var moveWOComments = ""
        var allComments = ""
        val str_regex = "[{}]"
        val ary = strIn.split(str_regex.toRegex())

        for (elem in ary) {
            allComments += if (elem.contains("[") && elem.contains("]")) elem else ""
            moveWOComments += if (elem.contains("[") && elem.contains("]")) "" else elem
        }

        return arrayOf(moveWOComments, allComments)
    }

    private fun getInfo(): String {
        val desc = game.description()

        val whiteResult = game.Result.split("-".toRegex())[0]
        val blackResult = game.Result.split("-".toRegex())[1]
        return "$white ($whiteResult) vs $black ($blackResult), $desc"
    }

    fun initBoard() {
        try {
            currentBoard = Board.initBoard()
            Drawboard(currentBoard)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }
    }

    fun initBoard(g: Game) {
        try {
            this.game = g

            currentBoard = Board.initBoard()
            Drawboard(currentBoard)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }
    }

    fun toTheEnd() {
        try {
            currentBoard = Board.toTheEnd(aryPGNs)
            Drawboard(currentBoard)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }
    }

    fun switchSides() {
        try {
            bIsFlipped = !bIsFlipped
            Drawboard(this.currentBoard)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }
    }

    fun halfMove() {
        try {
            currentBoard = Board.oneMove(nMoveNumber, aryPGNs, currentBoard)
            Drawboard(currentBoard)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }

    }

    fun halfMoveBackwards() {
        try {
            val board = Board.PGN2Board(nMoveNumber, aryPGNs)
            currentBoard = board
            Drawboard(board)
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }

    }

    private fun Drawboard(thisBoard: Array<Array<String>>) {
        try {
            for (i in 0..7) {
                for (j in 0..7) {
                    val piece: Piece.Type = Piece.Type.to(thisBoard[i][j])

                    // if the view is flipped, flip it
                    val newY = if (bIsFlipped) 7 - i else i
                    val imageView = findViewById(Board.boardRIDs[newY][j]) as ImageView

                    if (piece != Piece.Type.None) {
                        imageView.setImageResource(Board.mapStringsToResources[thisBoard[i][j]]!!)
                    } else {
                        imageView.setImageDrawable(null)
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }
    }
}