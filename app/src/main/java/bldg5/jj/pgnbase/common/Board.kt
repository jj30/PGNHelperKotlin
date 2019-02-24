package bldg5.jj.pgnbase.common


import android.util.Log
import bldg5.jj.pgnbase.R
import java.util.*


object Board {
    const val xAxis = "abcdefgh"
    const val yAxis = "12345678"
    const val nonPawns = "RNBQK"
    val mapStringsToResources: HashMap<String, Int> = hashMapOf(
            "wr" to R.drawable.wr,
            "wn" to R.drawable.wn,
            "wb" to R.drawable.wb,
            "wk" to R.drawable.wk,
            "wq" to R.drawable.wq,
            "wp" to R.drawable.wp,

            // black pieces
            "br" to R.drawable.br,
            "bn" to R.drawable.bn,
            "bb" to R.drawable.bb,
            "bk" to R.drawable.bk,
            "bq" to R.drawable.bq,
            "bp" to R.drawable.bp
    )

    // what color is the square x, y? returns "w" or "b"
    fun squareColor(x: Int, y: Int): String {
        return if ((x + y) % 2 == 0) "b" else "w"
    }

    // board resource ID's. This is just the board wo the pieces
    val boardRIDs = arrayOf(
            intArrayOf(R.id.row0col0, R.id.row0col1, R.id.row0col2, R.id.row0col3, R.id.row0col4, R.id.row0col5, R.id.row0col6, R.id.row0col7),
            intArrayOf(R.id.row1col0, R.id.row1col1, R.id.row1col2, R.id.row1col3, R.id.row1col4, R.id.row1col5, R.id.row1col6, R.id.row1col7),
            intArrayOf(R.id.row2col0, R.id.row2col1, R.id.row2col2, R.id.row2col3, R.id.row2col4, R.id.row2col5, R.id.row2col6, R.id.row2col7),
            intArrayOf(R.id.row3col0, R.id.row3col1, R.id.row3col2, R.id.row3col3, R.id.row3col4, R.id.row3col5, R.id.row3col6, R.id.row3col7),
            intArrayOf(R.id.row4col0, R.id.row4col1, R.id.row4col2, R.id.row4col3, R.id.row4col4, R.id.row4col5, R.id.row4col6, R.id.row4col7),
            intArrayOf(R.id.row5col0, R.id.row5col1, R.id.row5col2, R.id.row5col3, R.id.row5col4, R.id.row5col5, R.id.row5col6, R.id.row5col7),
            intArrayOf(R.id.row6col0, R.id.row6col1, R.id.row6col2, R.id.row6col3, R.id.row6col4, R.id.row6col5, R.id.row6col6, R.id.row6col7),
            intArrayOf(R.id.row7col0, R.id.row7col1, R.id.row7col2, R.id.row7col3, R.id.row7col4, R.id.row7col5, R.id.row7col6, R.id.row7col7)
    )

    fun initBoard(): Array<Array<String>> {
        return arrayOf(
            // pieces at the start of the game
            arrayOf("wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"),
            arrayOf("wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"),
            arrayOf("", "", "", "", "", "", "", ""),
            arrayOf("", "", "", "", "", "", "", ""),
            arrayOf("", "", "", "", "", "", "", ""),
            arrayOf("", "", "", "", "", "", "", ""),
            arrayOf("bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"),
            arrayOf("br", "bn", "bb", "bq", "bk", "bb", "bn", "br")
        )
    }

    fun toTheEnd(aryPGNs: Array<String>): Array<Array<String>> {
        val nLength = (aryPGNs.size - 1) * 2
        // the UI moves are half-moves
        return PGN2Board(nLength, aryPGNs)
    }

    fun PGN2Board(toMoveNumber: Int, aryPgns: Array<String>?): Array<Array<String>> {
        var board = initBoard()
        val nLoop = (toMoveNumber + 1) / 2

        if (aryPgns == null)
            return board

        for (i in 1..nLoop) {
            val movePGN = aryPgns[i - 1].trim()
            val movePGNMinusComments = splitCommentsMove(movePGN)[0]

            // \\s+ splits any number of whitespace, two spaces or one
            val white = movePGNMinusComments.split("\\s+".toRegex())[0]
            var black = ""

            try {
                black = movePGNMinusComments.split("\\s+".toRegex())[1]
            } catch (ex: ArrayIndexOutOfBoundsException) {
                Log.i(this.javaClass.toString(), "Game ends on white move.")
            } finally {
                board = transform("w", white, board)

                if (i < nLoop)
                    board = transform("b", black, board)
                else
                    // i == nLoop, last iteration
                    if (toMoveNumber % 2 == 0)
                        board = transform("b", black, board)
            }
        }

        return board
    }

