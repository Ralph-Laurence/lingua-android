package psu.signlinguaasl.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks
{
    private static MyApp instance;
    private int runningActivityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(this);
    }

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        runningActivityCount++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        runningActivityCount--;
    }

    public boolean isAppInForeground() {
        return runningActivityCount > 0;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
