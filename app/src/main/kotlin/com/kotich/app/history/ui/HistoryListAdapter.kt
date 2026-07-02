package com.kotich.app.history.ui

import android.content.Context
import com.kotich.app.core.ui.list.fastscroll.FastScroller
import com.kotich.app.list.ui.adapter.MangaListAdapter
import com.kotich.app.list.ui.adapter.MangaListListener
import com.kotich.app.list.ui.size.ItemSizeResolver

class HistoryListAdapter(
	listener: MangaListListener,
	sizeResolver: ItemSizeResolver,
) : MangaListAdapter(listener, sizeResolver), FastScroller.SectionIndexer {

	override fun getSectionText(context: Context, position: Int): CharSequence? {
		return findHeader(position)?.getText(context)
	}
}
