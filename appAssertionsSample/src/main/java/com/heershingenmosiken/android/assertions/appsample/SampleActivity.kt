package com.heershingenmosiken.android.assertions.appsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_sample.*
import com.heershingenmosiken.assertions.Assertions

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            Assertions.fail(IllegalStateException("Assertion occurred"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_sample, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_fail -> true.also { Assertions.fail(IllegalStateException("Fail")) }
            R.id.action_fail_silently -> true.also { Assertions.failSilently(IllegalStateException("Fail")) }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
