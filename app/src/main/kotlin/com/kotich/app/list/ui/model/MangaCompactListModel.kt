package com.kotich.app.list.ui.model

import com.kotich.app.core.ui.model.MangaOverride
import org.koitharu.kotatsu.parsers.model.Manga

data class MangaCompactListModel(
	override val manga: Manga,
	override val override: MangaOverride?,
	val subtitle: String,
	override val counter: Int,
) : MangaListModel()
