package com.lightingcontour.toucherLC;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WelcomeService extends Service {
    public WelcomeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
