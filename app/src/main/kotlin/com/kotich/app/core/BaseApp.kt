package com.kotich.app.core

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.InvalidationTracker
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.internal.platform.PlatformRegistry
import org.conscrypt.Conscrypt
import com.kotich.app.BuildConfig
import com.kotich.app.R
import com.kotich.app.core.db.MangaDatabase
import com.kotich.app.core.os.AppValidator
import com.kotich.app.core.os.RomCompat
import com.kotich.app.core.prefs.AppSettings
import com.kotich.app.core.util.ext.processLifecycleScope
import com.kotich.app.local.data.LocalStorageChanges
import com.kotich.app.local.data.index.LocalMangaIndex
import com.kotich.app.local.domain.model.LocalManga
import org.koitharu.kotatsu.parsers.util.suspendlazy.getOrNull
import com.kotich.app.settings.work.WorkScheduleManager
import java.security.Security
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
open class BaseApp : Application(), Configuration.Provider {

	@Inject
	lateinit var databaseObserversProvider: Provider<Set<@JvmSuppressWildcards InvalidationTracker.Observer>>

	@Inject
	lateinit var activityLifecycleCallbacks: Set<@JvmSuppressWildcards ActivityLifecycleCallbacks>

	@Inject
	lateinit var database: Provider<MangaDatabase>

	@Inject
	lateinit var settings: AppSettings

	@Inject
	lateinit var workerFactory: HiltWorkerFactory

	@Inject
	lateinit var appValidator: AppValidator

	@Inject
	lateinit var workScheduleManager: WorkScheduleManager

	@Inject
	lateinit var localMangaIndexProvider: Provider<LocalMangaIndex>

	@Inject
	@LocalStorageChanges
	lateinit var localStorageChanges: MutableSharedFlow<LocalManga?>

	override val workManagerConfiguration: Configuration
		get() = Configuration.Builder()
			.setWorkerFactory(workerFactory)
			.build()

	override fun onCreate() {
		super.onCreate()
		PlatformRegistry.applicationContext = this
		AppCompatDelegate.setDefaultNightMode(settings.theme)
		// TLS 1.3 support for Android < 10
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			Security.insertProviderAt(Conscrypt.newProvider(), 1)
		}
		setupActivityLifecycleCallbacks()
		processLifecycleScope.launch(Dispatchers.Default) {
			setupDatabaseObservers()
			localStorageChanges.collect(localMangaIndexProvider.get())
		}
		workScheduleManager.init()
	}

	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
	}

	@WorkerThread
	private fun setupDatabaseObservers() {
		val tracker = database.get().invalidationTracker
		databaseObserversProvider.get().forEach {
			tracker.addObserver(it)
		}
	}

	private fun setupActivityLifecycleCallbacks() {
		activityLifecycleCallbacks.forEach {
			registerActivityLifecycleCallbacks(it)
		}
	}
}
