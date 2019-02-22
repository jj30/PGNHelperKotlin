package bldg5.jj.pgnbase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

class PGNDBHelper(val ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PGNSDB", null, 1) {
    companion object {
        private var instance: PGNDBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): PGNDBHelper {
            if (instance == null) {
                instance = PGNDBHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("Games", true,
                "File" to TEXT,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "Event" to TEXT,
                "Site" to TEXT,
                "Date" to TEXT,
                "Round" to TEXT,
                "White" to TEXT,
                "Black" to TEXT,
                "Result" to TEXT,
                "WhiteELO" to TEXT,
                "BlackELO" to TEXT,
                "ECO" to TEXT,
                "PGN" to TEXT)

        //region Default Games
        db.insert("Games",
                "File" to "Anand.pgn",
                "id" to 11741,
                "Event" to "Mtel Masters",
                "Site" to "Sofia BUL",
                "Date" to "2005.05.12",
                "Round" to "1",
                "White" to "Anand, Viswanathan",
                "Black" to "Topalov, Veselin",
                "Result" to "1/2-1/2",
                "WhiteELO" to "2785",
                "BlackELO" to "2778",
                "ECO" to "B80",
                "PGN" to "1.e4 c5 2.Nf3 d6 3.d4 cxd4 4.Nxd4 Nf6 5.Nc3 a6 6.Be3 e6 7.f3 b5 8.g4 h6 9.Qd2 b4 10.Na4 Nbd7 11.O-O-O Ne5 12.b3 Bd7 13.Nb2 d5 14.Bf4 Nxf3 15.Nxf3 Nxe4 16.Qd4 f6 17.Bd3 Bc5 18.Bxe4 Bxd4 19.Bg6+ Kf8 20.Rxd4 a5 21.Re1 Be8 22.Nh4 e5 23.Rd2 a4 24.bxa4 Kg8 25.Bg3 d4 26.Rd3 h5 27.Bxe8 Qxe8 28.g5 Rc8 29.g6 Rh6 30.Rxd4 Rxg6 31.Nxg6 Qxg6 32.Rd2 Rc3 33.Red1 Kh7 34.Kb1 Qf5 35.Be1 Ra3 36.Rd6 Rh3 37.a5 Rxh2 38.Rc1 Qe4 39.a6 Qa8 40.Bxb4 h4 41.Bc5 h3 42.Nd3 Rd2 43.Rb6 h2 44.Nf2 Qd5 45.Be3 Re2 46.Rb3 f5 47.a7 Rxe3 48.Rxe3 Qb7+ 49.Rb3 Qxa7 50.Nh1 f4 51.c4 e4 52.c5 e3 53.c6 e2 54.c7 Qxc7 55.Rxc7 e1=Q+ 56.Rc1 Qe4+ 57.Ka1 Qd4+ 58.Kb1 Qe4+59.Ka1 Qd4+ 60.Kb1 Qe4+  1/2-1/2"
        )

        db.insert("Games",
                "File" to "Anderssen.pgn",
                "id" to 12490,
                "Event" to "London 'Immortal game'",
                "Site" to "London",
                "Date" to "1851.??.??",
                "Round" to "?",
                "White" to "Anderssen, Adolf",
                "Black" to "Kieseritzky, Lionel",
                "Result" to "1-0",
                "WhiteELO" to "",
                "BlackELO" to "",
                "ECO" to "C33",
                "PGN" to "1.e4 e5 2.f4 exf4 3.Bc4 Qh4+ 4.Kf1 b5 5.Bxb5 Nf6 6.Nf3 Qh6 7.d3 Nh5 8.Nh4 Qg5 9.Nf5 c6 10.g4 Nf6 11.Rg1 cxb5 12.h4 Qg6 13.h5 Qg5 14.Qf3 Ng8 15.Bxf4 Qf6 16.Nc3 Bc5 17.Nd5 Qxb2 18.Bd6 Bxg1 19.e5 Qxa1+ 20.Ke2 Na6 21.Nxg7+ Kd8 22.Qf6+ Nxf6 23.Be7+  1-0"
        )

        db.insert("Games",
                "File" to "Anderssen.pgn",
                "id" to 12553,
                "Event" to "Berlin 'Evergreen'",
                "Site" to "Berlin",
                "Date" to "1852.??.??",
                "Round" to "?",
                "White" to "Anderssen, Adolf",
                "Black" to "Dufresne, Jean",
                "Result" to "1-0",
                "WhiteELO" to "",
                "BlackELO" to "",
                "ECO" to "C52",
                "PGN" to "1.e4 e5 2.Nf3 Nc6 3.Bc4 Bc5 4.b4 Bxb4 5.c3 Ba5 6.d4 exd4 7.O-O d3 8.Qb3 Qf6 9.e5 Qg6 10.Re1 Nge7 11.Ba3 b5 12.Qxb5 Rb8 13.Qa4 Bb6 14.Nbd2 Bb7 15.Ne4 Qf5 16.Bxd3 Qh5 17.Nf6+ gxf6 18.exf6 Rg8 19.Rad1 Qxf3 20.Rxe7+ Nxe7 21.Qxd7+ Kxd7 22.Bf5+ Ke8 23.Bd7+ Kf8 24.Bxe7+  1-0"
        )

        db.insert("Games",
                "File" to "DeLaBourdonnais.pgn",
                "id" to 53148,
                "Event" to "London m4",
                "Site" to "London",
                "Date" to "1834.??.??",
                "Round" to "62",
                "White" to "McDonnell, Alexander",
                "Black" to "De Labourdonnais, Louis",
                "Result" to "0-1",
                "WhiteELO" to "",
                "BlackELO" to "",
                "ECO" to "B32",
                "PGN" to "1.e4 c5 2.Nf3 Nc6 3.d4 cxd4 4.Nxd4 e5 5.Nxc6 bxc6 6.Bc4 Nf6 7.Bg5 Be7 8.Qe2 d5 9.Bxf6 Bxf6 10.Bb3 O-O 11.O-O a5 12.exd5 cxd5 13.Rd1 d4 14.c4 Qb6 15.Bc2 Bb7 16.Nd2 Rae8 17.Ne4 Bd8 18.c5 Qc6 19.f3 Be7 20.Rac1 f5 21.Qc4+ Kh8 22.Ba4 Qh6 23.Bxe8 fxe4 24.c6 exf3 25.Rc2 Qe3+ 26.Kh1 Bc8 27.Bd7 f2 28.Rf1 d3 29.Rc3 Bxd7 30.cxd7 e4 31.Qc8 Bd8 32.Qc4 Qe1 33.Rc1 d2 34.Qc5 Rg8 35.Rd1 e3 36.Qc3 Qxd1 37.Rxd1 e2  0-1"
        )

        db.insert("Games",
                "File" to "Kasparov.pgn",
                "id" to 116029,
                "Event" to "Philadelphia m",
                "Site" to "Philadelphia",
                "Date" to "1996.??.??",
                "Round" to "1",
                "White" to "Comp Deep Blue",
                "Black" to "Kasparov, Gary",
                "Result" to "1-0",
                "WhiteELO" to "",
                "BlackELO" to "2795",
                "ECO" to "B22",
                "PGN" to "1.e4 c5 2.c3 d5 3.exd5 Qxd5 4.d4 Nf6 5.Nf3 Bg4 6.Be2 e6 7.h3 Bh5 8.O-O Nc6 9.Be3 cxd4 10.cxd4 Bb4 11.a3 Ba5 12.Nc3 Qd6 13.Nb5 Qe7 14.Ne5 Bxe2 15.Qxe2 O-O 16.Rac1 Rac8 17.Bg5 Bb6 18.Bxf6 gxf6 19.Nc4 Rfd8 20.Nxb6 axb6 21.Rfd1 f5 22.Qe3 Qf6 23.d5 Rxd5 24.Rxd5 exd5 25.b3 Kh8 26.Qxb6 Rg8 27.Qc5 d4 28.Nd6 f4 29.Nxb7 Ne5 30.Qd5 f3 31.g3 Nd3 32.Rc7 Re8 33.Nd6 Re1+ 34.Kh2 Nxf2 35.Nxf7+ Kg7 36.Ng5+ Kh6 37.Rxh7+  1-0"
        )

        db.insert("Games",
                "File" to "Kasparov.pgn",
                "id" to 116261,
                "Event" to "Hoogovens"	,
                "Site" to "Wijk aan Zee",
                "Date" to "1999.01.20",
                "Round" to "4",
                "White" to "Kasparov, Gary",
                "Black" to "Topalov, Veselin",
                "Result" to "1-0",
                "WhiteELO" to "2812",
                "BlackELO" to "2700",
                "ECO" to "B07",
                "PGN" to "1.e4 d6 2.d4 Nf6 3.Nc3 g6 4.Be3 Bg7 5.Qd2 c6 6.f3 b5 7.Nge2 Nbd7 8.Bh6 Bxh6 9.Qxh6 Bb7 10.a3 e5 11.O-O-O Qe7 12.Kb1 a6 13.Nc1 O-O-O 14.Nb3 exd4 15.Rxd4 c5 16.Rd1 Nb6 17.g3 Kb8 18.Na5 Ba8 19.Bh3 d5 20.Qf4+ Ka7 21.Rhe1 d4 22.Nd5 Nbxd5 23.exd5 Qd6 24.Rxd4 cxd4 25.Re7+ Kb6 26.Qxd4+ Kxa5 27.b4+ Ka4 28.Qc3 Qxd5 29.Ra7 Bb7 30.Rxb7 Qc4 31.Qxf6 Kxa3 32.Qxa6+ Kxb4 33.c3+ Kxc3 34.Qa1+ Kd2 35.Qb2+ Kd1 36.Bf1 Rd2 37.Rd7 Rxd7 38.Bxc4 bxc4 39.Qxh8 Rd3 40.Qa8 c3 41.Qa4+ Ke1 42.f4 f5 43.Kc1 Rd2 44.Qa7  1-0"
        )

        db.insert("Games",
                "File" to "Marshall.pgn",
                "id" to 153837,
                "Event" to "DSB-18.Kongress",
                "Site" to "Breslau",
                "Date" to "1912.??.??",
                "Round" to "6",
                "White" to "Levitsky, Stepan M",
                "Black" to "Marshall, Frank James",
                "Result" to "0-1",
                "WhiteELO" to "",
                "BlackELO" to "",
                "ECO" to "B40",
                "PGN" to "1.e4 e6 2.d4 d5 3.Nc3 c5 4.Nf3 Nc6 5.exd5 exd5 6.Be2 Nf6 7.O-O Be7 8.Bg5 O-O 9.dxc5 Be6 10.Nd4 Bxc5 11.Nxe6 fxe6 12.Bg4 Qd6 13.Bh3 Rae8 14.Qd2 Bb4 15.Bxf6 Rxf6 16.Rad1 Qc5 17.Qe2 Bxc3 18.bxc3 Qxc3 19.Rxd5 Nd4 20.Qh5 Ref8 21.Re5 Rh6 22.Qg5 Rxh3 23.Rc5 Qg3  0-1"
        )

        db.insert("Games",
                "File" to "Morphy.pgn",
                "id" to 161967,
                "Event" to "Paris it",
                "Site" to "Paris",
                "Date" to "1858.??.??",
                "Round" to "?",
                "White" to "Morphy, Paul",
                "Black" to "Duke Karl Count Isouard",
                "Result" to "1-0",
                "WhiteELO" to "",
                "BlackELO" to "",
                "ECO" to "C41",
                "PGN" to "1.e4 e5 2.Nf3 d6 3.d4 Bg4 4.dxe5 Bxf3 5.Qxf3 dxe5 6.Bc4 Nf6 7.Qb3 Qe7 8.Nc3 c6 9.Bg5 b5 10.Nxb5 cxb5 11.Bxb5+ Nbd7 12.O-O-O Rd8 13.Rxd7 Rxd7 14.Rd1 Qe6 15.Bxd7+ Nxd7 16.Qb8+ Nxb8 17.Rd8+  1-0"
        )

        db.insert("Games",
                "File" to "",
                "id" to 161968,
                "Event" to "Hastings",
                "Site" to "Hastings ENG"	,
                "Date" to "1922.09.14",
                "Round" to "5",
                "White" to 	"Alexander Alekhine",
                "Black" to "Efim Bogoljubov",
                "Result" to "1-0",
                "WhiteELO" to "?",
                "BlackELO" to "?",
                "ECO" to "D63",
                "PGN" to "1. d4 d5 2. Nf3 Nf6 3. c4 e6 4. Nc3 Nbd7 5. Bg5 Be7 6. e3 O-O 7. Rc1 a6 8. c5 c6 9. b4 Ne4 10. Bf4 g5 11. Bg3 Nxg3 12. hxg3 f5 13. g4 fxg4 14. Ne5 Nxe5 15. dxe5 Qc7 16. Qd4 Rf5 17. Bd3 Qxe5 18. Qxe5 Rxe5 19. Rxh7 Bf6 20. Kd2 Bg7 21. Rch1 Rb8 22. Na4 Rf5 23. Bxf5 exf5 24. R7h5 Be6 25. Rxg5 d4 26. exd4 Rd8 27. Kc3 Kf8 28. Rd1 Kf7 29. Nb6 Rh8 30. Rxg7+ Kxg7 31. a4 Rh2 32. Rg1 f4 33. d5 cxd5 34. Kd4 g3 35. f3 Kf6 36. b5 axb5 37. axb5 Rh5 38. c6 bxc6 39. bxc6 Ke7 40. c7 Kd6 41. c8=Q Bxc8 42. Nxc8+ Kd7 43. Rc1 Rh2 44. Rc2 Ke6 45. Re2+ Kf6 46. Nb6 Rh1 47. Nxd5+ Kg5 48. Ke5 Rh8 49. Nxf4 Ra8 50. Ne6+ Kh4 51. Re1 Rh8 52. Rh1# 1-0"
        )

        db.insert("Games",
                "File" to "",
                "id" to 161969,
                "Event" to "Third Rosenwald Trophy",
                "Site" to "New York, NY USA",
                "Date" to "1956.10.17",
                "Round" to "8",
                "White" to	"Donald Byrne",
                "Black" to "Robert James Fischer",
                "Result" to "0-1",
                "WhiteELO" to "?",
                "BlackELO" to "?",
                "ECO" to "D92",
                "PGN" to "1. Nf3 Nf6 2. c4 g6 3. Nc3 Bg7 4. d4 O-O 5. Bf4 d5 6. Qb3 dxc4 7. Qxc4 c6 8. e4 Nbd7 9. Rd1 Nb6 10. Qc5 Bg4 11. Bg5  Na4  12. Qa3 Nxc3 13. bxc3 Nxe4 14. Bxe7 Qb6 15. Bc4 Nxc3 16. Bc5 Rfe8+ 17. Kf1 Be6 18. Bxb6 Bxc4+ 19. Kg1 Ne2+ 20. Kf1 Nxd4+  21. Kg1 Ne2+ 22. Kf1 Nc3+ 23. Kg1 axb6 24. Qb4 Ra4 25. Qxb6 Nxd1 26. h3 Rxa2 27. Kh2 Nxf2 28. Re1 Rxe1 29. Qd8+ Bf8 30. Nxe1 Bd5 31. Nf3 Ne4 32. Qb8 b5 33. h4 h5 34. Ne5 Kg7 35. Kg1 Bc5+ 36. Kf1 Ng3+ 37. Ke1 Bb4+ 38. Kd1 Bb3+ 39. Kc1 Ne2+ 40. Kb1 Nc3+ 41. Kc1 Rc2# 0-1"
        )
        //endregion
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("Games", true)
    }

    fun addFromUSB(pgn: String) {
        val cr = "\n"
        val eof = "\r"
        val crRegex = cr.toRegex()
        val eofRegex = eof.toRegex()
        val game = Game()
        var pgnBuilder = ""

        for (line in pgn.split(crRegex)) {
            val key = Game.PGNFileProperty.toFromLine(line)
            val value = line
                    .replace(key.code, "")
                    .replace("\"", "")
                    .replace("[", "")
                    .replace("]", "")
                    .trim()

            Log.i("JJ", key.code + " " + value)

            when (key) {
                Game.PGNFileProperty.Event -> game.Event = value
                Game.PGNFileProperty.Site -> game.Site = value
                Game.PGNFileProperty.EventDate -> game.Date = value
                Game.PGNFileProperty.Round -> game.Round = value
                Game.PGNFileProperty.Result -> game.Result = value
                Game.PGNFileProperty.White -> game.White = value
                Game.PGNFileProperty.Black -> game.Black = value
                Game.PGNFileProperty.ECO -> game.ECO = value
                Game.PGNFileProperty.WhiteElo -> game.WhiteELO = value
                Game.PGNFileProperty.BlackElo -> game.BlackELO = value
                Game.PGNFileProperty.None -> {
                    pgnBuilder += value.split(eofRegex)[0]
                }
                Game.PGNFileProperty.Other -> { }
            }
        }

        pgnBuilder = pgnBuilder.trim()
        game.PGN = pgnBuilder


        getInstance(ctx).writableDatabase.insert("Games",
                "File" to game.file,
                "Event" to game.Event,
                "Site" to game.Site,
                "Date" to game.Date,
                "Round" to game.Round,
                "White" to game.White,
                "Black" to game.Black,
                "Result" to game.Result,
                "WhiteELO" to game.WhiteELO,
                "BlackELO" to game.BlackELO,
                "ECO" to game.ECO,
                "PGN" to game.PGN
        )
    }
}