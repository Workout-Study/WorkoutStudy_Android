package com.fitmate.fitmate.presentation.ui.certificate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDeepLinkBuilder
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.domain.usecase.DbCertificationUseCase
import com.fitmate.fitmate.util.PendingTokenValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class StopWatchService : Service() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val WARNING_NOTIFICATION_ID = 2
        const val RESET_NOTIFICATION_ID = 3
        const val PROGRESS_CHANNEL_ID = "CERTIFICATION_CHANNEL"
        const val WARNING_CHANNEL_ID = "WARNING_CERTIFICATION_CHANNEL"
        const val CHANNEL_NAME = "인증 타이머 알림"
        const val WARNING_CHANNEL_NAME = "인증 경고 알림"
    }

    @Inject
    lateinit var dbCertificationUseCase: DbCertificationUseCase
    private lateinit var timer: Timer
    private var elapsedTimeSeconds: Long = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationWarningBuilder: NotificationCompat.Builder
    private lateinit var contentView: RemoteViews
    private lateinit var pendingIntent: PendingIntent


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    //타이머에서 얻은 초단위의 값을 시 분 초로 변환
    private fun formatTime(): String {
        val hours = elapsedTimeSeconds / 3600
        val minutes = (elapsedTimeSeconds % 3600) / 60
        val remainingSeconds = elapsedTimeSeconds % 60
        return String.format("%02d : %02d : %02d", hours, minutes, remainingSeconds)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("pendingToken",PendingTokenValue.CERTIFICATION)
        val test = PendingIntent.getActivity(this,1,intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
/*        pendingIntent = NavDeepLinkBuilder(baseContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_main_graph)
            .setDestination(R.id.certificateFragment)
            .createPendingIntent()*/

        contentView = RemoteViews(packageName, R.layout.certificatiion_custom_notification)
        contentView.setTextViewText(R.id.textViewNotificationTitle, "피트메이트 인증 진행중")
        contentView.setTextViewText(R.id.textViewStopWatch, formatTime())
        notificationBuilder = notificationBuilder(contentView).setContentIntent(test)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    //인증 notification 매니저 초기화
    private fun createNotificationChannel() {
        val progressChannel =
            NotificationChannel(PROGRESS_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        progressChannel.setSound(null, null)

        val warningChannel2 =
            NotificationChannel(WARNING_CHANNEL_ID, WARNING_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

        notificationManager = baseContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(progressChannel)
        notificationManager.createNotificationChannel(warningChannel2)
    }

    private fun notificationBuilder(contentView: RemoteViews): NotificationCompat.Builder {
        return NotificationCompat.Builder(baseContext, PROGRESS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_health_24)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(contentView)
            .setVisibility(VISIBILITY_PUBLIC)
            .setOngoing(true)
    }

    //시간이 흐를 때마다 인증 Notification을 업데이트하는 메서드
    private fun updateNotification() {
        contentView.setTextViewText(R.id.textViewStopWatch, formatTime())
        notificationBuilder.setCustomContentView(contentView)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    //6시간 경과 Notification 호출 메서드
    private fun showWarningNotification() {
        notificationWarningBuilder = NotificationCompat.Builder(baseContext, WARNING_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_warning_24)
            .setContentTitle("아직도 운동중이신가요?")
            .setContentText("운동을 수행한지 6시간이 경과했습니다.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("인증이 12시간을 초과하면 해당 인증과 관련된 데이터가 초기화됩니다. 어서 인증을 완료해주세요!"))
            .setVisibility(VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
        notificationManager.notify(WARNING_NOTIFICATION_ID,notificationWarningBuilder.build())
    }

    private fun showResetNotification() {
        notificationWarningBuilder = NotificationCompat.Builder(baseContext, WARNING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setContentTitle("운동 데이터 초기화")
            .setContentText("운동을 수행한지 12시간이 경과했습니다.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("인증 수행 시간이 12시간을 초과하여 진행중이던 인증 데이터가 삭제되었습니다..."))
            .setVisibility(VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
        notificationManager.notify(RESET_NOTIFICATION_ID,notificationWarningBuilder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            STOP_WATCH_START-> {
                startStopWatch()
            }

            STOP_WATCH_RESET-> {
                //resetData()
                stopTimer()
                stopSelf()
            }

            STOP_WATCH_COMPLETE-> {
                stopTimer()
                //stopSelf()
            }

            else -> {startStopWatch()}
        }
        return START_REDELIVER_INTENT
    }

    private fun stopTimer() {
        timer.cancel()
    }

    private fun startStopWatch() {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                elapsedTimeSeconds++
                updateNotification()
                updateFragment(elapsedTimeSeconds)
                if (elapsedTimeSeconds == 6 * 60 * 60L) {
                    showWarningNotification()
                }
                if (elapsedTimeSeconds == 12 * 60 * 60L) {
                    showResetNotification()
                    resetData()
                    stopTimer()
                    endServiceInfoToFragment()
                    stopSelf()
                }
            }
        }, 1000, 1000)

    }

    //프래그먼트 스톱워치 업데이트 브로드 캐스트 리시버 전송
    private fun updateFragment(elapsedTimeSeconds: Long) {
        val intent = Intent("timer-update")
        intent.putExtra("elapsedTime", elapsedTimeSeconds)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    //room데이터 삭제
    private fun resetData() {
        CoroutineScope(Dispatchers.IO).launch {
            dbCertificationUseCase.delete()
        }
    }

    //서비스가 시간초과로 끝났을 때 프래그먼트 종료 브로드캐스트 리시버 전송
    fun endServiceInfoToFragment() {
        val intent = Intent("end_service")
        intent.putExtra("bye", true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    //강제로 종료되었을 때
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }
}