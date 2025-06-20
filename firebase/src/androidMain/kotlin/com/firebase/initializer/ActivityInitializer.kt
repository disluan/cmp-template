package com.firebase.initializer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.startup.Initializer
import java.lang.ref.WeakReference

internal lateinit var activityHolder: WeakReference<Activity>
    private set

internal lateinit var appContext: Context
    private set

internal class ActivityInitializer: Initializer<Unit> {
    private val callback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityHolder = WeakReference(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            activityHolder = WeakReference(activity)
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (activityHolder.get() === activity) {
                activityHolder = WeakReference(null)
            }
        }
    }

    override fun create(context: Context) {
        appContext = context.applicationContext
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(callback)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
