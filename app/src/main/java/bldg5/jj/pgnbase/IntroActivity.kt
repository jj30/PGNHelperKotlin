package bldg5.jj.pgnbase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
