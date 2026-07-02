package com.kotich.app.settings.tracker.categories

import com.kotich.app.core.model.FavouriteCategory
import com.kotich.app.core.ui.BaseListAdapter
import com.kotich.app.core.ui.list.OnListItemClickListener

class TrackerCategoriesConfigAdapter(
	listener: OnListItemClickListener<FavouriteCategory>,
) : BaseListAdapter<FavouriteCategory>() {

	init {
		delegatesManager.addDelegate(trackerCategoryAD(listener))
	}
}
