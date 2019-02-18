package bldg5.jj.pgnbase.common


class Piece {
    var x: Int = 0
    private var y: Int = 0
    var xDestination: Int = 0
    var yDestination: Int = 0
    private lateinit var type: Type // poss types R, N, B, Q, K, P

    enum class Type(val code: String) {
        None(""),
        Rook("R"),
        Knight("N"),
        Bishop("B"),
        Queen("Q"),
        King("K"),
        Pawn("P");

        companion object {
            fun to(getString: String): Type {
                var returnType: Type = Type.Pawn

                try {
                    returnType = Type.values().first { it.code.toUpperCase() == getString.toUpperCase() }
                } catch (ex: NoSuchElementException) {
                    // invalid string
                }

                return returnType
            }
        }
    }

    var color: String? = null // poss types w, b
    var doesCapture = false
        private set
    lateinit var board : Array<Array<String>>

    val location: IntArray
        get() = intArrayOf(this.x, this.y)

    val destination: IntArray
        get() = intArrayOf(this.x, this.y)

    // slope is infinity or 0
    // knight never moves to the same color
    // slope is 2 or .5
    // Divide by zero error.
    // max displacement in either horizontal or vertical is 3.
    // bishop stays on his color
    // bishops don't move up and down like this. Divide by zero error.
    // queen can move like a rook or a bishop
    // King ... one space in any direction
    // King two spaces to either g8 or g1
    // pawn. minus en passant.
    // a pawn can move horizontally one space either to or fro (if capturing)
    val isLegal: Boolean
        get() {
            var bResult = false
            var bCorrectSlope = false
            var bCorrectDisplacement = false
            var bClearPath = false

            when (this.type.code) {
                Type.Rook.code -> {
                    bClearPath = checkPath(x, y, xDestination, yDestination)
                    bResult = (xDestination == x || yDestination == y) && bClearPath
                }
                Type.Knight.code -> {
                    bResult = Board.squareColor(this.x, this.y) != Board.squareColor(this.xDestination, this.yDestination)

                    try {
                        bCorrectSlope = Math.abs((yDestination - y) / (xDestination - x)) == 2 || Math.abs((yDestination - y) * 1.0 / (xDestination - x)) == 0.5
                    } catch (ex: ArithmeticException) {
                        bCorrectSlope = false
                    }

                    bCorrectDisplacement = Math.abs(yDestination - y) <= 3 && Math.abs(xDestination - x) <= 3

                    bResult = bResult && bCorrectSlope && bCorrectDisplacement
                }
                Type.Bishop.code -> {
                    bResult = Board.squareColor(this.x, this.y) == Board.squareColor(this.xDestination, this.yDestination)

                    try {
                        bCorrectSlope = Math.abs((yDestination - y) / (xDestination - x)) == 1
                    } catch (ex: ArithmeticException) {
                        bCorrectSlope = false
                    }

                    bResult = bResult && bCorrectSlope
                }
                Type.Queen.code -> {
                    val bishop = Piece(Type.Bishop)
                    val rook = Piece(Type.Rook)

                    bishop.board = this.board
                    bishop.setbDoesCapture(this.doesCapture)
                    bishop.setLocation(this.x, this.y)
                    bishop.setDestination(this.xDestination, this.yDestination)

                    rook.board = this.board
                    rook.setbDoesCapture(this.doesCapture)
                    rook.setLocation(this.x, this.y)
                    rook.setDestination(this.xDestination, this.yDestination)

                    bResult = bishop.isLegal || rook.isLegal
                }
                Type.King.code -> {
                    // King ... one space in any direction
                    // King two spaces to either g8 or g1
                    val bCastle = xDestination == 6 && (yDestination == 0 || yDestination == 7)
                    bResult = yDestination == y + 1 ||
                            yDestination == y - 1 ||
                            xDestination == x + 1 ||
                            xDestination == x - 1

                    bResult = !bResult && bCastle || bResult
                }
                Type.Pawn.code -> {
                    val bHorizontalOk = doesCapture && (xDestination - 1 == x || xDestination == x - 1) || !doesCapture && x == xDestination

                    bClearPath = checkPath(x, y, xDestination, yDestination)

                    if (this.color === "w") {
                        bResult = yDestination == y + 1 && bHorizontalOk || x == xDestination && y == 1 && yDestination == 3
                    } else {
                        bResult = yDestination == y - 1 && bHorizontalOk || x == xDestination && y == 6 && yDestination == 4
                    }

                    bResult = bResult && bClearPath
                }
            }

            return bResult
        }

    constructor(type: Type) {
        this.type = type
    }

    constructor(type: Type, x: Int, y: Int, xDestination: Int, yDestination: Int) {
        this.type = type
        setLocation(x, y)
        setDestination(xDestination, yDestination)
    }

    fun getY(): Int {
        return this.x
    }

    fun setY(yLocation: Int) {
        this.x = yLocation
    }

    fun setLocation(xLocation: Int, yLocation: Int) {
        this.x = xLocation
        this.y = yLocation
    }

    fun setDestination(xLocation: Int, yLocation: Int) {
        this.xDestination = xLocation
        this.yDestination = yLocation
    }

    fun setbDoesCapture(bDoesCapture: Boolean) {
        this.doesCapture = bDoesCapture
    }

    private fun checkPath(xF: Int, yF: Int, x: Int, y: Int): Boolean {
        // checking the path is bidirectional
        val strOriginalPiece = board[yF][xF]
        var bClear = true
        var bDone = false
        var xFro = if (xF < x) xF else x
        val xTo = if (xF < x) x else xF
        var yFro = if (yF < y) yF else y
        val yTo = if (yF < y) y else yF

        while (bClear && !bDone) {
            if (yFro != yTo)
                yFro++

            if (xFro != xTo)
                xFro++

            bDone = yFro == yTo && xFro == xTo

            if (bDone) {
                return if (this.doesCapture) {
                    this.doesCapture
                } else {
                    // in the bidirectional sense, sometimes the "last" piece is the original piece
                    board[yFro][xFro] == "" || board[yFro][xFro] == strOriginalPiece
                }
            } else {
                bClear = bClear && board[yFro][xFro] == ""
            }
        }

        return bClear
    }
}
