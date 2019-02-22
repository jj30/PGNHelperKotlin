package bldg5.jj.pgnbase

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_intro.*


class IntroActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btnOK.setOnClickListener {
            val navToSearch = Intent(this.applicationContext, StartSearch::class.java)
            startActivity(navToSearch)
        }
    }
}
