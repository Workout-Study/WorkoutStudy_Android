package com.fitmate.fitmate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val channelName = "알림 채넗"
        val descriptionText = "채널 알림입니다."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(getString(R.string.default_notification_channel_id),channelName, importance)
        mChannel.description = descriptionText

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        message.notification?.let {
           val notificationBuilder =  NotificationCompat.Builder(applicationContext, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(it.title)
                .setContentText(it.body)

            notificationManager.notify(0, notificationBuilder.build())
        }


    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        //경우에 따라 FCM에서 메시지를 전송하지 못할 수 있습니다.
        //특정 기기가 연결될 때 앱에서 대기 중인 메시지가 너무 많거나(100개 초과) 기기가 한 달 넘게 FCM에 연결되지 않으면 이 문제가 발생합니다.
        //이 콜백을 수신한 앱 인스턴스는 앱 서버와 전체 동기화를 수행해야 합니다. 해당 기기의 앱으로 메시지를 보낸 지 4주 이상 경과한 경우 FCM은 onDeletedMessages()를 호출하지 않습니다.

    }
}