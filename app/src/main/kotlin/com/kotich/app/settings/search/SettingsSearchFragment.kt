package com.kotich.app.settings.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.kotich.app.core.ui.BaseFragment
import com.kotich.app.core.ui.BaseListAdapter
import com.kotich.app.core.ui.list.OnListItemClickListener
import com.kotich.app.core.util.ext.consumeAll
import com.kotich.app.core.util.ext.observe
import com.kotich.app.databinding.FragmentSearchSuggestionBinding
import com.kotich.app.list.ui.adapter.ListItemType

@AndroidEntryPoint
class SettingsSearchFragment : BaseFragment<FragmentSearchSuggestionBinding>(),
	OnListItemClickListener<SettingsItem>,
	ListListener<SettingsItem> {

	private val viewModel: SettingsSearchViewModel by activityViewModels()

	override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchSuggestionBinding {
		return FragmentSearchSuggestionBinding.inflate(inflater, container, false)
	}

	override fun onViewBindingCreated(binding: FragmentSearchSuggestionBinding, savedInstanceState: Bundle?) {
		super.onViewBindingCreated(binding, savedInstanceState)
		val adapter = BaseListAdapter<SettingsItem>()
			.addDelegate(ListItemType.NAV_ITEM, settingsItemAD(this))
		adapter.addListListener(this)
		binding.root.adapter = adapter
		binding.root.setHasFixedSize(true)
		viewModel.content.observe(viewLifecycleOwner, adapter)
	}

	override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
		val type = WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.systemBars()
		val barsInsets = insets.getInsets(type)
		v.setPadding(
			barsInsets.left,
			0,
			barsInsets.right,
			barsInsets.bottom,
		)
		return insets.consumeAll(type)
	}

	override fun onItemClick(item: SettingsItem, view: View) = viewModel.navigateToPreference(item)

	override fun onCurrentListChanged(
		previousList: List<SettingsItem?>,
		currentList: List<SettingsItem?>
	) {
		if (currentList.size != previousList.size) {
			(viewBinding?.root?.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
		}
	}
}
