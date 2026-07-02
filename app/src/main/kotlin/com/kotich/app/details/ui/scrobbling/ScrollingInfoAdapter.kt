package com.kotich.app.details.ui.scrobbling

import com.kotich.app.core.nav.AppRouter
import com.kotich.app.core.ui.BaseListAdapter
import com.kotich.app.list.ui.model.ListModel

class ScrollingInfoAdapter(
	router: AppRouter,
) : BaseListAdapter<ListModel>() {

	init {
		delegatesManager.addDelegate(scrobblingInfoAD(router))
	}
}
