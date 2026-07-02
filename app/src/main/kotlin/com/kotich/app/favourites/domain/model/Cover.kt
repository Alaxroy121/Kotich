package com.kotich.app.favourites.domain.model

import com.kotich.app.core.model.MangaSource

data class Cover(
	val url: String?,
	val source: String,
) {
	val mangaSource by lazy { MangaSource(source) }
}
