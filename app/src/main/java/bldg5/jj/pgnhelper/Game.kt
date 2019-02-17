package bldg5.jj.pgnhelper

data class Game (
    val file: String = "",
    val id: Int = 0,
    val Event: String = "",
    val Site: String = "",
    val Date: String = "",
    val Round: String = "",
    val White: String = "",
    val Black: String = "",
    val Result: String = "",
    val WhiteELO: String = "",
    val BlackELO: String = "",
    val ECO: String = "",
    var PGN: String = ""
) {
    fun description(): String {
        var builder = this.Event
        builder += if (!builder.isEmpty()) ", " + this.Site else ""
        builder += if (!builder.isEmpty()) ", " + this.Date else ""
        builder = builder.replace(", , ", ", ")

        if (builder.length > 500)
            builder = builder.substring(0, Math.min(builder.length, 500))
        else
            builder = builder.substring(0, builder.length - 2)

        return builder
    }

    fun fullDescription(): String {
        var builder = this.White
        builder += if (this.WhiteELO.isEmpty()) " " else " (" + this.WhiteELO + ")"
        builder += " vs " + this.Black
        builder += if (this.BlackELO.isEmpty()) " " else " (" + this.BlackELO + ")\n"
        builder += description()

        return builder
    }
}