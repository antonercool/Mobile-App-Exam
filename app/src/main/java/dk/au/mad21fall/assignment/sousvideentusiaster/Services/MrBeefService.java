package dk.au.mad21fall.assignment.sousvideentusiaster.Services;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.assignment.sousvideentusiaster.R;

public class MrBeefService extends Service {

    private static final int NOTIFICATION_ID = 238;
    private static final String CHANNEL = "beefService";
    private ExecutorService backgroundExecutor;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        backgroundExecutor = Executors.newSingleThreadExecutor();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chan = new NotificationChannel(CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chan);
        }

        // Build the notification layout
        Notification notification = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle("MrBeef suggestor")
                .setContentText("MrBeef is an online beef seller")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        // promote as foreground
        startForeground(NOTIFICATION_ID, notification);

        suggestMrBeef();
        // restart if gets killed.
        return START_NOT_STICKY;
    }

    private void suggestMrBeef() {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        // every 60 sec
                        Thread.sleep(1000 * 60);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Have you tried MrBeef.dk ?",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("MrBeef service failed", "Error");
                }
            }
        });
    }


    // Dont need this, since we are not bound service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
