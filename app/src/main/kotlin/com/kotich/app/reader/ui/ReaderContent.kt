package com.kotich.app.reader.ui

import com.kotich.app.reader.ui.pager.ReaderPage

data class ReaderContent(
	val pages: List<ReaderPage>,
	val state: ReaderState?
)