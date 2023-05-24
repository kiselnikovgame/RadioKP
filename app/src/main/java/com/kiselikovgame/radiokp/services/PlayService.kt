package com.kiselikovgame.radiokp.services

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kiselikovgame.radiokp.R

class PlayService : Service() {

    private lateinit var player: MediaPlayer
    private lateinit var builder : NotificationCompat.Builder
    private lateinit var manager : NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        manager = NotificationManagerCompat.from(this)
        player = MediaPlayer().apply {
            setOnCompletionListener {
                stopSelf()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            "stop" -> {
                stopSelf()
                return START_NOT_STICKY
            }
            "pause" -> {
                if (player.isPlaying) {
                    player.pause()
                    builder.setContentTitle("Приостановлено")
                }
                else {
                    player.start()
                    builder.setContentTitle("Воспроизводится")
                }

                notify(1, builder)
                return START_NOT_STICKY
            }
        }

        //get into about mp3 and title podcast
        val url = intent?.getStringExtra("mp3")
        val title = intent?.getStringExtra("title")

        //reset player and load new podcast
        player.reset()
        player.setDataSource(this, Uri.parse(url))
        player.setOnPreparedListener { p -> p.start() }
        player.prepareAsync()

        //show toast
        Toast.makeText(this, title, Toast.LENGTH_LONG).show()


        val iPause = Intent(this, PlayService::class.java).setAction("pause")
        val piPause = PendingIntent.getService(this, 0, iPause,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val iStop = Intent(this, PlayService::class.java).setAction("stop")
        val piStop = PendingIntent.getService(this, 0, iStop, PendingIntent.FLAG_IMMUTABLE)

        //create user notification
        builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.mipmap.ic_radio)
            .setContentTitle("Воспроизводится")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_radio_round, "Play/Pause", piPause)
            .addAction(R.mipmap.ic_radio_round, "Stop", piStop)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSilent(true)

        //check permission and notify user
        notify(1, builder)

        //show toast
        Toast.makeText(this, title, Toast.LENGTH_LONG)
            .show()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        manager.cancel(1)
        player.reset()
        super.onDestroy()
    }

    private fun notify(notifyID : Int, builder : NotificationCompat.Builder) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            manager.notify(notifyID, builder.build())
        }
    }
}