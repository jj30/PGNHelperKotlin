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
                if (line.contains(PGNFileProperty.EventDate.code))
                    return EventDate

                if (line.contains(PGNFileProperty.Event.code))
                    return Event

                if (line.contains(PGNFileProperty.Site.code))
                    return Site

                if (line.contains(PGNFileProperty.Round.code))
                    return Round

                if (line.contains(PGNFileProperty.Result.code))
                    return Result

                if (line.contains(PGNFileProperty.WhiteElo.code))
                    return WhiteElo

                if (line.contains(PGNFileProperty.BlackElo.code))
                    return BlackElo

                if (line.contains(PGNFileProperty.White.code))
                    return White

                if (line.contains(PGNFileProperty.Black.code))
                    return Black

                if (line.contains(PGNFileProperty.ECO.code))
                    return ECO

                if (line.contains("[") && line.contains("]"))
                    return Other

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