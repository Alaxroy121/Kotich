package com.kotich.app.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import com.kotich.app.core.model.isLocal
import com.kotich.app.core.os.NetworkState
import com.kotich.app.core.prefs.AppSettings
import com.kotich.app.history.data.HistoryRepository
import javax.inject.Inject

class ReadingResumeEnabledUseCase @Inject constructor(
	private val networkState: NetworkState,
	private val historyRepository: HistoryRepository,
	private val settings: AppSettings,
) {

	operator fun invoke(): Flow<Boolean> = settings.observe(
		AppSettings.KEY_MAIN_FAB,
		AppSettings.KEY_INCOGNITO_MODE,
	).map {
		settings.isMainFabEnabled && !settings.isIncognitoModeEnabled
	}.distinctUntilChanged()
		.flatMapLatest { isFabEnabled ->
			if (isFabEnabled) {
				observeCanResume()
			} else {
				flowOf(false)
			}
		}

	private fun observeCanResume() = combine(networkState, historyRepository.observeLast()) { isOnline, last ->
		last != null && (isOnline || last.isLocal)
	}.distinctUntilChanged()
}
