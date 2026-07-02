package com.kotich.app.download.ui.list

import androidx.lifecycle.LifecycleOwner
import com.kotich.app.core.ui.BaseListAdapter
import com.kotich.app.list.ui.adapter.ListItemType
import com.kotich.app.list.ui.adapter.emptyStateListAD
import com.kotich.app.list.ui.adapter.listHeaderAD
import com.kotich.app.list.ui.adapter.loadingStateAD
import com.kotich.app.list.ui.model.ListModel

class DownloadsAdapter(
	lifecycleOwner: LifecycleOwner,
	listener: DownloadItemListener,
) : BaseListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.DOWNLOAD, downloadItemAD(lifecycleOwner, listener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(null))
		addDelegate(ListItemType.HEADER, listHeaderAD(null))
	}
}
