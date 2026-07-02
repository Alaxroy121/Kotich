package com.kotich.app.tracker.domain

import com.kotich.app.core.prefs.AppSettings
import com.kotich.app.favourites.domain.FavouritesRepository
import com.kotich.app.list.domain.ListFilterOption
import com.kotich.app.list.domain.MangaListQuickFilter
import javax.inject.Inject

class UpdatesListQuickFilter @Inject constructor(
	private val favouritesRepository: FavouritesRepository,
	settings: AppSettings,
) : MangaListQuickFilter(settings) {

	override suspend fun getAvailableFilterOptions(): List<ListFilterOption> =
		favouritesRepository.getMostUpdatedCategories(
			limit = 4,
		).map {
			ListFilterOption.Favorite(it)
		}
}
