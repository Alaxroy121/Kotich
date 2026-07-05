package com.kotich.app.main.ui.protect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.kotich.app.core.prefs.AppSettings
import com.kotich.app.core.ui.DefaultActivityLifecycleCallbacks
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Strict app lock: re-locks the instant the app goes to background.
 * Fingerprint-only by default — password field hidden unless biometric fails.
 */
@Singleton
class AppProtectHelper @Inject constructor(private val settings: AppSettings) :
	DefaultActivityLifecycleCallbacks {

	private var isUnlocked = settings.appPassword.isNullOrEmpty()

	override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
		if (!isUnlocked && activity !is ProtectActivity) {
			val sourceIntent = Intent(activity, activity.javaClass)
			activity.intent?.let {
				sourceIntent.putExtras(it)
				sourceIntent.action = it.action
				sourceIntent.setDataAndType(it.data, it.type)
			}
			activity.startActivity(ProtectActivity.newIntent(activity, sourceIntent))
			activity.finishAfterTransition()
		}
	}

	/**
	 * STRICT: Re-lock the instant any activity stops (app goes to background).
	 * This means switching apps, pressing home, or any other backgrounding
	 * will require fingerprint again on return.
	 */
	override fun onActivityStopped(activity: Activity) {
		if (activity !is ProtectActivity) {
			// Lock immediately when app goes to background
			restoreLock()
		}
	}

	override fun onActivityDestroyed(activity: Activity) {
		if (activity !is ProtectActivity && activity.isFinishing && activity.isTaskRoot) {
			restoreLock()
		}
	}

	fun unlock() {
		isUnlocked = true
	}

	private fun restoreLock() {
		isUnlocked = settings.appPassword.isNullOrEmpty()
	}
}