    fun oneMove(toUIMoveNumber: Int, aryPGNs: Array<String>, board: Array<Array<String>>): Array<Array<String>> {
        var localboard = board

        try {
            val nPGNMoveNumber = (toUIMoveNumber + 1) / 2
            val movePGN = aryPGNs[nPGNMoveNumber - 1].trim()
            val movePGNMinusComments = splitCommentsMove(movePGN)[0]

            val movesPGN = movePGNMinusComments.split("\\s+".toRegex())
            val white = movesPGN[0]
            var black = ""

            try {
                black = movePGNMinusComments.split("\\s+".toRegex())[1]
            } catch (ex: ArrayIndexOutOfBoundsException) {
                Log.i(this.javaClass.toString(), "Game ends on white move.")
            } finally {
                // which half of the PGN move is this? the 1st or the 2nd half?
                if (toUIMoveNumber % 2 == 1)
                    localboard = transform("w", white, localboard)
                else
                    localboard = transform("b", black, localboard)
            }
        } catch (ex: ArrayIndexOutOfBoundsException) {
            // passed the last move in the game.
            Log.e(this.javaClass.toString(), ex.message)
        }

        return localboard
    }

    // Same function in CB.java.
    // TODO Consolidate
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

    private fun transform(wb: String, move: String, currentBoard: Array<Array<String>>): Array<Array<String>> {
        val emptySpaceRegex = "".toRegex()
        val localMove = move.replace("#", "")

        // they put a pound sign at the end of the game ... why???
        // for no good reason that i can think of
        if (localMove == "")
            return currentBoard

        val bCapture = localMove.contains("x")
        val destOnly = if (bCapture) localMove.split("x".toRegex())[1] else localMove
        var xOther: Piece.Type = if (move == "O-O" || move == "O-O-O") Piece.Type.None else Piece.Type.Pawn
        var pgnX = ""
        var pgnY = ""

        try {
            pgnX = destOnly.split(emptySpaceRegex)
                    .intersect(xAxis.split(emptySpaceRegex))
                    .dropWhile { it -> it.isEmpty() }
                    .last()

            pgnY = destOnly.split(emptySpaceRegex)
                    .intersect(yAxis.split(emptySpaceRegex))
                    .dropWhile { it -> it.isEmpty() }
                    .last()

            val xOtherString = nonPawns.split(emptySpaceRegex)
                    .intersect(move.split(emptySpaceRegex))
                    .dropWhile { it -> it.isEmpty() }
                    .last()

            xOther = Piece.Type.to(xOtherString)
        } catch (ex: Exception) { 
            Log.e(this.javaClass.toString(), ex.message)
        }

        // rank in PGN disambiguates, eg, two horses can move to the same square.
        // the rank will tell you which to move.
        val bNoRank = xAxis.split(emptySpaceRegex)
                .intersect(move.split(emptySpaceRegex))
                .dropWhile { it -> it.isEmpty() }
                .size < 2

        val hFileRank = if (bNoRank) ""
                        else move.replace(xOther.code, "")
                            .replace(pgnX + pgnY, "")
                            .replace("x", "")
                            .replace("+", "")
                            .first()
                            .toString()

        var xDest = 0
        var yDest = 0

        if (xOther == Piece.Type.None) {
            // could be castling
            if (move == "O-O" || move == "O-O-O") {
                // no code to test the legality of the move yet
                val nYaxis = if (wb == "w") 1 else 8

                if (move == "O-O") {
                    // king to g1 or g8
                    // rook to f1 or f8
                    currentBoard[nYaxis - 1][6] = currentBoard[nYaxis - 1][4]
                    currentBoard[nYaxis - 1][4] = ""

                    currentBoard[nYaxis - 1][5] = currentBoard[nYaxis - 1][7]
                    currentBoard[nYaxis - 1][7] = ""
                } else {
                    // king to c1 or c8
                    // rook to d1 or d8
                    currentBoard[nYaxis - 1][2] = currentBoard[nYaxis - 1][4]
                    currentBoard[nYaxis - 1][4] = ""

                    currentBoard[nYaxis - 1][3] = currentBoard[nYaxis - 1][0]
                    currentBoard[nYaxis - 1][0] = ""
                }
            } else {
                xDest = xAxis.indexOf(pgnX)
                yDest = yAxis.indexOf(pgnY)

                // find pawn that is going to xDest, yDest
                try {
                    val location = findPiece(hFileRank, wb, Piece.Type.Pawn, xDest, yDest, bCapture, currentBoard)

                    // so the new board doesn't have a pawn at xDest, ySource
                    // but does have a pawn at xDest, yDest
                    currentBoard[yDest][xDest] = currentBoard[location[1]][location[0]]
                    currentBoard[location[1]][location[0]] = ""

                    // for en passant
                    // either going from y-row 4 to 5, or from 3 to 2
                    val bXEnPassant = location[0] + 1 == xDest || location[0] - 1 == xDest
                    val bYEnPassant = location[1] == 4 && yDest == 5 || location[1] == 3 && yDest == 2
                    val bEnPassant = bXEnPassant && bYEnPassant
                    if (bCapture && bEnPassant) {
                        // the pawn just moved to column 3 or 4.
                        val yDestroy = if (wb === "w") 4 else 3
                        currentBoard[yDestroy][xDest] = ""
                    }
                } catch (ex: Exception) {
                    Log.e(this.javaClass.toString(), ex.message)
                }

            }
        } else {
            try {
                xDest = xAxis.indexOf(pgnX)
                yDest = yAxis.indexOf(pgnY)

                // promotion
                if (move.contains("=")) {
                    try {
                        val destPieceTypePart = move.split("=".toRegex())[1]

                        // poss types R, N, B, Q, K, P
                        val destType = destPieceTypePart.split(emptySpaceRegex)
                                .intersect(nonPawns.split(emptySpaceRegex))
                                .dropWhile { it -> it.isEmpty() }[0]
                                .toLowerCase().trim()

                        // pawn promotion, remove pawn, add "queen."
                        // "queen" goes on xDest, yDest
                        currentBoard[yDest][xDest] = wb + destType
                        val ySource = if (wb === "w") 6 else 1
                        currentBoard[ySource][xDest] = ""
                    } catch (e: Exception) {
                        Log.e(this.javaClass.toString(), "Trying to promote a pawn. " + e.message)
                    }

                } else {
                    val location = findPiece(hFileRank, wb, xOther, xDest, yDest, bCapture, currentBoard)

                    try {
                        // so the piece is at xSource, ySource
                        // and must move to xDest, yDest
                        currentBoard[yDest][xDest] = currentBoard[location[1]][location[0]]
                        currentBoard[location[1]][location[0]] = ""
                    } catch (ex: ArrayIndexOutOfBoundsException) {
                        Log.i(this.javaClass.toString(),  "Did not find piece. " + ex.message)
                    }

                }
            } catch (ex: Exception) {
                Log.e(this.javaClass.toString(), ex.message)
            }
        }

        return currentBoard
    }

