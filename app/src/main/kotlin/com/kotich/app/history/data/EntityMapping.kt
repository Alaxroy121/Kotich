package com.kotich.app.history.data

import com.kotich.app.core.model.MangaHistory
import java.time.Instant

fun HistoryEntity.toMangaHistory() = MangaHistory(
	createdAt = Instant.ofEpochMilli(createdAt),
	updatedAt = Instant.ofEpochMilli(updatedAt),
	chapterId = chapterId,
	page = page,
	scroll = scroll.toInt(),
	percent = percent,
	chaptersCount = chaptersCount,
)
