package com.kotich.app.history.data

import dagger.Reusable
import com.kotich.app.core.db.MangaDatabase
import com.kotich.app.core.db.entity.toManga
import com.kotich.app.core.db.entity.toMangaTags
import com.kotich.app.history.domain.model.MangaWithHistory
import com.kotich.app.list.domain.ListFilterOption
import com.kotich.app.list.domain.ListSortOrder
import com.kotich.app.local.data.index.LocalMangaIndex
import com.kotich.app.local.domain.LocalObserveMapper
import org.koitharu.kotatsu.parsers.model.Manga
import javax.inject.Inject

@Reusable
class HistoryLocalObserver @Inject constructor(
	localMangaIndex: LocalMangaIndex,
	private val db: MangaDatabase,
) : LocalObserveMapper<HistoryWithManga, MangaWithHistory>(localMangaIndex) {

	fun observeAll(
		order: ListSortOrder,
		filterOptions: Set<ListFilterOption>,
		limit: Int
	) = db.getHistoryDao().observeAll(order, filterOptions, limit).mapToLocal()

	override fun toManga(e: HistoryWithManga) = e.manga.toManga(e.tags.toMangaTags(), null)

	override fun toResult(e: HistoryWithManga, manga: Manga) = MangaWithHistory(
		manga = manga,
		history = e.history.toMangaHistory(),
	)
}
