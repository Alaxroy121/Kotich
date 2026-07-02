package com.kotich.app.scrobbling.common.domain

import com.kotich.app.scrobbling.anilist.data.AniListRepository
import com.kotich.app.scrobbling.common.data.ScrobblerRepository
import com.kotich.app.scrobbling.common.domain.model.ScrobblerService
import com.kotich.app.scrobbling.kitsu.data.KitsuRepository
import com.kotich.app.scrobbling.mal.data.MALRepository
import com.kotich.app.scrobbling.shikimori.data.ShikimoriRepository
import javax.inject.Inject
import javax.inject.Provider

class ScrobblerRepositoryMap @Inject constructor(
	private val shikimoriRepository: Provider<ShikimoriRepository>,
	private val aniListRepository: Provider<AniListRepository>,
	private val malRepository: Provider<MALRepository>,
	private val kitsuRepository: Provider<KitsuRepository>,
) {

	operator fun get(scrobblerService: ScrobblerService): ScrobblerRepository = when (scrobblerService) {
		ScrobblerService.SHIKIMORI -> shikimoriRepository
		ScrobblerService.ANILIST -> aniListRepository
		ScrobblerService.MAL -> malRepository
		ScrobblerService.KITSU -> kitsuRepository
	}.get()
}
