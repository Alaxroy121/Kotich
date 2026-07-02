package com.kotich.app.favourites.domain

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import com.kotich.app.core.db.MangaDatabase
import com.kotich.app.core.db.entity.toManga
import com.kotich.app.core.db.entity.toMangaTags
import com.kotich.app.favourites.data.FavouriteManga
import com.kotich.app.list.domain.ListFilterOption
import com.kotich.app.list.domain.ListSortOrder
import com.kotich.app.local.data.index.LocalMangaIndex
import com.kotich.app.local.domain.LocalObserveMapper
import org.koitharu.kotatsu.parsers.model.Manga
import javax.inject.Inject

@Reusable
class LocalFavoritesObserver @Inject constructor(
	localMangaIndex: LocalMangaIndex,
	private val db: MangaDatabase,
) : LocalObserveMapper<FavouriteManga, Manga>(localMangaIndex) {

	fun observeAll(
		order: ListSortOrder,
		filterOptions: Set<ListFilterOption>,
		limit: Int
	): Flow<List<Manga>> = db.getFavouritesDao().observeAll(order, filterOptions, limit).mapToLocal()

	fun observeAll(
		categoryId: Long,
		order: ListSortOrder,
		filterOptions: Set<ListFilterOption>,
		limit: Int
	): Flow<List<Manga>> = db.getFavouritesDao().observeAll(categoryId, order, filterOptions, limit).mapToLocal()

	override fun toManga(e: FavouriteManga) = e.manga.toManga(e.tags.toMangaTags(), null)

	override fun toResult(e: FavouriteManga, manga: Manga) = manga
}
