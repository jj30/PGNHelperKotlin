package bldg5.jj.pgnbase

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbAccessory
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start_search.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select
import com.github.mjdev.libaums.UsbMassStorageDevice
import com.github.mjdev.libaums.fs.UsbFileInputStream
import java.nio.charset.Charset


class StartSearch: Activity() {
    // Access property for Context
    val Context.database: PGNDBHelper
        get() = bldg5.jj.pgnbase.PGNDBHelper.getInstance(applicationContext)

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

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

        // register the broadcast receiver
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(usbReceiver, filter)

        for (dev in manager.deviceList)
        {
            // now ask for permission
            manager.requestPermission(dev.value, permissionIntent)
        }
    }

    // this is the broadcast receiver registered above.
    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        var numGamesAdded = 0L
                        val deviceList = UsbMassStorageDevice.getMassStorageDevices(this@StartSearch.applicationContext)

                        for (deviceNext in deviceList) {
                            // before interacting with a device you need to call init()!
                            deviceNext.init()

                            // Only uses the first partition on the device
                            val currentFs = deviceNext.getPartitions().get(0).getFileSystem()
                            val root = currentFs.rootDirectory

                            val files = root.listFiles()
                            for (file in files) {
                                if (file.name.toLowerCase().contains(".pgn".toRegex())) {
                                    val fileSource = UsbFileInputStream(file)
                                    val buffer = ByteArray(currentFs.chunkSize)
                                    fileSource.read(buffer)

                                    val pgn = buffer.toString(Charset.defaultCharset())

                                    numGamesAdded += this@StartSearch.database.addFromUSB(pgn)
                                }
                            }
                        }

                        Toast.makeText(context,"$numGamesAdded games were added to your local database.", Toast.LENGTH_LONG).show()
                    } else {
                        Log.d(javaClass.name + ".usbReceiver", "permission denied for device $device")
                    }
                }
            }
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