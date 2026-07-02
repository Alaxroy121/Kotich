package com.kotich.app.reader.ui

import com.kotich.app.bookmarks.domain.Bookmark
import org.koitharu.kotatsu.parsers.model.MangaChapter
import com.kotich.app.reader.ui.pager.ReaderPage

interface ReaderNavigationCallback {

	fun onPageSelected(page: ReaderPage): Boolean

	fun onChapterSelected(chapter: MangaChapter): Boolean

	fun onBookmarkSelected(bookmark: Bookmark): Boolean = onPageSelected(
		ReaderPage(bookmark.toMangaPage(), bookmark.page, bookmark.chapterId),
	)
}
