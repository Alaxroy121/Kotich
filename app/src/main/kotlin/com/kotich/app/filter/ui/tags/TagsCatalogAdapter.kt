package com.kotich.app.filter.ui.tags

import android.content.Context
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.kotich.app.core.ui.BaseListAdapter
import com.kotich.app.core.ui.list.OnListItemClickListener
import com.kotich.app.core.ui.list.fastscroll.FastScroller
import com.kotich.app.core.util.ext.setChecked
import com.kotich.app.databinding.ItemCheckableNewBinding
import com.kotich.app.filter.ui.model.TagCatalogItem
import com.kotich.app.list.ui.ListModelDiffCallback
import com.kotich.app.list.ui.adapter.ListItemType
import com.kotich.app.list.ui.adapter.errorFooterAD
import com.kotich.app.list.ui.adapter.errorStateListAD
import com.kotich.app.list.ui.adapter.loadingFooterAD
import com.kotich.app.list.ui.adapter.loadingStateAD
import com.kotich.app.list.ui.model.ListModel

class TagsCatalogAdapter(
	listener: OnListItemClickListener<TagCatalogItem>,
) : BaseListAdapter<ListModel>(), FastScroller.SectionIndexer {

	init {
		addDelegate(ListItemType.FILTER_TAG, tagCatalogDelegate(listener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
		addDelegate(ListItemType.FOOTER_LOADING, loadingFooterAD())
		addDelegate(ListItemType.FOOTER_ERROR, errorFooterAD(null))
		addDelegate(ListItemType.STATE_ERROR, errorStateListAD(null))
	}

	override fun getSectionText(context: Context, position: Int): CharSequence? {
		return (items.getOrNull(position) as? TagCatalogItem)?.tag?.title?.firstOrNull()?.uppercase()
	}

	private fun tagCatalogDelegate(
		listener: OnListItemClickListener<TagCatalogItem>,
	) = adapterDelegateViewBinding<TagCatalogItem, ListModel, ItemCheckableNewBinding>(
		{ layoutInflater, parent -> ItemCheckableNewBinding.inflate(layoutInflater, parent, false) },
	) {

		itemView.setOnClickListener {
			listener.onItemClick(item, itemView)
		}

		bind { payloads ->
			binding.root.text = item.tag.title
			binding.root.setChecked(item.isChecked, ListModelDiffCallback.PAYLOAD_CHECKED_CHANGED in payloads)
		}
	}
}
