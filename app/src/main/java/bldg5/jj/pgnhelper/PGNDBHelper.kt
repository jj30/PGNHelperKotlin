package bldg5.jj.pgnhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class PGNDBHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PGNSDB", null, 1) {
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("Games", true)
    }
}