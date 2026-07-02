package com.kotich.app.favourites.ui.categories.select.adapter

import com.kotich.app.core.ui.BaseListAdapter
import com.kotich.app.core.ui.list.OnListItemClickListener
import com.kotich.app.favourites.ui.categories.select.model.MangaCategoryItem
import com.kotich.app.list.ui.adapter.ListItemType
import com.kotich.app.list.ui.adapter.emptyStateListAD
import com.kotich.app.list.ui.adapter.loadingStateAD
import com.kotich.app.list.ui.model.ListModel

class MangaCategoriesAdapter(
	clickListener: OnListItemClickListener<MangaCategoryItem>,
) : BaseListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.NAV_ITEM, mangaCategoryAD(clickListener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(null))
	}
}