    private fun findPiece(wb: String, type: Piece.Type, x: Int, y: Int, bCapture: Boolean, board: Array<Array<String>>): IntArray {
        var xSource = 0
        var ySource = 0
        var bFound: Boolean

        try {
            // x and y are the destination, but what's the source? find the pawn
            ySource = 0
            outerloop@ while (ySource < 8) {
                xSource = 0
                while (xSource < 8) {
                    val strPiece = board[ySource][xSource]

                    if (strPiece == "") {
                        xSource++
                        continue
                    }

                    val move = Piece(type, xSource, ySource, x, y)
                    move.xDestination = x
                    move.yDestination = y
                    move.setbDoesCapture(bCapture)
                    move.color = wb
                    move.board = board

                    bFound = strPiece == wb + type.code.toLowerCase() && move.isLegal

                    if (bFound)
                        break@outerloop
                    xSource++
                }
                ySource++
            }
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }

        return intArrayOf(xSource, ySource)
    }

    private fun findPiece(hRank: String, wb: String, type: Piece.Type, x: Int, y: Int, bCapture: Boolean, board: Array<Array<String>>): IntArray {
        val xSource = xAxis.indexOf(hRank)
        var ySource = 0
        var bFound: Boolean

        if (hRank == "") {
            return findPiece(wb, type, x, y, bCapture, board)
        } else {
            // is hRank really vRank?
            try {
                val vRank = Integer.parseInt(hRank)
                return findPiece(vRank, wb, type, x, y, bCapture, board)
            } catch (ex: Exception) {
                Log.e(this.javaClass.toString(), ex.message)
            }
        }

        try {
            // x and y are the destination, but what's the source? find the piece
            // this overload is for a spec'ed hRank, so we already know the xSource
            ySource = 0
            outerloop@while (ySource < 8) {
                val strPiece = board[ySource][xSource]

                if (strPiece == "") {
                    ySource++
                    continue
                }

                val move = Piece(type, xSource, ySource, x, y)
                move.setbDoesCapture(bCapture)
                move.color = wb
                move.board = board

                bFound = strPiece == wb + type.code.toLowerCase() && move.isLegal

                if (bFound)
                    break@outerloop
                ySource++
            }
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }

        return intArrayOf(xSource, ySource)
    }

    private fun findPiece(vRank: Int?, wb: String, type: Piece.Type, x: Int, y: Int, bCapture: Boolean, board: Array<Array<String>>): IntArray {
        var xSource = 0
        val ySource = vRank!! - 1
        var bFound = false

        try {
            // x and y are the destination, but what's the source? find the piece
            // this overload is for a spec'ed vRank, so we already know the ySource
            xSource = 0
            outerloop@while (xSource < 8) {
                val strPiece = board[ySource][xSource]

                if (strPiece == "") {
                    xSource++
                    continue
                }

                val move = Piece(type, xSource, ySource, x, y)
                move.setbDoesCapture(bCapture)
                move.color = wb
                move.board = board

                bFound = strPiece == wb + type.code.toLowerCase() && move.isLegal

                if (bFound)
                    break@outerloop
                xSource++
            }
        } catch (ex: Exception) {
            Log.e(this.javaClass.toString(), ex.message)
        }

        return intArrayOf(xSource, ySource)
    }
}
