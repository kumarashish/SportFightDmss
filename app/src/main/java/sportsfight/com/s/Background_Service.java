package sportsfight.com.s;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;
import sportsfight.com.s.common.AppController;
import sportsfight.com.s.common.Common;
import sportsfight.com.s.util.Util;

/**
 * Created by ashish.kumar on 12-02-2019.
 */

public class Background_Service extends Service {
    private static Timer timer = new Timer();
    AppController controller;
    @Override
    public void onCreate() {
        super.onCreate();
        controller=(AppController) getApplicationContext();
        // Timer task makes your service will repeat after every 20 Sec.
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                String data="";
                if(Util.isNetworkAvailable(getApplicationContext()))
                { JSONObject jsonObject=new JSONObject();
                    data= controller.getApiCall().postData(Common.getLoginUrl("abc","def","2.2"),jsonObject.toString(),"");
                }
                Log.d("ServiceCalled","from backed"+data);
            }
        };
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(doAsynchronousTask, 60000,60000);  // 20 sec timer
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO do something useful
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}