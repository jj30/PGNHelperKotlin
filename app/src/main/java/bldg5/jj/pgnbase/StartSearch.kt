package bldg5.jj.pgnbase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import kotlinx.android.synthetic.main.activity_start_search.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select

class StartSearch: Activity() {
    // Access property for Context
    val Context.database: PGNDBHelper
        get() = bldg5.jj.pgnbase.PGNDBHelper.getInstance(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_search)

        // bind text watcher to the white EditText
        editTextWhite.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val str = s.toString()
                if (str.length > 1) {
                    popAutoComplete("w", str, "")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = s.toString()
                if (str.length > 1) {
                    popAutoComplete("w", str, "")
                }
            }
        })

        // bind text watcher to the black EditText
        editTextBlack.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val str = s.toString()
                if (str.length > 1) {
                    popAutoComplete("b", editTextWhite.getText().toString(), str)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = s.toString()
                if (str.length > 1) {
                    popAutoComplete("b", editTextWhite.getText().toString(), str)
                }
            }
        })

        btnFindGames.setOnClickListener {
            navToResults()
        }
    }

    private fun popAutoComplete(wb: String, w_player: String, b_player: String) {
        /// TODO read db from USB stick
        /* val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = manager.getDeviceList()
        val device = deviceList.get("deviceName")*/

        // playersList.adapter
        var listNames: List<String> = ArrayList()
        val selectColumn = if (wb == "w") "White" else "Black"

        database.use {
            listNames = select("Games", selectColumn)
                    .distinct()
                    .whereArgs("White LIKE '%$w_player%' AND Black LIKE '%$b_player%'")
                    .limit(10)
                    .exec { parseList(classParser()) }
        }

        val textView = (if (wb === "w") editTextWhite else editTextBlack) as AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listNames)
        textView.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun navToResults() {
        val strWhite = editTextWhite.getText().toString().trim()
        val strBlack = editTextBlack.getText().toString().trim()
        val nIC = if (chkIgnoreColor.isChecked) 1 else 0

        val params = Bundle()
        params.putString("White", strWhite)
        params.putString("Black", strBlack)
        params.putInt("IgnoreColor", nIC)

        val navMain = Intent(this.applicationContext, ListGames::class.java)
        navMain.putExtras(params)

        this.startActivity(navMain)
    }
}