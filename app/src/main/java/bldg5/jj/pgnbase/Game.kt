package bldg5.jj.pgnbase

data class Game (
    var file: String = "",
    var id: Int = 0,
    var Event: String = "",
    var Site: String = "",
    var Date: String = "",
    var Round: String = "",
    var White: String = "",
    var Black: String = "",
    var Result: String = "",
    var WhiteELO: String = "",
    var BlackELO: String = "",
    var ECO: String = "",
    var PGN: String = ""
) {
    enum class PGNFileProperty(val code: String) {
        None("None"),
        Other("Other"),
        Event("Event"),
        Site("Site"),
        EventDate("EventDate"),
        Round("Round"),
        Result("Result"),
        White("White"),
        Black("Black"),
        ECO("ECO"),
        WhiteElo("WhiteElo"),
        BlackElo("BlackElo");

        companion object {
            fun to(getString: String): PGNFileProperty {
                var returnPGNFileProperty: PGNFileProperty = Event

                try {
                    returnPGNFileProperty = PGNFileProperty.values().first { it.code.toUpperCase() == getString.toUpperCase() }
                } catch (ex: NoSuchElementException) {
                    // invalid string
                }

                return returnPGNFileProperty
            }

            fun toFromLine(line: String): PGNFileProperty {
                with (line) {
                    if (contains(PGNFileProperty.EventDate.code))
                        return EventDate

                    if (contains(PGNFileProperty.Event.code))
                        return Event

                    if (contains(PGNFileProperty.Site.code))
                        return Site

                    if (contains(PGNFileProperty.Round.code))
                        return Round

                    if (contains(PGNFileProperty.Result.code))
                        return Result

                    if (contains(PGNFileProperty.WhiteElo.code))
                        return WhiteElo

                    if (contains(PGNFileProperty.BlackElo.code))
                        return BlackElo

                    if (contains(PGNFileProperty.White.code))
                        return White

                    if (contains(PGNFileProperty.Black.code))
                        return Black

                    if (contains(PGNFileProperty.ECO.code))
                        return ECO

                    if (contains("[") && line.contains("]"))
                        return Other

                }

                return None
            }
        }
    }

    fun description(): String {
        var builder = this.Event
        builder += if (!builder.isEmpty()) ", " + this.Site else ""
        builder += if (!builder.isEmpty()) ", " + this.Date else ""
        builder = builder.replace(", , ", ", ")

        if (builder.length > 500)
            builder = builder.substring(0, Math.min(builder.length, 500))

        if (builder.length > 2)
            if (builder.substring( builder.length - 2, builder.length) == ", ")
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