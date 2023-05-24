package com.kiselikovgame.radiokp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kiselikovgame.radiokp.fragments.MainFragment
import com.kiselikovgame.radiokp.fragments.WebFragment
import com.kiselikovgame.radiokp.services.PlayService

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            val fr = MainFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_place, fr)
                .commitAllowingStateLoss()
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ActivityResultCallback {
                return@ActivityResultCallback
            }
        )

        //Request permission POST_NOTIFICATION
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    //show url in web browser
    fun showArticle(url: String) {

        val fr = WebFragment()
        fr.arguments = Bundle().apply {
            putString("url", url)
        }

        val place2 = findViewById<FrameLayout>(R.id.fragment_place2)
        if (place2 != null) {
            place2.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_place2, fr)
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_place, fr)
                .addToBackStack("url")
                .commitAllowingStateLoss()
        }
    }

    //play music on service
    fun playMusic(url: String, title: String) {

        val i = Intent(this, PlayService::class.java).apply {
            putExtra("title", title)
            putExtra("mp3", url)
        }

        startService(i)
    }
}