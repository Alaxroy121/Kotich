package com.kotich.app.favourites.ui.categories.adapter

import com.kotich.app.core.ui.ReorderableListAdapter
import com.kotich.app.favourites.ui.categories.FavouriteCategoriesListListener
import com.kotich.app.list.ui.adapter.ListItemType
import com.kotich.app.list.ui.adapter.ListStateHolderListener
import com.kotich.app.list.ui.adapter.emptyStateListAD
import com.kotich.app.list.ui.adapter.loadingStateAD
import com.kotich.app.list.ui.model.ListModel

class CategoriesAdapter(
	onItemClickListener: FavouriteCategoriesListListener,
	listListener: ListStateHolderListener,
) : ReorderableListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.CATEGORY_LARGE, categoryAD(onItemClickListener))
		addDelegate(ListItemType.NAV_ITEM, allCategoriesAD(onItemClickListener))
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(listListener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
	}
}
